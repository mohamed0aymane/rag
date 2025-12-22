package org.mql.generative.ai.rag.controllers;

import java.util.Map;

import org.mql.generative.ai.rag.services.DocumentLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequestMapping("/notebook")
public class DocumentLoaderController {

    private DocumentLoaderService documentLoaderService;
    public DocumentLoaderController(DocumentLoaderService documentLoaderService) {
        this.documentLoaderService = documentLoaderService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadMultiple(
            @RequestParam("pdfs") MultipartFile[] files) {
        
        if (files == null || files.length == 0)
            return ResponseEntity.badRequest().body(Map.of("response", "No files uploaded"));
        this.documentLoaderService.upload(files);
        return ResponseEntity.ok(Map.of("response", "files uploaded successfully"));
    }
}
