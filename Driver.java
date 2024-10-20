import java.io.*;
import java.util.*;

public class Driver {

    private static Graph graph = new Graph();

    private Map<String, String> nodeNamesMap = new HashMap<>(); // Stores node names
    private List<String> visitedNodes = new ArrayList<>();


    public static void main(String[] args) {
        loadGraph();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select an option: 1 for Search, 2 for Add Node, 3 for Remove Node, 4 for View Node Details, 5 for View All Nodes, 6 to Exit");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.println("Enter source eatery/node:");
                    String source = scanner.nextLine();

                    System.out.println("Enter destination eatery/node:");
                    String destination = scanner.nextLine();

                    System.out.println("Select algorithm: 1 for BFS, 2 for DFS, 3 for UCS, 4 for ID-DFS, 5 for A*, 6 for Greedy BFS");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            bfs(source, destination);
                            break;
                        case 2:
                            dfs(source, destination);
                            break;
                        case 3:
                            ucs(source, destination);
                            break;
                        case 4:
                            iddfs(source, destination);
                            break;
                        case 5:
                            aStar(source, destination);
                            break;
                        case 6:
                            greedyBFS(source, destination);
                            break;
                        default:
                            System.out.println("Invalid choice");
                            break;
                    }
                    System.out.println("Visited nodes: ");
                    break;

                case 2:
                    //addNode(scanner);
                    break;

                case 3:
                    //removeNode(scanner);
                    break;

                case 4:
                    //viewNodeDetails(scanner);
                    break;

                case 5:
                    viewAllNodes();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option");
                    break;
            }
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
