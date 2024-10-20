import java.util.*;


public class PathFind {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Adding nodes to the graph with their neighbors
        Node node1 = new Node(12.9715987, 77.594566, "Node1");
        Node node2 = new Node(12.972442, 77.580643, "Node2");
        Node node3 = new Node(12.975224, 77.592056, "Node3");
        Node intersection = new Node(12.973200, 77.590000, "Intersection");

        // Adding nodes to the graph and specifying their neighbors
        graph.addNode(node1, Arrays.asList("Node2", "Intersection"));
        graph.addNode(node2, Arrays.asList("Node1", "Node3", "Intersection"));
        graph.addNode(node3, Arrays.asList("Node2", "Intersection"));
        graph.addNode(intersection, Arrays.asList("Node1", "Node2", "Node3"));

        // Add eateries
        Node eateryA = new Node(14.566046, 120.993135, "Eatery A");
        graph.addNode(eateryA, new ArrayList<>()); // Eatery does not have direct connections yet

        // Command line interface for interaction
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter starting eatery:");
        String startEatery = scanner.nextLine();
        System.out.println("Enter destination eatery:");
        String endEatery = scanner.nextLine();

        // Find and add the temporary nodes for start and end eateries
        // Run pathfinding algorithm (e.g., Dijkstra or A*)

        scanner.close();
    }
}
