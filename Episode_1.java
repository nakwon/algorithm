import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Player player = new Player();
        player.start();
    }
    
    public void start() {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        
        Node[] nodes = new Node[N];
        
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            
            if (nodes[N1] == null) {
                nodes[N1] = new Node(N1);
            } 
            
            if (nodes[N2] == null) {
                nodes[N2] = new Node(N2);
            } 
        
            nodes[N1].addNeighborhood(nodes[N2]);
            nodes[N2].addNeighborhood(nodes[N1]);
        }
        
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            nodes[EI].setGateway(true);
        }

        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            int[] find = bfs(SI, nodes);
            
            cut(find, nodes);
            
            System.out.println(find[0] + " " + find[1]);
        }
    }
    
    private void cut(int[] find, Node[] nodes) {
        for (int i = 0; i < nodes[find[0]].neighborhoods.size(); i++) {
            if (nodes[find[0]].neighborhoods.get(i).id == find[1]) {
                nodes[find[0]].neighborhoods.remove(i);
            }
        }
        
        for (int i = 0; i < nodes[find[1]].neighborhoods.size(); i++) {
            if (nodes[find[1]].neighborhoods.get(i).id == find[0]) {
                nodes[find[1]].neighborhoods.remove(i);
            }
        }
    }
    
    private int[] bfs(int SI, Node[] nodes) {
        
        for (Node node : nodes) {
            node.checked = false;
        }
        
        ArrayList<Integer> check = new ArrayList<>();
        check.add(SI);
        nodes[SI].checked = true;
        
        while(check.size() > 0) {
            int position = check.get(0);
            check.remove(0);
            
            for (int i = 0; i < nodes[position].neighborhoods.size(); i++) {
                if (!nodes[position].neighborhoods.get(i).checked) {
                    check.add(nodes[position].neighborhoods.get(i).id);
                    nodes[position].neighborhoods.get(i).checked = true;
                    
                    if (nodes[position].neighborhoods.get(i).isGateway) {
                        return new int[] {nodes[position].neighborhoods.get(i).id, position};
                    }
                    
                }
            }
        }
        
        return new int[] {0, 0};
    }
    
    
    class Node {
        int id;
        boolean isGateway;
        boolean checked;
        ArrayList<Node> neighborhoods;
        
        public Node(int id) {
            this.id = id;
            isGateway = false;
            neighborhoods = new ArrayList<Node>();
        }
        
        public void addNeighborhood(Node neighborhood) {
            neighborhoods.add(neighborhood);
        }
        
        public void setGateway(boolean isGateway) {
            this.isGateway = isGateway;
        }
    }
}