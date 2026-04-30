package ui;

import hotel.CheckInSystem;
import hotel.Room;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.net.URL;

public class HotelGUI extends Application {
    private CheckInSystem system;
    private BorderPane rootLayout;
    // private TextArea displayArea;
    private FlowPane roomGrid;

    @Override
    public void start(Stage primaryStage) {
        system = new CheckInSystem();

        rootLayout = new BorderPane();
        // Load the initial page with the animation!
        switchPage(createLandingPage());

        Scene mainScene = new Scene(rootLayout, 900, 600);

        URL cssUrl = getClass().getResource("/assets/style.css");
        if (cssUrl != null) {
            mainScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("Warning: Could not find style.css");
        }

        primaryStage.setTitle("Morvath Hotel Management Terminal");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // --- THE ANIMATION ENGINE ---
    private void switchPage(Node page) {
        rootLayout.setCenter(page);

        // 1. Fade In
        FadeTransition fade = new FadeTransition(Duration.millis(400), page);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        // 2. Slide Up
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), page);
        slide.setFromY(20); // Starts 20 pixels down
        slide.setToY(0); // Slides to its normal position

        // Play them both at the exact same time
        ParallelTransition transition = new ParallelTransition(fade, slide);
        transition.play();
    }

    // ==========================================
    // PAGE 1: CINEMATIC LANDING
    // ==========================================
    private VBox createLandingPage() {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        // A subtle dark gradient overlay behind the text to make the gold pop against
        // any background
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.8));");

        // 1. The Majestic Title
        Label title = new Label("THE GRAND MORVATH");
        title.getStyleClass().add("landing-title");
        title.setOpacity(0); // Start invisible for the animation

        // 2. The Spaced-Out Subtitle (Using spaces since JavaFX CSS lacks
        // letter-spacing)
        Label subtitle = new Label("A   W I Z A R D I N G   E X P E R I E N C E");
        subtitle.getStyleClass().add("landing-subtitle");
        subtitle.setOpacity(0);

        // 3. Optional GIF/Logo (Wrapped in a glass effect)
        ImageView gifView = null;
        try {
            URL gifUrl = getClass().getResource("/assets/landing.gif");
            if (gifUrl != null) {
                gifView = new ImageView(new Image(gifUrl.toExternalForm()));
                gifView.setOpacity(0);
                // Make the GIF look like it's floating in glass
                gifView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 10);");
            }
        } catch (Exception e) {
            System.out.println("No landing.gif found, skipping image.");
        }

        // 4. The Elegant Ghost Button
        Button enterBtn = new Button("ENTER TERMINAL");
        enterBtn.getStyleClass().add("ghost-button");
        enterBtn.setOpacity(0);
        VBox.setMargin(enterBtn, new Insets(30, 0, 0, 0)); // Extra space above button

        enterBtn.setOnAction(e -> {
            updateDashboard();
            switchPage(createDashboardPage());
        });

        // Add elements to layout
        if (gifView != null) {
            layout.getChildren().addAll(gifView, title, subtitle, enterBtn);
        } else {
            layout.getChildren().addAll(title, subtitle, enterBtn);
        }

        // --- THE ANIMATION SEQUENCE ---
        // We create a helper method to build smooth fade+slide animations
        SequentialTransition cinematicEntry = new SequentialTransition();

        if (gifView != null) {
            cinematicEntry.getChildren().add(createRevealAnim(gifView, 800));
        }
        cinematicEntry.getChildren().add(createRevealAnim(title, 1000));
        cinematicEntry.getChildren().add(createRevealAnim(subtitle, 800));
        cinematicEntry.getChildren().add(createRevealAnim(enterBtn, 800));

        // Start the animation slightly after the app opens
        cinematicEntry.setDelay(Duration.millis(300));
        cinematicEntry.play();

        return layout;
    }

    // --- HELPER METHOD FOR CINEMATIC ANIMATIONS ---
    private ParallelTransition createRevealAnim(Node node, int durationMillis) {
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(durationMillis), node);
        slide.setFromY(30); // Start slightly lower
        slide.setToY(0); // Glide up to actual position
        slide.setInterpolator(Interpolator.EASE_OUT); // Smooth deceleration

        return new ParallelTransition(fade, slide);
    }

    // ==========================================
    // PAGE 2: SLEEK GLASSMORPHISM DASHBOARD
    // ==========================================
    private BorderPane createDashboardPage() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        // 1. LEFT SIDEBAR (Solid Dark)
        VBox sidebar = new VBox(5);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(30, 0, 0, 0));

        Label brand = new Label("MORVATH\nHOTEL");
        brand.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center;");
        brand.setAlignment(Pos.CENTER);
        brand.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(brand, new Insets(0, 0, 40, 0));

        Button viewRoomsBtn = new Button("View Rooms");
        Button checkInBtn = new Button("Check-In");
        Button updateBtn = new Button("Update Status");
        Button checkOutBtn = new Button("Check-Out");
        Button logoutBtn = new Button("Logout");

        Button[] navButtons = { viewRoomsBtn, checkInBtn, updateBtn, checkOutBtn, logoutBtn };
        for (Button btn : navButtons) {
            btn.getStyleClass().add("sidebar-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        viewRoomsBtn.setOnAction(e -> updateDashboard());
        checkInBtn.setOnAction(e -> switchPage(createCheckInPage()));
        updateBtn.setOnAction(e -> switchPage(createUpdatePage()));
        checkOutBtn.setOnAction(e -> switchPage(createCheckOutPage()));
        logoutBtn.setOnAction(e -> switchPage(createLandingPage()));

        sidebar.getChildren().addAll(brand, viewRoomsBtn, checkInBtn, updateBtn, checkOutBtn, new Region(), logoutBtn);
        VBox.setVgrow(sidebar.getChildren().get(5), Priority.ALWAYS);

        // 2. CENTER CONTENT (The New Room Grid)
        VBox centerArea = new VBox(20);
        centerArea.setPadding(new Insets(40, 40, 40, 60));

        Label mainTitle = new Label("ROOM STATUS");
        mainTitle.getStyleClass().add("gold-title");
        mainTitle.setStyle("-fx-font-size: 42px;"); // Slightly smaller for this page

        // Setup the FlowPane grid for the room cards
        roomGrid = new FlowPane(20, 20); // 20px gaps between cards
        roomGrid.setAlignment(Pos.TOP_LEFT);

        // Wrap it in a scroll pane so it doesn't break if you add 50 rooms
        ScrollPane scrollPane = new ScrollPane(roomGrid);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        centerArea.getChildren().addAll(mainTitle, scrollPane);

        layout.setLeft(sidebar);
        layout.setCenter(centerArea);

        updateDashboard();
        return layout;
    }

    // --- HELPER METHODS FOR ROOM CARDS ---
    private void updateDashboard() {
        if (roomGrid != null) {
            roomGrid.getChildren().clear(); // Wipe the old cards
            // Fetch the actual room objects and build a card for each!
            for (Room r : system.getAllRooms()) {
                roomGrid.getChildren().add(createRoomCard(r));
            }
        }
    }

    private VBox createRoomCard(Room r) {
        VBox card = new VBox(8);
        card.getStyleClass().add("glass-panel");
        card.setPrefWidth(220); // Width of each card

        Label header = new Label("Room " + r.roomNumber);
        header.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Label type = new Label(r.roomType.toUpperCase());
        type.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-font-weight: bold; -fx-letter-spacing: 1px;");

        // Dynamic colors based on availability
        String statusText = r.isAvailable ? "● AVAILABLE" : "● OCCUPIED";
        String statusColor = r.isAvailable ? "#4CAF50" : "#F44336"; // Green vs Red
        Label status = new Label(statusText);
        status.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold; -fx-font-size: 13px;");

        // Icons/Text for cleaning and supplies
        Label clean = new Label(r.isClean ? "✓ Clean" : "✗ Requires Cleaning");
        clean.setStyle("-fx-text-fill: #A0A0A0; -fx-font-size: 13px;");

        Label stock = new Label(r.suppliesStocked ? "✓ Stocked" : "✗ Needs Restock");
        stock.setStyle("-fx-text-fill: #A0A0A0; -fx-font-size: 13px;");

        // Separator line
        Region line = new Region();
        line.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
        line.setMinHeight(1);
        VBox.setMargin(line, new Insets(5, 0, 5, 0));

        Label amenities = new Label("Amenities:\n" + r.amenities.getStatus());
        amenities.setStyle("-fx-text-fill: #819590; -fx-font-size: 12px;");
        amenities.setWrapText(true);

        card.getChildren().addAll(header, type, status, clean, stock, line, amenities);
        return card;
    }

    // ==========================================
    // PAGE 3: CHECK-IN FORM (Glass Layout)
    // ==========================================
    private VBox createCheckInPage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER); // Centers the glass card on the screen

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(450);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER);

        Label title = new Label("GUEST CHECK-IN");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");

        VBox nameBox = createInputBox("Guest Name:");
        TextField nameField = (TextField) nameBox.getChildren().get(1);

        VBox roomBox = createInputBox("Specific Room (Leave blank for Random):");
        TextField roomField = (TextField) roomBox.getChildren().get(1);

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0)); // Add space above buttons

        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm Check-In");

        cancelBtn.getStyleClass().add("cancel-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> switchPage(createDashboardPage()));

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String roomText = roomField.getText().trim();
            Room r = null;

            if (roomText.isEmpty()) {
                r = system.processCheckInRandomRoom(name);
            } else {
                try {
                    r = system.processCheckInSpecificRoom(name, Integer.parseInt(roomText));
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid room number.");
                    return;
                }
            }

            if (r != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Welcome!\nRoom Amenities:\n" + r.amenities.getStatus());
                updateDashboard();
                switchPage(createDashboardPage());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Room unavailable or does not exist.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        form.getChildren().addAll(title, nameBox, roomBox, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // ==========================================
    // PAGE 4: UPDATE ROOM FORM (Glass Layout)
    // ==========================================
    private VBox createUpdatePage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(400);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER_LEFT); // Align checkboxes neatly to the left

        Label title = new Label("UPDATE ROOM");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        VBox roomBox = createInputBox("Room Number:");
        TextField roomNum = (TextField) roomBox.getChildren().get(1);

        VBox checks = new VBox(12);
        CheckBox clean = new CheckBox("Room is Clean");
        CheckBox stock = new CheckBox("Supplies are Stocked");
        CheckBox pool = new CheckBox("Pool Access");
        CheckBox gym = new CheckBox("Gym Access");
        CheckBox rest = new CheckBox("Restaurant Access");
        checks.getChildren().addAll(clean, stock, pool, gym, rest);

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button("Save Changes");

        cancelBtn.getStyleClass().add("cancel-button");
        saveBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> switchPage(createDashboardPage()));

        saveBtn.setOnAction(e -> {
            try {
                int rNum = Integer.parseInt(roomNum.getText().trim());
                system.updateRoomStatus(rNum, clean.isSelected(), stock.isSelected(), pool.isSelected(),
                        gym.isSelected(), rest.isSelected());
                updateDashboard();
                switchPage(createDashboardPage());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid room number.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, saveBtn);
        form.getChildren().addAll(title, roomBox, checks, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // ==========================================
    // PAGE 5: CHECK-OUT FORM (Glass Layout)
    // ==========================================
    private VBox createCheckOutPage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(400);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER);

        Label title = new Label("CHECK-OUT");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");

        VBox roomBox = createInputBox("Room Number to Vacate:");
        TextField roomField = (TextField) roomBox.getChildren().get(1);

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm Check-Out");

        cancelBtn.getStyleClass().add("cancel-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> switchPage(createDashboardPage()));

        confirmBtn.setOnAction(e -> {
            try {
                int roomNum = Integer.parseInt(roomField.getText().trim());
                Room room = system.getRoom(roomNum);

                if (room == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Room " + roomNum + " does not exist.");
                } else if (room.isAvailable) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Room " + roomNum + " is already empty.");
                } else {
                    system.processCheckout(roomNum);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Check-out successful.\nRoom requires cleaning and restocking.");
                    updateDashboard();
                    switchPage(createDashboardPage());
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid room number.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        form.getChildren().addAll(title, roomBox, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // --- UTILITIES ---
    private VBox createInputBox(String labelText) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        TextField field = new TextField();
        field.getStyleClass().add("text-field");
        field.setMaxWidth(Double.MAX_VALUE); // Ensures input boxes stretch to fill the form width
        box.getChildren().addAll(label, field);
        return box;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
