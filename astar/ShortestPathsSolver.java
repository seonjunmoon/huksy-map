package astar;

import java.util.List;

/**
 * Interface for shortest path solvers.
 * (This interface only provides methods for getting results of a shortest path problem,
 * since it expects the solving to be done in the constructor of the class.)
 */
public interface ShortestPathsSolver<Vertex> {
    /** Returns one of SOLVED, TIMEOUT, or UNSOLVABLE. */
    SolverOutcome outcome();

    /**
     * A list of vertices corresponding to the solution, from start to end.
     * Returns an empty list if problem was unsolvable or solving timed out.
     */
    List<Vertex> solution();

    /**
     * The total weight of the solution, taking into account edge weights.
     * Returns Double.POSITIVE_INFINITY if problem was unsolvable or solving timed out.
     */
    double solutionWeight();

    /** The total number of states explored while solving. */
    int numStatesExplored();

    /** The total time spent in seconds by the constructor to run A* search. */
    double explorationTime();

    /** This possible results of a shortest path problem. */
    enum SolverOutcome {
        /** Path successfully found. */
        SOLVED,
        /** Path not found due to exceeding allowed computation time. */
        TIMEOUT,
        /** No path exists from start to end. */
        UNSOLVABLE
    }
}
