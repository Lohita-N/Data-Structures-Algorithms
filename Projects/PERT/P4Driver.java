import java.util.Scanner;

// prints output only if size < 20 or details is true
public class P4Driver {
    public static void main(String[] args) throws Exception {
        boolean details = false;
        String graph = "10 13   1 2 1   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new java.io.File(args[0])) : new Scanner(graph);
        if(args.length > 1) { details = true; }
        Graph g = Graph.readDirectedGraph(in);
        int[] duration = new int[g.size()];
        for(int i=0; i<g.size(); i++) {
            duration[i] = in.nextInt();
        }
        PERT p = PERT.pert(g, duration);
        if(p == null) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println(p.criticalPath() + " " + p.numCritical());
            if(g.size() <= 20 || details) {
                System.out.println("u\tDur\tEC\tLC\tSlack\tCritical");
                for(Graph.Vertex u: g) {
                   System.out.println(u + "\t" + duration[u.getIndex()] + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
                }
            }
        }
    }
}
