package application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.concurrent.Task;

public class ImageProcessing extends Application {

    private ImageView imageView; // Obiect pentru afișarea imaginii
    private ProgressIndicator progressIndicator; // Indicator pentru progresul procesării imaginii
    private String filePath; // Calea fișierului selectat

    public void start(Stage primaryStage) {

        // Crearea unui GridPane pentru a organiza layout-ul aplicației
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 0, 0, 0)); // Setarea marginii interioare
        root.setHgap(10); // Setarea distanței orizontale între coloane
        root.setVgap(10); // Setarea distanței verticale între rânduri

        // Crearea și adăugarea elementelor UI în GridPane
        Label sourceLabel = new Label("File's path");
        TextField sourceField = new TextField();
        Button browseButton = new Button("Search");
        browseButton.setOnAction(e -> browseFile(primaryStage, sourceField)); // Definirea acțiunii butonului de căutare fișier

        sourceField.setMinWidth(310); // Setarea lățimii minime a câmpului de text

        root.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false); // Ascunde indicatorul de progres inițial

        Button processButton = new Button("Edge Extraction"); // Buton pentru procesarea imaginii

        // Crearea unui hyperlink
        Hyperlink hyperlink = new Hyperlink("link");

        hyperlink.setOnAction(event -> {
            // Obține URL-ul pe care dorești să îl deschizi
            String url = "https://www.freepik.com/popular-photos";
            // Deschide URL-ul într-un browser web
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (java.io.IOException | java.net.URISyntaxException e) {
                e.printStackTrace(); // sau afișează o alertă în caz de eroare
            }
        });

        // Adaugă label-ul și hyperlink-ul în GridPane
        root.add(new Label("Search an image:"), 0, 3);
        root.add(hyperlink, 0, 4);

        processButton.setOnAction(e -> {
            progressIndicator.setVisible(true);
            Task<BufferedImage> task = new Task<BufferedImage>() {
                @Override
                protected BufferedImage call() throws Exception {
                    String imagePath = sourceField.getText();
                    BufferedImage processedImage = processImage(imagePath);
                    return processedImage;
                }
            };

            task.setOnSucceeded(event -> {
                progressIndicator.setVisible(false);
                BufferedImage processedImage = task.getValue();
                if (processedImage != null) {
                    imageView.setImage(SwingFXUtils.toFXImage(processedImage, null));
                }
            });

            Thread thread = new Thread(task);
            thread.start();
        });

        imageView = new ImageView(); // Obiect pentru afișarea imaginii procesate
        imageView.setPreserveRatio(true); // Menține raportul de aspect
        imageView.setFitWidth(400); // Setează lățimea imaginii
        imageView.setFitHeight(300); // Setează înălțimea imaginii

        // Adăugarea elementelor în GridPane
        root.add(sourceLabel, 0, 0);
        root.add(sourceField, 0, 1);
        root.add(browseButton, 1, 1);
        root.add(processButton, 2, 1);
        root.add(imageView, 0, 2, 4, 1);
        root.add(progressIndicator, 0, 2, 3, 1);

        GridPane.setHalignment(processButton, HPos.CENTER);
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);

        // Crearea scenei și setarea acesteia în fereastra principală
        Scene scene = new Scene(root, 620, 450);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Processed Image");
        primaryStage.show();

        // Crearea butonului "Next" pentru a naviga la pagina următoare
        Button nextButton = new Button("Next");

        nextButton.setOnAction(event -> {
            Filters filtersPage = new Filters();
            Stage filtersStage = new Stage();
            try {
                filtersPage.start(filtersStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            primaryStage.close();
        });

        root.add(nextButton, 4, 4);

        // Crearea butonului "Back" pentru a reveni la pagina anterioară
        Button backButton = new Button("Back");

        backButton.setOnAction(event -> {
            Intro intro = new Intro();
            Stage introstage = new Stage();
            try {
                intro.start(introstage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            primaryStage.close();
        });

        root.add(backButton, 3, 4);
    }

    // Metoda pentru procesarea imaginii
    private BufferedImage processImage(String imagePath) {
        try {
            // Încarcă imaginea originală
            BufferedImage img = ImageIO.read(new File(imagePath));
            
            if (img == null) {
                System.out.println("Eroare la încărcarea imaginii.");
                return null;
            }
            
            // Procesează imaginea
            EdgeExtraction edgeDetection = new EdgeExtraction();
            BufferedImage processedImage = edgeDetection.detect(img);
            
            // Salvează imaginea procesată
            String outputImagePath = getOutputImagePath(imagePath);
            File outputFile = new File(outputImagePath);
            ImageIO.write(processedImage, "png", outputFile);
            
            return processedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Metoda pentru obținerea căii fișierului de ieșire
    private String getOutputImagePath(String imagePath) {
        String fileName = imagePath.substring(0, imagePath.lastIndexOf('.'));
        return fileName + "_processed.png";
    }

    // Metoda pentru a deschide un dialog de selecție a fișierului
    private void browseFile(Stage primaryStage, TextField sourceField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selectează un fișier");
        
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        
        if (selectedFile != null) {
            sourceField.setText(selectedFile.getAbsolutePath());
            this.filePath = selectedFile.getAbsolutePath();
        }
    }
    
    // Getter pentru filePath
    public String getFilePath() {
        return filePath;
    }
    
    // Setter pentru filePath
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
