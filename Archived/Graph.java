
import java.util.*;

public class Graph {
    List<Node> nodes;
    List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    private Node findNodeById(String id) {
        for (Node node : nodes) {
            if (node.id.equals(id)) {
                return node;
            }
        }
        return null;
    }

    public void addNode(Node node, List<String> neighborIds) {
        nodes.add(node);
        for (String neighborId : neighborIds) {
            Node neighbor = findNodeById(neighborId);
            if (neighbor != null) {
                addEdge(node, neighbor);
            }
        }
    }

    public void addEdge(Node from, Node to) {
        double weight = haversine(from.latitude, from.longitude, to.latitude, to.longitude);
        edges.add(new Edge(from, to, weight));
    }

    public void addConnection(Node node1, Node node2) {
        // Add the nodes as neighbors to each other
        node1.addNeighbor(node2);
        node2.addNeighbor(node1);

        // Automatically create an edge between them
        double weight = haversine(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
        edges.add(new Edge(node1, node2, weight));
    }

    public void addTemporaryNode(Node eateryNode) {
        Edge closestEdge = null;
        double minDistance = Double.MAX_VALUE;
        Node closestNodeA = null;
        Node closestNodeB = null;

        // Step 1: Find the closest edge to the eatery node
        for (Edge edge : edges) {
            Node A = edge.from;
            Node B = edge.to;

            // Calculate the distance of the eateryNode to the edge (A, B)
            double distance = perpendicularDistance(eateryNode, A, B);

            if (distance < minDistance) {
                minDistance = distance;
                closestEdge = edge;
                closestNodeA = A;
                closestNodeB = B;
            }
        }

        if (closestEdge != null) {
            // Step 2: Find the projection point of the eatery on the closest edge
            Node projectionNode = projectPointOnEdge(eateryNode, closestNodeA, closestNodeB);

            // Step 3: Add the projectionNode as a temporary node
            nodes.add(projectionNode);

            // Step 4: Update edges
            edges.remove(closestEdge);
            edges.add(new Edge(closestNodeA, projectionNode, haversine(closestNodeA.latitude, closestNodeA.longitude, projectionNode.latitude, projectionNode.longitude)));
            edges.add(new Edge(projectionNode, closestNodeB, haversine(projectionNode.latitude, projectionNode.longitude, closestNodeB.latitude, closestNodeB.longitude)));
        }
    }

    // Helper function to calculate perpendicular distance from a point to a line segment
    private double perpendicularDistance(Node p, Node A, Node B) {
        double x1 = A.latitude;
        double y1 = A.longitude;
        double x2 = B.latitude;
        double y2 = B.longitude;
        double x0 = p.latitude;
        double y0 = p.longitude;

        double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
        double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        return numerator / denominator;
    }

    // Helper function to find the projection of point P on line segment AB
    private Node projectPointOnEdge(Node p, Node A, Node B) {
        double ax = A.latitude, ay = A.longitude;
        double bx = B.latitude, by = B.longitude;
        double px = p.latitude, py = p.longitude;

        double abx = bx - ax;
        double aby = by - ay;
        double apx = px - ax;
        double apy = py - ay;

        // Calculate the scalar projection of AP onto AB, clamping between 0 and 1
        double ab2 = abx * abx + aby * aby;
        double ap_ab = apx * abx + apy * aby;
        double t = Math.max(0, Math.min(1, ap_ab / ab2));

        // Find the projected coordinates
        double projX = ax + t * abx;
        double projY = ay + t * aby;

        return new Node(projX, projY, "TemporaryNode");
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }
}