package com.studentportal.controller;

import com.studentportal.service.DataGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataGenerationController {

    private final DataGenerationService dataGenerationService;

    public DataGenerationController(DataGenerationService dataGenerationService) {
        this.dataGenerationService = dataGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateData(@RequestBody(required = false) Map<String, Integer> request) throws IOException {
        int count = 100; // Default value

        if (request != null && request.containsKey("count")) {
            count = request.get("count");
        }

        String filePath = dataGenerationService.generateStudentData(count);

        return ResponseEntity.ok(Map.of(
                "message", "Data generated successfully",
                "filePath", filePath
        ));
    }

}