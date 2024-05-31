package application;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessingApp extends Application {

    private ImageView imageView;
    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private String path;

    @Override
    public void start(Stage primaryStage) {
        // Crearea interfeței grafice
        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        Label sourceLabel = new Label("Calea către fișierul sursă:");
        TextField sourceField = new TextField();
        Button browseButton = new Button("Răsfoiește...");
        browseButton.setOnAction(e -> browseFile(primaryStage, sourceField));

        Button processButton = new Button("Procesare Imagine");
        processButton.setOnAction(e -> processImage(sourceField.getText()));

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);

        root.add(sourceLabel, 0, 0);
        root.add(sourceField, 1, 0);
        root.add(browseButton, 2, 0);
        root.add(processButton, 1, 1);
        root.add(imageView, 0, 2, 3, 1);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Procesare Imagine");
        primaryStage.show();
    }

    private void browseFile(Stage stage, TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selectați fișierul sursă");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            textField.setText(file.getPath());
        }
    }

    private void processImage(String filePath) {
        // Implementarea procesării imaginii aici
        // Aici puteți integra codul de procesare a imaginilor din metoda "method" din codul Swing
        // Assigurați-vă că operațiile de procesare a imaginilor sunt efectuate pe un alt fir de execuție decât firul JavaFX
    }

    public static void main(String[] args) {
        launch(args);
    }
}
