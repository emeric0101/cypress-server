package fr.emeric0101.cypressserver.services;

import fr.emeric0101.cypressserver.dto.CypressStateDTO;
import fr.emeric0101.cypressserver.services.process.ProcessManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CypressService {
    @Value("${cypress.project-npm-path}")
    private String npmPath;

    @Value("${project.path}")
    private String projectPath;

    @Value("${project.config-file.path}")
    private String configFilePath;

    private final ProcessManagerService processManagerService;
    /**
     * Return all cypress instances
     * @return
     */
    public CypressStateDTO getState() {
        return new CypressStateDTO(processManagerService.getInstances());
    }


    public void stop() {
        processManagerService.stopProcess(null);
    }

    public void startAll() {
        processManagerService.startProcess(npmPath, projectPath,configFilePath,  null);
    }

    public void start(String test) {
        processManagerService.startProcess(npmPath, projectPath,configFilePath,  test);

    }

    /**
     * Clear all not running instance
     */
    public void clear() {
        processManagerService.clear();
    }
}
