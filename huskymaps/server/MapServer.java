package huskymaps.server;

import huskymaps.ContractedStreetMapGraph;
import huskymaps.server.handler.APIRouteHandler;
import huskymaps.server.handler.impl.ClearRouteAPIHandler;
import huskymaps.server.handler.impl.RasterAPIHandler;
import huskymaps.server.handler.impl.RedirectAPIHandler;
import huskymaps.server.handler.impl.RoutingAPIHandler;
import huskymaps.server.handler.impl.SearchAPIHandler;

import java.util.Map;

import static huskymaps.Constants.HEROKU_DEPLOYMENT;
import static huskymaps.Constants.OSM_DB_PATH;
import static huskymaps.Constants.PORT;
import static huskymaps.Constants.SEMANTIC_STREET_GRAPH;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class MapServer {

    private static final Map<String, APIRouteHandler> HANDLERS = Map.of(
            "/raster", new RasterAPIHandler(),
            "/route", new RoutingAPIHandler(),
            "/clear_route", new ClearRouteAPIHandler(),
            "/search", new SearchAPIHandler(),
            "/", new RedirectAPIHandler()
            );

    /** Entry point for the MapServer. Everything starts here. */
    public static void main(String[] args) {
        port(getPort());

        SEMANTIC_STREET_GRAPH = new ContractedStreetMapGraph(OSM_DB_PATH);
        staticFileLocation("/static/page");
        /* Allow for all origin requests (since this is not an authenticated server, we do not
         * care about CSRF).  */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        for (Map.Entry<String, APIRouteHandler> apiRoute : HANDLERS.entrySet()) {
            get(apiRoute.getKey(), apiRoute.getValue());
        }
    }

    private static int getPort() {
        if (HEROKU_DEPLOYMENT) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (processBuilder.environment().get("PORT") != null) {
                return Integer.parseInt(processBuilder.environment().get("PORT"));
            }
        }
        return PORT;
    }
}
