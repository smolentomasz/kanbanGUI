package kanbanGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("templates/KanbanBoardGUI.fxml"));
        primaryStage.setTitle("Kanban board");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("kanbanGUI/img/folder.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
