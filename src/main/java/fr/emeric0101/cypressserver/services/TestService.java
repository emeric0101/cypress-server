package fr.emeric0101.cypressserver.services;

import fr.emeric0101.cypressserver.dto.TestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {
    @Value("${projects.path}")
    private String projectsPath;

    public List<TestDTO> findAll() {
        List<TestDTO> tests = new LinkedList<>();
        // fetch all cypress test
        File cypressProjectPath = new File(projectsPath);
        if (!cypressProjectPath.exists()) {
            return List.of();
        }
        // search for project
        for (var project: cypressProjectPath.listFiles()) {
            if (!project.isDirectory()) {
                continue;
            }
            // search for test in project
            File cypressPath = new File(project.getAbsolutePath() + File.separator + "cypress" + File.separator + "integration");
            var testFiles = findTest(cypressPath);
            tests.addAll(testFiles.stream().map(e -> new TestDTO(e.getName(),
                    e.getAbsolutePath().replace(cypressPath.getAbsolutePath(), ""),
                    project.getName()
            )).collect(Collectors.toList()));
        }
        return tests;
    }

    /**
     * Scan all .spec.js file from root
     * @param root
     * @return
     */
    public List<File> findTest(File root) {
        if (root.isFile()) {
            return List.of();
        }
        List<File> files = new LinkedList<>();
        for (var file: root.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(findTest(file));
            } else {
                if (file.getName().contains(".spec.js")) {
                    files.add(file);
                }
            }
        }
        return files;
    }
}
