package kanbanGUI.controllers;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import kanbanGUI.Main;
import kanbanGUI.models.Priority;
import kanbanGUI.models.TaskCell;
import kanbanGUI.models.TaskContextMenu;
import kanbanGUI.models.TaskModel;

public class MainController implements Initializable {
    @FXML
    public ListView<TaskModel> toDo_list;

    @FXML
    public ListView<TaskModel> in_progress_list;

    @FXML
    public ListView<TaskModel> done_list;
    private static ObservableList<TaskModel> toDoElementsListModel = FXCollections.observableArrayList();
    private static ObservableList<TaskModel> inProgressListModel = FXCollections.observableArrayList();
    private static ObservableList<TaskModel> doneListModel = FXCollections.observableArrayList();
    private static ObservableList<TaskModel> actualListModel = FXCollections.observableArrayList();
    private static Stage addElementStage;
    private static Parent editElementRoot;
    private static TaskModel actualTask;
    private static int indexOfTaskCell;
    private static Scene addItemScene;
    private static ListView<TaskModel> actualList;
    private static ContextMenu taskContextMenu;
    private static ListView<TaskModel> toDoElementsList;
    private static ListView<TaskModel> inProgressList;
    private static ListView<TaskModel> doneList;

    public static ListView<TaskModel> getActualList() {
        return actualList;
    }

    public static ObservableList<TaskModel> getActualListModel() {
        return actualListModel;
    }

    public static TaskModel getActualTask() {
        return actualTask;
    }

    public static Parent getEditElementRoot() {
        return editElementRoot;
    }

    public static int getIndexOfTaskCell() {
        return indexOfTaskCell;
    }

    public static Scene getAddItemScene() {
        return addItemScene;
    }

    public static Stage getAddElementStage() {
        return addElementStage;
    }

    public static ObservableList<TaskModel> getToDoElementsListModel() {
        return toDoElementsListModel;
    }

    public static ListView<TaskModel> getToDoElementsList() {
        return toDoElementsList;
    }


    @FXML
    public void exportJSON(ActionEvent event){

        JSONObject mainObject = new JSONObject();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Where do you want to save?");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileToExport = fileChooser.showSaveDialog(Main.getPrimaryStage());
        if(fileToExport != null) {
            try {
                mainObject.put("toDoList", createListObject(toDoElementsListModel));
                mainObject.put("inProgressList", createListObject(inProgressListModel));
                mainObject.put("doneList", createListObject(doneListModel));

                FileWriter fileToWrite = new FileWriter(fileToExport.getPath());
                fileToWrite.write(mainObject.toString());
                fileToWrite.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void exportCSV(ActionEvent event) throws IOException {
        FileChooser fileChooser3 = new FileChooser();
        fileChooser3.setTitle("Where do you want to save?");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser3.getExtensionFilters().add(extFilter);
        File fileToExport = fileChooser3.showSaveDialog(Main.getPrimaryStage());
        if(fileToExport != null) {
            FileWriter csvWriter = new FileWriter(fileToExport.getPath());

            csvWriter.append("Name");
            csvWriter.append(",");
            csvWriter.append("Description");
            csvWriter.append(",");
            csvWriter.append("Expiry");
            csvWriter.append(",");
            csvWriter.append("Priority");
            csvWriter.append(",");
            csvWriter.append("ListName");
            csvWriter.append("\n");

            createCSVFile(csvWriter, toDoElementsListModel, "toDoList");
            createCSVFile(csvWriter, inProgressListModel, "inProgressList");
            createCSVFile(csvWriter, doneListModel, "doneList");

            csvWriter.flush();
            csvWriter.close();
        }
    }
    @FXML
    public void importJSON(ActionEvent event){
        FileChooser fileChooser2 = new FileChooser();
        fileChooser2.setTitle("Where is file you want to load?");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser2.getExtensionFilters().add(extFilter);
        File filetoImport = fileChooser2.showOpenDialog(Main.getPrimaryStage());
        if(filetoImport != null) {
            try {
                FileReader objectFromFile = new FileReader(filetoImport.getPath());

                int content;
                StringBuilder jsonOutputBuilder = new StringBuilder();
                while ((content = objectFromFile.read()) != -1) {
                    jsonOutputBuilder.append((char)content);
                }
                String jsonOutputString = jsonOutputBuilder.toString();

                JSONObject obj = new Gson().fromJson(jsonOutputString,JSONObject.class);


                JSONObject toDoListJSON = new JSONObject((Map) obj.get("toDoList"));
                JSONObject inProgressListJSON = new JSONObject((Map) obj.get("inProgressList"));
                JSONObject doneListJSON = new JSONObject((Map) obj.get("doneList"));

                toDoElementsListModel.clear();
                inProgressListModel.clear();
                doneListModel.clear();

                readListFromJSON(toDoListJSON,toDoElementsListModel,toDoElementsList);
                readListFromJSON(inProgressListJSON,inProgressListModel,in_progress_list);
                readListFromJSON(doneListJSON,doneListModel,done_list);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void importCSV(ActionEvent event){
        FileChooser fileChooser4 = new FileChooser();
        fileChooser4.setTitle("Where is file you want to load?");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser4.getExtensionFilters().add(extFilter);
        File filetoImport = fileChooser4.showOpenDialog(Main.getPrimaryStage());
        if(filetoImport != null) {
            BufferedReader csvReader = null;
            toDoElementsListModel.clear();
            inProgressListModel.clear();
            doneListModel.clear();
            try {
                csvReader = new BufferedReader(new FileReader(filetoImport.getPath()));
                String content;
                csvReader.readLine();
                while ((content = csvReader.readLine()) != null) {
                    String[] data = content.split(",");
                        if(data[data.length-1].equals("toDoList")){
                            TaskModel readedTask = new TaskModel(data[0],data[1],TaskModel.toLocalDate(data[2]),Priority.toPriority(data[3]));
                            toDoElementsListModel.add(readedTask);
                            toDoElementsList.setItems(toDoElementsListModel);
                            toDoElementsList.refresh();
                        }
                        else if(data[data.length-1].equals("inProgressList")){
                            TaskModel readedTask = new TaskModel(data[0],data[1],TaskModel.toLocalDate(data[2]),Priority.toPriority(data[3]));
                            inProgressListModel.add(readedTask);
                            inProgressList.setItems(inProgressListModel);
                            inProgressList.refresh();
                        }
                        else{
                            TaskModel readedTask = new TaskModel(data[0],data[1],TaskModel.toLocalDate(data[2]),Priority.toPriority(data[3]));
                            doneListModel.add(readedTask);
                            doneList.setItems(doneListModel);
                            doneList.refresh();
                        }
                }
                csvReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void buttonClick(ActionEvent event) throws IOException {
        addElementStage = new Stage();
        Parent addElementRoot = FXMLLoader.load(getClass().getResource("/kanbanGUI/templates/AddNewTask.fxml"));
        addElementStage.setTitle("Add new task");
        addItemScene = new Scene(addElementRoot, 500, 400);
        addElementStage.setScene(addItemScene);
        ComboBox<Priority> taskPriority = (ComboBox<Priority>) getAddItemScene().lookup("#add_priority_picker");
        ObservableList<Priority> items = createPriorityList();

        taskPriority.setItems(items);
        taskPriority.getSelectionModel().selectFirst();
        addElementStage.initModality(Modality.WINDOW_MODAL);
        addElementStage.initOwner(Main.getPrimaryStage());
        addElementStage.setResizable(false);
        addElementStage.getIcons().add(new Image("kanbanGUI/img/add-file.png"));
        addElementStage.show();


        editElementRoot = FXMLLoader.load(getClass().getResource("/kanbanGUI/templates/EditTask.fxml"));
        taskContextMenu = TaskContextMenu.createContextMenu();

        toDoElementsList.setCellFactory(cellFactioryCallback);

    }
    public void menuClick(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        if(source.getText().equals("Close")){
            Main.getPrimaryStage().close();
        }
        else if(source.getText().equals("Save")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Where do you want to save?");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BIN files (*.bin)", "*.bin");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
            if(file != null){
            try (ObjectOutputStream outputData = new ObjectOutputStream(new FileOutputStream(file.getPath()))) {
                List<TaskModel> toDoElements = toDoElementsList.getItems();
                ArrayList<TaskModel> toDoElementsConverted = convertList(toDoElements);

                List<TaskModel> inProgressElements = inProgressList.getItems();
                ArrayList<TaskModel> inProgressElementsConverted = convertList(inProgressElements);

                List<TaskModel> doneListElements = doneList.getItems();
                ArrayList<TaskModel> doneListElementsConverted = convertList(doneListElements);

                ArrayList<ArrayList<TaskModel>> turboLista = new ArrayList<>();

                turboLista.add(toDoElementsConverted);
                turboLista.add(inProgressElementsConverted);
                turboLista.add(doneListElementsConverted);

                outputData.writeObject(turboLista);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
        else if(source.getText().equals("Open")){
            FileChooser fileChooser2 = new FileChooser();
            fileChooser2.setTitle("Where is file you want to load?");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BIN files (*.bin)", "*.bin");
            fileChooser2.getExtensionFilters().add(extFilter);
            File file = fileChooser2.showOpenDialog(Main.getPrimaryStage());
            if(file != null) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file.getPath()))) {
                    ArrayList<ArrayList<TaskModel>> turboLista = (ArrayList<ArrayList<TaskModel>>) inputStream.readObject();

                    toDoElementsListModel = FXCollections.observableArrayList(turboLista.get(0));
                    inProgressListModel = FXCollections.observableArrayList(turboLista.get(1));
                    doneListModel = FXCollections.observableArrayList(turboLista.get(2));

                    toDoElementsList.setItems(toDoElementsListModel);
                    inProgressList.setItems(inProgressListModel);
                    doneList.setItems(doneListModel);

                    toDoElementsList.refresh();
                    inProgressList.refresh();
                    doneList.refresh();
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About author");
            alert.setHeaderText(null);
            alert.setContentText("Copyright \u00a9 Tomasz Smoleń");
            alert.showAndWait();
        }
    }
    public void toDo_to_inProgress(ActionEvent event){
            if(toDoElementsListModel.size() > 0){
            inProgressListModel.add(actualTask);
            toDoElementsListModel.remove(indexOfTaskCell);
            inProgressList.setItems(inProgressListModel);
            inProgressList.setCellFactory(cellFactioryCallback);
            toDoElementsList.refresh();
            }
            else
            System.out.println("To do list is empty!");


    }
    public void inProgress_to_toDo(ActionEvent event){

            if(inProgressListModel.size() > 0) {
                toDoElementsListModel.add(actualTask);
                inProgressListModel.remove(indexOfTaskCell);
                toDoElementsList.setItems(toDoElementsListModel);
                inProgressList.refresh();
            }
            else
            System.out.println("In progress list is empty!");

    }
    public void inProgress_to_doneList(ActionEvent event){
        if(inProgressListModel.size() > 0){
            doneListModel.add(actualTask);
            inProgressListModel.remove(indexOfTaskCell);
            doneList.setItems(doneListModel);
            doneList.setCellFactory(cellFactioryCallback);
            inProgressList.refresh();
        }
        else
            System.out.println("In progress list is empty!");

    }
    private Callback<ListView<TaskModel>, ListCell<TaskModel>> cellFactioryCallback = new Callback<ListView<TaskModel>, ListCell<TaskModel>>() {
        @Override
        public ListCell<TaskModel> call(ListView<TaskModel> list) {
            final ListCell<TaskModel> cell = new TaskCell();
            cell.setOnMouseEntered(event1 -> {
                if (cell.getText() != null) {
                    final Tooltip tooltip = new Tooltip();
                    tooltip.setText("Description: " + ((TaskCell) cell).getCellDescription() + "\n" + "Task name: " + ((TaskCell) cell).getCellName() + "\n" + "Task expiry time: " + ((TaskCell) cell).getCellExpiryTime() + "\n" + ((TaskCell) cell).getCellTaskPriority());
                    cell.setTooltip(tooltip);
                } else {
                    cell.setTooltip(null);
                }
            });
            cell.setOnMouseReleased(event2 -> {
                TaskCell source = (TaskCell) event2.getSource();
                if (cell.getText() != null) {
                    if (event2.getButton() == MouseButton.SECONDARY) {
                        cell.setOnContextMenuRequested(event12 -> taskContextMenu.show(cell, event12.getScreenX(), event12.getScreenY()));
                    }
                    actualTask = list.getSelectionModel().getSelectedItem();
                    actualListModel = list.getItems();
                    actualList = list;
                    indexOfTaskCell = source.getIndex();
                    getToDoElementsList().getSelectionModel().clearSelection();
                } else {
                    cell.setOnContextMenuRequested(null);
                }
            });
            return cell;
        }
    };
    public static ObservableList<Priority> createPriorityList(){
        ObservableList<Priority> observableListObject = FXCollections.observableArrayList();
        observableListObject.add(Priority.High);
        observableListObject.add(Priority.Normal);
        observableListObject.add(Priority.Low);

        return observableListObject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toDoElementsList = toDo_list;
        inProgressList = in_progress_list;
        doneList = done_list;
        toDoElementsList.setCellFactory(cellFactioryCallback);
        inProgressList.setCellFactory(cellFactioryCallback);
        doneList.setCellFactory(cellFactioryCallback);
    }
    public ArrayList<TaskModel> convertList(List<TaskModel> listModel){
        ArrayList<TaskModel> convertedList;
        if (listModel instanceof ArrayList<?>) {
            convertedList = (ArrayList<TaskModel>) listModel;
        } else {
            convertedList = new ArrayList<>(listModel);
        }
        return convertedList;
    }
    public JSONObject createListObject(ObservableList<TaskModel> observableModel){
        JSONObject newListObject = new JSONObject();
        for(int i=0;i<observableModel.size();i++){
                JSONObject tmpObject = new JSONObject();
                tmpObject.put("title",observableModel.get(i).getTaskName());
                tmpObject.put("expiry",observableModel.get(i).getExpiryDate());
                tmpObject.put("priority",observableModel.get(i).getTaskPriority());
                tmpObject.put("description",observableModel.get(i).getTaskDescription());
                newListObject.put("task"+i,tmpObject);

        }
        return newListObject;
    }
    public void readListFromJSON(JSONObject listJSON, ObservableList<TaskModel> listModel, ListView<TaskModel> list){
        for(int i=0;i<listJSON.size();i++){
            JSONObject taskObject = new JSONObject((Map)listJSON.get("task"+i));
            TaskModel newTask = new TaskModel(taskObject.get("title").toString(), taskObject.get("description").toString(), TaskModel.toLocalDate(taskObject.get("expiry").toString()), Priority.toPriority(taskObject.get("priority").toString()));
            listModel.add(i,newTask);
        }
        list.setItems(listModel);

        list.refresh();
    }
    public void createCSVFile(FileWriter csvWriter, ObservableList<TaskModel> listModel, String listName){
        for(int i=0;i<listModel.size();i++){
            try {
                csvWriter.append(String.join(",", listModel.get(i).getTaskName()));
                csvWriter.append(",");
                csvWriter.append(String.join(",", listModel.get(i).getTaskDescription()));
                csvWriter.append(",");
                csvWriter.append(String.join(",", listModel.get(i).getExpiryDate().toString()));
                csvWriter.append(",");
                csvWriter.append(String.join(",", listModel.get(i).getTaskPriority().toString()));
                csvWriter.append(",");
                csvWriter.append(String.join(",", listName));
                csvWriter.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
