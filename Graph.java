
import java.util.*;

public final class Graph{

    Map<String, LinkedList<Edge>> adjacencyList; 

    ArrayList<Node> upperTaftAveNodes;
    ArrayList<Node> lowerTaftAveNodes; 
    ArrayList<Node> fidelReyesNodes;

    ArrayList<Node> nodeList; 

    public Graph() {
        this.adjacencyList = new HashMap<>();
        //initialize adjacency lists for all the vertices
        this.upperTaftAveNodes = new ArrayList<>(); 
        this.lowerTaftAveNodes = new ArrayList<>();
        this.fidelReyesNodes = new ArrayList<>();
        this.nodeList = new ArrayList<>(); 
        //initialize streets
        
        //upper taft ave (y = 5) {-80 < x 50}
        for (int i = -80; i<= 50; i+=5){
            Node taftAveNode = new Node(i, 5, "UTaft"+i, false);
            
            this.upperTaftAveNodes.add(taftAveNode);
            addNode(taftAveNode);
            if (i > -80){
                addEgde("UTaft"+(i - 5), "UTaft"+i);
            }
        }
        for (int i = -80; i <= 50; i+=5){
            Node taftAveNode = new Node(i, -3, "LTaft"+i, false);
            this.lowerTaftAveNodes.add(taftAveNode);
            addNode(taftAveNode);
            if (i > -80){
                addEgde("LTaft"+(i - 5), "LTaft"+i); 
            }
        }
        for (int i = -90; i <= -20; i+=5){
            Node fidelNode = new Node(i, -14, "FidelReyes"+i, false);
            this.fidelReyesNodes.add(fidelNode);
            addNode(fidelNode);
            if (i > -90){
                addEgde("FidelReyes"+(i - 5), "FidelReyes"+i); 
            }
        }
        //set crossings
        int[] taftAveCrossing = {-70, -50, -30, -10, 10};
        for (int i = 0; i < 5; i++){
            int x = taftAveCrossing[i];
            Node crossing = new Node(x, 1, "TaftCrossing"+x, false);
            addNode(crossing);
            addEgde("LTaft"+x, "TaftCrossing"+x);
            addEgde("UTaft"+x, "TaftCrossing"+x);
        }
    }

    public  void addNode(Node node){
        adjacencyList.put(node.id, new LinkedList<>()); 
        nodeList.add(node); 
     
    }

    // Removing a node from the graph
    public void removeNode(String nodeId) {
        // Remove from nodeList
        nodeList.removeIf(node -> node.id.equals(nodeId));

        // Remove all edges associated with the node in adjacency list
        adjacencyList.remove(nodeId);

        // Remove edges pointing to this node in other nodes' adjacency lists
        for (LinkedList<Edge> edges : adjacencyList.values()) {
            edges.removeIf(edge -> edge.dest.id.equals(nodeId)); // if dest is a Node
        }
    }

    // View a node's information
    public void viewNode(String nodeId) {
        for (Node node : nodeList) {
            if (node.id.equals(nodeId)) {
                System.out.println("Node ID: " + node.id);
                System.out.println("Latitude: " + node.latitude);
                System.out.println("Longitude: " + node.longitude);
                System.out.println("Is Eatery: " + node.isEatery);
                return;
            }
        }
        System.out.println("Node not found: " + nodeId);
    }

    public  void addEgde(String source, String destination) {
        Node nodeA = getNodeById(source);
        Node nodeB = getNodeById(destination);
        Edge edge = new Edge(nodeA, nodeB);
        Edge edge2 = new Edge(nodeB, nodeA);
        adjacencyList.get(source).addFirst(edge); //for directed graph
        adjacencyList.get(destination).addFirst(edge2);
    }

    public Node getNodeById(String nodeId){
        for (Node node : this.nodeList){
            if (node.id.equals(nodeId)){
                return node;
            }
        }
        return null; 
    }

    public Map<String, LinkedList<Edge>> getGraph (){
        return this.adjacencyList;
    }

    public void printGraph(){
        
        for (Map.Entry<String, LinkedList<Edge>> entry : adjacencyList.entrySet()){
            LinkedList<Edge> list = entry.getValue(); 
            for (int j = 0; j <list.size() ; j++) {
                System.out.print(entry.getKey() + " " +
                        list.get(j).dest.id + " ");
                System.out.printf("%2f \n", list.get(j).weight);
            }
        }
    }

    


}