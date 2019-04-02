package kanbanGUI.controllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import kanbanGUI.Main;
import kanbanGUI.models.Priority;
import kanbanGUI.models.TaskCell;
import kanbanGUI.models.TaskContextMenu;
import kanbanGUI.models.TaskModel;

public class MainController {
    @FXML
    public ListView<TaskModel> toDo_list;

    @FXML
    public ListView<TaskModel> in_progress_list;

    @FXML
    public ListView<TaskModel> done_list;
    private static ObservableList<TaskModel> toDoElementsListModel = FXCollections.observableArrayList();
    private static ObservableList<TaskModel> inProgressListModel = FXCollections.observableArrayList();
    private static ObservableList<TaskModel> doneListModel = FXCollections.observableArrayList();
    private static ObservableList<TaskModel> actualListModel = FXCollections.observableArrayList();
    private static Stage addElementStage;
    private static Parent editElementRoot;
    private static TaskModel actualTask;
    private static int indexOfTaskCell;
    private static Scene addItemScene;
    private static ListView<TaskModel> actualList;
    private static ContextMenu taskContextMenu;
    private static ListView<TaskModel> toDoElementsList;
    private static ListView<TaskModel> inProgressList;
    private static ListView<TaskModel> doneList;

    public static ListView<TaskModel> getActualList() {
        return actualList;
    }

    public static ObservableList<TaskModel> getActualListModel() {
        return actualListModel;
    }

    public static TaskModel getActualTask() {
        return actualTask;
    }

    public static Parent getEditElementRoot() {
        return editElementRoot;
    }

    public static int getIndexOfTaskCell() {
        return indexOfTaskCell;
    }

    public static Scene getAddItemScene() {
        return addItemScene;
    }

    public static Stage getAddElementStage() {
        return addElementStage;
    }

    public static ObservableList<TaskModel> getToDoElementsListModel() {
        return toDoElementsListModel;
    }

    public static ListView<TaskModel> getToDoElementsList() {
        return toDoElementsList;
    }

    public void buttonClick(ActionEvent event) throws IOException {
        addElementStage = new Stage();
        Parent addElementRoot = FXMLLoader.load(getClass().getResource("templates/AddNewTask.fxml"));
        addElementStage.setTitle("Add new task");
        addItemScene = new Scene(addElementRoot, 500, 400);
        addElementStage.setScene(addItemScene);
        ComboBox<Priority> taskPriority = (ComboBox<Priority>) getAddItemScene().lookup("#add_priority_picker");
        ObservableList<Priority> items = createPriorityList();

        taskPriority.setItems(items);
        taskPriority.getSelectionModel().selectFirst();
        addElementStage.initModality(Modality.WINDOW_MODAL);
        addElementStage.initOwner(Main.getPrimaryStage());
        addElementStage.setResizable(false);
        addElementStage.getIcons().add(new Image("kanbanGUI/img/add-file.png"));
        addElementStage.show();
        toDoElementsList = toDo_list;
        inProgressList = in_progress_list;
        doneList = done_list;

        editElementRoot = FXMLLoader.load(getClass().getResource("templates/EditTask.fxml"));
        taskContextMenu = TaskContextMenu.createContextMenu();

        toDoElementsList.setCellFactory(cellFactioryCallback);

    }
    public void menuClick(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        if(source.getText().equals("Close")){
            Main.getPrimaryStage().close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About author");
            alert.setHeaderText(null);
            alert.setContentText("Copyright \u00a9 Tomasz Smoleń");
            alert.showAndWait();
        }
    }
    public void toDo_to_inProgress(ActionEvent event){
            if(toDoElementsListModel.size() > 0){
            inProgressListModel.add(actualTask);
            toDoElementsListModel.remove(indexOfTaskCell);
            inProgressList.setItems(inProgressListModel);
            inProgressList.setCellFactory(cellFactioryCallback);
            toDoElementsList.refresh();
            }
            else
            System.out.println("To do list is empty!");


    }
    public void inProgress_to_toDo(ActionEvent event){

            if(inProgressListModel.size() > 0) {
                toDoElementsListModel.add(actualTask);
                inProgressListModel.remove(indexOfTaskCell);
                toDoElementsList.setItems(toDoElementsListModel);
                inProgressList.refresh();
            }
            else
            System.out.println("In progress list is empty!");

    }
    public void inProgress_to_doneList(ActionEvent event){
        if(inProgressListModel.size() > 0){
            doneListModel.add(actualTask);
            inProgressListModel.remove(indexOfTaskCell);
            doneList.setItems(doneListModel);
            doneList.setCellFactory(cellFactioryCallback);
            inProgressList.refresh();
        }
        else
            System.out.println("In progress list is empty!");

    }
    private Callback<ListView<TaskModel>, ListCell<TaskModel>> cellFactioryCallback = new Callback<ListView<TaskModel>, ListCell<TaskModel>>() {
        @Override
        public ListCell<TaskModel> call(ListView<TaskModel> list) {
            final ListCell<TaskModel> cell = new TaskCell();
            cell.setOnMouseEntered(event1 -> {
                if (cell.getText() != null) {
                    final Tooltip tooltip = new Tooltip();
                    tooltip.setText(((TaskCell) cell).getCellDescription());
                    cell.setTooltip(tooltip);
                } else {
                    cell.setTooltip(null);
                }
            });
            cell.setOnMouseReleased(event2 -> {
                TaskCell source = (TaskCell) event2.getSource();
                if (cell.getText() != null) {
                    if (event2.getButton() == MouseButton.SECONDARY) {
                        cell.setOnContextMenuRequested(event12 -> taskContextMenu.show(cell, event12.getScreenX(), event12.getScreenY()));
                    }
                    actualTask = list.getSelectionModel().getSelectedItem();
                    actualListModel = list.getItems();
                    actualList = list;
                    indexOfTaskCell = source.getIndex();
                    getToDoElementsList().getSelectionModel().clearSelection();
                } else {
                    cell.setOnContextMenuRequested(null);
                }
            });
            return cell;
        }
    };
    public static ObservableList<Priority> createPriorityList(){
        ObservableList<Priority> observableListObject = FXCollections.observableArrayList();
        observableListObject.add(Priority.High);
        observableListObject.add(Priority.Normal);
        observableListObject.add(Priority.Low);

        return observableListObject;
    }
}
