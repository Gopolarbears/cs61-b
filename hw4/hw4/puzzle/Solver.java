package hw4.puzzle;


import edu.princeton.cs.algs4.MinPQ;
import java.util.*;

public class Solver {
    private MinPQ<SearchNode> pq = new MinPQ<>(new WorldStateComparator());
    private List<WorldState> list = new ArrayList<>();
    private SearchNode lastSearchNode;
    private int moves = 0;
    private Map<WorldState, Integer> estimateMap = new HashMap<>();

    public Solver(WorldState initial) {
        SearchNode first = new SearchNode(initial, 0, null);
        pq.insert(first);
        while (!pq.isEmpty()) {
            SearchNode currentNode = pq.delMin();
            if (currentNode.worldState.isGoal()) {
                moves = currentNode.numberOfMoves;
                lastSearchNode = currentNode;
                break;
            } else {
                WorldState currentWorldState = currentNode.worldState;
                int currentMoves = currentNode.numberOfMoves;
                for (WorldState neighbor : currentWorldState.neighbors()) {
                    if (currentNode.previousNode != null && neighbor.equals(currentNode.previousNode.worldState)) {
                        continue;
                    }
                    pq.insert(new SearchNode(neighbor, currentMoves + 1, currentNode));
                }
            }
        }
    }

    private class SearchNode {
        WorldState worldState;
        int numberOfMoves;
        SearchNode previousNode;

        public SearchNode(WorldState worldState, int numberOfMoves, SearchNode previousNode) {
            this.worldState = worldState;
            this.numberOfMoves = numberOfMoves;
            this.previousNode = previousNode;
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        SearchNode currentNode = lastSearchNode;
        while (currentNode.previousNode != null) {
            list.add(currentNode.worldState);
            currentNode = currentNode.previousNode;
        }
        list.add(currentNode.worldState);
        return list;
    }

    private class WorldStateComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            int o1Edtg = getEdtg(o1);
            int o2Edtg = getEdtg(o2);
            int o1Priority = o1.numberOfMoves + o1Edtg;
            int o2Priority = o2.numberOfMoves + o2Edtg;
            return o1Priority - o2Priority;
        }

        private int getEdtg(SearchNode sn) {
            if (!estimateMap.containsKey(sn.worldState)) {
                estimateMap.put(sn.worldState, sn.worldState.estimatedDistanceToGoal());
            }
            return estimateMap.get(sn.worldState);
        }
    }
}
