package com.practice.expense.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.ml.maxent.quasinewton.QNTrainer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

@Service
public class InvoiceCategoryTrainer {

    private static final String MODEL_FILE = "invoice-category-model.bin";

    public String trainModel(File trainingFile) {
        try {
            InputStreamFactory isf = new MarkableFileInputStreamFactory(trainingFile);
            ObjectStream<String> lineStream = new PlainTextByLineStream(isf, "UTF-8");
            ObjectStream<opennlp.tools.doccat.DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 1);
            params.put(TrainingParameters.ALGORITHM_PARAM, QNTrainer.MAXENT_QN_VALUE);

            DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

            // Save the model
            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(MODEL_FILE))) {
                model.serialize(modelOut);
            }

            return "Model trained successfully and saved as " + MODEL_FILE;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Training failed: " + e.getMessage());
        }
    }
}
