package hotel;

public class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber) {
        super(roomNumber, "deluxe");
    }

    @Override
    protected void setupAmenities() {

        getAmenities().updateAmenities(true, true, false);
    }
}
