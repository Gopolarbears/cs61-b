package lab11.graphs;
import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final int s;
    private final int t;
    private boolean targetFound = false;
    private final Maze maze;
    private final Queue<Integer> fringe = new LinkedList<>();

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        marked[v] = true;
        announce();
        fringe.add(v);

        while (!fringe.isEmpty()) {
            int cur = fringe.poll();
            if (cur == t) {
                targetFound = true;
            }

            if (targetFound) {
                return;
            }

            for (int w : maze.adj(cur)) {
                if (!marked[w]) {
                    marked[w] = true;
                    fringe.add(w);
                    edgeTo[w] = cur;
                    announce();
                    distTo[w] = distTo[cur] + 1;
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

