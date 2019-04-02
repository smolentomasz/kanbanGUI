package kanbanGUI.models;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kanbanGUI.Main;
import kanbanGUI.controllers.MainController;

public class TaskContextMenu extends ContextMenu {
    private static ContextMenu contextMenu;
    private static Stage editElementStage;
    private static Scene editItemScene;

    public static Stage getEditElementStage() {
        return editElementStage;
    }

    TaskContextMenu(){

    }

    public static ContextMenu createContextMenu(){
        contextMenu = new ContextMenu();
        editItemScene = new Scene(MainController.getEditElementRoot(), 500, 400);
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction((ActionEvent event) -> {
            MainController.getActualListModel().remove(MainController.getIndexOfTaskCell());
            MainController.getActualList().setItems(MainController.getActualListModel());
        });

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> {
            editElementStage = new Stage();
            editElementStage.setTitle("Edit current task");
            editElementStage.setScene(editItemScene);

            ComboBox<Priority> taskPriorityEdit = (ComboBox<Priority>) editItemScene.lookup("#edit_priority_picker");
            ObservableList<Priority> itemsEdit = MainController.createPriorityList();

            taskPriorityEdit.setItems(itemsEdit);
            taskPriorityEdit.getSelectionModel().selectLast();

            editElementStage.initModality(Modality.WINDOW_MODAL);
            editElementStage.initOwner(Main.getPrimaryStage());
            editElementStage.setResizable(false);
            editElementStage.getIcons().add(new Image("kanbanGUI/img/test.png"));
            editElementStage.show();
            MainController.getAddElementStage().setScene(MainController.getAddItemScene());
        });
        contextMenu.getItems().addAll(deleteItem, editItem);
        return contextMenu;
    }
}
