package huskymaps.server.handler.impl;

import huskymaps.server.handler.APIRouteHandler;
import huskymaps.server.logic.Router;
import huskymaps.server.logic.Navigation;
import huskymaps.params.RouteRequest;
import huskymaps.params.RouteResult;

import spark.Request;
import spark.Response;

import java.util.List;

import static huskymaps.Constants.SEMANTIC_STREET_GRAPH;
import static huskymaps.Constants.ROUTE_LIST;

/**
 * Handles requests from the web browser for routes between locations. The
 * route will be returned as image data, as well as (optionally) driving directions.
 */
public class RoutingAPIHandler extends APIRouteHandler<RouteRequest, RouteResult> {

    @Override
    protected RouteRequest parseRequest(Request request) {
        return RouteRequest.from(request);
    }

    /**
     * Takes a user query in the form of a pair of (lat/lon) values, and finds
     * street directions between the given points.
     * @param request RouteRequest
     * @param response Ignored.
     * @return RouteResult
     */
    @Override
    protected RouteResult processRequest(RouteRequest request, Response response) {
        ROUTE_LIST = Router.shortestPath(SEMANTIC_STREET_GRAPH, request);
        return new RouteResult(!ROUTE_LIST.isEmpty(), getDirectionsText(
                    Navigation.routeDirections(SEMANTIC_STREET_GRAPH, ROUTE_LIST)
        ));
    }

    /** Takes the current route and converts it into an HTML-friendly String. */
    private String getDirectionsText(List<Navigation.Step> directions) {
        if (directions == null || directions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int step = 1;
        for (Navigation.Step d : directions) {
            sb.append(String.format("%d. %s <br>", step, d));
            step += 1;
        }
        return sb.toString();
    }
}
