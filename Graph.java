
import java.util.*;

public final class Graph{

    Map<String, LinkedList<Edge>> adjacencyList; 

    ArrayList<Node> upperTaftAveNodes;
    ArrayList<Node> lowerTaftAveNodes; 
    ArrayList<Node> fidelReyesNodes;
    ArrayList<Node> leonGuintoNodes;

    ArrayList<Node> nodeList; 

    public Graph() {
        this.adjacencyList = new HashMap<>();
        //initialize adjacency lists for all the vertices
        this.upperTaftAveNodes = new ArrayList<>(); 
        this.lowerTaftAveNodes = new ArrayList<>();
        this.fidelReyesNodes = new ArrayList<>();
        this.leonGuintoNodes = new ArrayList<>();
        this.nodeList = new ArrayList<>(); 
        //initialize streets
        
        //upper taft ave (y = 5) {-80 < x 50}
        for (int i = -80; i<= 50; i+=5){
            Node taftAveNode = new Node(i, 5, "UTaft"+i, false, -1);
            
            this.upperTaftAveNodes.add(taftAveNode);
            addNode(taftAveNode);
            if (i > -80){
                addEgde("UTaft"+(i - 5), "UTaft"+i);
            }
        }
        for (int i = -80; i <= 50; i+=5){
            Node taftAveNode = new Node(i, -3, "LTaft"+i, false, -1);
            this.lowerTaftAveNodes.add(taftAveNode);
            addNode(taftAveNode);
            if(i == -55){
                Node castroCrossingNode = new Node((i+2), -3, "LTaft"+(i+2), false, -1);
                this.lowerTaftAveNodes.add(castroCrossingNode);
                addNode(castroCrossingNode);
            }
            if (i > -80){
                if(i == -55){
                    addEgde("LTaft"+i, "LTaft"+(i+2));
                }
                else if(i == -50){
                    addEgde("LTaft"+(i-3), "LTaft"+i);
                }
                else{
                    addEgde("LTaft"+(i - 5), "LTaft"+i); 
                }
            }
        }
        for (int i = -90; i <= -20; i+=5){
            Node fidelNode = new Node(i, -14, "FidelReyes"+i, false, -1);
            this.fidelReyesNodes.add(fidelNode);
            addNode(fidelNode);
            if(i == -55){
                Node castroCrossingNode = new Node((i+2), -14, "FidelReyes"+(i+2), false, -1);
                this.fidelReyesNodes.add(castroCrossingNode);
                addNode(castroCrossingNode);
            }
            if (i > -90){
                if(i == -55){
                    addEgde("FidelReyes"+i, "FidelReyes"+(i+2));
                    addEgde("FidelReyes"+i,  "FidelReyes"+(i-5));
                }
                else if(i == -50){
                    addEgde("FidelReyes"+(i-3), "FidelReyes"+i);
                }
                else{
                    addEgde("FidelReyes"+(i - 5), "FidelReyes"+i);
                } 
            }
        }
        for (int i = -80; i <= 50; i+=5){
            Node leonGuintoNode = new Node(i, 15, "LeonGuinto"+i, false, -1);
            this.leonGuintoNodes.add(leonGuintoNode);
            addNode(leonGuintoNode);
            if (i > -80){
                addEgde("LeonGuinto"+(i - 5), "LeonGuinto"+i);
            }
        }
        //set crossings
        int[] taftAveCrossing = {-70, -50, -30, -10, 10};
        for (int i = 0; i < 5; i++){
            int x = taftAveCrossing[i];
            Node crossing = new Node(x, 1, "TaftCrossing"+x, false, -1);
            addNode(crossing);
            addEgde("LTaft"+x, "TaftCrossing"+x);
            addEgde("UTaft"+x, "TaftCrossing"+x);
        }
        
        Node castroStreetCrossing = new Node(-53, -9, "CastroStreetCrossing-53", false, -1);
        addNode(castroStreetCrossing);
        addEgde("FidelReyes-53", "CastroStreetCrossing-53");
        addEgde("LTaft-53", "CastroStreetCrossing-53");

        Node dagonoyStreetCrossing = new Node(-25, 10, "DagonoyStreetCrossing-25", false, -1);
        addNode(dagonoyStreetCrossing);
        addEgde("LeonGuinto-25", "DagonoyStreetCrossing-25");
        addEgde("UTaft-25", "DagonoyStreetCrossing-25");

        Node estradaStreetCrossing = new Node(15, 10, "EstradaStreetCrossing15", false, -1);
        addNode(estradaStreetCrossing);
        addEgde("LeonGuinto15", "EstradaStreetCrossing15");
        addEgde("UTaft15", "EstradaStreetCrossing15");
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
                System.out.println("Rating: " + node.rating); 
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

    public ArrayList<Node> getEateries(){
        ArrayList<Node> eateriesArrayList = new ArrayList<>(); 
        for(Node node: nodeList){
            if (node.isEatery)
                eateriesArrayList.add(node);
        }
        return eateriesArrayList; 
    }

    


}