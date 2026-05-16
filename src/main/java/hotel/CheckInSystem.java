package hotel;

import java.util.ArrayList;

public class CheckInSystem {
    private ArrayList<Room> rooms = new ArrayList<>();
    private CleaningTeam cleaningTeam = new CleaningTeam();
    private SupplyTracker supplyTracker = new SupplyTracker();

    public CheckInSystem() {
        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Deluxe"));
        rooms.add(new Room(103, "Suite"));
    }

    public String getFullDashboardText() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ROOM STATUS & AMENITIES ===\n\n");
        for (Room r : rooms) {
            String status = r.isAvailable ? "Available" : "Occupied";
            String cleanStatus = r.isClean ? "Clean" : "Dirty";
            String stockStatus = r.suppliesStocked ? "Stocked" : "Needs Restock";

            sb.append("Room ").append(r.roomNumber)
                    .append(" [").append(r.roomType).append("] | ")
                    .append(status).append(" | ").append(cleanStatus).append(" | ").append(stockStatus).append("\n")
                    .append("  └ Amenities Access -> ").append(r.amenities.getStatus()).append("\n\n");
        }
        return sb.toString();
    }

    // Helper method for the GUI to fetch a specific room's current data
    public Room getRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber)
                return room;
        }
        return null;
    }

    // Updated to handle all 5 checkboxes from the GUI
    public void updateRoomStatus(int roomNum, boolean isClean, boolean isStocked, boolean pool, boolean gym,
            boolean rest) {
        Room room = getRoom(roomNum);
        if (room != null) {
            room.isClean = isClean;
            room.suppliesStocked = isStocked;
            room.amenities.updateAmenities(pool, gym, rest);
        }
    }

    public Room findAvailableRoom() {
        for (Room r : rooms) {
            if (r.isAvailable)
                return r;
        }
        return null;
    }

    // Notice these now return the 'Room' instead of a boolean, so the GUI can see
    // the amenities!
    public Room processCheckInRandomRoom(String guestName) {
        Room room = findAvailableRoom();
        if (room != null && checkAndPrepareRoom(room)) {
            return room;
        }
        return null;
    }

    public Room processCheckInSpecificRoom(String guestName, int roomNumber) {
        Room targetRoom = getRoom(roomNumber);
        if (targetRoom != null && checkAndPrepareRoom(targetRoom)) {
            return targetRoom;
        }
        return null;
    }

    public void processCheckout(int roomNumber) {
        Room room = getRoom(roomNumber);
        if (room != null && !room.isAvailable) {
            room.isAvailable = true;
            room.isClean = false;
            room.suppliesStocked = false;
        }
    }

    private boolean checkAndPrepareRoom(Room targetRoom) {
        if (targetRoom == null || !targetRoom.isAvailable)
            return false;
        if (!targetRoom.isClean)
            cleaningTeam.clean(targetRoom);
        if (!targetRoom.suppliesStocked)
            supplyTracker.restock(targetRoom);
        targetRoom.isAvailable = false;
        return true;
    }

    // Add this right below your constructor
    public ArrayList<Room> getAllRooms() {
        return rooms;
    }
}
