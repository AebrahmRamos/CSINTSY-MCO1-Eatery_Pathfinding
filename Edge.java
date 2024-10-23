import java.util.Random;

public class Edge {
    Node dest;
    Node src;
    double weight;
    Random rand = new Random(); 

    public Edge(Node src, Node dest) {
        this.src = src;
        this.dest = dest;
        this.weight = distanceFormula(src, dest);
    }

    private double distanceFormula(Node src, Node dest){

        double x1 = src.latitude; 
        double y1 = src.longitude; 
        double x2 = dest.latitude; 
        double y2 = dest.longitude;

        double distance =  Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double randomFactor = 1 + (rand.nextDouble() * 0.2 - 0.1);  // range from 0.9 to 1.1
        return distance;
    }

   
}
