package hotel;

public class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, "suite");
    }

    @Override
    protected void setupAmenities() {

        getAmenities().updateAmenities(true, true, true);
    }
}
