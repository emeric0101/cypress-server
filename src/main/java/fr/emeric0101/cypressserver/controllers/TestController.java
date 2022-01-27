package fr.emeric0101.cypressserver.controllers;

import fr.emeric0101.cypressserver.dto.TestDTO;
import fr.emeric0101.cypressserver.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/test")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("")
    public List<TestDTO> findAll() {
        return testService.findAll();
    }
    

}
