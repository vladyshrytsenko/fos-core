package com.example.foscore.controller;

import com.example.foscore.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    @PostMapping
    public CompletableFuture<ResponseEntity<byte[]>> create() {
        return this.reportService.makeReport()
            .thenApply(report -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(report)
            );
    }

    private final ReportService reportService;
}
