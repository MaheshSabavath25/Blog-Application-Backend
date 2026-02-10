package com.Blog_Application.Services.Imple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Blog_Application.BlogServices.FileService;



@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // Original name
        String originalName = file.getOriginalFilename();

        // Random name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalName.substring(originalName.lastIndexOf(".")));

        // Full path
        String filePath = path + File.separator + fileName;

        // Create folder if not exists
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Save file
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
