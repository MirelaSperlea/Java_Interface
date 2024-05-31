package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Intro extends Application {

    private static String userDataFilePath;

    @Override
    public void start(Stage primaryStage) {
        // Seteaza titlul ferestrei principale
        primaryStage.setTitle("LogIn");

        // Creeaza si configureaza un layout de tip GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // Centreaza grid-ul
        grid.setHgap(10); // Seteaza spatiul orizontal intre coloane
        grid.setVgap(10); // Seteaza spatiul vertical intre randuri
        grid.setPadding(new Insets(25, 25, 25, 25)); // Seteaza padding-ul in jurul grid-ului

        // Creeaza si configureaza eticheta de introducere
        Text introLabel = new Text("Welcome, please enter your personal info!");
        introLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        grid.add(introLabel, 0, 0, 2, 1); // Adauga eticheta de introducere in grid

        // Creeaza si adauga eticheta si campul de text pentru email
        Label userName = new Label("Email:");
        grid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        // Creeaza si adauga eticheta si campul de parola
        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        // Creeaza si adauga caseta de selectare pentru afisarea parolei si caseta de selectare pentru termeni si conditii
        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        CheckBox termsCheckBox = new CheckBox("I agree to the terms and conditions");
        showPasswordCheckBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        termsCheckBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        grid.add(termsCheckBox, 0, 6, 2, 1); // Adauga caseta de selectare pentru termeni in grid
        grid.add(showPasswordCheckBox, 0, 3, 2, 1); // Adauga caseta de selectare pentru afisarea parolei in grid

        // Creeaza si adauga un nod text pentru afisarea mesajelor de actiune
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6, 2, 1);

        // Creeaza si adauga un separator
        Separator separator = new Separator();
        grid.add(separator, 0, 5, 2, 1);

        // Creeaza si configureaza butonul de autentificare
        Button btn = new Button("Sign in");
        btn.setDisable(true); // Dezactiveaza butonul in mod implicit
        HBox hbBtn = new HBox(8); // Creeaza un HBox pentru alinierea butonului
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT); // Aliniaza butonul in partea de jos-dreapta
        hbBtn.getChildren().add(btn); // Adauga butonul in HBox
        grid.add(hbBtn, 1, 7); // Adauga HBox-ul in grid

        // Adauga un hyperlink pentru inregistrare
        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.setOnAction(event -> {
            // Deschide pagina de inregistrare cand hyperlink-ul este apasat
            RegisterPage registerPage = new RegisterPage();
            Stage registerStage = new Stage();
            registerPage.start(registerStage);
        });
        grid.add(registerLink, 0, 8, 2, 1); // Adauga hyperlink-ul de inregistrare in grid

        // activarea butonului Sign in numai atunci cand termsCheckBox este bifat
        termsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            btn.setDisable(!newValue);
        });

        // Seteaza actiunea pentru butonul de autentificare
        btn.setOnAction(e -> {
            // Verifica daca emailul si parola introduse sunt "admin"
            if (userTextField.getText().equals("admin") && pwBox.getText().equals("admin")) {
                // Deschide AdminPage daca utilizatorul este admin
                AdminPage adminPage = new AdminPage();
                Stage adminStage = new Stage();
                try {
                    adminPage.start(adminStage);
                } catch (Exception ex) {
                    ex.printStackTrace(); // Afiseaza exceptiile
                }
                primaryStage.close(); // Inchide fereastra curenta
            } else {
                // In caz contrar, autentifica utilizatorul
                String email = userTextField.getText();
                String password = pwBox.getText();
                if (email.isEmpty() || password.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in both email and password fields!");
                } else {
                    if (authenticateUser(userDataFilePath, email, password)) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
                        // Deschide pagina principala dupa autentificare
                        ImageProcessing imagepro = new ImageProcessing();
                        Stage imgStage = new Stage();
                        try {
                            imagepro.start(imgStage); // Porneste stadiul de procesare a imaginilor
                        } catch (Exception e1) {
                            e1.printStackTrace(); // Afiseaza exceptiile
                        }
                        primaryStage.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid email or password!");
                    }
                }
            }
        });

        // Creeaza si seteaza scena pentru fereastra principala
        Scene scene = new Scene(grid, 300, 295);
        primaryStage.setScene(scene);
        primaryStage.show(); // Afiseaza fereastra principala
    }

    // Metoda pentru autentificarea utilizatorului verificand emailul si parola intr-un fisier
    private boolean authenticateUser(String filename, String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t"); // Imparte linia in email si parola
                if (parts.length == 2 && parts[0].equals(email) && parts[1].equals(password)) {
                    return true; // Returneaza true daca emailul si parola se potrivesc
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Afiseaza exceptiile
        }
        return false; // Returneaza false daca nu se gaseste nicio potrivire
    }

    // Metoda pentru afisarea dialogurilor de alerta
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title); // Seteaza titlul alertei
        alert.setHeaderText(null); // Elimina textul antetului
        alert.setContentText(message); // Seteaza textul continutului alertei
        alert.showAndWait(); // Afiseaza alerta si asteapta actiunea utilizatorului
    }

    public static void main(String[] args) {
        // Cere utilizatorului sa introduca calea catre fisierul userdata.txt din consola
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the userdata.txt file: ");
        userDataFilePath = scanner.nextLine();

        // Lanseaza aplicatia JavaFX
        launch(args);
    }
}
