package huskymaps.tests;

import huskymaps.params.RouteRequest;
import huskymaps.server.logic.Router;
import huskymaps.ContractedStreetMapGraph;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static huskymaps.Constants.BASE_DIR_PATH;
import static org.junit.Assert.assertEquals;

public class TestRouterTiny {
    private static final String OSM_DB_PATH_TINY = BASE_DIR_PATH + "tiny.osm.gz";
    private static ContractedStreetMapGraph tinyGraph;
    private static boolean initialized = false;

    @Before
    public void setUp() throws Exception {
        if (initialized) {
            return;
        }
        tinyGraph = new ContractedStreetMapGraph(OSM_DB_PATH_TINY);
        initialized = true;
    }

    @Test
    public void test22to66() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.55,
                "start_lon", -122.45,
                "end_lat", 47.75,
                "end_lon", -122.2
        ));
        List<Long> actual = Router.shortestPath(tinyGraph, request);
        List<Long> expected = List.of(22L, 46L, 66L);
        assertEquals("Best path from 22 to 66 is incorrect.", expected, actual);
    }

    @Test
    public void test22to11() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.55,
                "start_lon", -122.45,
                "end_lat", 47.5,
                "end_lon", -122.5
        ));
        List<Long> actual = Router.shortestPath(tinyGraph, request);
        List<Long> expected = List.of(22L, 11L);
        assertEquals(expected, actual);
    }

    @Test
    public void test41to46() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.5,
                "start_lon", -122.3,
                "end_lat", 47.75,
                "end_lon", -122.3
        ));
        List<Long> actual = Router.shortestPath(tinyGraph, request);
        List<Long> expected = List.of(41L, 63L, 66L, 46L);
        assertEquals(expected, actual);
    }

    @Test
    public void test66to55() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.75,
                "start_lon", -122.2,
                "end_lat", 47.7,
                "end_lon", -122.25
        ));
        List<Long> actual = Router.shortestPath(tinyGraph, request);
        List<Long> expected = List.of(66L, 63L, 55L);
        assertEquals(expected, actual);
    }
}
