package service;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletContext;
import model.Cart;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.*;

public class CartService {
    private static final String CART_SESSION_KEY = "cart";
    private static final String FILE_PATH = "WEB-INF/resources/data/carts.txt";
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
    private final ServletContext servletContext;
    private final FoodItemService foodItemService;

    public CartService(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.foodItemService = new FoodItemService(servletContext);
    }

    // Create or retrieve a cart from the session or file
    public Cart getOrCreateCart(HttpSession session, String userID) throws IOException {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = loadCartFromFile(userID);
            if (cart == null) {
                cart = new Cart(userID);
                LOGGER.info("Created new cart for user: " + userID);
            }
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    // Retrieve a food item by its ID
    public FoodItem getFoodItemById(String foodId) throws IOException {
        return foodItemService.getFoodItemById(foodId);
    }

    // Add an item to the cart
    public void addItem(HttpSession session, FoodItem foodItem) throws IOException {
        User user = (User) session.getAttribute("user");
        String userID = user != null ? user.getUserID() : "GUEST";
        Cart cart = getOrCreateCart(session, userID);
        cart.addItem(foodItem);
        saveCartToFile(cart);
    }

    // Remove an item from the cart
    public void removeItem(HttpSession session, FoodItem foodItem) throws IOException {
        User user = (User) session.getAttribute("user");
        String userID = user != null ? user.getUserID() : "GUEST";
        Cart cart = getOrCreateCart(session, userID);
        cart.removeItem(foodItem);
        saveCartToFile(cart);
    }

    // Update the quantity of an item in the cart
    public void updateQuantity(HttpSession session, FoodItem foodItem, int newQuantity) throws IOException {
        User user = (User) session.getAttribute("user");
        String userID = user != null ? user.getUserID() : "GUEST";
        Cart cart = getOrCreateCart(session, userID);
        cart.updateQuantity(foodItem, newQuantity);
        saveCartToFile(cart);
    }

    // Clear the cart
    public void clearCart(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String userID = user != null ? user.getUserID() : "GUEST";
        Cart cart = getOrCreateCart(session, userID);
        cart.clearCart();
        saveCartToFile(cart);
    }

    // Get the cart items
    public List<Cart.CartItem> getCartItems(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String userID = user != null ? user.getUserID() : "GUEST";
        Cart cart = getOrCreateCart(session, userID);
        return cart.getItems();
    }

    // Get the total amount
    public double getTotalAmount(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String userID = user != null ? user.getUserID() : "GUEST";
        Cart cart = getOrCreateCart(session, userID);
        return cart.getTotalAmount();
    }

    // Save the cart to a file
    private void saveCartToFile(Cart cart) throws IOException {
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.exists()) {
            LOGGER.warning("carts.txt does not exist at " + realPath + ". Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        // Read existing cart entries for other users
        List<String> allEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && !data[0].equals(cart.getUserID())) {
                    allEntries.add(line); // Keep entries for other users
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading carts.txt", e);
            throw e;
        }

        // Add the current user's cart entries
        for (Cart.CartItem item : cart.getItems()) {
            String line = String.format("%s,%s,%d",
                    cart.getUserID(),
                    item.foodItem.getFoodId(), item.quantity);
            allEntries.add(line);
        }

        // Write all entries back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String entry : allEntries) {
                writer.write(entry);
                writer.newLine();
            }
            LOGGER.info("Successfully saved cart for user " + cart.getUserID() + " to carts.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to carts.txt", e);
            throw e;
        }
    }

    // Load the cart from a file
    private Cart loadCartFromFile(String userID) throws IOException {
        Cart cart = null;
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.exists()) {
            LOGGER.warning("carts.txt does not exist at " + realPath + ". Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[0].equals(userID)) {
                    if (cart == null) {
                        cart = new Cart(data[0]);
                    }
                    FoodItem foodItem = foodItemService.getFoodItemById(data[1]);
                    if (foodItem != null) {
                        cart.updateQuantity(foodItem, Integer.parseInt(data[2]));
                    } else {
                        LOGGER.warning("Food item with ID " + data[1] + " not found for user " + userID);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading carts.txt", e);
            throw e;
        }
        return cart;
    }
}