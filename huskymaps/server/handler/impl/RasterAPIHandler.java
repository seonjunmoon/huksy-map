package huskymaps.server.handler.impl;

import huskymaps.params.Location;
import huskymaps.params.RasterRequest;
import huskymaps.params.RasterResult;
import huskymaps.params.RenderedRasterResult;
import huskymaps.server.handler.APIRouteHandler;
import huskymaps.server.logic.Rasterer;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static huskymaps.Constants.HEROKU_DEPLOYMENT;
import static huskymaps.Constants.IMG_ROOT;
import static huskymaps.Constants.ROUTE_LIST;
import static huskymaps.Constants.ROUTE_STROKE_COLOR;
import static huskymaps.Constants.ROUTE_STROKE_WIDTH_PX;
import static huskymaps.Constants.SEMANTIC_STREET_GRAPH;
import static huskymaps.Constants.TILE_SIZE;

/**
 * Handles requests from the web browser for map images. These images will be
 * rastered into one large image to be displayed to the user.
 */
public class RasterAPIHandler extends APIRouteHandler<RasterRequest, RenderedRasterResult> {

    @Override
    protected RasterRequest parseRequest(Request request) {
        return RasterRequest.from(request);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query.
     * @param request RasterRequest
     * @param response Ignored
     * @return RenderedRasterResult
     */
    @Override
    protected RenderedRasterResult processRequest(RasterRequest request, Response response) {
        RasterResult raster = Rasterer.rasterizeMap(request);
        if (raster.grid != null) {
            // Render the result as an image if successful
            BufferedImage image = render(raster);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new RenderedRasterResult(raster, Base64.getEncoder().encodeToString(os.toByteArray()));
        } else {
            return new RenderedRasterResult(raster);
        }
    }

    private static BufferedImage render(RasterResult result) {
        int numVertTiles = result.grid.length;
        int numHorizTiles = result.grid[0].length;

        BufferedImage image = new BufferedImage(
                numHorizTiles * TILE_SIZE,
                numVertTiles * TILE_SIZE,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics graphic = image.getGraphics();
        int x = 0;
        int y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getTile(result.grid[r][c]), x, y, null);
                x += TILE_SIZE;
                if (x >= image.getWidth()) {
                    x = 0;
                    y += TILE_SIZE;
                }
            }
        }

        final double wdpp = (result.lrlon - result.ullon) / image.getWidth();
        final double hdpp = (result.ullat - result.lrlat) / image.getHeight();
        if (ROUTE_LIST != null && !ROUTE_LIST.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ROUTE_LIST.stream().reduce((v, w) -> {
                Location vLocation = SEMANTIC_STREET_GRAPH.location(v);
                Location wLocation = SEMANTIC_STREET_GRAPH.location(w);
                g2d.drawLine(
                        (int) ((vLocation.lon() - result.ullon) * (1 / wdpp)),
                        (int) ((result.ullat - vLocation.lat()) * (1 / hdpp)),
                        (int) ((wLocation.lon() - result.ullon) * (1 / wdpp)),
                        (int) ((result.ullat - wLocation.lat()) * (1 / hdpp))
                );
                return w;
            });
        }
        return image;
    }

    private static BufferedImage getTile(Rasterer.Tile name) {
        String path = IMG_ROOT + name;
        BufferedImage tile = null;
        try {
            File in = new File(path);
            if (!HEROKU_DEPLOYMENT) {
                tile = ImageIO.read(in);
            } else {
                tile = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource(path));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return tile;
    }
}
