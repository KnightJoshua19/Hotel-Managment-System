
/**
 * Codespace creator:
 * 
 * @author Joshua M. Esclamado
 * 
 *         Date created:
 *         04/04/2026
 * 
 *         Co-authors:
 *         Jefel Joe V. Villacorta
 * 
 * 
 *         Program description:
 *         This codespace is prepared to set the environment for the OOP project
 *         written in Java language.
 */

import hotel.CheckInSystem;
import java.util.Scanner;

public class OOP_Project {
    public static void main(String[] args) {
        CheckInSystem hotelService = new CheckInSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the Hotel Management Terminal");

        while (running) {
            System.out.println("\n=========================");
            System.out.println("1. View All Rooms");
            System.out.println("2. Check-in (Specific Room)");
            System.out.println("3. Check-in (Any Available Room)");
            System.out.println("4. Check-out");
            System.out.println("5. Update Room Status (Cleaning/Supplies)");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelService.displayRooms();
                    break;
                case 2:
                    System.out.print("Enter guest name: ");
                    String specificName = scanner.nextLine();
                    System.out.print("Enter room number (e.g., 101): ");
                    int roomNum = scanner.nextInt();
                    hotelService.processCheckInSpecificRoom(specificName, roomNum);
                    break;
                case 3:
                    System.out.print("Enter guest name: ");
                    String randomName = scanner.nextLine();
                    hotelService.processCheckInRandomRoom(randomName);
                    break;
                case 4:
                    System.out.print("Enter room number to check out: ");
                    int checkoutNum = scanner.nextInt();
                    hotelService.processCheckout(checkoutNum);
                    break;
                case 5:
                    System.out.print("Enter room number to update: ");
                    int updateNum = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Is the room clean? (y/n): ");
                    boolean isClean = scanner.nextLine().equalsIgnoreCase("y");

                    System.out.print("Are supplies fully stocked? (y/n): ");
                    boolean isStocked = scanner.nextLine().equalsIgnoreCase("y");

                    hotelService.updateRoomStatus(updateNum, isClean, isStocked);
                    break;
                case 6:
                    running = false;
                    System.out.println("Shutting down terminal...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
