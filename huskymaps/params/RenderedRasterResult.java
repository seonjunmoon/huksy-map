package huskymaps.params;

import org.apache.commons.math3.util.Precision;

import java.util.Objects;

import static huskymaps.Constants.DECIMAL_PLACES;
import static huskymaps.Constants.EPSILON;

/** The computed and fully-rendered rastering result in response to a browser request. */
public class RenderedRasterResult {

    /** The base-64 encoded string of the image. */
    public final String image;
    /** The bounding upper-left, lower-right latitudes and longitudes of the final image. */
    public final double ullat;
    public final double ullon;
    public final double lrlat;
    public final double lrlon;
    /** True if the query was successful. */
    public final boolean success;

    /** Construct a new RenderedRasterResult with the given parameters. */
    public RenderedRasterResult(RasterResult result, String image) {
        this.image = image;
        this.ullat = result.ullat;
        this.ullon = result.ullon;
        this.lrlat = result.lrlat;
        this.lrlon = result.lrlon;
        this.success = result.grid != null;
    }

    /** Construct a new RenderedRasterResult with the given parameters. */
    public RenderedRasterResult(RasterResult result) {
        this(result, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RenderedRasterResult that = (RenderedRasterResult) o;
        return Precision.equals(that.ullat, ullat, EPSILON) &&
                Precision.equals(that.ullon, ullon, EPSILON) &&
                Precision.equals(that.lrlat, lrlat, EPSILON) &&
                Precision.equals(that.lrlon, lrlon, EPSILON) &&
                success == that.success &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Precision.round(ullat, DECIMAL_PLACES),
                Precision.round(ullon, DECIMAL_PLACES),
                Precision.round(lrlat, DECIMAL_PLACES),
                Precision.round(lrlon, DECIMAL_PLACES),
                success
        );
    }

    @Override
    public String toString() {
        return "RenderedRasterResult{"
                + "image"
                + ", ullat=" + ullat
                + ", ullon=" + ullon
                + ", lrlat=" + lrlat
                + ", lrlon=" + lrlon
                + ", success=" + success
                + '}';
    }
}
