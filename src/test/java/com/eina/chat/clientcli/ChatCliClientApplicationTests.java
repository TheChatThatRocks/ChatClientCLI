package com.eina.chat.clientcli;

import com.eina.chat.clientcli.utils.FileManagement;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;

class ChatCliClientApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void loadFileAndSave() {
        System.out.println(Paths.get("received_files", "pruebaIMG2.png").toString());

        String fileReadPath = Paths.get("home", "abel", "pruebaIMG.png").toString();
        String saveFilePath = Paths.get("received_files", "pruebaIMG2.png").toString();
        byte[] fileInBytes = FileManagement.readFile(fileReadPath);
        byte[] fileInBytesWithName = FileManagement.concatenateFileAndName(saveFilePath, fileInBytes);
        Pair<String, byte[]> receivedFile = FileManagement.splitFileAndName(fileInBytesWithName);
        FileManagement.createFolderIfNotExist("received_files");
        FileManagement.writeFile(receivedFile.getLeft(), receivedFile.getRight());
    }
}
