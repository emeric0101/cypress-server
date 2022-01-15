package fr.emeric0101.cypressserver.dto;

import fr.emeric0101.cypressserver.services.CypressInstance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CypressStateDTO {
    private List<CypressInstanceDTO> instances;
}
