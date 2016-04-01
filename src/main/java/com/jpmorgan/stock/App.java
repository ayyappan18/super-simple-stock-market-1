package com.jpmorgan.stock;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import com.jpmorgan.stock.exception.SuperSimpleStockMarketException;
import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.StockType;
import com.jpmorgan.stock.model.Trade;
import com.jpmorgan.stock.model.TradeType;
import com.jpmorgan.stock.service.StockService;
import com.jpmorgan.stock.service.TradeService;
import com.jpmorgan.stock.service.impl.StockServiceImpl;
import com.jpmorgan.stock.service.impl.TradeServiceImpl;

/**
 * JP Morgan - Super Simple Stock Market
 * @author nd@nathandeamer.com
 */
public class App {

  private static StockService stockService = StockServiceImpl.getInstance();
  private static TradeService tradeService = TradeServiceImpl.getInstance();
  private static Scanner scanner;

  public static void main(String[] args) {
    initStocks();
    printMenu();

    scanner = new Scanner(System.in);
    String choice= null;
    while (true) {
      choice = scanner.nextLine();
      if ("q".equals(choice)) {
        scanner.close();
        System.exit(0);
      } else {
        try {
          int option = Integer.parseInt(choice);
          Stock stockFromUser;
          double priceFromUser;

          switch (option) {
            case 1:
              stockFromUser = getStockFromUser();
              priceFromUser = getStockPriceFromUser();
              calculateDividendYield(stockFromUser, priceFromUser);
              break;
            case 2:
              stockFromUser = getStockFromUser();
              priceFromUser = getStockPriceFromUser();
              calculatePERatio(stockFromUser, priceFromUser);
              break;
            case 3:
              stockFromUser = getStockFromUser();
              int quantityFromUser = getQuantityFromUser();
              TradeType tradeTypeFromUser = getTradeType();
              priceFromUser = getStockPriceFromUser();
              recordTrade(stockFromUser, quantityFromUser, tradeTypeFromUser, priceFromUser);
              break;
            case 4:
              stockFromUser = getStockFromUser();
              calculateVolumeWeightedStockPrice(stockFromUser);
              break;
            case 5:
              calculateGBCE();
              break;
            default:
              break;
          }
        } catch (NumberFormatException e) {
          System.out.println("-------------------------------------");
          System.out.println("Invalid option");
          System.out.println("-------------------------------------");
        } catch (SuperSimpleStockMarketException e1) {
          System.out.println("-------------------------------------");
          System.out.println(e1.getMessage());
          System.out.println("-------------------------------------");
        }
        System.out.println("");
        printMenu();
      }
    }
  }

  private static Stock getStockFromUser() throws SuperSimpleStockMarketException {
    System.out.println("Please input stock symbol");
    String stockSymbol = scanner.nextLine();
    Stock stock = stockService.getStock(stockSymbol);
    if (stock == null) {
      throw new SuperSimpleStockMarketException("Stock not found");
    }
    return stock;
  }

  private static double getStockPriceFromUser() throws SuperSimpleStockMarketException {
    System.out.println("Please input stock price");
    String stockPrice = scanner.nextLine();
    try {
      double result = Double.parseDouble(stockPrice);
      if (result <= 0) {
        throw new SuperSimpleStockMarketException("Invalid price: Must be greated than 0");
      }
      return result;
    } catch (NumberFormatException e) {
      throw new SuperSimpleStockMarketException("Invalid price: Not a number");
    }
  }

  private static TradeType getTradeType() throws SuperSimpleStockMarketException {
    System.out.println("Please input trade type (BUY/SELL)");
    String type = scanner.nextLine();
    try {
      return TradeType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new SuperSimpleStockMarketException("Invalid trade type: Must be BUY or SELL");
    }
  }

  private static int getQuantityFromUser() throws SuperSimpleStockMarketException {
    System.out.println("Please input quantity");
    String quantity = scanner.nextLine();
    try {
      int result = Integer.parseInt(quantity);
      if (result <= 0) {
        throw new SuperSimpleStockMarketException("Invalid quantity: Must be greated than 0");
      }
      return result;
    } catch (NumberFormatException e) {
      throw new SuperSimpleStockMarketException("Invalid quantity: Not a number");
    }
  }

  private static void printMenu() {
    System.out.println("JPMorgan - Super simple stock market");
    System.out.println("1: Calculate dividend yield for stock");
    System.out.println("2: Calculate P/E ratio for stock");
    System.out.println("3: Record a trade for stock");
    System.out.println("4: Calculate Volume Weighted Stock Price for stock");
    System.out.println("5: Calculate GBCE All Share Index");
    System.out.println("q: Quit");
  }

  private static void calculateDividendYield(Stock stock, double price) {
    double result = stockService.calculateDividendYield(stock, price);
    printResult("Dividend Yield: " + result);
  }

  private static void calculatePERatio(Stock stock, double price) {
     double result = stockService.calculatePERatio(stock, price);
     System.out.println("PE Ratio: " + result);
  }

  private static void calculateVolumeWeightedStockPrice(Stock stock) {
    List<Trade> trades = tradeService.getTrades(stock, 15);
    if (trades == null || trades.isEmpty()) {
      System.out.println("Volume Weighted Stock Price: No trades");
    } else {
      double result = stockService.calculateVolumeWeightedStockPrice(trades);
      System.out.println("Volume Weighted Stock Price: " + result);
    }
  }

  private static void recordTrade(Stock stock, int quantity, TradeType type, double price) {
    tradeService.recordTrade(new Trade(stock, Calendar.getInstance().getTime(),
        quantity, type, price));
    System.out.println("Trade recorded");
  }

  private static void calculateGBCE() {
    List<Trade> allTrades = tradeService.getAllTrades();
    if (allTrades == null || allTrades.isEmpty()) {
      System.out.println("Unable to calculate GBCE: No trades");
    } else {
      System.out.println("GBCE: " + stockService.calculateGBCE(allTrades));
    }
  }

  private static void initStocks() {
    stockService.addStock(new Stock("TEA", StockType.COMMON, 0, 0, 100));
    stockService.addStock(new Stock("POP", StockType.COMMON, 8, 0, 100));
    stockService.addStock(new Stock("ALE", StockType.COMMON, 23, 0, 60));
    stockService.addStock(new Stock("GIN", StockType.PREFFERED, 8, 2, 100));
    stockService.addStock(new Stock("JOE", StockType.PREFFERED, 13, 0, 250));
  }

  private static void printResult(String result) {
    System.out.println("-------------------------------------");
    System.out.println(result);
    System.out.println("-------------------------------------");
  }
}
