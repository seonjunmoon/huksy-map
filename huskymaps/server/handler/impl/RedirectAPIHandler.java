package huskymaps.server.handler.impl;

import huskymaps.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;

public class RedirectAPIHandler extends APIRouteHandler {
    @Override
    protected Object parseRequest(Request request) {
        return null;
    }

    @Override
    protected Object processRequest(Object request, Response response) {
        response.redirect("/map.html", 301);
        return true;
    }

    @Override
    protected Object buildJsonResponse(Object result) {
        return true;
    }
}
