package application;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

public class FeedTreeTableView extends Application {

    public static void main(String[] args) {
        Application.launch(args); // Lanseaza aplicatia JavaFX
    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) {
        // Seteaza titlul ferestrei
        stage.setTitle("User Tree Table View");

        // Crearea elementelor de tip TreeItem pentru utilizatori
        final TreeItem<User> user1 = new TreeItem<>(new User("mirelasperlea@yahoo.com", "I really like this app!"));
        final TreeItem<User> user2 = new TreeItem<>(new User("user2@example.com", "feedback..."));
        final TreeItem<User> user3 = new TreeItem<>(new User("user3@example.com", "feedback..."));

        // Crearea elementului root (radacina)
        final TreeItem<User> root = new TreeItem<>(new User("Root", "")); // Utilizator placeholder pentru root
        root.setExpanded(true); // Expandeaza nodul root

        // Adaugarea elementelor de tip TreeItem la nodul root
        root.getChildren().setAll(user1, user2, user3);

        // Crearea coloanei pentru email
        TreeTableColumn<User, String> emailColumn = new TreeTableColumn<>("Email");
        emailColumn.setPrefWidth(150); // Seteaza latimea preferata a coloanei
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getEmail())); // Defineste modul de obtinere a datelor pentru celulele din coloana

        // Crearea coloanei pentru feedback
        TreeTableColumn<User, String> feedbackColumn = new TreeTableColumn<>("Feedback");
        feedbackColumn.setPrefWidth(150); // Seteaza latimea preferata a coloanei
        feedbackColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getFeedback())); // Defineste modul de obtinere a datelor pentru celulele din coloana

        // Crearea unui TreeTableView cu nodul root
        final TreeTableView<User> treeTableView = new TreeTableView<>(root);
        treeTableView.getColumns().addAll(emailColumn, feedbackColumn); // Adauga coloanele in TreeTableView
        treeTableView.setShowRoot(false); // Ascunde nodul root
        treeTableView.setPrefSize(320, 240); // Seteaza dimensiunea preferata a TreeTableView

        // Crearea scenei si adaugarea TreeTableView la aceasta
        Scene scene = new Scene(new Group(), 350, 270);
        Group sceneRoot = (Group) scene.getRoot(); // Obtine radacina scenei
        sceneRoot.getChildren().add(treeTableView); // Adauga TreeTableView la radacina scenei

        // Seteaza scena in fereastra si o afiseaza
        stage.setScene(scene);
        stage.show();
    }

    // Clasa statica User pentru reprezentarea unui utilizator cu email si feedback
    public static class User {
        private final String email; // Emailul utilizatorului
        private final String feedback; // Feedbackul utilizatorului

        // Constructorul clasei User
        public User(String email, String feedback) {
            this.email = email;
            this.feedback = feedback;
        }

        // Metoda pentru obtinerea emailului
        public String getEmail() {
            return email;
        }

        // Metoda pentru obtinerea feedbackului
        public String getFeedback() {
            return feedback;
        }
    }
}
