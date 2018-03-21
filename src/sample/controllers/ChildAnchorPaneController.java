package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;

public class ChildAnchorPaneController {


    private long currentTime,lastTime=0;

    private String currPath;

    //@FXML
    //private AnchorPane RigthAnchor;

    @FXML
    private ListView<String> ExplorerList;

    @FXML
    private TextField PathTextField;

    @FXML
    private ComboBox<String> DriveComboBox;

    //@FXML
    //private Button goingUpButton;

    public void initializingValuesOfDriverComboBox(){
        File[] drivers;
        //FileSystemView fsv = FileSystemView.getFileSystemView();
        drivers = File.listRoots();
        for(File driver:drivers)
            if(driver.isHidden())
                this.DriveComboBox.getItems().add(driver.toString());
        this.DriveComboBox.getSelectionModel().select(0);
    }

    public void initializnigPathTextField(){
        this.currPath = this.DriveComboBox.getSelectionModel().getSelectedItem();
        this.PathTextField.setText(this.currPath);
    }

    public void initializingExplorer(String parentPath){
      File directory = new File(parentPath);
      FileSystemView fsv = FileSystemView.getFileSystemView();

      FileFilter isDirect = fl -> fl.isDirectory();
      FileFilter isFile = fl -> !fl.isDirectory();

      File[] onlyFiles = directory.listFiles(isFile);
      File[] onlyDirectories = directory.listFiles(isDirect);

      if(onlyDirectories != null)
          for(File file:onlyDirectories)
              if(!file.isHidden())
                this.ExplorerList.getItems().add(file.getName());

      if(onlyFiles != null)
          for(File file:onlyFiles)
              if(!file.isHidden())
                this.ExplorerList.getItems().add(file.getName());
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

    public void isSingleClicked(){

    }

    public void isDoubleClicked(){
        String newPath = currPath + this.ExplorerList.getSelectionModel().getSelectedItem()+"\\";
        if(!new File(newPath).isFile() && !this.ExplorerList.getSelectionModel().isEmpty() && new File(newPath).exists()){
            currPath = newPath;
            this.PathTextField.setText(newPath);
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            initializingExplorer(currPath);
        }
    }

    @FXML
    void choosingDriveFromComboBox() {
        this.currPath = this.DriveComboBox.getSelectionModel().getSelectedItem();
        this.PathTextField.setText(currPath);
        this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
        initializingExplorer(currPath);
    }

    @FXML
    public void choosingDirectoryFromList() {
        if(!isElementOfListDoubleClicked())
            isSingleClicked();
        else
            isDoubleClicked();
    }


    @FXML
    void goingUp() {
        File file = new File(this.currPath);
        if(file.getParent() != null){
            if(new File(file.getParent()).getParent() == null)
                this.currPath = file.getParent();
            else
                this.currPath = file.getParent()+"\\";
            this.PathTextField.setText(currPath);
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            initializingExplorer(this.currPath);
        }
    }

    @FXML
    void ifEnterKeyIsTyped(KeyEvent event) {
        char znak = event.getCharacter().charAt(0);
        File file = new File(this.PathTextField.getText());
        if(znak == 13)
            if(!this.PathTextField.getText().isEmpty() && file.exists() && file.isDirectory()){
                currPath = PathTextField.getText();
                this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
                initializingExplorer(currPath);
            }
    }

    @FXML
    public void reloadDiskList(){

    }
    @FXML
    public void initialize(){
        initializingValuesOfDriverComboBox();
        initializnigPathTextField();
        initializingExplorer(this.currPath);

    }



}
