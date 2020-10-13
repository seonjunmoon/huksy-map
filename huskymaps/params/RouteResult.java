package huskymaps.params;

import java.util.Objects;

/** The computed routing result in response to a browser request. */
public class RouteResult {

    /** Whether or not the route was successfully computed. */
    public final boolean success;
    /** The HTML-friendly String representation of the navigation directions. */
    public final String directions;

    /**
     * Constructs a RouteResult instance and sets the success and distance fields.
     * @param success The success field.
     * @param directions The directions field.
     */
    public RouteResult(boolean success, String directions) {
        this.success = success;
        this.directions = directions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteResult that = (RouteResult) o;
        return success == that.success &&
                Objects.equals(directions, that.directions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, directions);
    }

    @Override
    public String toString() {
        return "RouteResult{" +
                "success=" + success +
                ", directions='" + directions + '\'' +
                '}';
    }
}
