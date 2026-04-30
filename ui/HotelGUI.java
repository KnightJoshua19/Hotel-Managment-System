package ui;

import hotel.CheckInSystem;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HotelGUI {
    private CheckInSystem system;
    private JTextArea displayArea;

    public HotelGUI() {
        system = new CheckInSystem();

        // 1. The Modern Web App Tweaks (Rounded Edges)
        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("ScrollBar.thumbArc", 15);
        UIManager.put("Button.margin", new Insets(8, 14, 8, 14)); // Fatter, clickable buttons

        // 2. Your Custom Color Palette
        Color darkOlive = new Color(66, 71, 50);
        Color mutedTeal = new Color(129, 149, 144);
        Color warmBeige = new Color(187, 170, 150);
        Color offWhite = new Color(242, 239, 235);

        // Map the colors to the UI components
        UIManager.put("Panel.background", offWhite);
        UIManager.put("OptionPane.background", offWhite);
        UIManager.put("Button.background", darkOlive);
        UIManager.put("Button.foreground", offWhite);
        UIManager.put("Button.hoverBackground", mutedTeal);
        UIManager.put("TextArea.background", warmBeige);
        UIManager.put("TextArea.foreground", darkOlive);
        UIManager.put("Label.foreground", darkOlive);
        UIManager.put("ScrollPane.background", offWhite);

        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setupWindow();
    }

    private void setupWindow() {
        JFrame frame = new JFrame("Hotel Reception Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(15, 15));
        frame.getContentPane().setBackground(UIManager.getColor("Panel.background"));

        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Room Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        frame.add(title, BorderLayout.NORTH);

        // Center Screen
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        displayArea.setMargin(new Insets(15, 15, 15, 15));

        // Remove the harsh border around the text area for a cleaner look
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Right Sidebar
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setOpaque(false); // Let the off-white background show through

        JButton viewBtn = new JButton("Refresh Rooms");
        JButton checkInRandomBtn = new JButton("Quick Check-In");
        JButton checkInSpecificBtn = new JButton("Specific Check-In");
        JButton checkOutBtn = new JButton("Check-Out");
        JButton updateBtn = new JButton("Update Room Status");

        sidebar.add(viewBtn);
        sidebar.add(checkInRandomBtn);
        sidebar.add(checkInSpecificBtn);
        sidebar.add(checkOutBtn);
        sidebar.add(updateBtn);

        frame.add(sidebar, BorderLayout.EAST);

        // Button Actions (Kept exactly the same as before)
        viewBtn.addActionListener(e -> updateScreen());

        checkInRandomBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter Guest Name:");
            if (name != null && !name.trim().isEmpty()) {
                system.processCheckInRandomRoom(name);
                updateScreen();
            }
        });

        checkInSpecificBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField roomField = new JTextField();
            Object[] message = { "Guest Name:", nameField, "Room Number:", roomField };

            int option = JOptionPane.showConfirmDialog(frame, message, "Specific Check-In",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int roomNum = Integer.parseInt(roomField.getText().trim());
                    system.processCheckInSpecificRoom(nameField.getText(), roomNum);
                    updateScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid room number.");
                }
            }
        });

        checkOutBtn.addActionListener(e -> {
            String room = JOptionPane.showInputDialog(frame, "Enter Room Number to Check Out:");
            if (room != null && !room.trim().isEmpty()) {
                try {
                    system.processCheckout(Integer.parseInt(room.trim()));
                    updateScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid room number.");
                }
            }
        });

        updateBtn.addActionListener(e -> {
            JTextField roomField = new JTextField();
            JCheckBox cleanBox = new JCheckBox("Room is Clean");
            JCheckBox stockedBox = new JCheckBox("Supplies are Stocked");
            Object[] message = { "Room Number:", roomField, cleanBox, stockedBox };

            int option = JOptionPane.showConfirmDialog(frame, message, "Update Status", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int roomNum = Integer.parseInt(roomField.getText().trim());
                    system.updateRoomStatus(roomNum, cleanBox.isSelected(), stockedBox.isSelected());
                    updateScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid room number.");
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        updateScreen();
    }

    private void updateScreen() {
        displayArea.setText(system.getRoomStatusText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelGUI());
    }
}
