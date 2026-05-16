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
import javafx.animation.SequentialTransition;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;

public class HotelGUI extends Application {
    private CheckInSystem system;
    private BorderPane rootLayout;
    private FlowPane roomGrid;

    @Override
    public void start(Stage primaryStage) {
        system = new CheckInSystem();

        rootLayout = new BorderPane();
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

        FadeTransition fade = new FadeTransition(Duration.millis(400), page);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(400), page);
        slide.setFromY(20);
        slide.setToY(0);

        ParallelTransition transition = new ParallelTransition(fade, slide);
        transition.play();
    }

    // --- FIX 1: Single method to go back to dashboard, eliminating redundant calls
    // ---
    private void goToDashboard() {
        updateDashboard();
        switchPage(createDashboardPage());
    }

    // ==========================================
    // PAGE 1: CINEMATIC LANDING
    // ==========================================
    private VBox createLandingPage() {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.8));");

        Label title = new Label("THE GRAND MORVATH");
        title.getStyleClass().add("landing-title");
        title.setOpacity(0);

        Label subtitle = new Label("A   W I Z A R D I N G   E X P E R I E N C E");
        subtitle.getStyleClass().add("landing-subtitle");
        subtitle.setOpacity(0);

        ImageView gifView = null;
        try {
            URL gifUrl = getClass().getResource("/assets/landing.gif");
            if (gifUrl != null) {
                gifView = new ImageView(new Image(gifUrl.toExternalForm()));
                gifView.setOpacity(0);
                gifView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 10);");
            }
        } catch (Exception e) {
            System.out.println("No landing.gif found, skipping image.");
        }

        Button enterBtn = new Button("ENTER TERMINAL");
        enterBtn.getStyleClass().add("ghost-button");
        enterBtn.setOpacity(0);
        VBox.setMargin(enterBtn, new Insets(30, 0, 0, 0));

        // FIX 1 applied: use goToDashboard()
        enterBtn.setOnAction(e -> goToDashboard());

        if (gifView != null) {
            layout.getChildren().addAll(gifView, title, subtitle, enterBtn);
        } else {
            layout.getChildren().addAll(title, subtitle, enterBtn);
        }

        SequentialTransition cinematicEntry = new SequentialTransition();
        if (gifView != null) {
            cinematicEntry.getChildren().add(createRevealAnim(gifView, 800));
        }
        cinematicEntry.getChildren().add(createRevealAnim(title, 1000));
        cinematicEntry.getChildren().add(createRevealAnim(subtitle, 800));
        cinematicEntry.getChildren().add(createRevealAnim(enterBtn, 800));
        cinematicEntry.setDelay(Duration.millis(300));
        cinematicEntry.play();

        return layout;
    }

    private ParallelTransition createRevealAnim(Node node, int durationMillis) {
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(durationMillis), node);
        slide.setFromY(30);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        return new ParallelTransition(fade, slide);
    }

    // ==========================================
    // PAGE 2: DASHBOARD
    // ==========================================
    private BorderPane createDashboardPage() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

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

        // FIX 1 applied: no redundant updateDashboard() call before switchPage()
        viewRoomsBtn.setOnAction(e -> goToDashboard());
        checkInBtn.setOnAction(e -> switchPage(createCheckInPage()));
        updateBtn.setOnAction(e -> switchPage(createUpdatePage()));
        checkOutBtn.setOnAction(e -> switchPage(createCheckOutPage()));
        logoutBtn.setOnAction(e -> switchPage(createLandingPage()));

        sidebar.getChildren().addAll(brand, viewRoomsBtn, checkInBtn, updateBtn, checkOutBtn, new Region(), logoutBtn);
        VBox.setVgrow(sidebar.getChildren().get(5), Priority.ALWAYS);

        VBox centerArea = new VBox(20);
        centerArea.setPadding(new Insets(40, 40, 40, 60));

        Label mainTitle = new Label("ROOM STATUS");
        mainTitle.getStyleClass().add("gold-title");
        mainTitle.setStyle("-fx-font-size: 42px;");

        roomGrid = new FlowPane(20, 20);
        roomGrid.setAlignment(Pos.TOP_LEFT);

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

    private void updateDashboard() {
        if (roomGrid != null) {
            roomGrid.getChildren().clear();
            for (Room r : system.getAllRooms()) {
                roomGrid.getChildren().add(createRoomCard(r));
            }
        }
    }

    private VBox createRoomCard(Room r) {
        VBox card = new VBox(8);
        card.getStyleClass().add("glass-panel");
        card.setPrefWidth(220);

        Label header = new Label("Room " + r.roomNumber);
        header.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Label type = new Label(r.roomType.toUpperCase());
        type.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-font-weight: bold;");

        String statusText = r.isAvailable ? "● AVAILABLE" : "● OCCUPIED";
        String statusColor = r.isAvailable ? "#4CAF50" : "#F44336";
        Label status = new Label(statusText);
        status.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label clean = new Label(r.isClean ? "✓ Clean" : "✗ Requires Cleaning");
        clean.setStyle("-fx-text-fill: #A0A0A0; -fx-font-size: 13px;");

        Label stock = new Label(r.suppliesStocked ? "✓ Stocked" : "✗ Needs Restock");
        stock.setStyle("-fx-text-fill: #A0A0A0; -fx-font-size: 13px;");

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
    // PAGE 3: CHECK-IN FORM
    // ==========================================
    private VBox createCheckInPage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

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

        // FIX 2: Restrict room field to digits only at the input level
        roomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                roomField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm Check-In");

        cancelBtn.getStyleClass().add("cancel-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> goToDashboard());

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String roomText = roomField.getText().trim();

            // FIX 3: Validate that the guest name is not empty
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Guest name cannot be empty.");
                return;
            }

            Room r;
            if (roomText.isEmpty()) {
                r = system.processCheckInRandomRoom(name);
            } else {
                // Safe to parse — field already enforces digits only
                r = system.processCheckInSpecificRoom(name, Integer.parseInt(roomText));
            }

            if (r != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Welcome, " + name + "!\nRoom " + r.roomNumber + " assigned.\nAmenities:\n"
                                + r.amenities.getStatus());
                goToDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Check-In Failed",
                        roomText.isEmpty()
                                ? "No available rooms at this time."
                                : "Room " + roomText + " is unavailable or does not exist.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        form.getChildren().addAll(title, nameBox, roomBox, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // ==========================================
    // PAGE 4: UPDATE ROOM FORM
    // ==========================================
    private VBox createUpdatePage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(400);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("UPDATE ROOM");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        VBox roomBox = createInputBox("Room Number:");
        TextField roomNum = (TextField) roomBox.getChildren().get(1);

        // FIX 2: Restrict to digits only
        roomNum.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                roomNum.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        VBox checks = new VBox(12);
        CheckBox clean = new CheckBox("Room is Clean");
        CheckBox stock = new CheckBox("Supplies are Stocked");
        CheckBox pool = new CheckBox("Pool Access");
        CheckBox gym = new CheckBox("Gym Access");
        CheckBox rest = new CheckBox("Restaurant Access");
        checks.getChildren().addAll(clean, stock, pool, gym, rest);

        // FIX 4: Pre-populate checkboxes with the room's current state when a valid
        // room number is entered
        roomNum.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !roomNum.getText().trim().isEmpty()) {
                try {
                    Room existing = system.getRoom(Integer.parseInt(roomNum.getText().trim()));
                    if (existing != null) {
                        clean.setSelected(existing.isClean);
                        stock.setSelected(existing.suppliesStocked);
                        // Assumes amenities expose individual boolean fields; adjust if your Amenities
                        // class differs
                        pool.setSelected(existing.amenities.poolAccess);
                        gym.setSelected(existing.amenities.gymAccess);
                        rest.setSelected(existing.amenities.restaurantAccess);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button("Save Changes");

        cancelBtn.getStyleClass().add("cancel-button");
        saveBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> goToDashboard());

        saveBtn.setOnAction(e -> {
            String roomText = roomNum.getText().trim();

            // FIX 3: Validate room number is not empty before attempting update
            if (roomText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a room number.");
                return;
            }

            int rNum = Integer.parseInt(roomText); // Safe: field is digits-only
            Room existing = system.getRoom(rNum);

            // FIX 5: Verify the room actually exists before updating
            if (existing == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Room " + rNum + " does not exist.");
                return;
            }

            system.updateRoomStatus(rNum, clean.isSelected(), stock.isSelected(),
                    pool.isSelected(), gym.isSelected(), rest.isSelected());
            showAlert(Alert.AlertType.INFORMATION, "Updated", "Room " + rNum + " has been updated.");
            goToDashboard();
        });

        btnRow.getChildren().addAll(cancelBtn, saveBtn);
        form.getChildren().addAll(title, roomBox, checks, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // ==========================================
    // PAGE 5: CHECK-OUT FORM
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

        // FIX 2: Digits only
        roomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                roomField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm Check-Out");

        cancelBtn.getStyleClass().add("cancel-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> goToDashboard());

        confirmBtn.setOnAction(e -> {
            String roomText = roomField.getText().trim();

            // FIX 3: Validate not empty
            if (roomText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a room number.");
                return;
            }

            int roomNum = Integer.parseInt(roomText); // Safe: digits-only field
            Room room = system.getRoom(roomNum);

            if (room == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Room " + roomNum + " does not exist.");
            } else if (room.isAvailable) {
                showAlert(Alert.AlertType.INFORMATION, "Already Empty", "Room " + roomNum + " is already vacant.");
            } else {
                // FIX 6: Confirmation dialog before a destructive check-out action
                Optional<ButtonType> result = showConfirmation(
                        "Confirm Check-Out",
                        "Check out guest from Room " + roomNum
                                + "?\nThe room will be flagged for cleaning and restocking.");
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    system.processCheckout(roomNum);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Room " + roomNum + " checked out.\nFlagged for cleaning and restocking.");
                    goToDashboard();
                }
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
        field.setMaxWidth(Double.MAX_VALUE);
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

    // FIX 6: New helper for confirmation dialogs
    private Optional<ButtonType> showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
