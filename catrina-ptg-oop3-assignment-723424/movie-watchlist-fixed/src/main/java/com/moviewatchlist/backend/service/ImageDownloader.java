package com.moviewatchlist.backend.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageDownloader {
    public String downloadImages(String title, java.util.List<String> imageUrls) {
        String folderPath = "images/" + title.replaceAll("\s+", "_");
        new File(folderPath).mkdirs();

        for (int i = 0; i < imageUrls.size(); i++) {
            try (InputStream in = new URL(imageUrls.get(i)).openStream()) {
                Files.copy(in, Paths.get(folderPath + "/img" + (i + 1) + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folderPath;
    }
}
