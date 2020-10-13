package huskymaps.server.handler.impl;

import huskymaps.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;

import static huskymaps.Constants.ROUTE_LIST;

/** Handles the "Clear Route" button in Bearmaps. */
public class ClearRouteAPIHandler extends APIRouteHandler {

    @Override
    protected Object parseRequest(Request request) {
        return null;
    }

    @Override
    protected Object processRequest(Object request, Response response) {
        ROUTE_LIST.clear();
        return true;
    }
}
