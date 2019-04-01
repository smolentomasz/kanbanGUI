package kanbanGUI;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TaskCell extends ListCell<TaskModel> {
    public String getCellDescription() {
        return cellDescription;
    }

    private String cellDescription;
    @Override
    public void updateItem(TaskModel item, boolean empty) {
        super.updateItem(item,empty);
        Rectangle rect = new Rectangle(10, 10);
        if (item != null) {
            cellDescription = item.getTaskDescription();
            if(item.getTaskPriority() == Priority.High){
                rect.setFill(Color.RED);
                setGraphic(rect);
                setText(item.toString() + " - " + item.getTaskPriority());
            }
            else if(item.getTaskPriority() == Priority.Normal){
                rect.setFill(Color.ORANGE);
                setGraphic(rect);
                setText(item.toString() + " - " + item.getTaskPriority());
            }
            else{
                rect.setFill(Color.GREEN);
                setGraphic(rect);
                setText(item.toString() + " - " + item.getTaskPriority());            }

        }
        else{
            setGraphic(null);
        }
    }
}
