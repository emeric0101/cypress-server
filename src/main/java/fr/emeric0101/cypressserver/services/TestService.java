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
    @Value("${project.path}")
    private String projectPath;

    public List<TestDTO> findAll() {
        // fetch all cypress test
        File cypressPath = new File(projectPath + File.separator + "cypress" + File.separator + "integration");
        var testFiles = findTest(cypressPath);
        return testFiles.stream().map(e -> new TestDTO(e.getName(),
                e.getAbsolutePath().replace(cypressPath.getAbsolutePath(), "")
        )).collect(Collectors.toList());
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
