// Eren, Samim, and Phillip
// Pet Paradise, 07/11/2025

import java.util.HashMap;
import java.util.Scanner;

// Store keeps products, shoppers, tax rate, and sales
public class Store {
    private String name;
    private double taxRate;
    private HashMap<String, Product> products = new HashMap<>();
    private HashMap<String, Shopper> shoppers = new HashMap<>();

    private int numberOfSales = 0;
    private double revenue = 0.0;

    public Store(String name, double taxRate) {
        this.name = name;
        this.taxRate = taxRate;
    }

    // Adding products
    public void addProduct(Product p) {
        products.put(p.getName().toLowerCase(), p);
    }

    // Find or create a shopper for this ID
    private Shopper getShopper(String id) {
        String key = id.toLowerCase();
        Shopper shopper = shoppers.get(key);
        if (shopper == null) {
            shopper = new Shopper(id);
            shoppers.put(key, shopper);
        }
        return shopper;
    }

    // Looking up points
    public int getPointsForId(String id) {
        Shopper s = shoppers.get(id.toLowerCase());
        return (s == null) ? 0 : s.getPoints();
    }

    // Main loop
    public void startShopping() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to " + name + "!");

        while (true) {
            System.out.print("Enter Shopper ID (or type 'leave' to exit): ");
            String shopperId = scanner.nextLine();
            if (shopperId.equalsIgnoreCase("leave")) {
                System.out.println("Goodbye!");
                break;
            }
            if (shopperId.isEmpty()) {
                System.out.println("Please enter a valid ID.");
                continue;
            }

            Shopper shopper = getShopper(shopperId);
            System.out.println("Hello, " + shopper.getId() + "! You currently have " + shopper.getPoints() + " points.");

            // Showing list of products and services along with quantity and their price
            System.out.println("Here are our products and services: (product/service, quantity, cost)");
            System.out.println("Animals:");
            for (Product prod : products.values()) {
                if (prod instanceof Pet) {
                    System.out.println(prod.getName() + " (" + prod.inventory + ") $" + String.format("%.2f", prod.getPrice()));
                }
            }
            System.out.println("\nAnimal Foods:");
            for (Product prod : products.values()) {
                if (prod instanceof PetFood) {
                    System.out.println(prod.getName() + " (" + prod.inventory + ") $" + String.format("%.2f", prod.getPrice()));
                }
            }
            System.out.println("\nServices:");
            for (Product prod : products.values()) {
                if (prod instanceof Service) {
                    System.out.println(prod.getName() + " (" + prod.inventory + ") $" + String.format("%.2f", prod.getPrice()));
                }
            }

            HashMap<String, Integer> cart = new HashMap<>();

            // Item entry loop
            while (true) {
                System.out.print("Please enter a product name: ");
                String productName = scanner.nextLine().toLowerCase();

                if (!products.containsKey(productName)) {
                    System.out.println("Product not found!");
                } else {
                    Product p = products.get(productName);
                    System.out.print("Please enter a quantity: ");
                    int qty;
                    try {
                        qty = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a whole number for quantity.");
                        continue;
                    }

                    if (p.reduceInventory(qty)) {
                        cart.put(productName, cart.getOrDefault(productName, 0) + qty);
                        System.out.println(p.getName() + " added to your cart.");
                    }
                }

                System.out.printf("Current total: $%.2f\n", getTotal(cart));
                System.out.print("Would you like to enter more products?(y/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) {
                    break;
                }
            }

            if (cart.isEmpty()) {
                System.out.println("No items purchased for this session.");
                continue; // back to the start of the loop
            }

            // Price breakdown
            double subtotal = getSubtotal(cart); // before tax
            double tax = getTax(cart); // tax on taxable items
            double totalBeforeRewards = subtotal + tax; // after tax, before discount

            System.out.printf("Subtotal (before tax): $%.2f\n", subtotal);
            System.out.printf("Tax: $%.2f\n", tax);
            System.out.printf("Total before rewards: $%.2f\n", totalBeforeRewards);

            // Rewards
            double discountApplied = 0.0;
            int availableBlocks = shopper.getPoints() / 100;
            if (availableBlocks > 0 && totalBeforeRewards > 0.0) {
                double maxDiscount = availableBlocks * 5.0;
                System.out.println("You have " + availableBlocks + " block(s) available = up to $" + String.format("%.2f", maxDiscount) + " off.");
                System.out.print("How many blocks would you like to redeem? (0-" + availableBlocks + "): ");
                int requestedBlocks = 0;
                try {
                    requestedBlocks = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    requestedBlocks = 0;
                }
                if (requestedBlocks < 0) requestedBlocks = 0;
                if (requestedBlocks > availableBlocks) requestedBlocks = availableBlocks;

                // Cap discount so it never exceeds the total
                int maxBlocksByTotal = (int)Math.floor(totalBeforeRewards / 5.0);
                int blocksToUse = Math.min(requestedBlocks, maxBlocksByTotal);

                if (blocksToUse > 0) {
                    int actuallyRedeemed = shopper.redeemBlocks(blocksToUse); // reduce points
                    discountApplied = actuallyRedeemed * 5.0;
                    System.out.printf("Rewards discount applied: -$%.2f\n", discountApplied);
                }
            }

            double finalTotal = totalBeforeRewards - discountApplied;
            System.out.printf("Amount due: $%.2f after tax\n", finalTotal);

            // Store stats
            numberOfSales++;
            revenue += finalTotal;

            // Earn points based on subtotal
            int pointsEarned = (int) subtotal;
            shopper.addPoints(pointsEarned);
            System.out.println("You earned " + pointsEarned + " points! You now have " + shopper.getPoints() + " points.");

            // Show remaining blocks for next time
            int newBlocks = shopper.getPoints() / 100;
            if (newBlocks > 0) {
                System.out.println("You now have " + newBlocks + " block(s) available for next time.");
            }

            // loop back to serve another customer or 'leave'
        }
    }

    // Sum of price * quantity
    private double getSubtotal(HashMap<String, Integer> cart) {
        double subtotal = 0.0;
        for (String name : cart.keySet()) {
            Product p = products.get(name);
            subtotal += p.getPrice() * cart.get(name);
        }
        return subtotal;
    }

    // Adding tax
    private double getTax(HashMap<String, Integer> cart) {
        double tax = 0.0;
        for (String name : cart.keySet()) {
            Product p = products.get(name);
            if (!p.isTaxExempt()) {
                tax += p.getPrice() * cart.get(name) * taxRate;
            }
        }
        return tax;
    }

    // Running total during item entry
    private double getTotal(HashMap<String, Integer> cart) {
        return getSubtotal(cart) + getTax(cart);
    }
}