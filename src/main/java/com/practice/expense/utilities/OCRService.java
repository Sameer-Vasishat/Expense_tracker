package com.practice.expense.utilities;

import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.bytedeco.opencv.opencv_core.RotatedRect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.springframework.stereotype.Component;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Component
public class OCRService {

    public String extractText(byte[] imageData) {
        try {
            if (imageData == null || imageData.length == 0) {
                throw new IllegalArgumentException("Image data cannot be null or empty");
            }

            // 1. Convert byte[] → Mat
           // Mat src = convertToMat(imageData);

            // 2. Preprocess
           // Mat processed = preprocessImage(src);

            // 3. Convert back to bytes for Tess4J
           // byte[] processedBytes = matToBytes(processed);
            byte[] processedBytes = imageData;
            // 4. OCR with Tess4J
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            tesseract.setLanguage("eng");

            String text = tesseract.doOCR(ImageIO.read(new ByteArrayInputStream(processedBytes)));
            return text;

        } catch (Exception e) {
            throw new RuntimeException("Failed to process image with OCR", e);
        }
    }

    // Convert byte[] to OpenCV Mat
    public Mat convertToMat(byte[] imageBytes) {
        return opencv_imgcodecs.imdecode(new Mat(new BytePointer(imageBytes)), opencv_imgcodecs.IMREAD_COLOR);
    }

    // Preprocess image
    public Mat preprocessImage(Mat raw) {
        if (raw == null || raw.empty()) {
            throw new RuntimeException("Failed to decode image from byte[]");
        }

        // 1. Grayscale
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(raw, gray, opencv_imgproc.COLOR_BGR2GRAY);

        // 2. Gaussian Blur
        Mat denoised = new Mat();
        opencv_imgproc.GaussianBlur(gray, denoised, new Size(3, 3), 0);

        // 3. Adaptive Threshold
        Mat binary = new Mat();
        opencv_imgproc.adaptiveThreshold(
                denoised, binary, 255,
                opencv_imgproc.ADAPTIVE_THRESH_MEAN_C,
                opencv_imgproc.THRESH_BINARY, 15, 10
        );

        // 4. Deskew
        Mat deskewed = deskew(binary);

        // 5. Sharpen edges
        Mat kernel = new Mat(3, 3, opencv_core.CV_32F);
        FloatRawIndexer indexer = kernel.createIndexer();
        indexer.put(0,0, 0);   indexer.put(0,1,-1);  indexer.put(0,2,0);
        indexer.put(1,0,-1);   indexer.put(1,1, 5);  indexer.put(1,2,-1);
        indexer.put(2,0, 0);   indexer.put(2,1,-1);  indexer.put(2,2,0);
        indexer.release();

        Mat sharpened = new Mat();
        opencv_imgproc.filter2D(deskewed, sharpened, -1, kernel);

        return sharpened;
    }

    // Deskew using minAreaRect
    private static Mat deskew(Mat src) {
        Mat points = new Mat();
        opencv_core.findNonZero(src, points);

        if (points.empty()) return src;

        RotatedRect box = opencv_imgproc.minAreaRect(new Mat(points));
        double angle = box.angle();
        if (angle < -45) {
            angle += 90;
        }

        Point2f center = new Point2f(src.cols() / 2.0f, src.rows() / 2.0f);
        Mat rotMat = opencv_imgproc.getRotationMatrix2D(center, angle, 1.0);

        Mat rotated = new Mat();
        opencv_imgproc.warpAffine(
            src,
            rotated,
            rotMat,
            src.size(),
            opencv_imgproc.INTER_CUBIC,
            opencv_core.BORDER_REPLICATE,
            new Scalar(0.0)
        );
        return rotated;
    }

    // Convert Mat → byte[]
    public byte[] matToBytes(Mat mat) {
        BytePointer buf = new BytePointer();
        opencv_imgcodecs.imencode(".png", mat, buf);
        byte[] bytes = new byte[(int) buf.limit()];
        buf.get(bytes);
        return bytes;
    }
}
