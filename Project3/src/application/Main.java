/* Jett Kopalek
 * CSCI 400
 * Better Library Management System 
 * 
 * This program is a CRUD application that manages a small library. It has the 
 * ability to checkin/checkout books, add/remove/update patrons, and add/remove/update
 * books. When the user of the Better Library Manager exits the program, the files are 
 * saved to the corresponding books.txt, checkouts.txt, and patrons.txt. The user will 
 * also be displayed the file directory that holds the data files. 
 * 
 * */

//Imports
package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    //Data lists for objects used
    private List<Book> books = new ArrayList<>();
    private List<Patron> patrons = new ArrayList<>();
    private List<Checkout> checkouts = new ArrayList<>();

    //Main layout pane
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        try {
        	//Set up stage
            primaryStage.setTitle("Better Library Management System");
            root = new BorderPane();
            root.setStyle("-fx-background-color: yellow;");

            VBox navBar = buildNavBar();
            navBar.setPrefWidth(170);
            root.setLeft(navBar);

            //Load data
            loadBooks();
            loadPatrons();
            loadCheckouts();

            //Show welcome screen
            showWelcomeScreen();

            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Safely close data files
    @Override
    public void stop() {
        saveBooks();
        savePatrons();
        saveCheckouts();
        System.out.println("Thank you for using the Better Library Management System!");
    }
    
   //Navigation pane
    private VBox buildNavBar() {
    	//Create container VBox
        VBox navBar = new VBox();
        navBar.setPadding(new Insets(20, 10, 20, 10));
        navBar.setSpacing(15);

        //Create and give logic to navbar buttons
        Button btnManageBooks = createStyleButton("Manage\nBooks");
        btnManageBooks.setOnAction(e -> showManageBooks());

        Button btnManagePatrons = createStyleButton("Manage\nPatrons");
        btnManagePatrons.setOnAction(e -> showManagePatrons());

        Button btnCheckoutReturn = createStyleButton("Checkout\nReturn");
        btnCheckoutReturn.setOnAction(e -> showCheckoutReturn());

        //Exit button, extra logic for closing out applcation
        Button btnExit = createStyleButton("Exit");
        btnExit.setMinSize(145, 45);
        btnExit.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to exit?");
            confirm.setHeaderText("Exit Library Mangement System?");
            confirm.setTitle("Exit Confirmation");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.close();
                    Platform.exit();
                }
            });
        });


        //Place buttons on the navbar
        navBar.getChildren().addAll(btnManageBooks, btnManagePatrons, btnCheckoutReturn, btnExit);
        return navBar;
    }

    //Generic method for styling buttons
    private Button createStyleButton(String text) {
        Button btn = new Button(text);
        btn.setMinSize(145, 75);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btn.setStyle("-fx-background-color: #0095ff; -fx-text-fill: white; -fx-background-radius: 5;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-background-radius: 5;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #0095ff; -fx-text-fill: white; -fx-background-radius: 5;"));

        return btn;
    }

    //Start screen
    private void showWelcomeScreen() {
        Label welcome = new Label("Welcome to the Better Library Management System");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        VBox box = new VBox(welcome);
        box.setAlignment(Pos.CENTER);
        root.setCenter(box);
    }

    //Manage books screen
    private void showManageBooks() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        //Initialize ListView of books
        ListView<Book> lvBooks = new ListView<>();
        lvBooks.getItems().addAll(books);
        lvBooks.setPrefHeight(350);
        pane.setCenter(lvBooks);

        
        //Initialize GridPane CRUD input fields
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.setAlignment(Pos.TOP_CENTER);

        inputGrid.add(new Label("Title:"), 0, 0);
        TextField tfTitle = new TextField();
        tfTitle.setPrefWidth(150);
        inputGrid.add(tfTitle, 1, 0);

        inputGrid.add(new Label("Author:"), 0, 1);
        TextField tfAuthor = new TextField();
        tfAuthor.setPrefWidth(150);
        inputGrid.add(tfAuthor, 1, 1);

        inputGrid.add(new Label("ISBN:"), 0, 2);
        TextField tfIsbn = new TextField();
        tfIsbn.setPrefWidth(150);
        inputGrid.add(tfIsbn, 1, 2);

        VBox buttonRow = new VBox(10);
        buttonRow.setAlignment(Pos.TOP_RIGHT);
        Button btnAdd = createStyleButton("Add\nBook");
        
        Button btnUpdate = createStyleButton("Update\nBook");
        
        Button btnDelete = createStyleButton("Delete\nBook");
        
        buttonRow.getChildren().addAll(btnAdd, btnUpdate, btnDelete);
        inputGrid.add(buttonRow, 0, 3, 2, 1);

        pane.setRight(inputGrid);

        //Button actions for manage books
        btnAdd.setOnAction(e -> {
            String title = tfTitle.getText().trim();
            String author = tfAuthor.getText().trim();
            String isbn = tfIsbn.getText().trim();
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                showError("All fields are required to add a book!");
                return;
            }
            for (Book b : books) {
                if (b.getIsbn().equalsIgnoreCase(isbn)) {
                    showError("A book with ISBN " + isbn + " already exists!");
                    return;
                }
            }
            Book newBook = new Book(title, isbn, author);
            books.add(newBook);
            lvBooks.getItems().add(newBook);
            tfTitle.clear();
            tfAuthor.clear();
            tfIsbn.clear();
        });
    
        btnUpdate.setOnAction(e -> {
            Book selected = lvBooks.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a book to update.");
                return;
            }
            String title = tfTitle.getText().trim();
            String author = tfAuthor.getText().trim();
            if (title.isEmpty() || author.isEmpty()) {
                showError("Title and Author must not be empty.");
                return;
            }
            selected.setTitle(title);
            selected.setAuthor(author);
            lvBooks.refresh();
            tfTitle.clear();
            tfAuthor.clear();
            tfIsbn.clear();
        });

        btnDelete.setOnAction(e -> {
            Book selected = lvBooks.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a book to delete.");
                return;
            }
            if (isBookCheckedOut(selected.getIsbn())) {
                showError("Cannot delete a book that is checked out!");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete the book \"" + selected.getTitle() + "\"?");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    books.remove(selected);
                    lvBooks.getItems().remove(selected);
                    tfTitle.clear();
                    tfAuthor.clear();
                    tfIsbn.clear();
                }
            });
        });

        //Populate fields when a book is selected
        lvBooks.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tfTitle.setText(newVal.getTitle());
                tfAuthor.setText(newVal.getAuthor());
                tfIsbn.setText(newVal.getIsbn());
            }
        });

        root.setCenter(pane);
    }

    //Manage patrons screen
    private void showManagePatrons() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        //Initialize ListView of patrons
        ListView<Patron> lvPatrons = new ListView<>();
        lvPatrons.getItems().addAll(patrons);
        lvPatrons.setPrefHeight(350);
        pane.setCenter(lvPatrons);

        //Initialize GridPane containing CRUD input fields
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.setAlignment(Pos.TOP_CENTER);

        inputGrid.add(new Label("Name:"), 0, 0);
        TextField tfName = new TextField();
        tfName.setPrefWidth(150);
        inputGrid.add(tfName, 1, 0);

        inputGrid.add(new Label("ID:"), 0, 1);
        TextField tfId = new TextField();
        tfId.setPrefWidth(150);
        inputGrid.add(tfId, 1, 1);

        VBox buttonRow = new VBox(10);
        buttonRow.setAlignment(Pos.TOP_RIGHT);
        Button btnAdd = createStyleButton("Add\nPatron");
        Button btnUpdate = createStyleButton("Update\nPatron");
        Button btnDelete = createStyleButton("Delete\nPatron");
        
        buttonRow.getChildren().addAll(btnAdd, btnUpdate, btnDelete);
        inputGrid.add(buttonRow, 0, 2, 2, 1);

        pane.setRight(inputGrid);

        //Button actions for manage patrons
        btnAdd.setOnAction(e -> {
            String name = tfName.getText().trim();
            String id = tfId.getText().trim();
            if (name.isEmpty() || id.isEmpty()) {
                showError("Both Name and ID are required to add a patron.");
                return;
            }
            for (Patron p : patrons) {
                if (p.getId().equalsIgnoreCase(id)) {
                    showError("A patron with ID " + id + " already exists!");
                    return;
                }
            }
            Patron newPatron = new Patron(name, id);
            patrons.add(newPatron);
            lvPatrons.getItems().add(newPatron);
            tfName.clear();
            tfId.clear();
        });

        btnUpdate.setOnAction(e -> {
            Patron selected = lvPatrons.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a patron to update.");
                return;
            }
            String name = tfName.getText().trim();
            String id = tfId.getText().trim();
            if (name.isEmpty() || id.isEmpty()) {
                showError("Both Name and ID must not be empty.");
                return;
            }
            for (Patron p : patrons) {
                if (p != selected && p.getId().equalsIgnoreCase(id)) {
                    showError("Another patron with ID " + id + " already exists!");
                    return;
                }
            }
            selected.setName(name);
            int index = lvPatrons.getSelectionModel().getSelectedIndex();
            patrons.remove(index);
            Patron updatedPatron = new Patron(name, id);
            patrons.add(index, updatedPatron);
            lvPatrons.getItems().set(index, updatedPatron);
            lvPatrons.refresh();
            tfName.clear();
            tfId.clear();
        });

        btnDelete.setOnAction(e -> {
            Patron selected = lvPatrons.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a patron to delete.");
                return;
            }
            if (doesPatronHaveCheckouts(selected.getId())) {
                showError("Cannot delete a patron who has checked-out books.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete patron \"" + selected.getName() + "\"?");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    patrons.remove(selected);
                    lvPatrons.getItems().remove(selected);
                    tfName.clear();
                    tfId.clear();
                }
            });
        });

        //Populate fields when a patron is selected
        lvPatrons.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tfName.setText(newVal.getName());
                tfId.setText(newVal.getId());
            }
        });

        root.setCenter(pane);
    }

    //Checkout/Return screen
    private void showCheckoutReturn() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        //Initialize ListView for current checkouts
        ListView<Checkout> lvCheckouts = new ListView<>();
        lvCheckouts.getItems().addAll(checkouts);
        lvCheckouts.setPrefHeight(300);
        pane.setCenter(lvCheckouts);

        //Initialize VBox to hold input fields and buttons
        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(10));
        bottom.setAlignment(Pos.CENTER);

        HBox inputRow = new HBox(10);
        inputRow.setAlignment(Pos.CENTER);
        ComboBox<Patron> cbPatron = new ComboBox<>();
        cbPatron.getItems().addAll(patrons);
        cbPatron.setPromptText("Patron");
        ComboBox<Book> cbBook = new ComboBox<>();
        cbBook.getItems().addAll(getAvailableBooks());
        cbBook.setPromptText("Book");
        inputRow.getChildren().addAll(cbPatron, cbBook);

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.TOP_LEFT);
        Button btnCheckout = createStyleButton("Checkout\nBook");

        Button btnReturn = createStyleButton("Return\nBook");
        
        buttonRow.getChildren().addAll(btnCheckout, btnReturn);

        bottom.getChildren().addAll(inputRow, buttonRow);
        pane.setBottom(bottom);

        //Button actions for manage books
        btnCheckout.setOnAction(e -> {
            Patron p = cbPatron.getValue();
            Book b = cbBook.getValue();
            if (p == null || b == null) {
                showError("Please select both a Patron and a Book.");
                return;
            }
            if (isBookCheckedOut(b.getIsbn())) {
                showError("This book is already checked out!");
                return;
            }
            Checkout co = new Checkout(p.getId(), b.getIsbn());
            checkouts.add(co);
            lvCheckouts.getItems().add(co);
            cbBook.getItems().remove(b);
            cbPatron.setValue(null);
            cbBook.setValue(null);
        });

        btnReturn.setOnAction(e -> {
            Checkout selected = lvCheckouts.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a checkout record to return.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Return this book: " + selected.getIsbn() + " for Patron: " + selected.getPatronId() + "?");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    checkouts.remove(selected);
                    lvCheckouts.getItems().remove(selected);
                    Book returnedBook = findBookByISBN(selected.getIsbn());
                    if (returnedBook != null) {
                        cbBook.getItems().add(returnedBook);
                    }
                }
            });
        });

        root.setCenter(pane);
    }
    
    //Helper methods
    
    //Get available books for patrons to checkout
    private List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book b : books) {
            if (!isBookCheckedOut(b.getIsbn())) {
                available.add(b);
            }
        }
        return available;
    }

    //Check if a book is checked out
    private boolean isBookCheckedOut(String isbn) {
        for (Checkout c : checkouts) {
            if (c.getIsbn().equalsIgnoreCase(isbn)) {
                return true;
            }
        }
        return false;
    }

    //Check if a patron has any books checkout
    private boolean doesPatronHaveCheckouts(String patronId) {
        for (Checkout c : checkouts) {
            if (c.getPatronId().equalsIgnoreCase(patronId)) {
                return true;
            }
        }
        return false;
    }

    //Get ISBN of a book
    private Book findBookByISBN(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        return null;
    }

    //Error handling message
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    //File management methods
    
    //Load books from books.txt 
    private void loadBooks() {
        File file = new File("books.txt");
        if (!file.exists()) {
            System.out.println("books.txt not found, starting with empty books list.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                books.add(new Book(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save books to books.txt
    private void saveBooks() {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                bw.write(b.getTitle() + "," + b.getIsbn() + "," + b.getAuthor());
                bw.newLine();   
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Load patrons from patrons.txt
    private void loadPatrons() {
        File file = new File("patrons.txt");
        if (!file.exists()) {
            System.out.println("patrons.txt not found, starting with empty patrons list.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                patrons.add(new Patron(parts[0], parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save patrons to patrons.txt
    private void savePatrons() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("patrons.txt"))) {
            for (Patron p : patrons) {
                bw.write(p.getName() + "," + p.getId());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Load checkout information from checkouts.txt
    private void loadCheckouts() {
        File file = new File("checkouts.txt");
        if (!file.exists()) {
            System.out.println("checkouts.txt not found, starting with empty checkouts list.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                checkouts.add(new Checkout(parts[0], parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save checkout information to checkouts.txt
    private void saveCheckouts() {
    	File checkoutsFile = new File("checkouts.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(checkoutsFile))) {
            for (Checkout c : checkouts) {
                bw.write(c.getPatronId() + "," + c.getIsbn());
                bw.newLine();
            }
            //Added this output to the terminal to see location in file directory for validation
            String filePath = checkoutsFile.getAbsolutePath();
            System.out.println("Files saved to directory: " + filePath.substring(0, filePath.length() - 14));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Main
    public static void main(String[] args) {
        launch(args);
    }
}
