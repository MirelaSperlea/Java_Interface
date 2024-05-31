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

public class RegisterPage extends Application {

    @Override
    public void start(Stage registerStage) {
        registerStage.setTitle("Register");

        // Crearea unui GridPane pentru a organiza elementele UI
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // Centram grid-ul
        grid.setHgap(10); // Setam spatiul orizontal intre celule
        grid.setVgap(10); // Setam spatiul vertical intre celule
        grid.setPadding(new Insets(25, 25, 25, 25)); // Setam padding-ul din jurul grid-ului

        // Adaugarea unui titlu pentru scena
        Text sceneTitle = new Text("Create an account");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Adaugam titlul in grid la pozitia 0,0 si se extinde pe 2 coloane

        // Eticheta pentru email
        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 1); // Adaugam eticheta la pozitia 0,1 in grid

        // Camp de text pentru email
        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 1); // Adaugam campul de text la pozitia 1,1 in grid

        // Eticheta pentru parola
        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2); // Adaugam eticheta la pozitia 0,2 in grid

        // Camp de parola
        PasswordField pwField = new PasswordField();
        grid.add(pwField, 1, 2); // Adaugam campul de parola la pozitia 1,2 in grid

        // Eticheta pentru confirmarea parolei
        Label confirmPwLabel = new Label("Confirm Password:");
        grid.add(confirmPwLabel, 0, 3); // Adaugam eticheta la pozitia 0,3 in grid

        // Camp de parola pentru confirmarea parolei
        PasswordField confirmPwField = new PasswordField();
        grid.add(confirmPwField, 1, 3); // Adaugam campul de parola la pozitia 1,3 in grid

        // Buton de inregistrare
        Button registerButton = new Button("Register");
        HBox hbBtn = new HBox(10); // Cream un HBox pentru a contine butonul, cu un spatiu de 10 pixeli intre elemente
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT); // Aliniem HBox-ul la dreapta jos
        hbBtn.getChildren().add(registerButton); // Adaugam butonul in HBox
        grid.add(hbBtn, 1, 4); // Adaugam HBox-ul la pozitia 1,4 in grid

        // Text pentru a afisa mesaje de actiune
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 5, 2, 1); // Adaugam textul la pozitia 0,5 si se extinde pe 2 coloane

        // Setarea actiunii pentru butonul de inregistrare
        registerButton.setOnAction(e -> {
            // Verificam daca toate campurile sunt completate
            if (emailTextField.getText().isEmpty() || pwField.getText().isEmpty() || confirmPwField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields!");
            } else if (!pwField.getText().equals(confirmPwField.getText())) {
                // Verificam daca parolele se potrivesc
                showAlert(Alert.AlertType.WARNING, "Warning", "Passwords do not match!");
            } else {
                // Logica pentru inregistrarea utilizatorului
                UserFileWriter.writeUserData(emailTextField.getText(), pwField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");

                // Inchide fereastra de inregistrare si deschide fereastra pentru procesarea imaginii
                registerStage.close();

                // Cream o noua fereastra pentru procesarea imaginii
                ImageProcessing imagepro = new ImageProcessing();
                Stage imgStage = new Stage();
                try {
                    imagepro.start(imgStage); // Pornim fereastra de procesare a imaginii
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Crearea scenei si setarea acesteia pe fereastra
        Scene scene = new Scene(grid, 340, 300);
        registerStage.setScene(scene);
        registerStage.show(); // Afisam fereastra
    }

    // Metoda pentru afisarea alertelor
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Setam header-ul alertei la null
        alert.setContentText(message); // Setam mesajul alertei
        alert.showAndWait(); // Afisam alerta si asteptam raspunsul utilizatorului
    }

    public static void main(String[] args) {
        launch(args); // Lansam aplicatia
    }
}
