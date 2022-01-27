package fr.emeric0101.cypressserver.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
public class CypressInstanceDTO {
    private Integer id;
    private boolean running;
    private List<String> result = new LinkedList<>();
    private LocalDateTime started;
    private LocalDateTime ended;
    private boolean failure;
    private String test;
    private String project;
}
