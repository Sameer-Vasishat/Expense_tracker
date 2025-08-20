package com.practice.expense.controller;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practice.expense.service.impl.InvoiceCategoryTrainer;
import com.practice.expense.utilities.Utils;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    private final InvoiceCategoryTrainer trainer;
    
    @Autowired
    Utils utils;

    public TrainingController(InvoiceCategoryTrainer trainer) {
        this.trainer = trainer;
    }

    @PostMapping("/train")
    public ResponseEntity<String> trainModel(@RequestParam("file") MultipartFile file) {
        try {
            // Save uploaded file temporarily
            File tempFile = File.createTempFile("training-", ".txt");
            file.transferTo(tempFile);

            // Train model
            String result = trainer.trainModel(tempFile);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Training failed: " + e.getMessage());
        }
    }
    @PostMapping("/retrain-model")
    public ResponseEntity<String> retrain(@RequestParam("file") MultipartFile file) {
        try {
            // ✅ Save uploaded file safely
            File tempFile = File.createTempFile("training-data", ".txt");
            file.transferTo(tempFile);

            // ✅ Load training data
            InputStreamFactory dataIn = new MarkableFileInputStreamFactory(tempFile);
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            // ✅ Set training parameters
            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 1);

            // ✅ Train new model
            DoccatModel newModel = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

            // ✅ Save model to file
            try (FileOutputStream modelOut = new FileOutputStream("invoice-category-model.bin")) {
                newModel.serialize(modelOut);
            }

            // ✅ Reload model in memory for immediate use
            utils.reload();

            return ResponseEntity.ok("✅ Model retrained successfully and reloaded!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("❌ Training failed: " + e.getMessage());
        }
    }



}
