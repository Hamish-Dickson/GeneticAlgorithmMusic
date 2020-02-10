package tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * ArrayTools.java
 *
 * Created on 30 September 2005, 10:37

/**
 * @author sb
 */
public class ArrayTools {
    /**
     * See http://rosettacode.org/wiki/Entropy
     *
     * @return Shannon entropy of array of ints
     */
    public static double entropy(int[] vals) {
        int[][] unique = uniqueElementsAndCounts(vals);

        double h = 0;

        double log2 = Math.log(2);
        for (int[] x : unique) {
            double px = ((double) x[1] / vals.length);  // probability of a label(i.e. count of appearances/total number of items)
            double ix = -(Math.log(px) / log2); // info content is neg log(px), here we're using base2 to get number of bits equivalent
            h += px * ix;
        }

        return h;
    }

    /**
     * @return a 2D array, with elements being 2-element arrays, for each unique member of vals (not in any particular order), holding the number and its frequency
     */
    public static int[][] uniqueElementsAndCounts(int[] vals) {
        Map<Integer, Integer> unique = new HashMap<Integer, Integer>();

        for (int i : vals) {
            Integer count = unique.get(i);
            if (count == null) {
                count = Integer.valueOf(1);
            } else {
                count = Integer.valueOf(count.intValue() + 1);
            }
            unique.put(Integer.valueOf(i), count);
        }

        int[][] rval = new int[unique.size()][2];
        int pointer = 0;
        for (Entry<Integer, Integer> e : unique.entrySet()) {
            rval[pointer++] = new int[]{e.getKey(), e.getValue()};
        }

        return rval;
    }
}
