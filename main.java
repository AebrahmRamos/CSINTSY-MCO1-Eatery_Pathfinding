import java.io.*;
import java.util.*;

public class Main {
    private static Map<String, List<String>> adjacencyList = new HashMap<>();
    private static Map<String, double[]> coordinatesMap = new HashMap<>(); // Stores coordinates of nodes
    private static Map<String, String> nodeNamesMap = new HashMap<>(); // Stores node names
    private static List<String> visitedNodes = new ArrayList<>();
    private static String nextNodeIdentifier = "A";

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
                    System.out.println("Visited nodes: " + visitedNodes);
                    break;

                case 2:
                    addNode(scanner);
                    break;

                case 3:
                    removeNode(scanner);
                    break;

                case 4:
                    viewNodeDetails(scanner);
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
                    String node = parts[0].trim();
                    String nodeName = parts[1].trim();
                    double latitude = Double.parseDouble(parts[2].trim());
                    double longitude = Double.parseDouble(parts[3].trim());
                    adjacencyList.putIfAbsent(node, new ArrayList<>());
                    coordinatesMap.put(node, new double[]{longitude, latitude});
                    nodeNamesMap.put(node, nodeName);
                    updateNextNodeIdentifier(node);
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
                    String nodeA = parts[0].trim();
                    String nodeB = parts[1].trim();

                    adjacencyList.putIfAbsent(nodeA, new ArrayList<>());
                    adjacencyList.putIfAbsent(nodeB, new ArrayList<>());

                    adjacencyList.get(nodeA).add(nodeB);
                    adjacencyList.get(nodeB).add(nodeA); // Assuming the graph is undirected
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV files: " + e.getMessage());
        }
    }

    private static void addNode(Scanner scanner) {
        System.out.println("Enter the name of the new eatery/node:");
        String nodeName = scanner.nextLine();

        System.out.println("Enter the longitude of the new eatery/node:");
        double longitude = Double.parseDouble(scanner.nextLine());

        System.out.println("Enter the latitude of the new eatery/node:");
        double latitude = Double.parseDouble(scanner.nextLine());

        String newNode = nextNodeIdentifier;
        updateNextNodeIdentifier(newNode);

        adjacencyList.putIfAbsent(newNode, new ArrayList<>());
        coordinatesMap.put(newNode, new double[]{longitude, latitude});
        nodeNamesMap.put(newNode, nodeName);

        System.out.println("Enter the connections for the new node, separated by commas:");
        String[] connections = scanner.nextLine().split(",");
        for (String connection : connections) {
            connection = connection.trim();
            if (!coordinatesMap.containsKey(connection)) {
                System.out.println("Warning: Connection to non-existent node " + connection + ". Skipping this connection.");
                continue;
            }
            adjacencyList.putIfAbsent(connection, new ArrayList<>());
            adjacencyList.get(newNode).add(connection);
            adjacencyList.get(connection).add(newNode);
        }

        // Update CSV files
        try (FileWriter nodesWriter = new FileWriter("Data/NodesEateries.csv", true);
             FileWriter edgesWriter = new FileWriter("Data/Edges.csv", true)) {

            nodesWriter.write(newNode + "," + nodeName + "," + longitude + "," + latitude + "\n");

            for (String connection : connections) {
                if (coordinatesMap.containsKey(connection)) {
                    edgesWriter.write(newNode + "," + connection + "," + calculateHaversineDistance(newNode, connection) + "\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Error writing to CSV files: " + e.getMessage());
        }

        System.out.println("New node added successfully.");
    }

    private static void removeNode(Scanner scanner) {
        System.out.println("Enter the name of the node to remove:");
        String nodeToRemove = scanner.nextLine();

        if (!adjacencyList.containsKey(nodeToRemove)) {
            System.out.println("Node not found.");
            return;
        }

        // Remove the node from adjacency list, coordinates map, and node names map
        adjacencyList.remove(nodeToRemove);
        coordinatesMap.remove(nodeToRemove);
        nodeNamesMap.remove(nodeToRemove);

        // Remove all edges associated with this node
        for (List<String> neighbors : adjacencyList.values()) {
            neighbors.remove(nodeToRemove);
        }

        // Update CSV files
        try (FileWriter nodesWriter = new FileWriter("Data/NodesEateries.csv");
             FileWriter edgesWriter = new FileWriter("Data/Edges.csv")) {

            // Write updated nodes
            nodesWriter.write("Identifier,Name,Longitude,Latitude\n");
            for (Map.Entry<String, double[]> entry : coordinatesMap.entrySet()) {
                String node = entry.getKey();
                double[] coords = entry.getValue();
                nodesWriter.write(node + "," + nodeNamesMap.get(node) + "," + coords[0] + "," + coords[1] + "\n");
            }

            // Write updated edges
            edgesWriter.write("NodeA,NodeB,Distance\n");
            for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
                String nodeA = entry.getKey();
                for (String nodeB : entry.getValue()) {
                    if (coordinatesMap.containsKey(nodeB)) {
                        edgesWriter.write(nodeA + "," + nodeB + "," + calculateHaversineDistance(nodeA, nodeB) + "\n");
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error updating CSV files: " + e.getMessage());
        }

        System.out.println("Node removed successfully.");
    }

    private static void viewNodeDetails(Scanner scanner) {
        System.out.println("Enter the identifier of the node to view details:");
        String nodeName = scanner.nextLine();

        if (!coordinatesMap.containsKey(nodeName)) {
            System.out.println("Node not found.");
            return;
        }

        double[] coords = coordinatesMap.get(nodeName);
        System.out.println("Node Identifier: " + nodeName);
        System.out.println("Node Name: " + nodeNamesMap.get(nodeName));
        System.out.println("Longitude: " + coords[0]);
        System.out.println("Latitude: " + coords[1]);

        List<String> neighbors = adjacencyList.getOrDefault(nodeName, new ArrayList<>());
        System.out.println("Connections:");
        for (String neighbor : neighbors) {
            if (coordinatesMap.containsKey(neighbor)) {
                double distance = calculateHaversineDistance(nodeName, neighbor);
                System.out.println(" - " + neighbor + " (Distance: " + distance + " km)");
            }
        }
    }

    private static void viewAllNodes() {
        System.out.println("All nodes:");
        for (Map.Entry<String, String> entry : nodeNamesMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    private static void updateNextNodeIdentifier(String currentIdentifier) {
        if (currentIdentifier.compareTo(nextNodeIdentifier) >= 0) {
            nextNodeIdentifier = getNextIdentifier(currentIdentifier);
        }
    }

    private static String getNextIdentifier(String currentIdentifier) {
        StringBuilder nextIdentifier = new StringBuilder(currentIdentifier);
        int index = nextIdentifier.length() - 1;

        while (index >= 0) {
            char currentChar = nextIdentifier.charAt(index);
            if (currentChar == 'Z') {
                nextIdentifier.setCharAt(index, 'A');
                index--;
            } else {
                nextIdentifier.setCharAt(index, (char) (currentChar + 1));
                return nextIdentifier.toString();
            }
        }

        nextIdentifier.insert(0, 'A');
        return nextIdentifier.toString();
    }

    private static double calculateHaversineDistance(String nodeA, String nodeB) {
        if (!coordinatesMap.containsKey(nodeA) || !coordinatesMap.containsKey(nodeB)) {
            throw new IllegalArgumentException("One or both nodes do not exist in the coordinates map.");
        }

        double[] coordA = coordinatesMap.get(nodeA);
        double[] coordB = coordinatesMap.get(nodeB);

        double lat1 = Math.toRadians(coordA[1]);
        double lon1 = Math.toRadians(coordA[0]);
        double lat2 = Math.toRadians(coordB[1]);
        double lon2 = Math.toRadians(coordB[0]);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radius = 6371.0; // Radius of the Earth in kilometers

        return radius * c;
    }

    private static void bfs(String source, String destination) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            visitedNodes.add(current);
            if (current.equals(destination)) {
                System.out.println("Found destination: " + current);
                System.out.println("Final route: " + visitedNodes);
                return;
            }

            List<String> neighbors = adjacencyList.getOrDefault(current, new ArrayList<>());
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        System.out.println("Destination not found");
        System.out.println("Visited nodes: " + visitedNodes);
    }

    private static void dfs(String source, String destination) {
        Set<String> visited = new HashSet<>();
        dfsHelper(source, destination, visited);
        System.out.println("Visited nodes: " + visitedNodes);
    }

    private static void dfsHelper(String current, String destination, Set<String> visited) {
        if (visited.contains(current)) {
            return;
        }
        visited.add(current);
        visitedNodes.add(current);
        if (current.equals(destination)) {
            System.out.println("Found destination: " + current);
            System.out.println("Final route: " + visitedNodes);
            return;
        }

        List<String> neighbors = adjacencyList.getOrDefault(current, new ArrayList<>());
        for (String neighbor : neighbors) {
            dfsHelper(neighbor, destination, visited);
        }
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
