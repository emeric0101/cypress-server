package fr.emeric0101.cypressserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestDTO {
    private String name;
    private String path;
    private String project;
}
