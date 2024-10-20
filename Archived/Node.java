import java.util.*;

public class Node {
    double latitude;
    double longitude;
    String id;
    List<Node> neighbors;

    public Node(double latitude, double longitude, String id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }
}
