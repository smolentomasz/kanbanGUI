package kanbanGUI;

import java.time.LocalDate;

import javafx.stage.Window;

public class TaskModel extends Window {
    private String taskName;

    public String getTaskDescription() {
        return taskDescription;
    }

    private String taskDescription;
    private LocalDate expiryDate;

    public Priority getTaskPriority() {
        return taskPriority;
    }

    private Priority taskPriority;

    public TaskModel(String taskName, String taskDescription, LocalDate expiryDate, Priority taskPriority) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.expiryDate = expiryDate;
        this.taskPriority = taskPriority;
    }
    @Override
    public String toString(){
        return taskName;
    }
}
