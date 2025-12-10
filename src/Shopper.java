// Identifying shoppers
// Every 100 points you get 1 block which is worth $5
public class Shopper {
    private String id;
    private int points;

    public Shopper(String id) {
        this.id = id.toLowerCase();
        this.points = 0;
    }

    // Returning the ID to greet the customer
    public String getId() {return id;}

    // Returning points to display how many points the customer has
    public int getPoints() {return points;}

    // Adding points after a purchase
    public void addPoints(int amount) {points += amount;}

    // Redeeming blocks of points
    // Removing used points and returning the number of blocks used
    public int redeemBlocks(int requestedBlocks) {
        int availableBlocks = points / 100;
        int blocks = Math.min(requestedBlocks, availableBlocks);
        points -= blocks * 100;
        return blocks;
    }
}