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

    @GetMapping("state")
    public CypressStateDTO state() {
        return cypressService.getState();
    }


    @GetMapping("stop")
    public void stop() {
        cypressService.stop();
    }

    @GetMapping("start/all")
    public void startAll() {
        cypressService.startAll();
    }

    @GetMapping("start/{test}")
    public void startTest(@PathVariable("test") final String test) {
        cypressService.start(test);
    }

    @GetMapping("clear")
    public void clear() {
        cypressService.clear();
    }

}
