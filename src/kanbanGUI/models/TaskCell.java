package kanbanGUI.models;

import java.time.LocalDate;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TaskCell extends ListCell<TaskModel> {
    private String cellDescription;

    public String getCellName() {
        return cellName;
    }

    public LocalDate getCellExpiryTime() {
        return cellExpiryTime;
    }

    public Priority getCellTaskPriority() {
        return cellTaskPriority;
    }

    private String cellName;
    private LocalDate cellExpiryTime;
    private Priority cellTaskPriority;
    public String getCellDescription() {
        return cellDescription;
    }

    @Override
    public void updateItem(TaskModel item, boolean empty) {
        super.updateItem(item,empty);
        Rectangle rect = new Rectangle(10, 10);
        if (item != null) {
            cellDescription = item.getTaskDescription();
            cellExpiryTime = item.getExpiryDate();
            cellName = item.getTaskName();
            cellTaskPriority = item.getTaskPriority();
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
                setText(item.toString() + " - " + item.getTaskPriority());
            }
        }
        else{
            setGraphic(null);
            setText(null);
        }
    }
}
