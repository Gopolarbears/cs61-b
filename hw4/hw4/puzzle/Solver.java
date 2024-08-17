package hw4.puzzle;


import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    MinPQ<SearchNode> pq = new MinPQ<>(new worldStateComparator());
    Map<WorldState, SearchNode> map = new HashMap<>();
    private List<WorldState> list = new ArrayList<>();
    private SearchNode lastSearchNode;
    int moves = 0;

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

    private class worldStateComparator implements Comparator<SearchNode> {
        public int compare(SearchNode o1, SearchNode o2) {
            return (o1.numberOfMoves + o1.worldState.estimatedDistanceToGoal()) - (o2.numberOfMoves + o2.worldState.estimatedDistanceToGoal());
        }
    }
}
