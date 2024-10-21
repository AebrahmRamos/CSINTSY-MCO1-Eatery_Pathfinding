import java.io.*;
import java.util.*;

public class Driver {

    @SuppressWarnings("FieldMayBeFinal")
    private static Graph graph = new Graph();
    // Stores node names



    public static void main(String[] args) {
        loadGraph();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select an option: 1 for Search, 2 for Add Node, 3 for Remove Node, 4 for View Node Details, 5 for View All Nodes, 6 to Exit");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1 -> {
                    System.out.println("Enter source eatery/node:");
                    String source = scanner.nextLine();

                    System.out.println("Enter destination eatery/node:");
                    String destination = scanner.nextLine();

                    System.out.println("Select algorithm: 1 for BFS, 2 for DFS, 3 for UCS, 4 for ID-DFS, 5 for A*, 6 for Greedy BFS");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1 -> bfs(source, destination);
                        case 2 -> dfs(source, destination);
                        case 3 -> ucs(source, destination);
                        case 4 -> iddfs(source, destination);
                        case 5 -> aStar(source, destination);
                        case 6 -> greedyBFS(source, destination);
                        default -> System.out.println("Invalid choice");
                    }
                    System.out.println("Visited nodes: ");
                }


                case 2 -> {
                }

                case 3 -> {
                }

                case 4 -> {
                }

                case 5 -> viewAllNodes();

                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }

                default -> System.out.println("Invalid option");
            }
            //addNode(scanner);
            //removeNode(scanner);
            //viewNodeDetails(scanner);
                    }
    }

    private static void loadGraph() {
        try (BufferedReader nodesReader = new BufferedReader(new FileReader("Data/NodesEateries.csv"));
             BufferedReader edgesReader = new BufferedReader(new FileReader("Data/Edges.csv"))) {

            String line;
            // Read NodesEateries.csv to create nodes and store coordinates
            boolean isFirstLine = true;
            while ((line = nodesReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String nodeName = parts[0].trim();
                    double latitude = Double.parseDouble(parts[1].trim());
                    double longitude = Double.parseDouble(parts[2].trim());
                    boolean isEatery = Boolean.parseBoolean(parts[3].trim()); 

                    Node node = new Node(latitude, longitude, nodeName, isEatery); 
                    graph.addNode(node); 
                }
            }

            // Read Edges.csv to create edges (relationships between nodes)
            isFirstLine = true;
            while ((line = edgesReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String nodeAId = parts[0].trim();
                    String nodeBId = parts[1].trim();
                    
                    graph.addEgde(nodeAId, nodeBId);                    
                }
                //add edge
                
            }
            graph.printGraph(); 
        } catch (IOException e) {
            System.err.println("Error reading CSV files: " + e.getMessage());
        }
    }

    
   
    private static void viewAllNodes() {
    }

    

    private static void bfs(String source, String destination) {
        Map map = graph.getGraph();
        
        Queue<String> queue = new LinkedList<>();
        // Set to track visited nodes
        Set<String> visited = new HashSet<>();

        // Enqueue the starting node and mark it as visited
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String currentNode = queue.poll();
            System.out.println("Visited: " + currentNode);

            // If the current node is the target node, stop
            if (currentNode.equals(destination)) {
                System.out.println("Destination node " + destination + " reached.");
                break;
            }
            LinkedList<Edge> edges = (LinkedList<Edge>) map.get(currentNode);

            if (edges != null) {
                for (Edge edge : edges) {
                    String neighbor = edge.dest.id;

                    // If neighbor hasn't been visited, enqueue it
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
    }

    private static void dfs(String source, String destination) {
        
    }

    private static void dfsHelper(String current, String destination, Set<String> visited) {
        
    }

    private static void ucs(String source, String destination) {
        // Placeholder for Uniform Cost Search
    }

    private static void iddfs(String source, String destination) {
        // Placeholder for Iterative Deepening Depth First Search
    }

    private static void aStar(String source, String destination) {
        // Placeholder for A* Search
    }

    private static void greedyBFS(String source, String destination) {
        // Placeholder for Greedy Best-First Search
    }
}
