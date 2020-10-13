package huskymaps.params;

import org.apache.commons.math3.util.Precision;
import spark.Request;

import java.util.Map;
import java.util.Objects;

import static huskymaps.Constants.DECIMAL_PLACES;
import static huskymaps.Constants.EPSILON;
import static huskymaps.Constants.HALT_RESPONSE;
import static spark.Spark.halt;

/** Represents a rastering request received from the browser. */
public final class RasterRequest {

    /** The browser's bounding upper-left, lower-right longitudes and latitudes. */
    public final double ullat;
    public final double ullon;
    public final double lrlat;
    public final double lrlon;
    /** The browser's requested depth. */
    public final int depth;

    /**
     * Return a RasterRequest with the required parameters.
     * @param request Map containing the required parameters
     * @return A populated RasterRequest of input parameter to numerical value
     */
    public static RasterRequest from(Map<String, Object> request) {
        try {
            return new RasterRequest(
                    (double) request.get("ullat"),
                    (double) request.get("ullon"),
                    (double) request.get("lrlat"),
                    (double) request.get("lrlon"),
                    (int) request.get("depth")
                    );
        } catch (NullPointerException e) {
            halt(HALT_RESPONSE, "Request failed: parameter not found.");
        }
        return null;
    }

    /**
     * Returns a RasterRequest with the required parameters.
     * @param request Spark Request
     * @return A populated RasterRequest of input parameter to numerical value
     */
    public static RasterRequest from(Request request) {
        try {
            return new RasterRequest(
                        Double.parseDouble(request.queryParams("ullat")),
                        Double.parseDouble(request.queryParams("ullon")),
                        Double.parseDouble(request.queryParams("lrlat")),
                        Double.parseDouble(request.queryParams("lrlon")),
                        Integer.parseInt(request.queryParams("depth"))
                        );
        } catch (NullPointerException e) {
            halt(HALT_RESPONSE, "Request failed: parameter not found.");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            halt(HALT_RESPONSE, "Request failed: unable to parse value.");
        }
        return null;
    }

    /** Use the from factory method to avoid misplacing parameters. */
    private RasterRequest(double ullat, double ullon, double lrlat, double lrlon, int depth) {
        this.ullat = ullat;
        this.ullon = ullon;
        this.lrlat = lrlat;
        this.lrlon = lrlon;
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RasterRequest that = (RasterRequest) o;
        return Precision.equals(that.ullat, ullat, EPSILON) &&
                Precision.equals(that.ullon, ullon, EPSILON) &&
                Precision.equals(that.lrlat, lrlat, EPSILON) &&
                Precision.equals(that.lrlon, lrlon, EPSILON) &&
                depth == that.depth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Precision.round(ullat, DECIMAL_PLACES),
                Precision.round(ullon, DECIMAL_PLACES),
                Precision.round(lrlat, DECIMAL_PLACES),
                Precision.round(lrlon, DECIMAL_PLACES),
                depth
        );
    }

    @Override
    public String toString() {
        return "RasterRequest{" +
                "ullat=" + ullat +
                ", ullon=" + ullon +
                ", lrlat=" + lrlat +
                ", lrlon=" + lrlon +
                ", depth=" + depth +
                '}';
    }
}
