package huskymaps.params;

import org.apache.commons.math3.util.Precision;
import spark.Request;

import java.util.Map;
import java.util.Objects;

import static huskymaps.Constants.DECIMAL_PLACES;
import static huskymaps.Constants.EPSILON;
import static huskymaps.Constants.HALT_RESPONSE;
import static spark.Spark.halt;

/** Represents a shortest-route request received from the browser. */
public final class RouteRequest {

    /** The start and ending latitude and longitude. */
    public final double startLat;
    public final double startLon;
    public final double endLat;
    public final double endLon;

    /**
     * Return a RouteRequest with the required parameters.
     * @param request Map containing the required parameters
     * @return A populated RouteRequest of input parameter to numerical value
     */
    public static RouteRequest from(Map<String, Double> request) {
        try {
            return new RouteRequest(
                    request.get("start_lat"),
                    request.get("start_lon"),
                    request.get("end_lat"),
                    request.get("end_lon")
                    );
        } catch (NullPointerException e) {
            halt(HALT_RESPONSE, "Request failed: parameter not found.");
        }
        return null;
    }

    /**
     * Returns a RouteRequest with the required parameters.
     * @param request Spark Request
     * @return A populated RouteRequest of input parameter to numerical value
     */
    public static RouteRequest from(Request request) {
        try {
            return new RouteRequest(
                        Double.parseDouble(request.queryParams("start_lat")),
                        Double.parseDouble(request.queryParams("start_lon")),
                        Double.parseDouble(request.queryParams("end_lat")),
                        Double.parseDouble(request.queryParams("end_lon"))
                        );
        } catch (NullPointerException e) {
            halt(HALT_RESPONSE, "Request failed: parameter not found.");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            halt(HALT_RESPONSE, "Request failed: unable to parse double.");
        }
        return null;
    }

    private RouteRequest(double startLat, double startLon, double endLat, double endLon) {
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteRequest that = (RouteRequest) o;
        return Precision.equals(that.startLat, startLat, EPSILON) &&
                Precision.equals(that.startLon, startLon, EPSILON) &&
                Precision.equals(that.endLat, endLat, EPSILON) &&
                Precision.equals(that.endLon, endLon, EPSILON);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Precision.round(startLat, DECIMAL_PLACES),
                Precision.round(startLon, DECIMAL_PLACES),
                Precision.round(endLat, DECIMAL_PLACES),
                Precision.round(endLon, DECIMAL_PLACES)
        );
    }

    @Override
    public String toString() {
        return "RouteRequest{" +
                "startLat=" + startLat +
                ", startLon=" + startLon +
                ", endLat=" + endLat +
                ", endLon=" + endLon +
                '}';
    }
}
