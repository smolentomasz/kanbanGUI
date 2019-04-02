package kanbanGUI.models;

import java.time.LocalDate;

import javafx.stage.Window;

public class TaskModel extends Window {
    private String taskName;
    private String taskDescription;
    private LocalDate expiryDate;
    private Priority taskPriority;

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Priority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setTaskPriority(Priority taskPriority) {
        this.taskPriority = taskPriority;
    }

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
