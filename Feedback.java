package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Feedback extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Feedback");

        // Creare eticheta pentru intrebare
        Label questionLabel = new Label("Tell us your opinion about our app?");

        // Creare zona pentru introducerea feedback-ului
        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Write your feedback...");
        feedbackArea.setMaxSize(300, 100); // Seteaza dimensiunea maxima la 300x100 pixeli

        // Creare eticheta pentru evaluare satisfactie
        Label ratingLabel = new Label("How satisfied are you with the image processing?");
        
        // Creare butoane radio pentru evaluare
        ToggleGroup ratingGroup = new ToggleGroup();
        RadioButton excellentRadioButton = new RadioButton("Very Satisfied");
        RadioButton goodRadioButton = new RadioButton("Satisfied");
        RadioButton poorRadioButton = new RadioButton("Dissatisfied");
        excellentRadioButton.setToggleGroup(ratingGroup);
        goodRadioButton.setToggleGroup(ratingGroup);
        poorRadioButton.setToggleGroup(ratingGroup);

        // Creare eticheta pentru slider
        Label sliderLabel = new Label("Would you recommend our application?");
        
        // Creare slider pentru evaluare recomandare
        Slider usabilitySlider = new Slider();
        usabilitySlider.setMin(0);
        usabilitySlider.setMax(10);
        usabilitySlider.setShowTickLabels(true);
        usabilitySlider.setShowTickMarks(true);

        // Creare eticheta pentru afisarea valorii slider-ului
        Label sliderValueLabel = new Label("0.0");
        sliderValueLabel.textProperty().bind(usabilitySlider.valueProperty().asString("%.1f"));

        // Creare buton "Back"
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            Filters filtersPage = new Filters();
            Stage filtersStage = new Stage();
            try {
                filtersPage.start(filtersStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            primaryStage.close();
        });

        // Creare buton "Submit Feedback"
        Button submitButton = new Button("Submit Feedback");
        submitButton.setOnAction(event -> {
            String feedback = feedbackArea.getText();
            String rating = ((RadioButton) ratingGroup.getSelectedToggle()).getText();
            double usabilityRating = usabilitySlider.getValue();

            if (!feedback.isEmpty()) {
                // Trimite feedback-ul catre metoda de gestionare a feedback-ului
                handleFeedback(feedback, rating, usabilityRating);
                // Afiseaza un dialog de confirmare
                showAlert("Feedback Sent", "Your feedback has been successfully submitted! Thank you!");
                // Curata zona de feedback dupa trimitere
                feedbackArea.clear();
                // Deselecteaza butonul radio
                ratingGroup.selectToggle(null);
                // Reseteaza valoarea slider-ului
                usabilitySlider.setValue(0);
            } else {
                // Afiseaza un mesaj de eroare daca zona de feedback este goala
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter your feedback before submitting.");
                alert.showAndWait();
            }
        });

        // Creare layout pentru zona de feedback
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);
        layout.add(questionLabel, 0, 0, 2, 1);
        layout.add(feedbackArea, 0, 1, 2, 1);
        layout.add(ratingLabel, 0, 2, 2, 1);
        layout.add(excellentRadioButton, 0, 3);
        layout.add(goodRadioButton, 0, 4);
        layout.add(poorRadioButton, 0, 5);
        layout.add(sliderLabel, 0, 6, 2, 1);
        layout.add(usabilitySlider, 0, 7);
        layout.add(sliderValueLabel, 1, 7);
        layout.add(submitButton, 0, 8);
        layout.add(backButton, 4, 8);

        // Afisare scena
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metoda pentru gestionarea feedback-ului
    private void handleFeedback(String feedback, String rating, double usabilityRating) {
        // Aici puteti implementa logica de gestionare a feedback-ului, cum ar fi trimiterea feedback-ului catre un server sau salvarea intr-o baza de date
        System.out.println("Feedback primit: " + feedback);
        System.out.println("Rating: " + rating);
        System.out.println("Rating de utilizare: " + usabilityRating);
    }

    // Metoda pentru afisarea unui mesaj intr-o fereastra de dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
