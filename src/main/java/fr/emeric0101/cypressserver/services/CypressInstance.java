package fr.emeric0101.cypressserver.services;

import fr.emeric0101.cypressserver.dto.CypressInstanceDTO;
import lombok.Data;


@Data
public class CypressInstance extends CypressInstanceDTO {
    private Integer id;
    private Process process;
    private String project;

}
