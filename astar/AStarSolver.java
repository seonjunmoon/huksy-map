package astar;

import edu.princeton.cs.algs4.Stopwatch;
import pq.TreeMapMinPQ;

import java.util.*;

/**
 * @see ShortestPathsSolver for more method documentation
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private int numStatesExplored;
    private List<Vertex> solution;
    private double timeSpent;
    //private ArrayHeapMinPQ<Vertex> pq;
    private TreeMapMinPQ<Vertex> pq;
    private Map<Vertex, Vertex> edgeTo;
    private Map<Vertex, Double> distTo;
    /**
     * Immediately solves and stores the result of running memory optimized A*
     * search, computing everything necessary for all other methods to return
     * their results in constant time. The timeout is given in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        pq = new TreeMapMinPQ<>();
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        Stopwatch sw = new Stopwatch();
        solution = new LinkedList<>();
        numStatesExplored = 0;

        distTo.put(start, 0.0);
        pq.add(start, distTo.get(start) + input.estimatedDistanceToGoal(start, end));
        Vertex v = start;

        if (start.equals(end)) {
            solution.add(start);
            outcome = SolverOutcome.SOLVED;
            timeSpent = sw.elapsedTime();
            return;
        }


        while (!pq.isEmpty() && sw.elapsedTime() < timeout) {
            v = pq.removeSmallest();
            if (v.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                break;
            }
            numStatesExplored += 1;
            List<WeightedEdge<Vertex>> neighborEdges = input.neighbors(v);
            for (WeightedEdge<Vertex> e : neighborEdges) {
                Vertex from = e.from();
                Vertex to = e.to();
                double currDistance = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double thisDistance = distTo.get(from) + e.weight();
                if (thisDistance < currDistance) {
                    edgeTo.put(to, from);
                    distTo.put(to, thisDistance);
                    double combinedDistance = distTo.get(to) + input.estimatedDistanceToGoal(to, end);
                    if (pq.contains(to)) {
                        pq.changePriority(to, combinedDistance);
                    } else {
                        pq.add(to, combinedDistance);
                    }
                }
            }
        }
        timeSpent = sw.elapsedTime();
        if (v.equals(end) && timeSpent < timeout) {

            outcome = SolverOutcome.SOLVED;
            while (v != null) {
                solution.add(v);
                v = edgeTo.get(v);
            }
            Collections.reverse(solution);
            solutionWeight = distTo.get(end);
            return;
        } else if (timeSpent >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
            return;
        } else if (pq.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            return;
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    /** The total number of priority queue removeSmallest operations. */
    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }
}
