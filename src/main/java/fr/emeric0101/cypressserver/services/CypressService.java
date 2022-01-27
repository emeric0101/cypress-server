package fr.emeric0101.cypressserver.services;

import fr.emeric0101.cypressserver.dto.CypressStateDTO;
import fr.emeric0101.cypressserver.services.process.ProcessManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CypressService {
    @Value("${cypress.project-npm-path}")
    private String npmPath;

    @Value("${projects.path}")
    private String projectsPath;

    private String configFilename = "cypress.json";


    private final ProcessManagerService processManagerService;
    /**
     * Return all cypress instances
     * @return
     * @param project
     */
    public CypressStateDTO getState(String project) {
        return new CypressStateDTO(processManagerService.getInstances(project));
    }


    public void stop(String project) {
        processManagerService.stopProcess(project, null);
    }

    public void startAll(String project) {
        processManagerService.startProcess(project, npmPath, getProjectPath(project), configFilename,  null);
    }

    public void start(String project, String test) {
        processManagerService.startProcess(project, npmPath, getProjectPath(project), configFilename, test);

    }

    private String getProjectPath(String project) {
        return projectsPath + File.separator + project;
    }

    /**
     * Clear all not running instance
     * @param project
     */
    public void clear(String project) {
        processManagerService.clear(project);
    }

}
