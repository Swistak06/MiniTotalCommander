package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import sample.services.ChildControllerService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ChildAnchorPaneController {


    private final ChildControllerService service = new ChildControllerService();

    private MainAnchorPaneController main;

    private ChildAnchorPaneController otherChild;

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

    public ChildControllerService getService() {
        return service;
    }


    public void incjectMain(MainAnchorPaneController main){
        this.main = main;
    }

    public void initializnigPathTextField(){
        this.service.setCurrPath(this.DriveComboBox.getSelectionModel().getSelectedItem());
        this.PathTextField.setText(this.service.getCurrPath());
    }

    public void isSingleClicked(){

    }

    public void isDoubleClicked(){
        String newPath = service.getCurrPath() + this.ExplorerList.getSelectionModel().getSelectedItem()+"\\";
        if(!new File(newPath).isFile() && !this.ExplorerList.getSelectionModel().isEmpty() && new File(newPath).exists()){
            service.setCurrPath(newPath);
            this.PathTextField.setText(newPath);
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            service.initializingExplorerList(this.ExplorerList,service.getCurrPath());
        }
    }

    @FXML
    public void choosingDriveFromComboBox(ActionEvent event) {
        ComboBox comboBox = (ComboBox)event.getSource();
        if(!comboBox.getSelectionModel().isEmpty()){
            this.service.setCurrPath(comboBox.getSelectionModel().getSelectedItem().toString());
            this.PathTextField.setText(service.getCurrPath());
            ExplorerList.getItems().remove(0,ExplorerList.getItems().size());
            service.initializingExplorerList(ExplorerList,service.getCurrPath());
        }

    }

    @FXML
    public void choosingDirectoryFromList() {
        if(!service.isElementOfListDoubleClicked())
            isSingleClicked();
        else
            isDoubleClicked();
    }


    @FXML
    void goingUp() {
        File file = new File(service.getCurrPath());
        if(file.getParent() != null){
            if(new File(file.getParent()).getParent() == null)
                service.setCurrPath(file.getParent());
            else
                service.setCurrPath(file.getParent()+"\\");
            this.PathTextField.setText(service.getCurrPath());
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            service.initializingExplorerList(this.ExplorerList,service.getCurrPath());
        }
    }

    @FXML
    void ifEnterKeyIsTyped(KeyEvent event) {
        char znak = event.getCharacter().charAt(0);
        File file = new File(this.PathTextField.getText());
        if(znak == 13)
            if(!this.PathTextField.getText().isEmpty() && file.exists() && file.isDirectory()){
                service.setCurrPath(PathTextField.getText());
                this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
                service.initializingExplorerList(this.ExplorerList,service.getCurrPath());
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
    void copySelectedFile() throws IOException {
        if(otherChild == null)
            otherChild = main.getOtherController(this);
        String pathToCopy="empty";
        String destinationPath = otherChild.PathTextField.getText();
        if(!this.ExplorerList.getSelectionModel().isEmpty())
            pathToCopy = service.getCurrPath()+this.ExplorerList.getSelectionModel().getSelectedItem();
        File src = new File(pathToCopy);
        File dst = new File(destinationPath+src.getName());
        if(src.isFile())
        Files.copy(src.toPath(),dst.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    @FXML
    void deleteSelectedFile() {
        String pathToDelete = "wrong";
        if(!this.ExplorerList.getSelectionModel().isEmpty())
            pathToDelete = service.getCurrPath()+this.ExplorerList.getSelectionModel().getSelectedItem();
        File file = new File(pathToDelete);
        file.delete();
        if(this.main.getOtherController(this).service.getCurrPath() == this.service.getCurrPath()){
            System.out.println("Kappa");
            main.getOtherController(this).ExplorerList.getItems().clear();
            main.getOtherController(this).service.initializingExplorerList(main.getOtherController(this).ExplorerList,main.getOtherController(this).service.getCurrPath());
        }
        this.ExplorerList.getItems().clear();
        service.initializingExplorerList(this.ExplorerList,service.getCurrPath());
    }

    @FXML
    public void initialize(){
        service.initializeDrivers(this.DriveComboBox);
        this.DriveComboBox.getSelectionModel().select(0);
        initializnigPathTextField();
        service.initializingExplorerList(this.ExplorerList,service.getCurrPath());
    }



}
