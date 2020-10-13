package huskymaps.server.handler;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the base class that defines the procedure for handling an API request
 * The process is defined as such that first the request parameters are read, then
 * request is process based on those parameters and finally the response is built.
 */
public abstract class APIRouteHandler<Req, Res> implements Route {

    private Gson gson;

    public APIRouteHandler() {
        gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Req req = parseRequest(request);
        Res res = processRequest(req, response);
        return buildJsonResponse(res);
    }

    /**
     * Defines how to parse and extract the request parameters from request
     * @param request the request object received
     * @return extracted request parameters
     */
    protected abstract Req parseRequest(Request request);

    /**
     * Process the request using the given parameters
     * @param request parameters
     * @param response response object
     * @return the result computed after processing request
     */
    protected abstract Res processRequest(Req request, Response response);

    /**
     * Builds a JSON response to return from the result object
     * @param result the result of processing the request
     * @return JSON response
     */
    protected Object buildJsonResponse(Res result) {
        return gson.toJson(result);
    }
}
