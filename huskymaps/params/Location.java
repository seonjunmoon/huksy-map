package huskymaps.params;

import org.apache.commons.math3.util.Precision;
import pointset.Point;

import java.util.Objects;

import static huskymaps.Constants.DECIMAL_PLACES;
import static huskymaps.Constants.EPSILON;
import static huskymaps.Constants.K0;
import static huskymaps.Constants.R;
import static huskymaps.Constants.ROOT_LAT;
import static huskymaps.Constants.ROOT_LON;

public class Location {
    protected final double lat;
    protected final double lon;
    protected final String name;

    public Location(double lat, double lon) {
        this(lat, lon, null);
    }

    public Location(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public double lat() {
        return lat;
    }

    public double lon() {
        return lon;
    }

    public String name() {
        return name;
    }

    /**
     * Returns the great-circle (haversine) distance between geographic coordinates.
     * @param other The other location.
     * @return The great-circle distance between the two vertices.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public double greatCircleDistance(Location other) {
        double phi1 = Math.toRadians(this.lat);
        double phi2 = Math.toRadians(other.lat);
        double dphi = Math.toRadians(other.lat - this.lat);
        double dlambda = Math.toRadians(other.lon - this.lon);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * @param other The other location.
     * @return The initial bearing between the vertices.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public double bearing(Location other) {
        double phi1 = Math.toRadians(this.lat);
        double phi2 = Math.toRadians(other.lat);
        double lambda1 = Math.toRadians(this.lon);
        double lambda2 = Math.toRadians(other.lon);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Return the Euclidean point for this location.
     * @return The Euclidean point centered on the root.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    public Point toPoint() {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);

        double b = Math.sin(dlon) * Math.cos(phi);
        double x = (K0 / 2) * Math.log((1 + b) / (1 - b));
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        double y = K0 * (con - Math.toRadians(ROOT_LAT));
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Precision.equals(location.lat, lat, EPSILON) &&
                Precision.equals(location.lon, lon, EPSILON) &&
                Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Precision.round(lat, DECIMAL_PLACES),
                Precision.round(lon, DECIMAL_PLACES),
                name
        );
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                '}';
    }
}
