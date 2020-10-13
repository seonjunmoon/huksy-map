package huskymaps.params;

import huskymaps.server.logic.Rasterer;

import java.util.Arrays;

/** The computed rastering result in response to a browser request. */
public class RasterResult {

    /** The 2-dimensional string grid of map tilenames. */
    public final Rasterer.Tile[][] grid;
    /** The bounding upper-left, lower-right latitudes and longitudes of the final image. */
    public final double ullat;
    public final double ullon;
    public final double lrlat;
    public final double lrlon;

    /** Return a new RasterResult with the given grid. */
    public RasterResult(Rasterer.Tile[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            this.grid = null;
            this.ullat = Double.NEGATIVE_INFINITY;
            this.ullon = Double.NEGATIVE_INFINITY;
            this.lrlat = Double.NEGATIVE_INFINITY;
            this.lrlon = Double.NEGATIVE_INFINITY;
        } else {
            this.grid = grid;
            Rasterer.Tile gridUl = grid[0][0];
            Rasterer.Tile gridLr = grid[grid.length - 1][grid[0].length - 1].offset();
            this.ullat = gridUl.lat();
            this.ullon = gridUl.lon();
            this.lrlat = gridLr.lat();
            this.lrlon = gridLr.lon();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RasterResult that = (RasterResult) o;
        return Arrays.deepEquals(grid, that.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    @Override
    public String toString() {
        return "RasterResult{" +
                "grid=" + Arrays.deepToString(grid) +
                '}';
    }
}
