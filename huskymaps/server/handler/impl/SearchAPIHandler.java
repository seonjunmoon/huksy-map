package huskymaps.server.handler.impl;

import huskymaps.server.handler.APIRouteHandler;
import huskymaps.params.SearchRequest;
import spark.Request;
import spark.Response;

import java.util.List;

import static huskymaps.Constants.SEMANTIC_STREET_GRAPH;

public class SearchAPIHandler extends APIRouteHandler<SearchRequest, List> {

    @Override
    protected SearchRequest parseRequest(Request request) {
        return new SearchRequest(request.queryParams("term"), request.queryParams("full") != null);
    }

    @Override
    protected List processRequest(SearchRequest request, Response response) {
        if (request.full) {
            return SEMANTIC_STREET_GRAPH.getLocations(request.term);
        } else {
            return SEMANTIC_STREET_GRAPH.getLocationsByPrefix(request.term);
        }
    }
}
