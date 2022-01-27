package fr.emeric0101.cypressserver.services.process;

import fr.emeric0101.cypressserver.dto.CypressInstanceDTO;
import fr.emeric0101.cypressserver.services.CypressInstance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessManagerService {
    public static Integer idSequence = 1;

    Collection<CypressInstance> cypressInstances = new ConcurrentLinkedDeque<CypressInstance>();
    Semaphore semaphore = new Semaphore(1);

    /**
     *
     * @param instance null for all
     */
    public void stopProcess(String project, CypressInstance instance) {
        if (instance == null) {
            cypressInstances.stream().filter(e -> project.equals(e.getProject())).forEach(e -> stopProcess(project, instance));
        } else {
            ProcessHelper.stop(instance.getProcess());
        }
    }

    /**
     * Return instance
     * @return
     */
    public List<CypressInstanceDTO> getInstances(String project) {
        try {
            semaphore.acquire();
            return cypressInstances.stream().filter(e -> project.equals(e.getProject())).map(e -> {
                CypressInstanceDTO instanceDTO = new CypressInstanceDTO();
                instanceDTO.setId(e.getId());
                instanceDTO.setFailure(e.isFailure());
                instanceDTO.setRunning(e.isRunning());
                instanceDTO.setStarted(e.getStarted());
                instanceDTO.setTest(e.getTest());
                instanceDTO.setEnded(e.getEnded());
                instanceDTO.setProject(e.getProject());
                if (e.getResult() != null) {
                    instanceDTO.setResult(e.getResult().stream().collect(Collectors.toList()));

                }
                return instanceDTO;
            }).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to fetch instance");
        } finally {
            semaphore.release();
        }
    }

    /**
     * Create a process
     * @Param test null for all
     * @return
     */
    public CypressInstance startProcess(String project, String executablePath, String projectPath, String configFilePath, String test) {
        CypressInstance instance = new CypressInstance();
        instance.setId(idSequence++);
        instance.setStarted(LocalDateTime.now());
        instance.setTest(test);
        instance.setRunning(true);
        instance.setProject(project);


        String configFile = configFilePath;
        String spec = "";

        String cypressStartCommand = executablePath + File.separator + "node_modules" + File.separator + ".bin" + File.separator
                + "cypress.cmd run --config-file " + configFile +
                " --project " + projectPath + " " +
                spec;
        var cmds = cypressStartCommand.split(" ");
        log.info(Arrays.stream(cmds).collect(Collectors.joining(" ")));
        ProcessBuilder pb = new ProcessBuilder(cmds);
        // only for local use in airbus computer
      //  pb.environment().put("CYPRESS_RUN_BINARY", "C:\\Tools\\DEV\\tools\\cypress\\Cypress.exe");

        pb.redirectErrorStream(true);
        try {
             var process = pb.start();
            BufferThreadManager bufferThreadManager = new BufferThreadManager(process, (retval) -> {

            }, (output)-> {
                log.info(output);
                try {
                    semaphore.acquire();
                    instance.getResult().add(output);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });
            bufferThreadManager.start();

            log.info("Starting..." + (test != null ? test : " all"));
            process.onExit().thenAccept((retval) -> {
                // update result
                try {
                    semaphore.acquire();
                    instance.setRunning(false);
                    instance.setFailure(retval.exitValue() != 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                log.info("Ended..." + (test != null ? test : " all"));

            });
            instance.setProcess(process);
            try {
                semaphore.acquire();
                cypressInstances.add(instance);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
            return instance;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * Clear all not running instances
     */
    public void clear(String project) {
        try {
            semaphore.acquire();
            cypressInstances = cypressInstances.stream().filter(e -> !project.equals(e.getProject()) || e.isRunning()).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
