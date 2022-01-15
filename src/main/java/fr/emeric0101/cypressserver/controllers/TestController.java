package fr.emeric0101.cypressserver.controllers;

import fr.emeric0101.cypressserver.dto.TestDTO;
import fr.emeric0101.cypressserver.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
