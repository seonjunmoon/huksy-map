package huskymaps.tests;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import huskymaps.server.logic.Rasterer;
import huskymaps.params.RasterRequest;
import huskymaps.params.RasterResult;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static huskymaps.Constants.BASE_DIR_PATH;
import static org.junit.Assert.assertEquals;

/** Test of the rastering part of the assignment.*/
public class TestRasterer {
    private static final String REQUEST_FORMAT = BASE_DIR_PATH + "tests/rasterer/request%d.json",
                                RESULT_FORMAT = BASE_DIR_PATH + "tests/rasterer/result%d.json";
    private static final int NUM_TESTS = 10;

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Test
    public void testRasterizeMap() throws IOException {
        for (int i = 0; i < NUM_TESTS; i += 1) {
            System.out.println(String.format("Running test: %d", i));
            RasterRequest request;
            try (Reader reader = new FileReader(String.format(REQUEST_FORMAT, i))) {
                request = gson.fromJson(reader, RasterRequest.class);
            }
            RasterResult actual = Rasterer.rasterizeMap(request);
            RasterResult expected;
            try (Reader reader = new FileReader(String.format(RESULT_FORMAT, i))) {
                expected = gson.fromJson(reader, RasterResult.class);
            }
            assertEquals(
                    "Your results did not match the expected results for input " + request + ".\n",
                    expected,
                    actual
            );
        }
    }
}
