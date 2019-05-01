package kanbanGUI.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KanbanModel {
    private ObservableList<Priority> priorityList;
    private ObservableList<TaskModel> todoList;
    private ObservableList<TaskModel> inProgressList;
    private ObservableList<TaskModel> doneList;

    private static KanbanModel kanbanModel = new KanbanModel();

    public static KanbanModel getInstance() {
        return kanbanModel;
    }

    private KanbanModel() {
        this.todoList = FXCollections.observableArrayList();
        this.inProgressList = FXCollections.observableArrayList();
        this.doneList = FXCollections.observableArrayList();
        this.priorityList = FXCollections.observableArrayList();
        this.priorityList.addAll(Priority.Low, Priority.Normal, Priority.High);
    }
}
