package lxn240002;

import lxn240002.Graph.Vertex;
import lxn240002.Graph.Edge;
import lxn240002.Graph.GraphAlgorithm;
import lxn240002.Graph.Factory;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.*;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
    LinkedList<Vertex> finishList;
    boolean iscyclefound = false;

    public static class PERTVertex implements Factory {
        // Add fields to represent attributes of vertices here
        int d;      // duration
        int es;     // earliest start
        int ec;     // earliest finish
        int ls;     // latest start
        int lc;     // latest finish
        int slack;

        public PERTVertex(Vertex u) {
            d = 0;
            es = 0;
            ec = 0;
            ls = 0;
            lc = Integer.MAX_VALUE;
            slack = 0;
        }

        public PERTVertex make(Vertex u) {
            return new PERTVertex(u);
        }
    }

    private PERT(Graph g) {
        super(g, new PERTVertex(null));
    }

    public void setDuration(Vertex u, int d) {
        get(u).d = d;
    }


    // Implement the PERT algorithm. Returns false if the graph g is not a DAG.
    public boolean pert() {

        //check for DAG
        //if (!isDAG()) return false;
        topologicalOrder();
        if(iscyclefound) return false;

        // calculate ec for all the adjacent edges v of u
        for (Vertex u : finishList) {
            PERTVertex pu = get(u);
            pu.ec = pu.es + pu.d;

            for (Edge e : g.outEdges(u)) {
                Vertex v = e.to;
                PERTVertex pv = get(v);
                if (pv.es<pu.ec)
                 pv.es=pu.ec;

            }
        }

        // Get Critical Path Length
        int CPL = 0;
        for (Vertex u : g) {
            CPL = Math.max(CPL, get(u).ec);
        }

        // set lc to CPL
        for (Vertex u : g) {
            get(u).lc = CPL;
        }

        // calculate lc for all the adjacent edges v of u
        ListIterator<Vertex> revIter = finishList.listIterator(finishList.size());
        while (revIter.hasPrevious()) {
            Vertex u = revIter.previous();
            PERTVertex pu = get(u);
            pu.ls = pu.lc - pu.d;
            pu.slack = pu.ls - pu.es;

            for (Edge e : g.inEdges(u)) {
                Vertex v = e.from;
                PERTVertex pv = get(v);
                if(pv.lc > pu.ls) pv.lc=pu.ls;

            }
        }

        return true;
    }

    // Find a topological order of g using DFS
    LinkedList<Vertex> topologicalOrder() {
        finishList = new LinkedList<>();
        boolean[] marked = new boolean[g.size()];
        boolean[] inStack = new boolean[g.size()];
        iscyclefound = false;

        for (Vertex u : g) {
            if (!marked[u.getIndex()]) {
                dfs(u, marked,inStack);
            }
        }
        return finishList;
    }

    //DFS
    private void dfs(Vertex u, boolean[] marked, boolean[] inStack) {

        marked[u.getIndex()] = true;
        inStack[u.getIndex()] = true;

        for (Edge e : g.outEdges(u)) {
            Vertex v = e.to;
            if (inStack[v.getIndex()]) {
                iscyclefound = true;  // check for cycle
                return;
            }

            if (!marked[v.getIndex()]) {
                dfs(v, marked, inStack);
            }
        }
        inStack[u.getIndex()] = false;
        finishList.addFirst(u);
    }


    // The following methods are called after calling PERT

    //Earliest time at which Task u can be completed
    public int ec(Vertex u) {
        return get(u).ec;
    }

    //Latest completion time of u
    public int lc(Vertex u) {
        return get(u).lc;
    }

    //Slack of u
    public int slack(Vertex u) {
        return get(u).slack;
    }

    //CPL
    public int CPL() {
        int max = 0;
        for (Vertex u : g) {
            max = Math.max(max, get(u).ec);
        }
        return max;
    }

 // Length of a critical path (time taken to complete the project)
    public  int criticalPath( ) {
        return CPL();
    }

    // Is u a critical vertex?
    public boolean critical(Vertex u) {
        return slack(u) == 0;
    }

    // Number of critical vertices of g
    public int numCritical() {
        int count = 0;
        for (Vertex u : g) {
            if (critical(u)) count++;
        }
        return count;
    }

 /* Create a PERT instance on g, runs the algorithm.
 * Returns PERT instance if successful. Returns null if G is not a DAG
 */
    public static PERT pert(Graph g, int[] duration) {
        PERT p = new PERT(g);
        for (Vertex u : g) {
            p.setDuration(u, duration[u.getIndex()]);
        }
        //Run PERT algorithm. Returns false if g is not a DAG
        if(p.pert())
        {
            return p;
        }
        else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String graph = "10 13   1 2 1   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1";

        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        Scanner in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        int[] duration = new int[g.size()];
        for (int i = 0; i < g.size(); i++) {
            duration[i] = in.nextInt();
        }

        PERT p = pert(g, duration);
        if (p == null) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tDur\tEC\tLC\tSlack\tCritical");
            for (Vertex u : g) {
                System.out.println(u + "\t" + p.get(u).d+ "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
    }
}
