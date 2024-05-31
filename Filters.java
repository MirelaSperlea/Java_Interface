package application;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Filters extends Application {

    private String filePath; // Calea fisierului selectat

    // Matricile kernel pentru detectarea marginilor (Sobel)
    static int[] sobel_y = { 1, 0, -1, 2, 0, -2, 1, 0, -1 };
    static int[] sobel_x = { 1, 2, 1, 0, 0, 0, -1, -2, -1 };

    // Metoda pentru conversia imaginii la tonuri de gri
    private static BufferedImage greyscale(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                img.setRGB(x, y, p);
            }
        }
        return img;
    }

    // Metoda pentru aplicarea kernel-ului de detectare a marginilor
    private static BufferedImage detect(BufferedImage img, int[] kernel) {
        int height = img.getHeight();
        int width = img.getWidth();
        BufferedImage result = new BufferedImage(width - 1, height - 1, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int[] tmp = {
                    img.getRGB(x - 1, y - 1) & 0xff, img.getRGB(x, y - 1) & 0xff,
                    img.getRGB(x + 1, y - 1) & 0xff, img.getRGB(x - 1, y) & 0xff, img.getRGB(x, y) & 0xff,
                    img.getRGB(x + 1, y) & 0xff, img.getRGB(x - 1, y + 1) & 0xff, img.getRGB(x, y + 1) & 0xff,
                    img.getRGB(x + 1, y + 1) & 0xff
                };
                int value = convolution(kernel, tmp);
                result.setRGB(x, y, 0xff000000 | (value << 16) | (value << 8) | value);
            }
        }
        return result;
    }

    // Metoda pentru calculul convolutiei
    private static int convolution(int[] kernel, int[] pixel) {
        int result = 0;
        for (int i = 0; i < pixel.length; i++) {
            result += kernel[i] * pixel[i];
        }
        return (int) (Math.abs(result) / 9);
    }

    // Metoda pentru afisarea imaginii procesate intr-o fereastra noua
    public static void displayImage(BufferedImage image) {
        WritableImage fxImage = SwingFXUtils.toFXImage(image, null);
        ImageView imageView = new ImageView(fxImage);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(imageView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(new Group(scrollPane));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Processed Image");
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Filters");

        Label sourceLabel = new Label("File's path");
        TextField sourceField = new TextField();
        sourceField.setEditable(false); // Face campul text non-editabil
        Button browseButton = new Button("Search");

        // Creeaza un HBox pentru a alinia campul text si butonul "Cauta" in aceeasi linie
        HBox searchBox = new HBox(5);
        sourceField.setPrefWidth(350); 
        searchBox.getChildren().addAll(sourceField, browseButton); // Adauga campul text si butonul in HBox
        searchBox.setAlignment(Pos.CENTER_LEFT); // Aliniaza continutul la stanga

        // Definirea actiunii butonului "Search" pentru a deschide un dialog de selectie fisier
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save image");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                filePath = selectedFile.getAbsolutePath();
                sourceField.setText(filePath);
            }
        });

        // Creeaza un ChoiceBox pentru selectarea filtrului
        ChoiceBox<String> filtersChoiceBox = new ChoiceBox<>();
        filtersChoiceBox.setItems(FXCollections.observableArrayList("Grayscale", "Edge Detection Vertically", "Edge Detection Horizontally"));
        Label filtersLabel = new Label("Choose a filter");
        ImageView imageView = new ImageView();

        // Definirea actiunii de schimbare a filtrului
        filtersChoiceBox.setOnAction(event -> {
            String selectedFilter = filtersChoiceBox.getValue();
            BufferedImage imgIn = null;
            try {
                imgIn = ImageIO.read(new File(filePath));
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (selectedFilter.equals("Grayscale")) {
                BufferedImage imgGrey = greyscale(imgIn);
                displayImage(imgGrey);
            } else if (selectedFilter.equals("Edge Detection Vertically")) {
                BufferedImage imgGrey = greyscale(imgIn);
                BufferedImage edgesX = detect(imgGrey, sobel_x);
                displayImage(edgesX);
            } else if (selectedFilter.equals("Edge Detection Horizontally")) {
                BufferedImage imgGrey = greyscale(imgIn);
                BufferedImage edgesY = detect(imgGrey, sobel_y);
                displayImage(edgesY);
            }
        });

        // Buton pentru a trece la urmatoarea fereastra
        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> {
            Feedback feedback = new Feedback();
            Stage feedStage = new Stage();
            try {
                feedback.start(feedStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // Buton pentru a reveni la fereastra anterioara
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            ImageProcessing imgP = new ImageProcessing();
            Stage prostage = new Stage();
            try {
                imgP.start(prostage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            primaryStage.close();
        });

        // Creeaza un VBox pentru a organiza layout-ul principal
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));

        // Creeaza un HBox pentru a alinia butoanele "Back" si "Next"
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT); // Aliniaza butoanele la dreapta
        buttonBox.getChildren().addAll(backButton, nextButton);

        // Adauga toate elementele in layout-ul principal
        mainLayout.getChildren().addAll(sourceLabel, searchBox, new VBox(20), filtersLabel, filtersChoiceBox, imageView, buttonBox);

        // Seteaza scena si afiseaza fereastra principala
        Scene scene = new Scene(mainLayout, 450, 220);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
