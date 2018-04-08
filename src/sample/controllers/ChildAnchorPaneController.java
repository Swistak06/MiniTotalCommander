package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import sample.services.ChildControllerService;
import sample.services.FilesManagmentService;

import java.io.File;


public class ChildAnchorPaneController {

    private ChildControllerService childControllerService = new ChildControllerService();

    private FilesManagmentService filesManagmentService = new FilesManagmentService();

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

    @FXML
    private Button goingUpButton;

    @FXML
    private Button moveSelectedFileButton;

    private ChildControllerService getChildControllerService() {
        return childControllerService;
    }

    public void incjectMain(MainAnchorPaneController main){
        this.main = main;
    }

    private void initializnigPathTextField(){
        this.childControllerService.setCurrPath(this.DriveComboBox.getSelectionModel().getSelectedItem());
        this.PathTextField.setText(this.childControllerService.getCurrPath());
    }



    private void isDoubleClicked(){
        String newPath = childControllerService.getCurrPath() + this.ExplorerList.getSelectionModel().getSelectedItem()+"\\";
        if(!new File(newPath).isFile() && !this.ExplorerList.getSelectionModel().isEmpty() && new File(newPath).exists()){
            childControllerService.setCurrPath(newPath);
            this.PathTextField.setText(newPath);
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            childControllerService.initializingExplorerList(this.ExplorerList, childControllerService.getCurrPath());
        }
    }

    @FXML
    public void choosingDriveFromComboBox(ActionEvent event) {
        ComboBox comboBox = (ComboBox)event.getSource();
        if(!comboBox.getSelectionModel().isEmpty()){
            this.childControllerService.setCurrPath(comboBox.getSelectionModel().getSelectedItem().toString());
            this.PathTextField.setText(childControllerService.getCurrPath());
            ExplorerList.getItems().remove(0,ExplorerList.getItems().size());
            childControllerService.initializingExplorerList(ExplorerList, childControllerService.getCurrPath());
        }
    }

    @FXML
    public void choosingDirectoryFromList() {
        if(childControllerService.isElementOfListDoubleClicked())
            isDoubleClicked();
    }


    @FXML
    void goingUp() {
        File file = new File(childControllerService.getCurrPath());
        if(file.getParent() != null){
            if(new File(file.getParent()).getParent() == null)
                childControllerService.setCurrPath(file.getParent());
            else
                childControllerService.setCurrPath(file.getParent()+"\\");
            this.PathTextField.setText(childControllerService.getCurrPath());
            this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
            childControllerService.initializingExplorerList(this.ExplorerList, childControllerService.getCurrPath());
        }
    }

    @FXML
    void ifEnterKeyIsTyped(KeyEvent event) {
        char znak = event.getCharacter().charAt(0);
        File file = new File(this.PathTextField.getText());
        if(znak == 13)
            if(!this.PathTextField.getText().isEmpty() && file.exists() && file.isDirectory()){
                childControllerService.setCurrPath(PathTextField.getText());
                this.ExplorerList.getItems().remove(0,this.ExplorerList.getItems().size());
                childControllerService.initializingExplorerList(this.ExplorerList, childControllerService.getCurrPath());
            }
    }

    @FXML
    public void reloadDiskList(){
        if(!DriveComboBox.getItems().isEmpty()){
            this.DriveComboBox.getSelectionModel().clearSelection();
            this.DriveComboBox.getItems().clear();
            childControllerService.initializeDrivers(this.DriveComboBox);
        }
    }

    @FXML
    void copySelectedFile() {
        //Przypisanie do składowej otherChild referencji do bliźniaczej kontrolki
        if(otherChild == null)
            otherChild = main.getOtherChildController(this);

        //Utworzenie zmiennych przechowujących ścieżki
        String pathToCopy;
        String destinationPath = otherChild.getChildControllerService().getCurrPath();

        //Jeżeli został wybrany z listy jakiś element
        if(!this.ExplorerList.getSelectionModel().isEmpty()){
            pathToCopy = childControllerService.getCurrPath()+this.ExplorerList.getSelectionModel().getSelectedItem();
            filesManagmentService.copySelectedFileToDirection(pathToCopy,destinationPath);
            //Odświeżenie listy w bliźniaczej kontrolce
            otherChild.ExplorerList.getItems().clear();
            otherChild.childControllerService.initializingExplorerList(otherChild.ExplorerList,otherChild.childControllerService.getCurrPath());
        }
    }

    @FXML
    public void deleteSelectedFile() {
        //Przypisanie do składowej otherChild referencji do bliźniaczej kontrolki
        if(otherChild == null)
            otherChild = main.getOtherChildController(this);
        String pathToDelete;
        //Jeżeli został wybrany z listy jakiś element
        if(!this.ExplorerList.getSelectionModel().isEmpty()){
            pathToDelete = childControllerService.getCurrPath()+this.ExplorerList.getSelectionModel().getSelectedItem();
            filesManagmentService.deleteSelectedFile(pathToDelete);
            //Jeżeli obie kontrolki wskazują na tą samą ścieżkę to lista bliźniaczej kontrolki jest odświeżana
            if(otherChild.childControllerService.getCurrPath().equals(this.childControllerService.getCurrPath())){
                otherChild.ExplorerList.getItems().clear();
                otherChild.childControllerService.initializingExplorerList(otherChild.ExplorerList,otherChild.childControllerService.getCurrPath());
            }
            this.ExplorerList.getItems().clear();
            childControllerService.initializingExplorerList(this.ExplorerList, childControllerService.getCurrPath());
        }
    }

    @FXML
    public void moveSelectedFile(){
        //Przypisanie do składowej otherChild referencji do bliźniaczej kontrolki
        if(otherChild == null)
            otherChild = main.getOtherChildController(this);
        String pathToMove;
        String destinationPath = otherChild.getChildControllerService().getCurrPath();

        //Jeżeli został wybrany z listy jakiś element
        if(!this.ExplorerList.getSelectionModel().isEmpty()){
            pathToMove = childControllerService.getCurrPath()+this.ExplorerList.getSelectionModel().getSelectedItem();
            filesManagmentService.moveSelectedFileToDirection(pathToMove,destinationPath);
            //Odświeżenie list w obu kontrolkach
            this.ExplorerList.getItems().clear();
            this.childControllerService.initializingExplorerList(this.ExplorerList,this.childControllerService.getCurrPath());
            otherChild.ExplorerList.getItems().clear();
            otherChild.childControllerService.initializingExplorerList(otherChild.ExplorerList,otherChild.childControllerService.getCurrPath());
        }

    }

    @FXML
    public void initialize(){
        childControllerService.initializeDrivers(this.DriveComboBox);
        this.DriveComboBox.getSelectionModel().select(0);
        initializnigPathTextField();
        childControllerService.initializingExplorerList(this.ExplorerList, childControllerService.getCurrPath());
    }



}
