package huskymaps;

import astar.WeightedEdge;
import edu.princeton.cs.algs4.Stopwatch;
import pq.ExtrinsicMinPQ;
import pq.TreeMapMinPQ;

import java.util.*;
import java.util.stream.Collectors;

public class ContractedStreetMapGraph extends StreetMapGraph {

    public ContractedStreetMapGraph(String filename) {
        super(filename);
        Set<ContractableNode> uncontractedNodes = vertices().parallelStream()
                .map(this::node)
                .filter(this::isNavigable)
                .collect(Collectors.toUnmodifiableSet());
        int order = 0;
        Stopwatch timer = new Stopwatch();
        while (!uncontractedNodes.isEmpty()) {
            System.out.println("Uncontracted: " + uncontractedNodes.size());

            Map<ContractableNode, Priority> priorities = uncontractedNodes
                    .parallelStream().collect(Collectors.toMap(node -> node, node -> new Priority(node)));

            Set<ContractableNode> indepNodeSet = uncontractedNodes
                    .parallelStream().filter(node -> isIndependent(node, priorities)).collect(Collectors.toSet());

            Set<Shortcuts> shortcutEdges = indepNodeSet
                    .parallelStream().map(node -> new Shortcuts(node)).collect(Collectors.toSet());

            for (Shortcuts shortcut : shortcutEdges) {
                for (WeightedEdge weightedEdge: shortcut) {
                    addWeightedEdge(weightedEdge);
                    addWeightedEdge(weightedEdge.flip());
                }
            }

            order += 1;

            Set<ContractableNode> updatedNodes = new HashSet<>();
            for (ContractableNode node: uncontractedNodes) {
                updatedNodes.add(node);
            }
            for (ContractableNode node: indepNodeSet) {
                node.setContractionOrder(order);
                node.updateDepths(neighboringNodes(node));
                updatedNodes.remove(node);
                uncontractedNodes = updatedNodes;
            }
        }
        System.out.println("Contraction hierarchies generated in " + timer.elapsedTime() + " seconds");
    }

    /** Return true if and only if the node is independent within its 2-nearest neighborhood. */
    private boolean isIndependent(ContractableNode node, Map<ContractableNode, Priority> priorities) {
        for (ContractableNode immediateNeighbor : neighboringNodes(node)) {
            if (!immediateNeighbor.isContracted()) {
                int cmp = Double.compare(priorities.get(node).value, priorities.get(immediateNeighbor).value);
                // Tie-breaking: lower node id is independent
                if ((cmp > 0) || (cmp == 0 && node.id() > immediateNeighbor.id())) {
                    return false;
                }
            }
            for (ContractableNode nextNeighbor : neighboringNodes(immediateNeighbor)) {
                if (!nextNeighbor.isContracted() && !node.equals(nextNeighbor)) {
                    int cmp = Double.compare(priorities.get(node).value, priorities.get(nextNeighbor).value);
                    // Tie-breaking: lower node id is independent
                    if ((cmp > 0) || (cmp == 0 && node.id() > nextNeighbor.id())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Computes the priority value for a given node. Shortcuts are cached if computed. */
    private class Priority {
        final double value;
        Shortcuts shortcuts;

        Priority(ContractableNode node) {
            List<WeightedEdge<Long>> neighbors = neighbors(node.id());
            int numTrueNeighbors = neighbors.size() - numContracted(neighbors);
            if (numTrueNeighbors > 0) {
                shortcuts = new Shortcuts(neighbors);
                double edgeQuotient = shortcuts.size() / (double) numTrueNeighbors;
                value = 3 * edgeQuotient + node.getDepth();
            } else {
                shortcuts = null;
                value = node.getDepth();
            }
        }

        /** Return the number of contracted neighbors. */
        int numContracted(List<WeightedEdge<Long>> neighbors) {
            int contractedNeighbors = 0;
            for (WeightedEdge<Long> edge : neighbors) {
                ContractableNode neighbor = node(edge.to());
                if (neighbor.isContracted()) {
                    contractedNeighbors += 1;
                }
            }
            return contractedNeighbors;
        }
    }

    /** Computes the shortcut edges that could be added to the graph. */
    private class Shortcuts implements Iterable<WeightedShortcut<Long>> {
        final List<WeightedShortcut<Long>> result = new ArrayList<>();

        Shortcuts(ContractableNode node) {
            this(neighbors(node.id()));
        }

        Shortcuts(List<WeightedEdge<Long>> neighbors) {
            int i = 1;
            for (WeightedEdge<Long> srcEdge : neighbors) {
                ContractableNode src = node(srcEdge.to());
                if (!src.isContracted()) {
                    for (WeightedEdge<Long> destEdge : neighbors.subList(i, neighbors.size())) {
                        ContractableNode dest = node(destEdge.to());
                        WeightedShortcut<Long> shortcut = new WeightedShortcut<>(
                                srcEdge.flip(), destEdge, srcEdge.weight() + destEdge.weight(), "Shortcut"
                        );
                        if (!dest.isContracted() && shortcutRequired(src.id(), dest.id(), shortcut)) {
                            result.add(shortcut);
                        }
                    }
                }
                i += 1;
            }
        }

        @Override
        public Iterator<WeightedShortcut<Long>> iterator() {
            return result.iterator();
        }

        /** Return the number of shortcuts. */
        int size() {
            return result.size();
        }

        /** Returns true if and only if the given shortcutDistance <= shortest path distance. */
        boolean shortcutRequired(long start, long end, WeightedShortcut<Long> shortcut) {
            Map<Long, Double> distTo = new HashMap<>();
            ExtrinsicMinPQ<Long> pq = new TreeMapMinPQ<>();
            pq.add(start, estimatedDistanceToGoal(start, end));
            distTo.put(start, 0.0);
            while (!pq.isEmpty() && pq.getSmallest() != end && shortcut.weight() >= distTo.get(pq.getSmallest())) {
                long v = pq.removeSmallest();
                for (WeightedEdge<Long> edge : neighbors(v)) {
                    long w = edge.to();
                    if (!node(w).isContracted() && !edge.equals(shortcut.srcEdge) && !edge.equals(shortcut.destEdge)) {
                        double bestDistance = distTo.getOrDefault(w, Double.POSITIVE_INFINITY);
                        double thisDistance = distTo.get(v) + edge.weight();
                        if (thisDistance < bestDistance) {
                            distTo.put(w, thisDistance);
                            double priority = estimatedDistanceToGoal(w, end) + thisDistance;
                            if (pq.contains(w)) {
                                pq.changePriority(w, priority);
                            } else {
                                pq.add(w, priority);
                            }
                        }
                    }
                }
            }
            return shortcut.weight() < distTo.getOrDefault(end, Double.POSITIVE_INFINITY);
        }
    }

    /** Return a list of neighboring nodes. */
    private List<ContractableNode> neighboringNodes(ContractableNode node) {
        List<WeightedEdge<Long>> neighbors = neighbors(node.id());
        List<ContractableNode> result = new ArrayList<>(neighbors.size());
        for (WeightedEdge<Long> e : neighbors) {
            result.add(node(e.to()));
        }
        return result;
    }

    /** Return the node with the given id. */
    ContractableNode node(long id) {
        return ((ContractableNode) location(id));
    }

    @Override
    Node.Builder nodeBuilder() {
        return new ContractableNode.Builder();
    }
}
