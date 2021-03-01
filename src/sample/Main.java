package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private final static String SUCCESS_MESSAGE = "Inputfile: %s - Success";
    private final static String FAIL_MESSAGE = "Inputfile: %s - Fail: expectedResult=[%d], actualResult=[%d]";
    private final static String ERROR_MESSAGE = "Inputfile: %s - Error";

    @Override
    public void start(Stage primaryStage) {
        Button buttonString = new Button("Протестировать задачу String");
        Button buttonTickets = new Button("Протестировать задачу LuckyTickets");
        Scene scene = new Scene(new VBox(buttonString, buttonTickets), 400, 300);
        primaryStage.setTitle("Система тестирования");
        primaryStage.setScene(scene);
        primaryStage.show();

        buttonString.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                List<File> files = getInFiles(selectedDirectory);
                Text text = new Text(files.stream().map(file -> test(file, this::testString)).collect(Collectors.joining("\n")));
                primaryStage.setScene(new Scene(new HBox(text), 400, 300));
            }
        });

        buttonTickets.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                List<File> files = getInFiles(selectedDirectory);
                Text text = new Text(files.stream().map(file -> test(file, this::testTickets)).collect(Collectors.joining("\n")));
                primaryStage.setScene(new Scene(new HBox(text), 400, 300));
            }
        });
    }

    private String testTickets(String in, String out, String filename) {
        LuckyTicket luckyTicket = new LuckyTicket();
        long expectedResult = luckyTicket.luckyTicketQty(Integer.valueOf(in));
        long actualResult = Long.valueOf(out);
        if (expectedResult == actualResult) {
            return String.format(SUCCESS_MESSAGE, filename);
        } else {
            return String.format(FAIL_MESSAGE, filename, expectedResult, actualResult);
        }
    }

    private String testString(String in, String out, String filename) {
        long expectedResult = in.length();
        long actualResult = Long.valueOf(out);
        if (expectedResult == actualResult) {
            return String.format(SUCCESS_MESSAGE, filename);
        } else {
            return String.format(FAIL_MESSAGE, filename, expectedResult, actualResult);
        }
    }

    public String test(File inFile, TestInterface tester) {
        Path inPath = Paths.get(inFile.getAbsolutePath());
        Path outPath = Paths.get(inFile.getAbsolutePath().replace(".in", ".out"));
        String filename = inFile.getName();
        try (BufferedReader inReader = Files.newBufferedReader(inPath);
             BufferedReader outReader = Files.newBufferedReader(outPath)) {
            String inString = inReader.readLine();
            String outString = outReader.readLine();
            return tester.test(inString, outString, filename);
        } catch (Exception e) {
            System.out.println("Error");
        }
        return String.format(ERROR_MESSAGE, filename);
    }

    public List<File> getInFiles(File folder) {
        List<File> ins = new ArrayList<>();
        for (final File file : folder.listFiles()) {
            if (file.getName().contains("in"))
                ins.add(file);
        }
        return ins;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
