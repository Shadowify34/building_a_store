public class PetStore {
    public static void main(String[] args) {
        // Store name and tax rate
        Store store = new Store("Pet Paradise", 0.13);

        // Adding products
        store.addProduct(new Pet("Lizard", 50.0, 5));
        store.addProduct(new Pet("Cat", 100.0, 3));
        store.addProduct(new Pet("Dog", 150.0, 2));
        store.addProduct(new Pet("Parrot", 80.0, 4));
        store.addProduct(new Pet("Hamster", 25.0, 12));
        store.addProduct(new Pet("Rabbit", 40.0, 6));
        store.addProduct(new Pet("Goldfish", 10.0, 20));
        store.addProduct(new Pet("Turtle", 35.0, 7));

        // Adding services
        store.addProduct(new Service("Adoption Fee", 25.0, 50));
        store.addProduct(new Service("Pet Cleaning", 30.0, 100));
        store.addProduct(new Service("Grooming", 45.0, 100));
        store.addProduct(new Service("Training Session", 60.0, 50));
        store.addProduct(new Service("Vet Checkup", 70.0, 40));

        // Adding pet foods
        store.addProduct(new PetFood("Dog Food", 25.0, 15));
        store.addProduct(new PetFood("Cat Food", 22.0, 18));
        store.addProduct(new PetFood("Fish Food", 8.0, 30));
        store.addProduct(new PetFood("Bird Food", 10.0, 25));
        store.addProduct(new PetFood("Rabbit Food", 12.0, 20));
        store.addProduct(new PetFood("Turtle Food", 14.0, 15));
        store.addProduct(new PetFood("Hamster Food", 9.0, 25));

        // Starting shopping loop
        store.startShopping();
    }
}