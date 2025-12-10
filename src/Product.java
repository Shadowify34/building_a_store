// The parent class for all products/services
public class Product {
    protected String name;
    protected double price;
    protected int inventory;
    protected boolean taxExempt;

    // The product constructor
    public Product(String name, double price, int inventory, boolean taxExempt) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.taxExempt = taxExempt;
    }

    public String getName() {return name;}
    public double getPrice() {return price;}
    public boolean isTaxExempt() {return taxExempt;}

    // Keeping track of inventory
    public boolean reduceInventory(int qty) {
        if (qty <= 0 || inventory < qty) {
            System.out.println("Sorry, not enough stock for " + name);
            return false;
        }
        inventory -= qty;
        return true;
    }
}

// The child classes / the products and services
class Pet extends Product {
    public Pet(String name, double price, int inventory) {
        super(name, price, inventory, false);
    }
}

class PetFood extends Product {
    public PetFood(String name, double price, int inventory) {
        super(name, price, inventory, false);
    }
}

class Service extends Product {
    public Service(String name, double price, int inventory) {
        super(name, price, inventory, true); // tax exempt
    }
}