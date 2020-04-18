package com.eina.chat.clientcli.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileManagement {
    /**
     * Read file from hard drive
     * @param filePath path of the file
     * @return file in bytes
     */
    private static byte[] readFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.canWrite()) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Write file to hard drive
     * @param filePath path of the file
     * @return true if not error
     */
    private boolean writeFile(String filePath, byte[] fileContent) {
        boolean response;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileContent);
            response = true;
        } catch (IOException e) {
            response = false;
        }
        return response;
    }
}
