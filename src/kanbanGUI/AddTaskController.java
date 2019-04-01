package kanbanGUI;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddTaskController {

    public void btClick (ActionEvent event){
        TextField taskTitle = (TextField) MainController.getAddItemScene().lookup("#add_title_label");
        DatePicker taskExpiryDate = (DatePicker) MainController.getAddItemScene().lookup("#add_expiry_picker");
        ComboBox<Priority> taskPriority = (ComboBox<Priority>) MainController.getAddItemScene().lookup("#add_priority_picker");
        TextArea taskDescription = (TextArea) MainController.getAddItemScene().lookup("#add_description_area");

        MainController.getToDoElementsListModel().add(new TaskModel(taskTitle.getText(), taskDescription.getText(), taskExpiryDate.getValue(), taskPriority.getValue()));

        MainController.getToDoElementsList().setItems(MainController.getToDoElementsListModel());

        MainController.getAddElementStage().close();
    }


}

