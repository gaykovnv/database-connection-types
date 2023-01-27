package com.example.adminservice.controller;

import com.example.adminservice.KafkaProducer;
import com.example.adminservice.service.parse_file.ParserFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminController {

    private final KafkaProducer kafkaProducer;
    private final ParserFileService parserFileService;

    @GetMapping("/api/greeting")
    public String getGreeting() {
//        parserFileService.parseFile();
        kafkaProducer.sendMessage("Hello kafka", "TEST");
        return "AdminService";
    }
}
