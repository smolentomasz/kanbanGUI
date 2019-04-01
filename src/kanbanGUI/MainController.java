package kanbanGUI;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController {
    private static ObservableList<TaskModel> toDoElementsListModel = FXCollections.observableArrayList();
    private static Stage addElementStage;
    private static ListView<TaskModel> toDoElementsList;
    private static Scene addItemScene;

    @FXML
    public ListView<TaskModel> toDo_list;

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
        Parent addElementRoot = FXMLLoader.load(getClass().getResource("AddNewTask.fxml"));
        addElementStage.setTitle("Add new task");
        addItemScene = new Scene(addElementRoot, 500, 400);
        addElementStage.setScene(addItemScene);
        ComboBox<Priority> taskPriority = (ComboBox<Priority>) getAddItemScene().lookup("#add_priority_picker");
        ObservableList<Priority> items = FXCollections.observableArrayList();
        items.add(Priority.High);
        items.add(Priority.Normal);
        items.add(Priority.Low);
        taskPriority.setItems(items);
        taskPriority.getSelectionModel().selectFirst();
        addElementStage.initModality(Modality.WINDOW_MODAL);
        addElementStage.initOwner(Main.getPrimaryStage());
        addElementStage.show();
        toDoElementsList = toDo_list;
        toDoElementsList.setCellFactory(list -> {
            final ListCell<TaskModel> cell = new TaskCell();
            cell.setOnMouseEntered(event1 -> {
                if(cell.getText() != null){
                    Rectangle rect = new Rectangle(0, 0, 100, 100);
                    final Tooltip tooltip = new Tooltip();
                    tooltip.setText(((TaskCell) cell).getCellDescription());
                    Tooltip.install(rect, tooltip);
                    cell.setTooltip(tooltip);
                }
            });
            cell.setOnMouseReleased(event2 -> {
                if(cell.getText() != null){
                    if(event2.getButton() == MouseButton.SECONDARY){
                       ContextMenu menu = new TaskContextMenu();
                       cell.setContextMenu(menu);
                       menu.show(cell,event2.getScreenX(), event2.getScreenY());
                    }
                }
            });
            return cell;
        });

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

}