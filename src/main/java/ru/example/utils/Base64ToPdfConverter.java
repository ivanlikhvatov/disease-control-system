package ru.example.utils;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.example.dto.request.disease.ApproveDiseaseRequest;

import java.io.FileOutputStream;
import java.io.IOException;

@Data
@Service
public class Base64ToPdfConverter {

    private static String convertBase64ToPdf(ApproveDiseaseRequest request) throws IOException {

        String dataDir = "/Users/ivanlikhvatov/Downloads/";

        String base64 = request.getScannedCertificate();
        String base64ImageString = base64.replace("data:application/pdf;base64,", "");
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64ImageString);

        String path = dataDir + "Base64_to_Pdf.pdf";

        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(imageBytes);
        }

        return null;


    }
}
