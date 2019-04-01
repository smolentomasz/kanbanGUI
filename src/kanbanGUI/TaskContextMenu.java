package kanbanGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

public class TaskContextMenu extends ContextMenu {
private static ContextMenu contextMenu;
private static ObservableList<TaskModel> listaDoResetu = FXCollections.observableArrayList();
    TaskContextMenu(){

    }

    public static ContextMenu createContextMenu(){
        contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction((ActionEvent event) -> {
            MainController.getToDoElementsListModel().remove(MainController.getIndexOfTaskCell());
            MainController.getToDoElementsList().setItems(MainController.getToDoElementsListModel());
        });

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> System.out.println("Kliknięto edytuj!"));

        contextMenu.getItems().addAll(deleteItem, editItem);

        return contextMenu;
    }
}
