package fr.emeric0101.cypressserver.controllers;

import fr.emeric0101.cypressserver.dto.CypressStateDTO;
import fr.emeric0101.cypressserver.services.CypressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/cypress")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CypressController {
    final CypressService cypressService;

    @GetMapping("{project}/state")
    public CypressStateDTO state(@PathVariable("project") final String project) {
        return cypressService.getState(project);
    }


    @GetMapping("{project}/stop")
    public void stop(@PathVariable("project") final String project) {
        cypressService.stop(project);
    }

    @GetMapping("{project}/start/all")
    public void startAll(@PathVariable("project") final String project) {
        cypressService.startAll(project);
    }

    @GetMapping("{project}/start/{test}")
    public void startTest(@PathVariable("project") final String project, @PathVariable("test") final String test) {
        cypressService.start(project, test);
    }

    @GetMapping("{project}/clear")
    public void clear(@PathVariable("project") final String project) {
        cypressService.clear(project);
    }


}
