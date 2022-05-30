package ru.example.utils;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dto.request.disease.ApproveDiseaseRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
@Service
public class Base64Converter {

    private static final String FILE_STARTING = "data:application/pdf;base64,";

    public static String convertBase64ToPdf(ApproveDiseaseRequest request) throws IOException {

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

    public static String encodePdfFileToBase64(String fileName) {
        File file = new File(fileName);

        byte[] encoded;

        try{
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }

        String base64File = new String(encoded, StandardCharsets.US_ASCII);

        return FILE_STARTING.concat(base64File);
    }
}
