package para;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class LongestSequence {
    public static class SequenceRange {
        public int matchingOnLeft, matchingOnRight;
        public int longestRange, sequenceLength;

        public SequenceRange(int left, int right, int longest, int length) {
            this.matchingOnLeft = left;
            this.matchingOnRight = right;
            this.longestRange = longest;
            this.sequenceLength = length;
        }
    }

    public static SequenceRange sequential(int[] arr, int lo, int hi, int target) {
        int max = 0;
        int currMax = 0;
        int left = 0;
        int right = 0;
        int ptrL = lo;
        int ptrR = hi - 1;
        while (ptrL < hi && arr[ptrL] == target) {
            left++;
            ptrL++;
        }
        while (ptrR >= lo && arr[ptrR] == target) {
            right++;
            ptrR--;
        }
        for (int i = lo; i < hi; i++) {
            if (arr[i] == target) {
                currMax++;
            } else {
                currMax = 0;
            }
            max = Math.max(currMax, max);
        }

        SequenceRange result = new SequenceRange(left, right, max, hi - lo);

        return result;
    }

    static final ForkJoinPool POOL = new ForkJoinPool();

    public static int getLongestSequence(int val, int[] arr, int sequentialCutoff) {
        return POOL.invoke(new LongestSequenceTask(arr, 0, arr.length, val, sequentialCutoff)).longestRange;
    }

    private static class LongestSequenceTask extends RecursiveTask<SequenceRange> {
        int[] arr;
        int lo;
        int hi;
        int val;
        int sequentialCutOff;

        public LongestSequenceTask(int[] arr, int lo, int hi, int val, int sequentialCutOff) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.sequentialCutOff = sequentialCutOff;
        }


        @Override
        protected SequenceRange compute() {
            if (hi - lo <= sequentialCutOff) {
                return sequential(arr, lo, hi, val);
            }

            int mid = lo + (hi - lo) / 2;

            LongestSequenceTask left = new LongestSequenceTask(arr, lo, mid, val, sequentialCutOff);
            LongestSequenceTask right = new LongestSequenceTask(arr, mid, hi, val, sequentialCutOff);

            left.fork();

            SequenceRange rightResult = right.compute();
            SequenceRange leftResult = left.join();

            int rightLeft = rightResult.matchingOnLeft;
            int leftLeft = leftResult.matchingOnLeft;
            int rightRight = rightResult.matchingOnRight;
            int leftRight = leftResult.matchingOnRight;

            int rightLen = rightResult.sequenceLength;
            int leftLen = leftResult.sequenceLength;

            int rightLongest = rightResult.longestRange;
            int leftLongest = leftResult.longestRange;


            int newRight = 0;
            if (rightRight == rightResult.sequenceLength) {
                newRight = rightRight + leftRight;
            } else {
                newRight = rightRight;
            }
            int newLeft = 0;
            if (leftLeft == leftResult.sequenceLength) {
                newLeft = rightLeft + leftLeft;
            } else {
                newLeft = leftLeft;
            }

            int newLen = rightLen + leftLen;

            int newLongest = Math.max(rightLongest, leftLongest);
            if (rightLeft + leftRight > newLongest) {
                newLongest = rightLeft + leftRight;
            }


            SequenceRange result = new SequenceRange(newLeft, newRight, newLongest, newLen);

            return result;
        }
    }

    private static void usage() {
        System.err.println("USAGE: LongestSequence <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }
        int val = 0;
        int[] arr = null;
        try {
            val = Integer.parseInt(args[0]);
            String[] stringArr = args[1].replaceAll("\\s*",  "").split(",");
            arr = new int[stringArr.length];
            for (int i = 0; i < stringArr.length; i++) {
                arr[i] = Integer.parseInt(stringArr[i]);
            }
            System.out.println(getLongestSequence(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
