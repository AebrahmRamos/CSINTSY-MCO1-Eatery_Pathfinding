import java.util.*;

public final class Graph{

    Map<String, LinkedList<Edge>> adjacencyList; 

    ArrayList<Node> upperTaftAveNodes;
    ArrayList<Node> lowerTaftAveNodes; 

    ArrayList<Node> nodeList; 

    public Graph() {
        this.adjacencyList = new HashMap<>();
        //initialize adjacency lists for all the vertices
        this.upperTaftAveNodes = new ArrayList<>(); 
        this.lowerTaftAveNodes = new ArrayList<>();
        this.nodeList = new ArrayList<>(); 
        //initialize streets
        
        //upper taft ave (y = 5) {-80 < x 50}
        for (int i = -81; i<= 51; i+=3){
            Node taftAveNode = new Node(i, 5, "UTaft"+i, false);
            this.upperTaftAveNodes.add(taftAveNode);
            addNode(taftAveNode);
            if (i > -81){
                addEgde("UTaft"+(i - 3), "UTaft"+i);
            }
        }
        for (int i = -81; i <= 51; i+=3){
            Node taftAveNode = new Node(i, -3, "LTaft"+i, false);
            this.lowerTaftAveNodes.add(taftAveNode);
            addNode(taftAveNode);
            if (i > -81){
                addEgde("LTaft"+(i - 3), "LTaft"+i); 
            }
        }

    }

    public  void addNode(Node node){
        adjacencyList.put(node.id, new LinkedList<>()); 
        nodeList.add(node); 
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

    public Map getGraph (){
        return this.adjacencyList;
    }

    public void printGraph(){
        
        for (Map.Entry<String, LinkedList<Edge>> entry : adjacencyList.entrySet()){
            LinkedList<Edge> list = entry.getValue(); 
            for (int j = 0; j <list.size() ; j++) {
                System.out.print(entry.getKey() + " is connected to " +
                        list.get(j).dest.id + " with weight ");
                System.out.printf("%2f. \n", list.get(j).weight);
            }
        }
    }

    


}