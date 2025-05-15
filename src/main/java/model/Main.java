package model;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        OrderQueue oq = new OrderQueue();

        try {
            // Add a new order
            Order o1 = new Order("1", "John Doe", "Burger", "Pending");
            oq.addOrder(o1);

            // View all orders
            for (Order o : oq.getAllOrders()) {
                System.out.println(o.toFileString());
            }

            // Update status
            oq.updateOrderStatus("1", "Completed");

            // Delete order
            oq.deleteOrder("1");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

