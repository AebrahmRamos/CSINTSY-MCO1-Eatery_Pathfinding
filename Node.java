

public class Node {
    String id; 
    double latitude;
    double longitude;
    boolean isEatery; 
    int rating; 
    double crowdedness; 

    public Node(double latitude, double longitude, String id, boolean isEatery, int rating) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.isEatery = isEatery;
        this.rating = rating; 
    }

    public double getManhattanDist(Node dest){
        double x2 = dest.latitude; 
        double y2 = dest.longitude; 

        return Math.abs(this.latitude-x2) + Math.abs(this.longitude-y2);
    }
}
