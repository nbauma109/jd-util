package org.jd.core.v1.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BestChainTrackerTest {
    @Test
    public void testNothingIsTrackedInitially() {
        BestChainTracker tracker = new BestChainTracker(3, false);

        assertEquals(-1, tracker.bestAtOrBelow(3));
        assertEquals(-1, tracker.bestBelow(1));
        assertEquals(-1, tracker.bestAt(2));
    }

    @Test
    public void testTheBestChainAtOrBelowARank() {
        BestChainTracker tracker = new BestChainTracker(4, false);

        tracker.update(0, 1, 1);
        tracker.update(1, 2, 3);
        tracker.update(2, 4, 2);

        assertEquals(1, tracker.bestAtOrBelow(3));
        assertEquals(1, tracker.bestAtOrBelow(4));
        assertEquals(0, tracker.bestAtOrBelow(1));
        assertEquals(0, tracker.bestBelow(2));
        assertEquals(-1, tracker.bestBelow(1));
        assertEquals(2, tracker.bestAt(4));
    }

    @Test
    public void testTheTieOfTwoChainsOfTheSameScore() {
        BestChainTracker keepFirst = new BestChainTracker(2, false);
        BestChainTracker keepLatest = new BestChainTracker(2, true);

        for (BestChainTracker tracker : new BestChainTracker[] {keepFirst, keepLatest}) {
            tracker.update(0, 1, 2);
            tracker.update(1, 1, 2);
        }

        assertEquals(0, keepFirst.bestAtOrBelow(2));
        assertEquals(1, keepLatest.bestAtOrBelow(2));
    }

    @Test
    public void testAChainOfNonPositiveScoreIsReturned() {
        BestChainTracker tracker = new BestChainTracker(2, false);

        tracker.update(0, 1, 0);

        assertEquals(0, tracker.bestAtOrBelow(2));
    }

    @Test
    public void testTheExactRankFollowsTheTiePreference() {
        BestChainTracker keepFirst = new BestChainTracker(1, false);
        BestChainTracker keepLatest = new BestChainTracker(1, true);

        for (BestChainTracker tracker : new BestChainTracker[] {keepFirst, keepLatest}) {
            tracker.update(0, 1, 2);
            tracker.update(1, 1, 2);
        }

        assertEquals(0, keepFirst.bestAt(1));
        assertEquals(1, keepLatest.bestAt(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testARankBelowOneIsRejected() {
        new BestChainTracker(2, false).update(0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testARankAboveTheDistinctValuesIsRejected() {
        new BestChainTracker(2, false).update(0, 3, 1);
    }

    @Test
    public void testALowerScoreDoesNotReplaceABetterChain() {
        for (boolean preferLatest : new boolean[] {false, true}) {
            BestChainTracker tracker = new BestChainTracker(4, preferLatest);

            tracker.update(0, 2, 5);
            tracker.update(1, 2, 3);
            tracker.update(2, 3, 4);
            tracker.update(3, 4, 1);

            assertEquals(0, tracker.bestAt(2));
            assertEquals(0, tracker.bestAtOrBelow(2));
            assertEquals(0, tracker.bestAtOrBelow(4));
            assertEquals(2, tracker.bestAt(3));
            assertEquals(-1, tracker.bestAt(1));
        }
    }

    @Test
    public void testTheBestOfSeveralNodesOfTheTree() {
        BestChainTracker tracker = new BestChainTracker(8, false);

        tracker.update(0, 4, 2);
        tracker.update(1, 5, 7);
        tracker.update(2, 6, 3);
        tracker.update(3, 7, 9);

        // 7 = 4 + 2 + 1: the nodes 4 (ranks 1 to 4), 6 (ranks 5 and 6) and 7 are looked up
        assertEquals(3, tracker.bestAtOrBelow(7));
        assertEquals(1, tracker.bestAtOrBelow(6));
        assertEquals(1, tracker.bestAtOrBelow(5));
        assertEquals(0, tracker.bestAtOrBelow(4));
        assertEquals(-1, tracker.bestAtOrBelow(3));
        assertEquals(3, tracker.bestAtOrBelow(8));
    }

    @Test
    public void testALowerChainBelowTheBestOneIsNotPreferred() {
        BestChainTracker tracker = new BestChainTracker(4, false);

        tracker.update(0, 3, 5);
        tracker.update(1, 2, 1);

        assertEquals(0, tracker.bestAtOrBelow(3));
    }

    @Test
    public void testAgainstAnExhaustiveSearch() {
        java.util.Random random = new java.util.Random(7);

        for (boolean preferLatest : new boolean[] {false, true}) {
            for (int round = 0; round < 200; round++) {
                int distinct = 1 + random.nextInt(9);
                BestChainTracker tracker = new BestChainTracker(distinct, preferLatest);
                int[] ranks = new int[12];
                int[] scores = new int[12];

                for (int item = 0; item < ranks.length; item++) {
                    ranks[item] = 1 + random.nextInt(distinct);
                    scores[item] = random.nextInt(5);
                    tracker.update(item, ranks[item], scores[item]);

                    for (int rank = 1; rank <= distinct; rank++) {
                        int best = -1;
                        for (int other = 0; other <= item; other++) {
                            if (ranks[other] <= rank && (best == -1 || scores[other] > scores[best])) {
                                best = other;
                            }
                        }
                        int found = tracker.bestAtOrBelow(rank);

                        assertEquals(best == -1 ? -1 : scores[best], found == -1 ? -1 : scores[found]);
                    }
                }
            }
        }
    }
}
