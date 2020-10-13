package huskymaps.tests;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import huskymaps.ContractedStreetMapGraph;
import huskymaps.params.RouteRequest;
import huskymaps.server.logic.Router;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import static huskymaps.Constants.BASE_DIR_PATH;
import static org.junit.Assert.assertEquals;

/** Test of the routing part of the assignment. */
public class TestRouter {
    private static final String REQUEST_FORMAT = BASE_DIR_PATH + "tests/router/request%d.json";
    private static final String RESULT_FORMAT = BASE_DIR_PATH + "tests/router/result%d.json";
    private static final String LARGE_OSM_DB_PATH = BASE_DIR_PATH + "seattle.osm.gz";
    private static final int NUM_TESTS = 10;

    private boolean initialized = false;
    private ContractedStreetMapGraph graph;
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Before
    public void setUp() {
        if (initialized) {
            return;
        }
        graph = new ContractedStreetMapGraph(LARGE_OSM_DB_PATH);
        initialized = true;
    }

    @Test
    public void testShortestPath() throws IOException {
        for (int i = 0; i < NUM_TESTS; i += 1) {
            System.out.println(String.format("Running test: %d", i));
            RouteRequest request;
            try (Reader reader = new FileReader(String.format(REQUEST_FORMAT, i))) {
                request = gson.fromJson(reader, RouteRequest.class);
            }
            List<Long> actual = Router.shortestPath(graph, request);
            List<Long> expected;
            try (Reader reader = new FileReader(String.format(RESULT_FORMAT, i))) {
                expected = Arrays.asList(gson.fromJson(reader, Long[].class));
            }
            assertEquals("Your results did not match the expected results", expected, actual);
        }
    }
}
