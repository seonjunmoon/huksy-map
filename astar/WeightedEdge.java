package astar;

import java.util.List;

/** Utility class that represents a weighted edge. */
public class WeightedEdge<Vertex> {
    private Vertex v;
    private Vertex w;
    private double weight;
    private String name;

    public WeightedEdge(Vertex v, Vertex w, double weight) {
        this(v, w, weight, null);
    }

    public WeightedEdge(Vertex v, Vertex w, double weight, String name) {
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.name = name;
    }

    public Vertex from() {
        return v;
    }

    public Vertex to() {
        return w;
    }

    public double weight() {
        return weight;
    }

    public String name() {
        return name;
    }

    public List<Vertex> predecessors() {
        return List.of(v);
    }

    public WeightedEdge<Vertex> flip() {
        return new WeightedEdge<>(w, v, weight, name);
    }
}
