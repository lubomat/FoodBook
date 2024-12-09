package com.cookBook.CookBook.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {
        logger.info("Initializing CloudinaryService with provided credentials.");
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
        logger.info("CloudinaryService initialized successfully.");
    }

    public String uploadFile(MultipartFile file) throws IOException {
        logger.info("Starting file upload: {}", file.getOriginalFilename());
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String uploadedUrl = uploadResult.get("url").toString();
            logger.info("File uploaded successfully: {}", uploadedUrl);
            return uploadedUrl;
        } catch (IOException e) {
            logger.error("File upload failed for file: {}", file.getOriginalFilename(), e);
            throw e;
        }
    }
}

/*TEST*/
