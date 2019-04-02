package kanbanGUI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditTaskController {
    @FXML
    private TextField edit_title_label;

    @FXML
    private DatePicker edit_expiry_picker;

    @FXML
    private TextArea edit_description_area;

    @FXML
    private ComboBox<Priority> edit_priority_picker;

    public void btEdit(ActionEvent event) throws IOException {
        MainController.getActualTask().setTaskName(edit_title_label.getText());
        MainController.getActualTask().setExpiryDate(edit_expiry_picker.getValue());
        MainController.getActualTask().setTaskPriority(edit_priority_picker.getValue());
        MainController.getActualTask().setTaskDescription(edit_description_area.getText());
        MainController.getActualList().refresh();
        TaskContextMenu.getEditElementStage().close();
    }
    public void btGetCurrent(ActionEvent event){
        edit_title_label.setText(MainController.getActualTask().getTaskName());
        edit_description_area.setText(MainController.getActualTask().getTaskDescription());
        edit_expiry_picker.setValue(MainController.getActualTask().getExpiryDate());
        edit_priority_picker.getSelectionModel().select(MainController.getActualTask().getTaskPriority());
    }
}
