import java.util.*;

public class Graph{
    int totalNodes;

    Map<String, LinkedList<Edge>> adjacencyList; 
    ArrayList<Node> nodeList;
    ArrayList<Edge> edgeList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        //initialize adjacency lists for all the vertices
        this.nodeList = new ArrayList<>(); 
        this.edgeList = new ArrayList<>(); 
    }

    public void addNode(Node node){
        adjacencyList.put(node.id, new LinkedList<>()); 
        nodeList.add(node); 
    }

    public void addEgde(String source, String destination) {
        Node nodeA = getNodeById(source);
        Node nodeB = getNodeById(destination);
        Edge edge = new Edge(nodeA, nodeB);
        adjacencyList.get(source).addFirst(edge); //for directed graph
    }

    private Node getNodeById(String nodeId){
        for (Node node : this.nodeList){
            if (node.id.equals(nodeId)){
                return node;
            }
        }
        return null; 
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