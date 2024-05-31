package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserFileWriter {
    public static void writeUserData(String email, String password) {
        Scanner scanner = new Scanner(System.in);

        // Cere utilizatorului sa introduca calea catre directorul dorit
        System.out.println("Introduceti calea catre directorul in care doriti sa salvati fisierul:");
        String directoryPath = scanner.nextLine();

        // Numele fisierului
        String filename = "userdata.txt";

        // Calea absoluta catre fisier
        String absoluteFilePath = directoryPath + "/" + filename;

        // Se scrie text in buffer si BufferedWriter este initializat cu un FileWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(absoluteFilePath, true))) {
            // Adauga datele utilizatorului in fisier, separate prin coloane
            writer.write(email + "\t" + password + System.lineSeparator());
            System.out.println("Datele utilizatorului au fost scrise cu succes in fisier.");
        } catch (IOException e) {
            System.err.println("Eroare la scrierea in fisier: " + e.getMessage());
        } finally {
            // Inchide Scanner-ul pentru a evita scurgerile de resurse
            scanner.close();
        }
    }
}
