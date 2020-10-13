package huskymaps;

import astar.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

public class WeightedShortcut<Vertex> extends WeightedEdge<Vertex> {
    final WeightedEdge<Vertex> srcEdge;
    final WeightedEdge<Vertex> destEdge;

    public WeightedShortcut(WeightedEdge<Vertex> srcEdge, WeightedEdge<Vertex> destEdge, double weight, String name) {
        super(srcEdge.from(), destEdge.to(), weight, name);
        this.srcEdge = srcEdge;
        this.destEdge = destEdge;
    }

    @Override
    public List<Vertex> predecessors() {
        List<Vertex> predecessors = new ArrayList<>(srcEdge.predecessors());
        predecessors.addAll(destEdge.predecessors());
        return predecessors;
    }

    @Override
    public WeightedShortcut<Vertex> flip() {
        return new WeightedShortcut<>(destEdge.flip(), srcEdge.flip(), weight(), name());
    }
}
