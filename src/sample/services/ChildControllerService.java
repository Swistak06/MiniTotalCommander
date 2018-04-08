package sample.services;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;

public class ChildControllerService {


    private String currPath;

    private long currentTime;
    private long lastTime;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public ChildControllerService(){
        lastTime = 0;
    }

    public String getCurrPath() {
        return currPath;
    }

    public void setCurrPath(String currPath) {
        this.currPath = currPath;
    }

    public void initializeDrivers(ComboBox<String> comboBox ){
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

    public boolean isElementOfListDoubleClicked(){
        long diff;
        boolean isdblClicked = false;
        currentTime=System.currentTimeMillis();
        if(lastTime!=0 && currentTime!=0){
            diff=currentTime-lastTime;
            isdblClicked = diff <= 215;
        }
        lastTime=currentTime;
        return isdblClicked;
    }


}
