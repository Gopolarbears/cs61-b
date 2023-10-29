package lab11.graphs;
import java.util.PriorityQueue;
/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private final int targetX;
    private final int targetY;
    PriorityQueue<Node> priority = new PriorityQueue<>();

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        this.targetX = targetX;
        this.targetY = targetY;
        priority.add(new Node(0));
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - targetX) + Math.abs(maze.toY(v) - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        int cur;
        marked[s] = true;
        while (!priority.isEmpty()) {
            cur = priority.remove().position;
            marked[cur] = true;
            for (int x : maze.adj(cur)) {
                if (!marked[x]) {
                    edgeTo[x] = cur;
                    distTo[x] = distTo[cur] + 1;
                    announce();
                    if (x == t) {
                        return;
                    } else {
                        priority.add(new Node(x));
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

    private class Node implements Comparable<Node> {
        int position;
        int priority;
        public Node(int xy) {
            position = xy;
            priority = distTo[xy] + h(xy);
        }

        public int compareTo(Node x) {
            return priority - x.priority;
        }
    }
}

