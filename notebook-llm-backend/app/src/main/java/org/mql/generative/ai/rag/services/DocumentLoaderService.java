package org.mql.generative.ai.rag.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Vector;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.embedding.Embedding;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;

@Service
public class DocumentLoaderService {
    private static final String UPLOAD_DIR = "src/main/resources/uploads";

    private EmbeddingStore<TextSegment> embeddingStore;
    private EmbeddingModel embeddingModel;

    public DocumentLoaderService(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
    }

    private void writeFilesOnDisk(MultipartFile[] files) {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {

                if (file == null || file.isEmpty())
                    continue;

                if (!"application/pdf".equalsIgnoreCase(file.getContentType()))
                    continue;
                
                Path targetPath = uploadPath.resolve(file.getOriginalFilename());
                
                Files.copy(
                        file.getInputStream(),
                        targetPath,
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write files on disk", e);
        }
    }

    private void embedFiles(Path pdfsPath) {
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(
                pdfsPath.toString(),
                new ApachePdfBoxDocumentParser());
        DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(500, 100);

        List<TextSegment> pdfSegments = new Vector<>();
        for (Document document : documents) {
            List<TextSegment> segments = splitter.split(document);
            pdfSegments.addAll(segments);
        }

        int wordCount = 0;

        for (TextSegment segment : pdfSegments) {
            if (segment.text() == null || segment.text().isBlank())
                continue;
            String[] words = segment.text().trim().split("\\s+");
            wordCount += words.length;
        }
        List<Embedding> embeddings = embeddingModel.embedAll(pdfSegments).content();
        embeddingStore.addAll(embeddings, pdfSegments);
    }

    public void upload(MultipartFile[] files) {
        writeFilesOnDisk(files);
        Path pdfsPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(pdfsPath)) {
            return;
        }
        embedFiles(pdfsPath);
    }
}
