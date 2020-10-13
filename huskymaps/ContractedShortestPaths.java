package huskymaps;

import astar.WeightedEdge;
import edu.princeton.cs.algs4.Stopwatch;
import pq.ExtrinsicMinPQ;
import pq.TreeMapMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContractedShortestPaths {
    private Map<Long, WeightedEdge<Long>> edgeTo = new HashMap<>();
    private Map<Long, Double> distTo = new HashMap<>();
    private long start;
    private int numStatesExplored = 0;
    private double explorationTime;

    public ContractedShortestPaths(ContractedStreetMapGraph g, long start, double timeout) {
        this.start = start;
        ExtrinsicMinPQ<Long> pq = new TreeMapMinPQ<>();
        pq.add(start, 0);
        edgeTo.put(start, null);
        distTo.put(start, 0.0);

        Stopwatch timer = new Stopwatch();
        while (pq.size() != 0 && timer.elapsedTime() < timeout) {
            long v = pq.removeSmallest();
            numStatesExplored += 1;
            for (WeightedEdge<Long> e : g.neighbors(v)) {
                long w = e.to();
                if (g.node(v).contractionOrder() < g.node(w).contractionOrder()) {
                    double currDistance = distTo(w);
                    double thisDistance = distTo(v) + e.weight();
                    if (thisDistance < currDistance) {
                        edgeTo.put(w, e);
                        distTo.put(w, thisDistance);
                        if (pq.contains(w)) {
                            pq.changePriority(w, thisDistance);
                        } else {
                            pq.add(w, thisDistance);
                        }
                    }
                }
            }
        }
        explorationTime = timer.elapsedTime();
    }

    /** Returns a path from w back to the start. */
    public List<Long> constructPath(long w) {
        List<Long> path = new ArrayList<>();
        path.add(w);
        while (w != start) {
            WeightedEdge<Long> e = edgeTo.get(w);
            List<Long> predecessors = e.predecessors();
            Collections.reverse(predecessors);
            path.addAll(predecessors);
            w = e.from();
        }
        Collections.reverse(path);
        return path;
    }

    public Set<Long> vertices() {
        return new HashSet<>(distTo.keySet());
    }

    public double distTo(long w) {
        return distTo.getOrDefault(w, Double.POSITIVE_INFINITY);
    }

    public WeightedEdge<Long> edgeTo(long w) {
        return edgeTo.get(w);
    }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return explorationTime;
    }
}
