package com.studentportal.controller;

import com.studentportal.service.DataProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataProcessing {

    private final DataProcessingService dataProcessingService;

    // Constructor injection to get an instance of DataProcessingService
    public DataProcessing(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processExcelToCsv() throws IOException {
        // Call the instance method processLatestExcel() on the injected service
        String result = dataProcessingService.processLatestExcel();

        // Return the response as a map with a success message and the path of the generated CSV
        return ResponseEntity.ok(Map.of(
                "message", "Data processed successfully",
                "csvPath", result
        ));
    }
}
