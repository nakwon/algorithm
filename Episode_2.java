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
        
            nodes[N1].neighborhoods.add(nodes[N2]);
            nodes[N2].neighborhoods.add(nodes[N1]);
        }
        
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            nodes[EI].isGateway = true;
        }

        // game loop
        while (true) {
            int SI = in.nextInt();
            
            bfs(SI, nodes);
            
            int[] find = findCutNode(SI, nodes);
            
            nodes[find[0]].cut(find[1]);
            nodes[find[1]].cut(find[0]);
        
            System.out.println(find[0] + " " + find[1]);
        }
    }
    
    private void bfs(int SI, Node[] nodes) {
        
        for (Node node : nodes) {
            node.checked = false;
            node.depth = 0;
            node.warning = 0;
        }
        
        ArrayList<Integer> check = new ArrayList<>();
        check.add(SI);
        nodes[SI].checked = true;
        
        while(check.size() > 0) {
            int position = check.get(0);
            check.remove(0);
            
            for (int i = 0; i < nodes[position].neighborhoods.size(); i++) {
                Node neighborhood = nodes[position].neighborhoods.get(i);
                if (!neighborhood.checked && !neighborhood.isGateway) {
                    check.add(neighborhood.id);
                    neighborhood.checked = true;
                    neighborhood.depth = nodes[position].depth + 1;
                    neighborhood.warning = nodes[position].warning + neighborhood.gateways();
                }
            }
        }
    }
    
    private int[] findCutNode(int SI, Node[] nodes) {
        int maxPosition = SI;
        int exceptPosition = SI;
        
        for (Node node : nodes) {
            if (node.isGateway) {
                continue;
            }
            
            if (node.depth == 0 && node.gateways() > 0) {
                return new int[] {node.id, node.firstGatewayPosition()};
            }
            
            if (node.gateways() > 0) {
                if (node.depth < nodes[exceptPosition].depth) {
                    exceptPosition = node.id;
                } else {
                    if (exceptPosition == SI) {
                        exceptPosition = node.id;
                    }
                }
            }
        
            if (node.warning > node.depth) { 
                if (node.depth < nodes[maxPosition].depth) {
                    maxPosition = node.id;
                } else {
                    if (maxPosition == SI) {
                        maxPosition = node.id;
                    }
                }
            }
        }
        
        if (maxPosition == SI) {
            return new int[] {exceptPosition, nodes[exceptPosition].firstGatewayPosition()};
        } else {
            return new int[] {maxPosition, nodes[maxPosition].firstGatewayPosition()};
        }
    }
    
    
    class Node {
        int id;
        boolean isGateway;
        boolean checked;
        ArrayList<Node> neighborhoods;
        int depth;
        int warning;
        
        public Node(int id) {
            this.id = id;
            isGateway = false;
            neighborhoods = new ArrayList<Node>();
            depth = 0;
            warning = 0;
        }

        public void cut(int id) {
            for (int i = 0; i < neighborhoods.size(); i++) {
                if (neighborhoods.get(i).id == id) {
                    neighborhoods.remove(i);
                    return;
                }
            }
        }
        
        public int gateways() {
            int sum = 0;
            for (Node neighborhood : neighborhoods) {
                if (neighborhood.isGateway) {
                    sum++;
                }
            }
            return sum;
        }
        
        public int firstGatewayPosition() {
            for (Node neighborhood : neighborhoods) {
                if (neighborhood.isGateway) {
                    return neighborhood.id;
                }
            }
            return 0;
        }
    }
}