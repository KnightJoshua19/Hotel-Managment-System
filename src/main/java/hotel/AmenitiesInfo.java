package hotel;

public class AmenitiesInfo {
    private boolean poolAccess = true;
    private boolean gymAccess = true;
    private boolean restaurantAccess = true;

    public String getStatus() {
        String pool = poolAccess ? "Yes" : "No";
        String gym = gymAccess ? "Yes" : "No";
        String rest = restaurantAccess ? "Yes" : "No";
        return "Pool: " + pool + " | Gym: " + gym + " | Rest: " + rest;
    }

    public void updateAmenities(boolean pool, boolean gym, boolean rest) {
        this.poolAccess = pool;
        this.gymAccess = gym;
        this.restaurantAccess = rest;
    }

    public boolean getPoolAccessStatus() {
        return poolAccess;
    }

    public boolean getGymAccessStatus() {
        return gymAccess;
    }

    public boolean getRestaurantAccessStatus() {
        return restaurantAccess;
    }
}
