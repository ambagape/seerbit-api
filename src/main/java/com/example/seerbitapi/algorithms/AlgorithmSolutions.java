package com.example.seerbitapi.algorithms;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ambrose
 */
public class AlgorithmSolutions {

    private static class Interval {

        private long start;
        private long end;

        public Interval(Date start, Date end) {
            this.start = start.getTime();
            this.end = end.getTime();
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

    }

    public static List<Interval> mergeLists(List<Interval> intervals) {
        List<Interval> mergedLists = new ArrayList<>();

        if (intervals == null || intervals.isEmpty()) {
            return mergedLists;
        }

        Interval currentInterval = intervals.get(0);

        //Find overlapping intervals, merge them and add non-overlapping ones to result
        for (int i = 1; i < intervals.size(); i++) {
            Interval nextInterval = intervals.get(i);

            if (currentInterval.getEnd() >= nextInterval.getStart()) {
                currentInterval.setEnd(Math.max(currentInterval.getEnd(), nextInterval.getEnd()));
            } else {
                mergedLists.add(currentInterval);
                currentInterval = nextInterval;
            }
        }

        // Add the last interval to the result
        mergedLists.add(currentInterval);

        return mergedLists;
    }

    public static int findMaxXORSubarray(int[] arr) {
        int maxXOR = 0;

        List<int[]> subArrays = findAllSubarrays(arr);

        for (int[] subArray : subArrays) {
            int currentXOR = findXOR(subArray);
            maxXOR = Math.max(maxXOR, currentXOR);
        }
        return maxXOR;
    }

    private static List<int[]> findAllSubarrays(int[] array) {
        List<int[]> subarrays = new ArrayList<>();

        for (int start = 0; start < array.length; start++) {
            for (int end = start; end < array.length; end++) {
                int[] subarray = Arrays.copyOfRange(array, start, end + 1);
                subarrays.add(subarray);
            }
        }

        return subarrays;
    }

    private static int findXOR(int[] array) {
        int result = 0;
        for (int num : array) {
            result = result ^ num;
        }
        return result;
    }

    public static void main(String[] args) {
        //Test interval merging
        List<Interval> intervals;
        intervals = Arrays.asList(
                new Interval(Date.from(Instant.parse("2007-12-01T10:15:30.00Z")), Date.from(Instant.parse("2007-12-03T10:15:30.00Z"))),
                new Interval(Date.from(Instant.parse("2007-12-02T10:15:30.00Z")), Date.from(Instant.parse("2007-12-06T10:15:30.00Z"))),
                new Interval(Date.from(Instant.parse("2007-12-08T10:15:30.00Z")), Date.from(Instant.parse("2007-12-10T10:15:30.00Z"))),
                new Interval(Date.from(Instant.parse("2007-12-15T10:15:30.00Z")), Date.from(Instant.parse("2007-12-18T10:15:30.00Z")))
        );

        List<Interval> mergedIntervals = mergeLists(intervals);

        for (Interval interval : mergedIntervals) {
            System.out.println("[" + new Date(interval.start) + ", " + new Date(interval.end) + "]");
        }

        //Test Max SubArray XOR
        int[] arr = {1, 2, 3, 4};
        int maxXOR = findMaxXORSubarray(arr);
        System.out.println("Maximum XOR of subarray: " + maxXOR);
    }

}
