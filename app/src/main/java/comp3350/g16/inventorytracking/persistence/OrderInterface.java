package comp3350.g16.inventorytracking.persistence;

import java.util.List;

import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;

public interface OrderInterface {
    boolean addOrder(Order newOrder);
    Order removeOrder();
    boolean removeOrder(int orderNum);
    void changeOrderStatus(int orderNum, Status newStatus);
    void getList(List<Order> list);
    Order search(int orderNumber);
    int size();
}
