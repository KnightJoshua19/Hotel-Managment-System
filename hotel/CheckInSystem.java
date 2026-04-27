package hotel;

import java.util.ArrayList;

public class CheckInSystem {
    private ArrayList<Room> rooms = new ArrayList<>();
    private CleaningTeam cleaningTeam = new CleaningTeam();
    private SupplyTracker supplyTracker = new SupplyTracker();

    public CheckInSystem() {
        rooms.add(new Room(101));
        rooms.add(new Room(102));
        rooms.add(new Room(103));
    }

    public void displayRooms() {
        System.out.println("\n--- Current Room Status ---");
        for (Room r : rooms) {
            String status = r.isAvailable ? "Available" : "Occupied";
            String cleanStatus = r.isClean ? "Clean" : "Dirty";
            String stockStatus = r.suppliesStocked ? "Stocked" : "Needs Restock";

            System.out.println("Room " + r.roomNumber + " | " + status + " | " + cleanStatus + " | " + stockStatus);
        }
    }

    public void updateRoomStatus(int roomNumber, boolean isClean, boolean isStocked) {
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber) {
                room.isClean = isClean;
                room.suppliesStocked = isStocked;
                System.out.println("Success: Room " + roomNumber + " has been manually updated.");
                return;
            }
        }
        System.out.println("Error: Room " + roomNumber + " does not exist.");
    }

    public Room findAvailableRoom() {
        for (Room r : rooms) {
            if (r.isAvailable)
                return r;
        }
        return null;
    }

    public void processCheckInRandomRoom(String guestName) {
        Room room = findAvailableRoom();
        if (room == null) {
            System.out.println("Error: No rooms available in the hotel!");
            return;
        }

        if (checkAndPrepareRoom(room)) {
            System.out.println("Success: Assigned " + guestName + " to Room " + room.roomNumber);
        }
    }

    public void processCheckInSpecificRoom(String guestName, int roomNumber) {
        Room targetRoom = null;
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber) {
                targetRoom = room;
                break;
            }
        }

        if (checkAndPrepareRoom(targetRoom)) {
            System.out.println("Success: Assigned " + guestName + " to Room " + targetRoom.roomNumber);
        }
    }

    private boolean checkAndPrepareRoom(Room targetRoom) {
        if (targetRoom == null) {
            System.out.println("Error: The requested room does not exist.");
            return false;
        }

        if (!targetRoom.isAvailable) {
            System.out.println("Error: Room " + targetRoom.roomNumber + " is currently occupied!");
            return false;
        }

        System.out.println("Preparing Room " + targetRoom.roomNumber + "...");

        if (!targetRoom.isClean) {
            cleaningTeam.clean(targetRoom);
        } else {
            System.out.println(" > Room is already clean.");
        }

        if (!targetRoom.suppliesStocked) {
            supplyTracker.restock(targetRoom);
        } else {
            System.out.println(" > Room is already stocked.");
        }

        targetRoom.isAvailable = false;
        return true;
    }

    public void processCheckout(int roomNumber) {
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber) {
                if (room.isAvailable) {
                    System.out.println("Error: Room " + roomNumber + " is already empty!");
                    return;
                }

                room.isAvailable = true; // free up the room
                room.isClean = false; // mark as dirty
                room.suppliesStocked = false; // needs new supplies

                System.out.println("Success: Guest checked out of Room " + roomNumber + ".");
                System.out.println(" > Room is now marked as dirty and needs restocking.");
                return;
            }
        }
        System.out.println("Error: Room " + roomNumber + " does not exist.");
    }
}
