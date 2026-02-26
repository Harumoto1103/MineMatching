package com.harumoto.matching;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ResourceUtil {

    public static String copyResourceToTempFile(String resourcePath) throws IOException {
        try (InputStream is = App.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            File tempFile = Files.createTempFile("resource-", ".tmp").toFile();
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
            }
            return tempFile.getAbsolutePath();
        }
    }
}

