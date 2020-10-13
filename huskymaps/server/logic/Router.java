package huskymaps.server.logic;

import astar.AStarSolver;
import huskymaps.ContractedShortestPaths;
import huskymaps.ContractedStreetMapGraph;
import huskymaps.StreetMapGraph;
import huskymaps.params.Location;
import huskymaps.params.RouteRequest;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/** Application logic for the RoutingAPIHandler. */
public class Router {

    /**
     * Overloaded method for shortestPath that has flexibility to specify a solver
     * and returns a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination location.
     * @param g The graph to use.
     * @param request The requested route.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(StreetMapGraph g, RouteRequest request) {
        long src = g.closest(new Location(request.startLat, request.startLon));
        long dest = g.closest(new Location(request.endLat, request.endLon));
        return new AStarSolver<>(g, src, dest, 20).solution();
    }

    /**
     * Overloaded method for shortestPath that has flexibility to specify a solver
     * and returns a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination location.
     * @param g The graph to use.
     * @param request The requested route.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(ContractedStreetMapGraph g, RouteRequest request) {
        long src = g.closest(new Location(request.startLat, request.startLon));
        long dest = g.closest(new Location(request.endLat, request.endLon));
        ContractedShortestPaths srcPaths = new ContractedShortestPaths(g, src, 20);
        ContractedShortestPaths destPaths = new ContractedShortestPaths(g, dest, 20);
        Set<Long> sharedVertices = srcPaths.vertices();
        sharedVertices.retainAll(destPaths.vertices());
        if (sharedVertices.isEmpty()) {
            return List.of();
        }
        long bestVertex = Long.MIN_VALUE;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (long id : sharedVertices) {
            double thisDistance = srcPaths.distTo(id) + destPaths.distTo(id);
            if (thisDistance < bestDistance) {
                bestVertex = id;
                bestDistance = thisDistance;
            }
        }
        List<Long> forward = srcPaths.constructPath(bestVertex);
        List<Long> backward = destPaths.constructPath(bestVertex);
        // Remove duplicate bestVertex from backward path
        backward.remove(backward.size() - 1);
        Collections.reverse(backward);
        forward.addAll(backward);
        return forward;
    }
}
