import java.util.*;

public class Node {
    double latitude;
    double longitude;
    String id;
    boolean isEatery; 
    Map<Node, Float> neighbors; //neighhbor and their weight

    public Node(double latitude, double longitude, String id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.neighbors = new HashMap<>();
    }

    public void addNeighbor(Node neighbor, Float weight) {
        neighbors.put(neighbor, weight);
        System.out.println("Added node between " + this.id + "and " + neighbor.id);
    }

    
}
