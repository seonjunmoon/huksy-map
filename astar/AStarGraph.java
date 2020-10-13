package astar;

import java.util.List;

/** Represents a graph of vertices. */
public interface AStarGraph<Vertex> {
    /** Returns the list of outgoing edges from the given vertex. */
    List<WeightedEdge<Vertex>> neighbors(Vertex v);

    /**
     *  Returns an estimated distance from vertex s to the goal vertex according to
     *  the A* heuristic function for this graph.
     */
    double estimatedDistanceToGoal(Vertex s, Vertex goal);
}
