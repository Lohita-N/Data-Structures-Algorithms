package BFS;

import BFS.Graph.*;

import java.io.File;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

public class BFS extends Graph.GraphAlgorithm<BFS.BFSVertex> {
    public static final int INFINITY = Integer.MAX_VALUE;
    Vertex src;  // the starting vertex for this BFS run

    // Holds per-vertex info during BFS
    public static class BFSVertex implements Factory {
        boolean seen;    // have we visited this node yet?
        Vertex parent;   // where we came from
        int distance;    // how far from the source

        // constructor: start unseen, no parent, infinite distance
        public BFSVertex(Vertex u) {
            seen = false;
            parent = null;
            distance = INFINITY;
        }
        // Factory method to create a new BFSVertex
        public BFSVertex make(Vertex u) {
            return new BFSVertex(u);
        }
    }

    // Set up the algorithm with the graph and a sample BFSVertex
    public BFS(Graph g) {
        super(g, new BFSVertex(null));
    }

    // The main BFS logic
    public void bfs(Vertex src) {
        this.src = src;
        Queue<Vertex> queue = new LinkedList<>();

        // Reset all vertices to “unvisited”
        for (Vertex u : g) {
            BFSVertex bv = get(u);
            bv.seen     = false;
            bv.distance = INFINITY;
            bv.parent   = null;
        }

        // Mark source as seen, distance 0, no parent
        BFSVertex srcBFS = get(src);
        srcBFS.seen     = true;
        srcBFS.distance = 0;
        srcBFS.parent   = null;

        queue.add(src);  // start from the source

        // Process until no more nodes in queue
        while (!queue.isEmpty()) {
            Vertex u = queue.remove();               // take next node
            for (Edge e : g.incident(u)) {           // look at each outgoing edge
                Vertex v = e.otherEnd(u);            // find the neighbor
                BFSVertex bv = get(v);
                if (!bv.seen) {                      // if neighbor unseen
                    bv.seen     = true;             // mark seen
                    bv.distance = get(u).distance + 1;  // set distance
                    bv.parent   = u;                // record path
                    queue.add(v);                   // enqueue for later
                }
            }
        }
    }

    // Helper to run BFS on a graph from a given vertex
    public static BFS breadthFirstSearch(Graph g, Vertex src) {
        BFS b = new BFS(g);
        b.bfs(src);
        return b;
    }

    // Overload: run BFS by vertex index
    public static BFS breadthFirstSearch(Graph g, int s) {
        return breadthFirstSearch(g, g.getVertex(s));
    }

    public static void main(String[] args) throws Exception {
        // Sample input string: 6 nodes, 12 edges, then edges (u v w), then source
        String string = "6 12  1 2 1  1 3 1  2 3 1  2 4 1  3 4 1  3 5 1  " +
                "4 5 1  4 6 1  5 6 1  2 6 1  1 6 1  6 1 1 1";

        // If a filename is given, read from file; otherwise use the sample string
        Scanner in = args.length > 0
                ? new Scanner(new File(args[0]))
                : new Scanner(string);

        Graph g = Graph.readDirectedGraph(in);  // build graph
        int s = in.nextInt();                   // source vertex index

        BFS b = breadthFirstSearch(g, s);       // run BFS

        g.printGraph(false);                    // show graph structure

        // Print distances and parents for each node
        System.out.println("Output of BFS:\nNode\tDist\tParent\n----------------------");
        for (Vertex u : g) {
            int dist = b.get(u).distance;
            String distStr   = (dist == INFINITY) ? "Inf" : String.valueOf(dist);
            String parentStr = (b.get(u).parent == null) ? "--"
                    : b.get(u).parent.toString();
            System.out.println(u + "\t" + distStr + "\t" + parentStr);
        }
    }
}
