package com.whitebit.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.whitebit.util.TestUtil.getListOfDeals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DepthCacheTest {

    private DepthCache depthCache;

    @BeforeEach
    void init() {
        depthCache = new DepthCache();
    }

    @Test
    void newDepthCacheShouldBeEmpty() {
        Map.Entry<BigDecimal, BigDecimal> bestAsk = depthCache.getBestAsk();
        Map.Entry<BigDecimal, BigDecimal> bestBid = depthCache.getBestBid();

        assertNull(bestAsk);
        assertNull(bestBid);
    }

    @Test
    void shouldReturnBestBidAfterReload() {
        List<List<String>> bids = getListOfDeals(
                "14.0761", "2.0007",
                "13.0001", "5.0007",
                "15.0001", "1.0007");
        depthCache.reloadBidsCache(bids);

        Map.Entry<BigDecimal, BigDecimal> bestBid = depthCache.getBestBid();

        assertEquals(new BigDecimal("15.0001"), bestBid.getKey());
        assertEquals(new BigDecimal("1.0007"), bestBid.getValue());
    }

    @Test
    void shouldReturnBestAskAfterReload() {
        List<List<String>> asks = getListOfDeals(
                "14.0761", "2.0007",
                "13.0001", "5.0007",
                "15.0001", "1.0007");

        depthCache.reloadAsksCache(asks);

        Map.Entry<BigDecimal, BigDecimal> bestAsk = depthCache.getBestAsk();

        assertEquals(new BigDecimal("13.0001"), bestAsk.getKey());
        assertEquals(new BigDecimal("5.0007"), bestAsk.getValue());
    }

    @Test
    void shouldReturnBestBidAfterReloadAndUpdate() {
        List<List<String>> bids = getListOfDeals(
                "14.0761", "2.0007",
                "13.0001", "5.0007",
                "15.0001", "1.0007");

        depthCache.reloadBidsCache(bids);

        bids = getListOfDeals("18.0001", "0.0063");

        depthCache.updateBids(bids);

        Map.Entry<BigDecimal, BigDecimal> bestBid = depthCache.getBestBid();

        assertEquals(new BigDecimal("18.0001"), bestBid.getKey());
        assertEquals(new BigDecimal("0.0063"), bestBid.getValue());
    }

    @Test
    void shouldReturnBestAskAfterReloadAndUpdate() {
        List<List<String>> asks = getListOfDeals(
                "14.0761", "2.0007",
                "13.0001", "5.0007",
                "15.0001", "1.0007");

        depthCache.reloadAsksCache(asks);

        asks = getListOfDeals(  "11.0001", "0.0063");

        depthCache.updateAsks(asks);

        Map.Entry<BigDecimal, BigDecimal> bestAsk = depthCache.getBestAsk();

        assertEquals(new BigDecimal("11.0001"), bestAsk.getKey());
        assertEquals(new BigDecimal("0.0063"), bestAsk.getValue());
    }
}