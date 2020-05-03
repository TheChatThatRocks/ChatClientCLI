package com.eina.chat.clientcli.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileManagement {
    /**
     * Read file from hard drive
     *
     * @param filePath path of the file
     * @return file in bytes
     */
    public static byte[] readFile(String filePath) {
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
     *
     * @param filePath path of the file
     * @return true if not error
     */
    public static boolean writeFile(String filePath, byte[] fileContent) {
        boolean response;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileContent);
            response = true;
        } catch (IOException e) {
            response = false;
        }
        return response;
    }

    /**
     * Create byte array from file and name
     *
     * @param fileName    name of the file
     * @param fileContent content of the file
     * @return concatenation of tje name and the content
     */
    public static byte[] concatenateFileAndName(String fileName, byte[] fileContent) {
        byte[] nameBytes = fileName.getBytes();
        byte nameBytesSize = (byte) nameBytes.length;
        byte[] nameBytesWithSize = ArrayUtils.addAll(new byte[]{nameBytesSize}, nameBytes);
        return ArrayUtils.addAll(nameBytesWithSize, fileContent);
    }

    public static Pair<String, byte[]> splitFileAndName(byte[] fileContentWithName) {
        byte nameBytesSize = fileContentWithName[0];
        byte[] nameBytes = ArrayUtils.subarray(fileContentWithName, 1, nameBytesSize + 1);
        byte[] fileContent = ArrayUtils.subarray(fileContentWithName, nameBytesSize + 1, fileContentWithName.length);
        String fileName = new String(nameBytes);
        return new ImmutablePair<>(fileName, fileContent);
    }

    public static void createFolderIfNotExist(String folderName){
        File file = new File(folderName);
        //Creating the directory
        file.mkdir();
    }
}
