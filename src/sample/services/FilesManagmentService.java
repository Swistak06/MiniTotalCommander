package sample.services;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilesManagmentService {
    public void copySelectedFileToDirection(String source, String destination){
        File src = new File(source);
        File dest = new File(destination+src.getName());
        try {
                FileUtils.copyDirectory(src, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void deleteSelectedFile(String pathToDelete){
        File file = new File(pathToDelete);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveSelectedFileToDirection(String source, String destination){
        File src = new File(source);
        File dest = new File(destination+src.getName());
        try {
            FileUtils.moveDirectory(src,dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
