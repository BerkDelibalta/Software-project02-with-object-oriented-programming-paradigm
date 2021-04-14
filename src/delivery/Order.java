package delivery;

import java.util.HashMap;
import java.util.Map;

enum orderStatus{NOT_YET_ASSIGNED,ASSIGNED}

public class Order {

    private int orderId;
    private String customerName;
    private Restaurant restaurantName;
    private int deliveryTime;
    private int deliveryDistance;
    private orderStatus status;



    public Order(int orderId, String customerName, Restaurant restaurantName,int deliveryTime, int deliveryDistance) {
        this.orderId=orderId;
        this.customerName=customerName;
        this.restaurantName=restaurantName;
        this.deliveryTime=deliveryTime;
        this.deliveryDistance=deliveryDistance;
        this.status=orderStatus.NOT_YET_ASSIGNED;
    }


    public int getOrderId() {
        return orderId;
    }


    public Restaurant getRestaurantName() {
        return restaurantName;
    }


    public int getDeliveryTime() {
        return deliveryTime;
    }

    public int getDeliveryDistance() {
        return deliveryDistance;
    }

    public orderStatus getStatus() {
        return status;
    }

    public void setStatus(orderStatus status) {
        this.status = status;
    }
}
