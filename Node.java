import java.util.*;

public class Node {
    int index; 
    String id; 
    double latitude;
    double longitude;
    boolean isEatery; 

    public Node(int index, double latitude, double longitude, String id, boolean isEatery) {
        this.index = index; 
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.isEatery = isEatery;
    }    
}
