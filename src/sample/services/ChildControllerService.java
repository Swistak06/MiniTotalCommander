package sample.services;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;

public class ChildControllerService {

    private String currPath;

    public void initializeDrivers(ComboBox comboBox ){
        File[] drivers;
        //FileSystemView fsv = FileSystemView.getFileSystemView();
        drivers = File.listRoots();
        for(File driver:drivers)
            if(driver.isHidden())
                comboBox.getItems().add(driver.toString());
    }

    public void initializingExplorerList(ListView listView, String parentPath){
        File directory = new File(parentPath);
        FileSystemView fsv = FileSystemView.getFileSystemView();

        FileFilter isDirect = fl -> fl.isDirectory();
        FileFilter isFile = fl -> !fl.isDirectory();

        File[] onlyFiles = directory.listFiles(isFile);
        File[] onlyDirectories = directory.listFiles(isDirect);

        if(onlyDirectories != null)
            for(File file:onlyDirectories)
                if(!file.isHidden())
                    listView.getItems().add(file.getName());

        if(onlyFiles != null)
            for(File file:onlyFiles)
                if(!file.isHidden())
                    listView.getItems().add(file.getName());
    }


}
