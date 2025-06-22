import java.util.*;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Transaction {
    String type; // Buy or Sell
    String stockSymbol;
    int quantity;
    double priceAtTransaction;

    Transaction(String type, String stockSymbol, int quantity, double priceAtTransaction) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.priceAtTransaction = priceAtTransaction;
    }

    @Override
    public String toString() {
        return type + " " + quantity + " of " + stockSymbol + " at $" + priceAtTransaction;
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    double cash = 10000.0; // Starting balance

    void buyStock(String symbol, int qty, double price) {
        double cost = qty * price;
        if (cash >= cost) {
            holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
            cash -= cost;
            history.add(new Transaction("BUY", symbol, qty, price));
            System.out.println("Bought " + qty + " of " + symbol + " at $" + price);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    void sellStock(String symbol, int qty, double price) {
        int currentQty = holdings.getOrDefault(symbol, 0);
        if (currentQty >= qty) {
            holdings.put(symbol, currentQty - qty);
            cash += qty * price;
            history.add(new Transaction("SELL", symbol, qty, price));
            System.out.println("Sold " + qty + " of " + symbol + " at $" + price);
        } else {
            System.out.println("You don't own enough of that stock.");
        }
    }

    void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n=== Portfolio Summary ===");
        System.out.println("Cash Balance: $" + cash);
        double totalValue = cash;

        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int qty = entry.getValue();
            double price = market.get(symbol).price;
            double value = qty * price;
            totalValue += value;
            System.out.println(symbol + ": " + qty + " shares @ $" + price + " = $" + value);
        }

        System.out.println("Total Portfolio Value: $" + totalValue);
    }

    void showTransactionHistory() {
        System.out.println("\n=== Transaction History ===");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }
}

public class StockTradingApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Stock> market = new HashMap<>();

        // Market data setup
        market.put("AAPL", new Stock("AAPL", 180.0));
        market.put("GOOG", new Stock("GOOG", 2700.0));
        market.put("TSLA", new Stock("TSLA", 750.0));
        market.put("AMZN", new Stock("AMZN", 3300.0));

        Portfolio userPortfolio = new Portfolio();

        while (true) {
            System.out.println("\n=== Stock Market Menu ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("\n=== Market Stocks ===");
                    for (Stock stock : market.values()) {
                        System.out.println(stock.symbol + ": $" + stock.price);
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    if (market.containsKey(buySymbol)) {
                        System.out.print("Enter quantity to buy: ");
                        int qty = scanner.nextInt();
                        userPortfolio.buyStock(buySymbol, qty, market.get(buySymbol).price);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    if (market.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity to sell: ");
                        int qty = scanner.nextInt();
                        userPortfolio.sellStock(sellSymbol, qty, market.get(sellSymbol).price);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;
                case 4:
                    userPortfolio.showPortfolio(market);
                    break;
                case 5:
                    userPortfolio.showTransactionHistory();
                    break;
                case 6:
                    System.out.println("Thank you for trading!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
