package com.deployment.dockerjenkinspipeline.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/deploy")
public class TestController {

    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping
    public ResponseEntity<String> releaseTest() {
        String release = "RELEASE";
        return ResponseEntity.ok("Deployment success. Profile: " + profile + " " + release + "1.0.0");
    }
}
