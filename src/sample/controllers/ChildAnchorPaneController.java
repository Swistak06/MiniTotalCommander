package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import sample.services.ChildControllerService;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ChildAnchorPaneController {


    private long currentTime,lastTime=0;

    public String getCurrPath() {
        return currPath;
    }

    private String currPath;

    private final ChildControllerService service = new ChildControllerService();

    private MainAnchorPaneController main;

    //@FXML
    //private AnchorPane RigthAnchor;

    @FXML
    private ListView<String> ExplorerList;

    @FXML
    private TextField PathTextField;

    @FXML
    private ComboBox<String> DriveComboBox;

    @FXML
    private Button deleteSelectedFileButton;

    @FXML
    private Button copySelectedFileButton;

    //@FXML
    //private Button goingUpButton;

    public void incjectMain(MainAnchorPaneController main){
        this.main = main;
    }

    public void initializnigPathTextField(){
        this.currPath = this.DriveComboBox.getSelectionModel().getSelectedItem();
        this.PathTextField.setText(this.currPath);
    }

//    public void initializingExplorer(String parentPath){
//
//    }


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
            service.initializingExplorerList(this.ExplorerList,currPath);
        }
    }

    @FXML
    void choosingDriveFromComboBox() {
        if(!DriveComboBox.getSelectionModel().isEmpty()){
            this.currPath = this.DriveComboBox.getSelectionModel().getSelectedItem();
            this.PathTextField.setText(currPath);
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            service.initializingExplorerList(this.ExplorerList,currPath);
        }

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
            service.initializingExplorerList(this.ExplorerList,currPath);
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
                service.initializingExplorerList(this.ExplorerList,currPath);
            }
    }

    @FXML
    public void reloadDiskList(){
        if(!DriveComboBox.getItems().isEmpty()){
            this.DriveComboBox.getSelectionModel().clearSelection();
            this.DriveComboBox.getItems().clear();
            service.initializeDrivers(this.DriveComboBox);

        }



    }

    @FXML
    void copySelectedFile() {
        String pathToCopy="empty";
        if(!this.ExplorerList.getSelectionModel().isEmpty())
            pathToCopy = currPath+this.ExplorerList.getSelectionModel().getSelectedItem();
        File src = new File(pathToCopy);
        File dst = new File(main.returnOtherController(this).getCurrPath());
        


    }

    @FXML
    void deleteSelectedFile() {
        String pathToDelete = "wrong";
        if(!this.ExplorerList.getSelectionModel().isEmpty())
            pathToDelete = currPath+this.ExplorerList.getSelectionModel().getSelectedItem();
        File file = new File(pathToDelete);
        file.delete();
        this.ExplorerList.getItems().clear();
        service.initializingExplorerList(this.ExplorerList,currPath);
    }

    @FXML
    public void initialize(){
        service.initializeDrivers(this.DriveComboBox);
        this.DriveComboBox.getSelectionModel().select(0);
        initializnigPathTextField();
        service.initializingExplorerList(this.ExplorerList,currPath);

    }



}
