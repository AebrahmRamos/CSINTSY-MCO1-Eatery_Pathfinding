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
                    
                    String source = "", destination = ""; 
                    System.out.println("Select algorithm: 1 for BFS, 2 for DFS, 3 for UCS, 4 for ID-DFS, 5 for Heuristic Search (Manhattan Distance), 6 for Heuristic Search (Rating)");

                    int choice = scanner.nextInt(), alg = 0, rating = 0; 
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter source eatery/node:");

                    source = scanner.nextLine();
                    if (1<= choice && choice <= 5){
                        System.out.println("Enter destination eatery/node:");
                        destination = scanner.nextLine();

                    }
                    if (choice >= 5){
                        System.out.println("Select algorithm: 1 for A* Search, 2 for Greedy BFS");
                        alg = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (choice == 6){
                            while (rating < 1 || rating > 5){
                                System.out.println("Enter minimum eatery rating (1-5): "); 
                                rating = scanner.nextInt(); 
                                scanner.nextLine(); 
                            }                            
                        }
                    }

                    long startTime = System.currentTimeMillis(), endTime, executionTime;
                    switch (choice) {
                        case 1 -> bfs(source, destination);
                        case 2 -> dfs(source, destination);
                        case 3 -> ucs(source, destination);
                        case 4 -> iddfs(source, destination);
                        case 5 -> manhattanDistance(source, destination, alg);
                        case 6 -> ratingSearch(source, rating, alg);
                        default -> System.out.println("Invalid choice");
                    }
                    // System.out.println("Visited nodes: ");
                    endTime = System.currentTimeMillis();
                    executionTime = endTime - startTime; 
                    System.out.println("Execution time: " + executionTime + "ms");
                }


                case 2 -> {
                    System.out.println("Enter Node ID:");
                    String nodeId = scanner.nextLine();

                    System.out.println("Enter Latitude:");
                    double latitude = scanner.nextDouble();

                    System.out.println("Enter Longitude:");
                    double longitude = scanner.nextDouble();

                    System.out.println("Is it an eatery? (true/false):");
                    boolean isEatery = scanner.nextBoolean();

                    Node newNode = new Node(latitude, longitude, nodeId, isEatery, 0);
                    graph.addNode(newNode);
                    System.out.println("Node added successfully!");
                }

                // Remove Node
                case 3 -> {
                    System.out.println("Enter Node ID to remove:");
                    String nodeId = scanner.nextLine();
                    graph.removeNode(nodeId);
                    System.out.println("Node removed successfully!");
                }

                // View Node Details
                case 4 -> {
                    System.out.println("Enter Node ID to view:");
                    String nodeId = scanner.nextLine();
                    graph.viewNode(nodeId);
                }

                // View All Nodes
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
                if (parts.length >= 5) {
                    String nodeName = parts[0].trim();
                    double latitude = Double.parseDouble(parts[1].trim());
                    double longitude = Double.parseDouble(parts[2].trim());
                    boolean isEatery = Boolean.parseBoolean(parts[3].trim()); 
                    int rating = Integer.parseInt(parts[4].trim()); 
                    Node node = new Node(latitude, longitude, nodeName, isEatery, rating); 
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
        Map<String, LinkedList<Edge>> graphMap = graph.getGraph();
        for (String key : graphMap.keySet()) {
            System.out.println(key);
        }
    }



    private static void bfs(String source, String destination) {
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();
        List<String> visitedOrder = new ArrayList<>();
    
        Queue<String> queue = new LinkedList<>();
        queue.add(source);
        visited.add(source);
        visitedOrder.add(source);
    
        boolean found = false;
    
        while (!queue.isEmpty()) {
            String currentNode = queue.poll();
    
            // If we reached the destination
            if (currentNode.equals(destination)) {
                found = true;
                break;
            }
    
            LinkedList<Edge> edges = graph.adjacencyList.get(currentNode);
    
            if (edges != null) {
                for (Edge edge : edges) {
                    String neighbor = edge.dest.id;
    
                    // If neighbor hasn't been visited, enqueue it
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        visitedOrder.add(neighbor);
                        predecessors.put(neighbor, currentNode);  // Track predecessor
                    }
                }
            }
        }
    
        System.out.println("Visited nodes: " + visitedOrder);
    
        if (!found) {
            System.out.println("No path found from " + source + " to " + destination);
        } else {
            printPath(predecessors, source, destination);
        }
    }    
     
    private static void dfs(String source, String destination) {
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();
        List<String> visitedOrder = new ArrayList<>();
        
        dfsHelper(source, destination, visited, predecessors, visitedOrder);
        
        System.out.println("Visited nodes: " + visitedOrder);
        if (predecessors.containsKey(destination)) {
            printPath(predecessors, source, destination);
        } else {
            System.out.println("No path found from " + source + " to " + destination);
        }
    }
    
    private static void dfsHelper(String current, String destination, Set<String> visited, Map<String, String> predecessors, List<String> visitedOrder) {
        visited.add(current);
        visitedOrder.add(current);
        
        if (current.equals(destination)) {
            return;
        }
    
        LinkedList<Edge> edges = graph.adjacencyList.get(current);
        
        if (edges != null) {
            for (Edge edge : edges) {
                String neighbor = edge.dest.id;
                if (!visited.contains(neighbor)) {
                    predecessors.put(neighbor, current);
                    dfsHelper(neighbor, destination, visited, predecessors, visitedOrder);
                    if (predecessors.containsKey(destination)) return; // Stop early if destination found
                }
            }
        }
    }
    
    private static void printPath(Map<String, String> predecessors, String source, String destination) {
        LinkedList<String> path = new LinkedList<>();
        String step = destination;
        
        while (step != null) {
            path.addFirst(step);
            step = predecessors.get(step);
        }
        
        System.out.println("\nBest path from " + source + " to " + destination + ": " + String.join(" -> ", path));
    }
    
    private static void iddfs(String source, String destination) {
        int depth = 0;
        List<String> visitedOrder = new ArrayList<>();
        
        while (true) {
            Set<String> visited = new HashSet<>();
            Map<String, String> predecessors = new HashMap<>();
            
            boolean found = dls(source, destination, depth, visited, predecessors, visitedOrder);
            if (found) {
                System.out.println("Visited nodes: " + visitedOrder);
                printPath(predecessors, source, destination);
                return;
            }
            
            if (visitedOrder.contains(destination)) {
                System.out.println("No path found from " + source + " to " + destination);
                return;
            }
            
            depth++;
            visitedOrder.clear(); // Clear visited nodes for the next depth level
        }
    }
    
    private static boolean dls(String current, String destination, int depth, Set<String> visited, Map<String, String> predecessors, List<String> visitedOrder) {
        visited.add(current);
        visitedOrder.add(current);
        
        if (depth == 0 && current.equals(destination)) {
            return true;
        }
        if (depth > 0) {
            LinkedList<Edge> edges = graph.adjacencyList.get(current);
            
            if (edges != null) {
                for (Edge edge : edges) {
                    String neighbor = edge.dest.id;
                    if (!visited.contains(neighbor)) {
                        predecessors.put(neighbor, current);
                        boolean found = dls(neighbor, destination, depth - 1, visited, predecessors, visitedOrder);
                        if (found) return true;
                    }
                }
            }
        }
        
        return false;
    }   
    
    private static void ucs(String source, String destination) {
        PriorityQueue<NodeCost> frontier = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();
        List<String> visitedOrder = new ArrayList<>();
        
        frontier.add(new NodeCost(source, 0.0));
        
        while (!frontier.isEmpty()) {
            NodeCost current = frontier.poll();
            
            if (visited.contains(current.node)) continue;
            visited.add(current.node);
            visitedOrder.add(current.node);
            
            if (current.node.equals(destination)) {
                System.out.println("Visited nodes: " + visitedOrder);
                printPath(predecessors, source, destination);
                return;
            }
            
            LinkedList<Edge> edges = graph.adjacencyList.get(current.node);
            if (edges != null) {
                for (Edge edge : edges) {
                    String neighbor = edge.dest.id;
                    double newCost = current.cost + edge.weight;
                    if (!visited.contains(neighbor)) {
                        predecessors.put(neighbor, current.node);
                        frontier.add(new NodeCost(neighbor, newCost));
                    }
                }
            }
        }
        
        System.out.println("No path found from " + source + " to " + destination);
        System.out.println("Visited nodes: " + visitedOrder);
    }
    
    
    private static class NodeCost {
        String node;
        double cost;  // Use double for cost
    
        public NodeCost(String node, double cost) {
            this.node = node;
            this.cost = cost;
        }
    }    
    
    private static void manhattanDistance(String source, String destination, int choice){
       if (choice == 1) 
            aStar(source, destination);
        else
            greedyBFS(source, destination);

    }

    private static void ratingSearch(String source, int rating, int choice){
        String destination; 
        ArrayList<Node> eateries = graph.getEateries(); 
        PriorityQueue<NodeCost> frontier = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));

        eateries.removeIf(eatery -> ((eatery.rating < rating) || (eatery.id.equals(source))));
        for (Node eatery: eateries){
            System.out.println(eatery.id); 
        }
        Node srcNode = graph.getNodeById(source); 
        for (Node eatery : eateries) {
            frontier.add(new NodeCost(eatery.id, srcNode.getManhattanDist(eatery)));
        }
        destination = frontier.peek().node; 
        System.out.println("\nNearest eatery with a rating of at least " + rating + " is " + destination);
        
        if (choice == 1)
            aStar(source, destination);
        else
            greedyBFS(source, destination);
    }

    private static void aStar(String source, String destination) {
        PriorityQueue<NodeCost> frontier = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();
        List<String> visitedOrder = new ArrayList<>();

        Node destinationNode = graph.getNodeById(destination); 
        
        frontier.add(new NodeCost(source, 0.0));
        
        while (!frontier.isEmpty()) {
            NodeCost current = frontier.poll();
            
            if (visited.contains(current.node)) continue;
            visited.add(current.node);
            visitedOrder.add(current.node);
            
            if (current.node.equals(destination)) {
                System.out.println("Visited nodes: " + visitedOrder);
                printPath(predecessors, source, destination);
                return;
            }
            
            LinkedList<Edge> edges = graph.adjacencyList.get(current.node);
            if (edges != null) {
                for (Edge edge : edges) {
                    String neighbor = edge.dest.id;
                    double newCost = edge.dest.getManhattanDist(destinationNode) + edge.weight;
                    if (!visited.contains(neighbor)) {
                        predecessors.put(neighbor, current.node);
                        frontier.add(new NodeCost(neighbor, newCost));
                    }
                }
            }
        }
        
        System.out.println("No path found from " + source + " to " + destination);
        System.out.println("Visited nodes: " + visitedOrder);
    }

    private static void greedyBFS(String source, String destination) {
        PriorityQueue<NodeCost> frontier = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();
        List<String> visitedOrder = new ArrayList<>();

        Node destinationNode = graph.getNodeById(destination); 
        
        frontier.add(new NodeCost(source, 0.0));
        
        while (!frontier.isEmpty()) {
            NodeCost current = frontier.poll();
            
            if (visited.contains(current.node)) continue;
            visited.add(current.node);
            visitedOrder.add(current.node);
            
            if (current.node.equals(destination)) {
                System.out.println("Visited nodes: " + visitedOrder);
                printPath(predecessors, source, destination);
                return;
            }
            
            LinkedList<Edge> edges = graph.adjacencyList.get(current.node);
            if (edges != null) {
                for (Edge edge : edges) {
                    String neighbor = edge.dest.id;
                    double newCost = edge.dest.getManhattanDist(destinationNode);
                    if (!visited.contains(neighbor)) {
                        predecessors.put(neighbor, current.node);
                        frontier.add(new NodeCost(neighbor, newCost));
                    }
                }
            }
        }
        
        System.out.println("No path found from " + source + " to " + destination);
        System.out.println("Visited nodes: " + visitedOrder);
    }
}
