package para;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * This code is inefficient in practice because we should
 * just be using array indices rather than explicitly
 * creating the tree.  This code does it the slow way
 * for clarity rather than efficiency.
 *
 * @author blank
 */
public class ParallelPrefixSum {
    public static final ForkJoinPool POOL = new ForkJoinPool();
    public static final Random RAND = new Random();
    private static final int CUTOFF = 1000;
    private static final int LENGTH = 100000000;

    public static int[] makeInput(int length) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = RAND.nextInt(Integer.MAX_VALUE / 10000);
        }
        return arr;
    }

    private static class PSTNode {
        public int lo, hi;
        public int sum;
        private PSTNode left, right;

        public PSTNode(int sum, int lo, int hi) {
            this.sum = sum;
            this.lo = lo;
            this.hi = hi;
        }

        public PSTNode(int sum, int lo, int hi, PSTNode left, PSTNode right) {
            this(sum, lo, hi);
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public String toString() {
            return "[" + lo + ", " + hi + ") = " + sum;
        }
    }

    private static class ProcessInputTask extends RecursiveTask<PSTNode> {
        private int[] input;
        private int lo, hi;

        public ProcessInputTask(int[] input, int lo, int hi) {
            this.input = input;
            this.lo = lo;
            this.hi = hi;
        }

        protected PSTNode compute() {
            if (hi - lo <= CUTOFF) {
                return new PSTNode(sum(input, lo, hi), lo, hi);
            }

            int mid = lo + (hi - lo)/2;
            ProcessInputTask leftTask = new ProcessInputTask(input, lo, mid);
            ProcessInputTask rightTask = new ProcessInputTask(input, mid, hi);

            leftTask.fork();

            PSTNode right = rightTask.compute();
            PSTNode left = leftTask.join();

            return new PSTNode(left.sum + right.sum, lo, hi, left, right);
        }
    }

    public static class CreateOutputTask extends RecursiveAction {
        private PSTNode current;
        private int[] input, output;
        private int prescan;

        public CreateOutputTask(int[] input, int[] output, PSTNode current, int prescan) {
            this.input = input;
            this.output = output;
            this.current = current;
            this.prescan = prescan;
        }

        protected void compute() {
            if (current.isLeaf()) {
                int sum = prescan;
                for (int i = current.lo; i < current.hi; i++) {
                    sum += input[i];
                    output[i] = sum;
                }
            } else {
                CreateOutputTask left = new CreateOutputTask(input, output, current.left, prescan);
                CreateOutputTask right = new CreateOutputTask(input, output, current.right, prescan + current.left.sum);

                left.fork();
                right.compute();
                left.join();
            }
        }
    }

    public static int sum(int[] input, int lo, int hi) {
        int sum = 0;
        for (int i = lo; i < hi; i++) {
            sum += input[i];
        }
        return sum;
    }

    public static int[] prefixSum(int[] input) {
        int[] output = new int[input.length];
        int sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += input[i];
            output[i] = sum;
        }
        return output;
    }

    public static int[] parallelPrefixSum(int[] input) {
        /* Make the tree */
        ProcessInputTask make = new ProcessInputTask(input, 0, input.length);
        PSTNode root = POOL.invoke(make);

        /* Use the tree */
        int[] output = new int[input.length];
        CreateOutputTask use = new CreateOutputTask(input, output, root, 0);
        POOL.invoke(use);

        return output;
    }
}
