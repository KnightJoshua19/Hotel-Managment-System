package ui;

import hotel.CheckInSystem;
import hotel.Room;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
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

import javafx.animation.SequentialTransition;
import javafx.animation.Interpolator;

import java.net.URL;
import java.util.Optional;

public class HotelGUI extends Application {
    private CheckInSystem system;
    private BorderPane rootLayout;
    private VBox roomsContainer;

    private StackPane dashboardStack;
    private VBox detailOverlay;
    private VBox detailPanel;

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

    private void switchPage(Node page) {
        rootLayout.setCenter(page);

        FadeTransition fade = new FadeTransition(Duration.millis(400), page);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(400), page);
        slide.setFromY(20);
        slide.setToY(0);

        new ParallelTransition(fade, slide).play();
    }

    private void goToDashboard() {
        updateDashboard();
        switchPage(createDashboardPage());
    }

    private VBox createLandingPage() {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.8));");

        Label title = new Label("CDO VIEW HOTEL");
        title.getStyleClass().add("landing-title");
        title.setOpacity(0);

        // Label subtitle = new Label("A W I Z A R D I N G E X P E R I E N C E");
        Label subtitle = new Label("IN THE MIDDLE OF THE CITY, YOU SLEEP LIKE A BABY");
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

        Button enterBtn = new Button("OPEN DASHBOARD");
        enterBtn.getStyleClass().add("ghost-button");
        enterBtn.setOpacity(0);
        VBox.setMargin(enterBtn, new Insets(30, 0, 0, 0));

        enterBtn.setOnAction(e -> goToDashboard());

        if (gifView != null) {
            layout.getChildren().addAll(gifView, title, subtitle, enterBtn);
        } else {
            layout.getChildren().addAll(title, subtitle, enterBtn);
        }

        SequentialTransition cinematicEntry = new SequentialTransition();
        if (gifView != null)
            cinematicEntry.getChildren().add(createRevealAnim(gifView, 800));
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

    private StackPane createDashboardPage() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        VBox sidebar = new VBox(5);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(30, 0, 0, 0));

        Label brand = new Label("CDO VIEW\nHOTEL");
        brand.setStyle(
                "-fx-text-fill: #CDAD75; -fx-font-family: 'Palatino Linotype'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center;");
        brand.setAlignment(Pos.CENTER);
        brand.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(brand, new Insets(0, 0, 40, 0));

        Button viewRoomsBtn = new Button("View Rooms");
        Button checkInBtn = new Button("Check-In");
        Button updateBtn = new Button("Update Status");
        Button checkOutBtn = new Button("Check-Out");
        Button logoutBtn = new Button("← Back");
        logoutBtn.setId("back-button");

        for (Button btn : new Button[] { viewRoomsBtn, checkInBtn, updateBtn, checkOutBtn, logoutBtn }) {
            btn.getStyleClass().add("sidebar-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

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

        Label hint = new Label("Click any room card to view details");
        hint.setStyle(
                "-fx-text-fill: rgba(180,180,180,0.6); -fx-font-size: 12px; -fx-font-style: italic; -fx-font-family: 'Gill Sans MT';");

        roomsContainer = new VBox(40);
        roomsContainer.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(roomsContainer);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        centerArea.getChildren().addAll(mainTitle, hint, scrollPane);

        layout.setLeft(sidebar);
        layout.setCenter(centerArea);

        detailOverlay = new VBox();
        detailOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        detailOverlay.setAlignment(Pos.CENTER);
        detailOverlay.setVisible(false);
        detailOverlay.setOpacity(0);

        detailPanel = new VBox(16);
        detailPanel.getStyleClass().add("glass-panel");
        detailPanel.setMaxWidth(420);
        detailPanel.setPadding(new Insets(36));
        detailPanel.setOpacity(0);

        detailOverlay.getChildren().add(detailPanel);

        detailOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == detailOverlay)
                hideDetailOverlay();
        });

        dashboardStack = new StackPane(layout, detailOverlay);

        updateDashboard();
        return dashboardStack;
    }

    private void showDetailOverlay(Room r) {
        detailPanel.getChildren().clear();

        Label roomNumLabel = new Label("Room " + r.getRoomNumber());
        roomNumLabel.setStyle(
                "-fx-text-fill: #CDAD75; -fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");

        Label roomTypeLabel = new Label(r.getRoomType().toUpperCase());
        roomTypeLabel.setStyle(
                "-fx-text-fill: #a09488; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Gill Sans MT';");

        String statusText;
        String statusColor;
        if (!r.isAvailable()) {
            statusText = "● OCCUPIED";
            statusColor = "#F44336";
        } else if (!r.isClean() || !r.isSuppliesStocked()) {
            statusText = "● NEEDS ATTENTION";
            statusColor = "#FFA726";
        } else {
            statusText = "● AVAILABLE";
            statusColor = "#4CAF50";
        }
        Label statusLabel = new Label(statusText);
        statusLabel.setStyle("-fx-text-fill: " + statusColor
                + "; -fx-font-weight: bold; -fx-font-size: 13px; -fx-font-family: 'Gill Sans MT';");

        detailPanel.getChildren().addAll(roomNumLabel, roomTypeLabel, statusLabel);

        if (!r.isAvailable() && r.getCurrentGuestName() != null && !r.getCurrentGuestName().isEmpty()) {
            detailPanel.getChildren().add(makeDivider());

            Label guestHeader = new Label("GUEST INFORMATION");
            guestHeader.setStyle(
                    "-fx-text-fill: #CDAD75; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Gill Sans MT';");

            Label guestName = new Label("Name:   " + r.getCurrentGuestName());
            guestName.setStyle("-fx-text-fill: #f0ece6; -fx-font-size: 14px; -fx-font-family: 'Gill Sans MT';");

            detailPanel.getChildren().addAll(guestHeader, guestName);
        }

        detailPanel.getChildren().add(makeDivider());

        Label conditionHeader = new Label("ROOM CONDITION");
        conditionHeader.setStyle(
                "-fx-text-fill: #CDAD75; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Gill Sans MT';");

        Label cleanLabel = new Label(r.isClean() ? "✓  Clean & Ready" : "✗  Requires Cleaning");
        cleanLabel.setStyle("-fx-text-fill: " + (r.isClean() ? "#4CAF50" : "#FF7043")
                + "; -fx-font-size: 13px; -fx-font-family: 'Gill Sans MT';");

        Label stockLabel = new Label(r.isSuppliesStocked() ? "✓  Fully Stocked" : "✗  Needs Restocking");
        stockLabel.setStyle(
                "-fx-text-fill: " + (r.isSuppliesStocked() ? "#4CAF50" : "#FF7043")
                        + "; -fx-font-size: 13px; -fx-font-family: 'Gill Sans MT';");

        detailPanel.getChildren().addAll(conditionHeader, cleanLabel, stockLabel);

        detailPanel.getChildren().add(makeDivider());

        Label amenitiesHeader = new Label("AMENITIES");
        amenitiesHeader.setStyle(
                "-fx-text-fill: #CDAD75; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Gill Sans MT';");

        VBox amenityRows = new VBox(6);
        amenityRows.getChildren().addAll(
                makeAmenityRow("Pool Access", r.getAmenities().getPoolAccessStatus()),
                makeAmenityRow("Gym Access", r.getAmenities().getGymAccessStatus()),
                makeAmenityRow("Restaurant Access", r.getAmenities().getRestaurantAccessStatus()));

        detailPanel.getChildren().addAll(amenitiesHeader, amenityRows);

        Button closeBtn = new Button("CLOSE");
        closeBtn.getStyleClass().add("cancel-button");
        closeBtn.setOnAction(e -> hideDetailOverlay());
        VBox.setMargin(closeBtn, new Insets(12, 0, 0, 0));

        detailPanel.getChildren().add(closeBtn);

        detailOverlay.setVisible(true);

        FadeTransition backdropFade = new FadeTransition(Duration.millis(200), detailOverlay);
        backdropFade.setFromValue(0);
        backdropFade.setToValue(1);

        FadeTransition panelFade = new FadeTransition(Duration.millis(250), detailPanel);
        panelFade.setFromValue(0);
        panelFade.setToValue(1);

        ScaleTransition panelScale = new ScaleTransition(Duration.millis(250), detailPanel);
        panelScale.setFromX(0.92);
        panelScale.setFromY(0.92);
        panelScale.setToX(1.0);
        panelScale.setToY(1.0);
        panelScale.setInterpolator(Interpolator.EASE_OUT);

        backdropFade.play();
        new ParallelTransition(panelFade, panelScale).play();
    }

    private void hideDetailOverlay() {
        FadeTransition backdropFade = new FadeTransition(Duration.millis(200), detailOverlay);
        backdropFade.setFromValue(1);
        backdropFade.setToValue(0);
        backdropFade.setOnFinished(e -> detailOverlay.setVisible(false));

        FadeTransition panelFade = new FadeTransition(Duration.millis(150), detailPanel);
        panelFade.setFromValue(1);
        panelFade.setToValue(0);

        ScaleTransition panelScale = new ScaleTransition(Duration.millis(150), detailPanel);
        panelScale.setToX(0.95);
        panelScale.setToY(0.95);

        new ParallelTransition(panelFade, panelScale).play();
        backdropFade.play();
    }

    private Region makeDivider() {
        Region line = new Region();
        line.setStyle("-fx-background-color: rgba(205, 173, 117, 0.2);");
        line.setMinHeight(1);
        VBox.setMargin(line, new Insets(4, 0, 4, 0));
        return line;
    }

    private HBox makeAmenityRow(String name, boolean enabled) {
        Label icon = new Label(enabled ? "✓" : "✗");
        icon.setStyle("-fx-text-fill: " + (enabled ? "#4CAF50" : "#4a4440")
                + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        icon.setMinWidth(20);

        Label label = new Label(name);
        label.setStyle("-fx-text-fill: " + (enabled ? "#e0d8ce" : "#4a4440")
                + "; -fx-font-size: 13px; -fx-font-family: 'Gill Sans MT';");

        HBox row = new HBox(10, icon, label);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void updateDashboard() {
        if (roomsContainer != null) {
            roomsContainer.getChildren().clear();

            java.util.List<Room> standardRooms = new java.util.ArrayList<>();
            java.util.List<Room> deluxeRooms = new java.util.ArrayList<>();
            java.util.List<Room> suiteRooms = new java.util.ArrayList<>();

            for (Room r : system.getAllRooms()) {
                if (r.getRoomType().equals("suite")) {
                    suiteRooms.add(r);
                } else if (r.getRoomType().equals("deluxe")) {
                    deluxeRooms.add(r);
                } else {
                    standardRooms.add(r);
                }
            }

            if (!standardRooms.isEmpty()) {
                roomsContainer.getChildren().add(createCategorySection("STANDARD ROOMS", standardRooms));
            }
            if (!deluxeRooms.isEmpty()) {
                roomsContainer.getChildren().add(createCategorySection("DELUXE ROOMS", deluxeRooms));
            }
            if (!suiteRooms.isEmpty()) {
                roomsContainer.getChildren().add(createCategorySection("SUITE ROOMS", suiteRooms));
            }

        }
    }

    private VBox createCategorySection(String titleText, java.util.List<Room> rooms) {
        VBox section = new VBox(15);

        Label categoryTitle = new Label(titleText);
        categoryTitle.setStyle(
                "-fx-text-fill: #CDAD75; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");

        FlowPane grid = new FlowPane(20, 20);
        grid.setAlignment(Pos.TOP_LEFT);

        for (Room r : rooms) {
            grid.getChildren().add(createRoomCard(r));
        }

        section.getChildren().addAll(categoryTitle, grid);
        return section;
    }

    private VBox createRoomCard(Room r) {
        VBox card = new VBox(10);
        card.getStyleClass().add("glass-panel");
        card.setPrefWidth(150);
        card.setPrefHeight(130);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-cursor: hand; -fx-background-radius: 12;");

        Label header = new Label(String.valueOf(r.getRoomNumber()));
        header.setStyle(
                "-fx-text-fill: #CDAD75; -fx-font-size: 30px; -fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");

        Label type = new Label(r.getRoomType().toUpperCase());
        type.setStyle("-fx-text-fill: rgba(240, 236, 230, 0.5); -fx-font-size: 10px; -fx-font-family: 'Gill Sans MT';");

        String statusText;
        String statusColor;
        if (!r.isAvailable()) {
            statusText = "Occupied";
            statusColor = "#F44336";
        } else if (!r.isClean() || !r.isSuppliesStocked()) {
            statusText = "Needs Prep";
            statusColor = "#FFA726";
        } else {
            statusText = "Available";
            statusColor = "#4CAF50";
        }

        Label status = new Label(statusText);
        status.setStyle("-fx-text-fill: " + statusColor
                + "; -fx-font-weight: bold; -fx-font-size: 13px; -fx-font-family: 'Gill Sans MT';");

        card.getChildren().addAll(header, type, status);

        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        card.setOnMouseClicked(e -> showDetailOverlay(r));

        return card;
    }

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

        roomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                roomField.setText(newVal.replaceAll("[^\\d]", ""));
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

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Guest name cannot be empty.");
                return;
            }

            if (!roomText.isEmpty()) {
                Room target = system.getRoom(Integer.parseInt(roomText));
                if (target != null && (!target.isClean() || !target.isSuppliesStocked())) {
                    showAlert(Alert.AlertType.ERROR, "Room Not Ready",
                            "Room " + roomText + " requires cleaning and restocking and cannot be occupied.");
                    return;
                }
            } else {
                boolean hasReadyRoom = system.getAllRooms().stream()
                        .anyMatch(room -> room.isAvailable() && room.isClean() && room.isSuppliesStocked());

                if (!hasReadyRoom) {
                    showAlert(Alert.AlertType.ERROR, "No Rooms Ready",
                            "There are no clean and fully stocked rooms available right now.");
                    return;
                }
            }

            Room r = roomText.isEmpty()
                    ? system.processCheckInRandomRoom(name)
                    : system.processCheckInSpecificRoom(name, Integer.parseInt(roomText));

            if (r != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Welcome, " + name + "!\nRoom " + r.getRoomNumber() + " assigned.\nAmenities:\n"
                                + r.getAmenities().getStatus());
                goToDashboard();
            } else {
                if (roomText.isEmpty()) {
                    boolean anyAvailable = system.getAllRooms().stream().anyMatch(room -> room.isAvailable());
                    if (!anyAvailable) {
                        showAlert(Alert.AlertType.ERROR, "Check-In Failed", "No available rooms at this time.");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Check-In Failed",
                                "All available rooms still require cleaning or restocking.\nPlease use Update Status to prepare a room first.");
                    }
                } else {
                    Room target = system.getRoom(Integer.parseInt(roomText));
                    if (target == null) {
                        showAlert(Alert.AlertType.ERROR, "Check-In Failed", "Room " + roomText + " does not exist.");
                    } else if (!target.isAvailable()) {
                        showAlert(Alert.AlertType.ERROR, "Check-In Failed",
                                "Room " + roomText + " is currently occupied.");
                    } else if (!target.isClean() && !target.isSuppliesStocked()) {
                        showAlert(Alert.AlertType.WARNING, "Room Not Ready",
                                "Room " + roomText
                                        + " requires cleaning and restocking.\nPlease update its status first.");
                    } else if (!target.isClean()) {
                        showAlert(Alert.AlertType.WARNING, "Room Not Ready",
                                "Room " + roomText + " has not been cleaned yet.\nPlease update its status first.");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Room Not Ready",
                                "Room " + roomText + " needs restocking.\nPlease update its status first.");
                    }
                }
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        form.getChildren().addAll(title, nameBox, roomBox, btnRow);
        container.getChildren().add(form);
        return container;
    }

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

        roomNum.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                roomNum.setText(newVal.replaceAll("[^\\d]", ""));
        });

        VBox checks = new VBox(12);
        CheckBox clean = new CheckBox("Room is Clean");
        CheckBox stock = new CheckBox("Supplies are Stocked");

        checks.getChildren().addAll(clean, stock);

        roomNum.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !roomNum.getText().trim().isEmpty()) {
                try {
                    Room existing = system.getRoom(Integer.parseInt(roomNum.getText().trim()));
                    if (existing != null) {
                        clean.setSelected(existing.isClean());
                        stock.setSelected(existing.isSuppliesStocked());
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

            if (roomText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a room number.");
                return;
            }

            int rNum = Integer.parseInt(roomText);
            Room existing = system.getRoom(rNum);

            if (existing == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Room " + rNum + " does not exist.");
                return;
            }

            system.updateRoomStatus(rNum, clean.isSelected(), stock.isSelected());
            showAlert(Alert.AlertType.INFORMATION, "Updated", "Room " + rNum + " has been updated.");
            goToDashboard();
        });

        btnRow.getChildren().addAll(cancelBtn, saveBtn);
        form.getChildren().addAll(title, roomBox, checks, btnRow);
        container.getChildren().add(form);
        return container;
    }

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

        roomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                roomField.setText(newVal.replaceAll("[^\\d]", ""));
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

            if (roomText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a room number.");
                return;
            }

            int roomNum = Integer.parseInt(roomText);
            Room room = system.getRoom(roomNum);

            if (room == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Room " + roomNum + " does not exist.");
            } else if (room.isAvailable()) {
                showAlert(Alert.AlertType.INFORMATION, "Already Empty", "Room " + roomNum + " is already vacant.");
            } else {
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
