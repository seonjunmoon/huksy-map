package para;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FilterEmpty {
    static ForkJoinPool POOL = new ForkJoinPool();

    public static int[] filterEmpty(String[] arr) {
        int[] bitset = mapToBitSet(arr);
        int[] bitsum = ParallelPrefixSum.parallelPrefixSum(bitset);
        int[] result = mapToOutput(arr, bitsum);
        return result;
    }

    public static int[] mapToBitSet(String[] arr) {
        int[] bitset = new int[arr.length];
        POOL.invoke(new MapToBitSetAction(arr, bitset, 0, arr.length));
        return bitset;
    }

    public static int[] mapToOutput(String[] input, int[] bitsum) {
        if (input.length == 0) {
            return new int[0];
        }
        int[] result = new int[bitsum[bitsum.length - 1]];
        POOL.invoke(new MapToOutputAction(input, bitsum, result, 0, input.length));
        return result;
    }

    public static void sequential(String[] arr, int[] bitset, int lo, int hi) {
        for (int i = lo; i < hi; i++) {
            if (arr[i] == "") {
                bitset[i] = 0;
            } else {
                bitset[i] = 1;
            }
        }
    }

    public static void sequential2(String[] input, int[] bitsum, int[] result, int lo, int hi) {
        for (int i = hi - 1; i >= lo; i--) {
            if (i == 0) {
                if (bitsum[0] == 1) {
                    result[0] = input[0].length();
                }
            } else if (bitsum[i] != bitsum[i - 1]) {
                result[bitsum[i] - 1] = input[i].length();
            }
        }
    }

    private static class MapToBitSetAction extends RecursiveAction {
        String[] arr;
        int[] bitset;
        int lo;
        int hi;

        public MapToBitSetAction(String[] arr, int[] bitset, int lo, int hi) {
            this.arr = arr;
            this.bitset = bitset;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo <= 1) {
                sequential(arr, bitset, lo, hi);
                return;
            }

            int mid = lo + (hi - lo) / 2;

            MapToBitSetAction left = new MapToBitSetAction(arr, bitset, lo, mid);
            MapToBitSetAction right = new MapToBitSetAction(arr, bitset, mid, hi);

            left.fork();

            right.compute();
            left.join();
        }
    }

    private static class MapToOutputAction extends RecursiveAction {
        String[] input;
        int[] bitsum;
        int[] result;
        int lo;
        int hi;

        public MapToOutputAction(String[] input, int[] bitsum, int[] result, int lo, int hi) {
            this.input = input;
            this.bitsum = bitsum;
            this.result = result;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo <= 1) {
                sequential2(input, bitsum, result, lo, hi);
                return;
            }

            int mid = lo + (hi - lo) / 2;

            MapToOutputAction left = new MapToOutputAction(input, bitsum, result, lo, mid);
            MapToOutputAction right = new MapToOutputAction(input, bitsum, result, mid, hi);

            left.fork();

            right.compute();
            left.join();
        }
    }

    private static void usage() {
        System.err.println("USAGE: FilterEmpty <String array>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }
        String[] arr = args[0].replaceAll("\\s*", "").split(",");
        System.out.println(Arrays.toString(filterEmpty(arr)));
    }
}
