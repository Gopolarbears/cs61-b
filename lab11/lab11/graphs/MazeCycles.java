package lab11.graphs;
import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean targetFound = true;
    private final Maze maze;
    private final int s;
    private final Stack<Integer> fringe = new Stack<>();
    private int[] edgeTo2 = new int[edgeTo.length];

    public MazeCycles(Maze m) {
        super(m);
        System.arraycopy(edgeTo, 0, edgeTo2, 0, edgeTo2.length);
        maze = m;
        s = maze.xyTo1D(1, 1);
        distTo[s] = 0;
        edgeTo[s] = 0;
        edgeTo2[s] = 0;
    }

    @Override
    public void solve() {
        dfsCycle(s);
    }

    // Helper methods go here
    private void dfsCycle(int v) {
        fringe.push(v);
        marked[v] = true;

        while (!fringe.isEmpty()) {
            int cur = fringe.pop();
            if (!marked[cur]) {
                marked[cur] = true;
            }
            for (int w : maze.adj(cur)) {
                if (marked[w] && edgeTo2[cur] != w) {
                    showCycle(cur, w);
                    return;
                }
                if (!marked[w]) {
                    fringe.push(w);
                    edgeTo2[w] = cur;
                    announce();
                    distTo[w] = distTo[cur] + 1;
                }
            }
        }
    }

    private void showCycle(int source, int target) {
        edgeTo[target] = source;
        int cur = source;
        while (cur != target) {
            edgeTo[cur] = edgeTo2[cur];
            announce();
            cur = edgeTo2[cur];
        }
    }
}

