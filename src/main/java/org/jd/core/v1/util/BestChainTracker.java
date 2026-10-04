/*
 * Copyright (c) 2026 Nicolas Baumann (@nbauma109)
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.util;

import java.util.Arrays;

/**
 * Tracks, for chains of items ordered by a value (a line number), the best chain (score and ending item) per rank of value.
 * A Fenwick tree answers "the best chain ending at or below a rank" in logarithmic time, so that the heaviest chain of
 * non decreasing values is found in O(n log n).
 *
 * <p>Ranks are 1-based positions in the sorted distinct values. Items are the indexes given to {@link #update}; -1 means none.</p>
 */
public final class BestChainTracker {
    private final int distinct;
    private final boolean preferLatest;
    private final int[] treeScore;
    private final int[] treeItem;
    private final int[] equalScore;
    private final int[] equalItem;

    /**
     * @param distinct     number of distinct values
     * @param preferLatest whether, for the same score, an updated chain replaces the one already stored (in the tree nodes
     *                     and for an exact rank); a query returns the first best node it meets, which is not always the latest
     */
    public BestChainTracker(int distinct, boolean preferLatest) {
        this.distinct = distinct;
        this.preferLatest = preferLatest;
        treeScore = new int[distinct + 1];
        treeItem = new int[distinct + 1];
        equalScore = new int[distinct + 1];
        equalItem = new int[distinct + 1];
        Arrays.fill(treeItem, -1);
        Arrays.fill(equalItem, -1);
    }

    /** @return the item ending the best chain whose value has a rank lower than or equal to the rank, -1 if there is none */
    public int bestAtOrBelow(int rank) {
        int bestScore = 0;
        int bestItem = -1;

        for (int k = rank; k > 0; k -= k & -k) {
            if (treeItem[k] != -1 && (bestItem == -1 || treeScore[k] > bestScore)) {
                bestScore = treeScore[k];
                bestItem = treeItem[k];
            }
        }
        return bestItem;
    }

    /** @return the item ending the best chain whose value has a rank lower than the rank, -1 if there is none */
    public int bestBelow(int rank) {
        return bestAtOrBelow(rank - 1);
    }

    /** @return the item ending the best chain which ends on exactly this rank, -1 if there is none */
    public int bestAt(int rank) {
        return equalItem[rank];
    }

    /** Records the chain of the given score which ends with the item, whose value has the given rank. */
    public void update(int item, int rank, int score) {
        if (rank < 1 || rank > distinct) {
            throw new IllegalArgumentException("rank " + rank + " is not in [1, " + distinct + "]");
        }
        if (equalItem[rank] == -1 || (preferLatest ? score >= equalScore[rank] : score > equalScore[rank])) {
            equalScore[rank] = score;
            equalItem[rank] = item;
        }
        for (int k = rank; k <= distinct; k += k & -k) {
            if (treeItem[k] == -1 || (preferLatest ? score >= treeScore[k] : score > treeScore[k])) {
                treeScore[k] = score;
                treeItem[k] = item;
            }
        }
    }
}
