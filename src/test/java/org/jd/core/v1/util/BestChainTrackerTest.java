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
}
