

public class Node {
    String id; 
    double latitude;
    double longitude;
    boolean isEatery; 

    public Node(double latitude, double longitude, String id, boolean isEatery) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.isEatery = isEatery;
    }    
}
