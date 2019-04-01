package kanbanGUI;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class TaskContextMenu extends ContextMenu {

    TaskContextMenu(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Edit");

        contextMenu.getItems().add(deleteItem);
        contextMenu.getItems().add(editItem);
    }



}
