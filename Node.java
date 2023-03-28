import java.util.*;
import java.util.stream.Collectors;

public class Node {
    private static final int MAX_VALUE = Integer.MAX_VALUE;
    private String label;
    private int weight = MAX_VALUE;
    private static TreeMap<Node, Integer> links;
    private Node prev = null;

    public Node(String label) {
        this.label = label;
        links = new TreeMap<>((a, b) -> a.label.compareTo(b.label));
    }

    public void link(Node node2, int weight) {
        links.put(node2, weight);
        node2.links.put(this, weight);
    }

    public String getPath(){
        String weight = "";
        if(this.weight < MAX_VALUE){
            weight += this.weight;
        }
        else{
            weight = "inf";
        }
        String out = "(" + label + "-" + weight + ")";
        if(prev != null){
            out = prev.getPath() + "-" + links.get(prev) + "->" + out;
        }
        return out;
    }
    

    public static List<Node> dijkstra(Node start, Node end) {
        Set<Node> unvisited = new HashSet<>();
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> prevNode = new HashMap<>();
        List<Node> shortestPath = new ArrayList<>();

        for (Node node : links.keySet()) {
            unvisited.add(node);
            distance.put(node, MAX_VALUE);
        }

        distance.put(start, 0);

        while (!unvisited.isEmpty()) {
            Node current = Collections.min(unvisited, Comparator.comparing(distance::get));

            if (current == end) {
                while (current != null) {
                    shortestPath.add(current);
                    current = prevNode.get(current);
                }
                Collections.reverse(shortestPath);
                return shortestPath;
            }

            unvisited.remove(current);

            for (Node neighbor : current.links.keySet()) {
                int altDistance = distance.get(current) + current.links.get(neighbor);
                if (altDistance < distance.get(neighbor)) {
                    distance.put(neighbor, altDistance);
                    prevNode.put(neighbor, current);
                }
            }
        }
        return shortestPath;
    }


    public static void main(String[] args) {
        Node node1 = new Node("A");
        Node node2 = new Node("B");
        Node node3 = new Node("C");
        Node node4 = new Node("D");
        node1.link(node2, 10);
        node1.link(node3, 3);
        node2.link(node3, 1);
        node2.link(node4, 2);
        node3.link(node2, 4);
        node3.link(node4, 8);

        List<Node> path = dijkstra(node1, node4);
        if (path.isEmpty()) {
            System.out.println("nessuna strada trovata");
        } else {
            System.out.println("percorso più corto: " + path.stream().map(Node::getPath).collect(Collectors.joining(" ")));
        }
    }
}
