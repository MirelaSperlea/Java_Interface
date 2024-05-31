package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminPage extends Application {

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) {
        // Citirea caii catre fisierul userdata.txt de la consola
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
        System.out.print("Introduceti calea catre fisierul userdata.txt: ");
        String filePath = null;
        try {
            filePath = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Citirea datelor din fisierul userdata.txt si parsarea lor intr-o lista de obiecte User
        ObservableList<User> users = FXCollections.observableArrayList();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initializarea TableView si adaugarea coloanelor
        TableView<User> tableView = new TableView<>();
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());

        tableView.getColumns().addAll(emailColumn, passwordColumn);
        tableView.setItems(users);

        // Initializarea ListView si a Label pentru culori
        ListView<String> list = new ListView<>();
        ObservableList<String> data = FXCollections.observableArrayList("black", "blue", "blueviolet", "brown", "red", "green", "yellow");
        final Label label = new Label();
        label.setLayoutX(10);
        label.setLayoutY(115);
        label.setFont(Font.font("Verdana", 20));

        list.setItems(data);

        list.setCellFactory((ListView<String> l) -> new ColorRectCell());

        list.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldVal, newVal) -> {
                    if (newVal.equals("black")) {
                        label.setText("Hello, admin!");
                        label.setTextFill(Color.BLACK);
                    } else if (newVal.equals("blue")) {
                        label.setText("Hello, admin!");
                        label.setTextFill(Color.BLUE);
                    } else if (newVal.equals("blueviolet")) {
                        label.setText("Hello, admin!");
                        label.setTextFill(Color.BLUEVIOLET);
                    } else if (newVal.equals("brown")) {
                        label.setText("Hello, admin!");
                        label.setTextFill(Color.BROWN);
                    } else {
                        label.setText(newVal);
                        label.setTextFill(Color.web(newVal));
                    }
                });

       // Initializarea TreeView
        TreeItem<String> rootNode = new TreeItem<>("Departments");
        rootNode.setExpanded(true);

        // Adaugarea departamentelor
        TreeItem<String> hrNode = new TreeItem<>("Human Resources");
        TreeItem<String> itNode = new TreeItem<>("IT");
        TreeItem<String> financeNode = new TreeItem<>("Finance");

        // Adăugarea departamentelor ca sub-noduri ale nodului rădăcină
        rootNode.getChildren().addAll(hrNode, itNode, financeNode);

        // Crearea unui mesaj de afisare a selectiei
        Label selectionMessage = new Label("Current departament:");

        // Setarea comportamentului la selectarea unui element din TreeView
        TreeView<String> treeView = new TreeView<>(rootNode);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedDepartment = newValue.getValue();
                selectionMessage.setText("" + selectedDepartment);
            }
        });

        // Initializarea VBox pentru culori
        VBox colorVBox = new VBox(list, label);
        colorVBox.setSpacing(10);

        // Incapsularea VBox-ului pentru culori intr-un ScrollPane
        ScrollPane colorScrollPane = new ScrollPane(colorVBox);
        colorScrollPane.setFitToWidth(true);

        // Crearea butonului
        Button feedButton = new Button("Go to Feedback Page");
        feedButton.setOnAction(event -> {
            // Deschiderea paginii FeedTreeTableView
            FeedTreeTableView feedPage = new FeedTreeTableView();
            Stage feedStage = new Stage();
            feedPage.start(feedStage);
        });

        // Initializarea Scene si Stage
        VBox mainVBox = new VBox(tableView, colorScrollPane, treeView, selectionMessage, feedButton);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        Scene scene = new Scene(mainVBox);
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.setWidth(600); // Setarea dimensiunii maxime a ferestrei
        stage.setHeight(300); // Setarea dimensiunii maxime a ferestrei
        stage.show();
    }

    // Clasa ColorRectCell pentru a afisa culoarea selectata in ListView
    static class ColorRectCell extends ListCell<String> {
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(100, 20);
            if (item != null) {
                rect.setFill(Color.web(item));
                setGraphic(rect);
            } else {
                setGraphic(null);
            }
        }
    }

    // Clasa User pentru a modela datele utilizatorilor
    public class User {
        private final StringProperty emailProperty;
        private final StringProperty passwordProperty;

        public User(String email, String password) {
            this.emailProperty = new SimpleStringProperty(email);
            this.passwordProperty = new SimpleStringProperty(password);
        }

        public StringProperty emailProperty() {
            return emailProperty;
        }

        public StringProperty passwordProperty() {
            return passwordProperty;
        }

        public String getEmail() {
            return emailProperty.get();
        }

    }
}
