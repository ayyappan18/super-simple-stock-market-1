package com.jpmorgan.stock;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

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

  public static void main(String[] args) {
    initStocks();
    printMenu();

    Scanner scanner = new Scanner(System.in);
    String choice= null;
    while (true) {
      choice = scanner.nextLine();
      if ("m".equals(choice)) {
        printMenu();
      }else if ("q".equals(choice)) {
        scanner.close();
        System.exit(0);
      } else {
        try {
          int option = Integer.parseInt(choice);
          Stock stockFromUser;
          double priceFromUser;

          switch (option) {
            case 1:
              stockFromUser = getStockFromUser(scanner);
              priceFromUser = getStockPriceFromUser(scanner);
              calculateDividendYield(stockFromUser, priceFromUser);
              break;
            case 2:
              stockFromUser = getStockFromUser(scanner);
              priceFromUser = getStockPriceFromUser(scanner);
              calculatePERatio(stockFromUser, priceFromUser);
              break;
            case 3:
              stockFromUser = getStockFromUser(scanner);
              int quantityFromUser = getQuantityFromUser(scanner);
              TradeType tradeTypeFromUser = getTradeType(scanner);
              priceFromUser = getStockPriceFromUser(scanner);
              recordTrade(stockFromUser, quantityFromUser, tradeTypeFromUser, priceFromUser);
              break;
            case 4:
              stockFromUser = getStockFromUser(scanner);
              calculateVolumeWeightedStockPrice(stockFromUser);
              break;
            case 5:
              calculateGBCE();
              break;
            default:
              break;
          }
        } catch(NumberFormatException e) {
          System.out.println("Invalid Option");
        }
      }
    }
  }

  private static Stock getStockFromUser(Scanner scanner) {
    System.out.println("Please input stock symbol");
    String stockSymbol = scanner.nextLine();
    Stock stock = stockService.getStock(stockSymbol);
    return stock;
  }

  private static double getStockPriceFromUser(Scanner scanner) {
    System.out.println("Please input stock price");
    String stockPrice = scanner.nextLine();
    return Double.parseDouble(stockPrice);
  }

  private static TradeType getTradeType(Scanner scanner) {
    System.out.println("Please input trade type (BUY/SELL)");
    String type = scanner.nextLine();
    return TradeType.valueOf(type.toUpperCase());
  }

  private static int getQuantityFromUser(Scanner scanner) {
    System.out.println("Please input quantity");
    String quantity = scanner.nextLine();
    return Integer.parseInt(quantity);
  }

  private static void printMenu() {
    System.out.println("JPMorgan - Super simple stock market");
    System.out.println("m: Show menu");
    System.out.println("1: Calculate dividend yield for stock");
    System.out.println("2: Calculate P/E ratio for stock");
    System.out.println("3: Record a trade for stock");
    System.out.println("4: Calculate Volume Weighted Stock Price for stock");
    System.out.println("5: Calculate GBCE All Share Index");
    System.out.println("q: Quit");
  }

  private static void calculateDividendYield(Stock stock, double price) {
    double result = stockService.calculateDividendYield(stock, price);
    System.out.println("Dividend Yield: " + result);
  }

  private static void calculatePERatio(Stock stock, double price) {
     double result = stockService.calculatePERatio(stock, price);
     System.out.println("PE Ratio: " + result);
  }

  private static void calculateVolumeWeightedStockPrice(Stock stock) {
    List<Trade> trades = tradeService.getTrades(stock, 15);
    double result = stockService.calculateVolumeWeightedStockPrice(trades);
    System.out.println("Volume Weighted Stock Price: " + result);
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
}
