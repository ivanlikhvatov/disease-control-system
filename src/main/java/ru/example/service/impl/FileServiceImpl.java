package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.service.FileService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final String FILE_STARTING = "data:application/pdf;base64,";
    private static final String PDF_POSTFIX = ".pdf";

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String createAndSavePdfFileFromBase64(String base64File) {
        String base64ImageString = base64File.replace(FILE_STARTING, StringUtils.EMPTY);
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64ImageString);

        String fileName = UUID.randomUUID().toString().concat(PDF_POSTFIX);

        String path = uploadPath + fileName;

        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
