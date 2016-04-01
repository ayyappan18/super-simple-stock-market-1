package com.jpmorgan.stock.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.StockType;
import com.jpmorgan.stock.model.Trade;
import com.jpmorgan.stock.model.TradeType;
import com.jpmorgan.stock.service.impl.TradeServiceImpl;

/**
 * Tests for {@code TradeService}
 * @author nd@nathandeamer.com
 */
public class TradeServiceImplTest {

  private TradeService tradeService;
  private Stock stock1;

  @Before
  public void setup() {
    tradeService = new TradeServiceImpl();
    stock1 = new Stock("TEST", StockType.COMMON, 1, 0, 1);
  }

  @Test
  public void testRecordTrade() {
    Trade trade = new Trade(stock1, Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0);
    tradeService.recordTrade(trade);
    List<Trade> result = tradeService.getTrades(stock1, 15);
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  public void testGetAllTradesInLast15Minutes() {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.MINUTE, -16);
    Trade firstTrade = new Trade(stock1, c.getTime(), 1, TradeType.BUY, 1.0);
    tradeService.recordTrade(firstTrade);

    Date time = Calendar.getInstance().getTime();
    Trade secondTrade = new Trade(stock1, time, 1, TradeType.BUY, 1.0);
    tradeService.recordTrade(secondTrade);

    List<Trade> result = tradeService.getTrades(stock1, 15);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(time, result.get(0).getTimestamp());
  }

  @Test
  public void testGetAllTrades() {
    tradeService.recordTrade(new Trade(stock1, Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0));
    tradeService.recordTrade(new Trade(stock1, Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0));
    tradeService.recordTrade(new Trade(stock1, Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0));
    List<Trade> result = tradeService.getAllTrades();
    assertEquals(3, result.size());
  }

}
