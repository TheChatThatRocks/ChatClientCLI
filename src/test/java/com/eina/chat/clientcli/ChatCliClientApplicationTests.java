package com.eina.chat.clientcli;

import com.eina.chat.clientcli.utils.FileManagement;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class ChatCliClientApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void loadFileAndSave() {
        String fileReadPath = "/home/abel/pruebaIMG.png";
        String saveFilePath = "/home/abel/pruebaIMG2.png";
        byte[] fileInBytes = FileManagement.readFile(fileReadPath);
        byte[] fileInBytesWithName = FileManagement.concatenateFileAndName(saveFilePath, fileInBytes);
        Pair<String, byte[]> receivedFile = FileManagement.splitFileAndName(fileInBytesWithName);
        FileManagement.writeFile(receivedFile.getLeft(), receivedFile.getRight());
    }
}
