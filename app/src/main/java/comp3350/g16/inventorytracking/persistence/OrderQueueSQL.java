package comp3350.g16.inventorytracking.persistence;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;


public class OrderQueueSQL implements OrderInterface{
    private Connection connection;
    private Statement statement;
    private int result;
    private int size;

    public OrderQueueSQL(){
        initialize();
    }

    public boolean addOrder(Order newOrder) {
        boolean orderAdded = false;
        connection = null;
        statement = null;
        result = 0;
        if (newOrder!=null){
            if(search(newOrder.getOrderNumber())== null) {
                try {
                    Class.forName("org.hsqldb.jdbcDriver");
                    connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
                    statement = connection.createStatement();
                    result = statement.executeUpdate("INSERT INTO ORDERQUEUE VALUES (NULL, " +
                            newOrder.getOrderPrice() + ", " +
                            newOrder.getSize() + ", '" +
                            newOrder.getStatus().toString() + "');");
                    connection.commit();
                    orderAdded = true;
                    size++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return orderAdded;
    }

    public Order removeOrder() {
        Order removed = new Order();
        connection = null;
        statement = null;
        ResultSet resultSet;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT TOP 1 * FROM ORDERQUEUE;");
            while (resultSet.next()) {
                removed.setOrderNumber(resultSet.getInt("ORDERNUMBER"));
                removed.setOrderPrice(resultSet.getDouble("PRICE"));
                removed.setSize(resultSet.getInt("ITEMCOUNT"));
                removed.setOrderStatus(Status.valueOf(resultSet.getString("STATUS")));
            }
            result = statement.executeUpdate("DELETE FROM ORDER_ITEMS WHERE ORDERNUMBER = " + removed.getOrderNumber());
            result = statement.executeUpdate("DELETE FROM ORDERQUEUE WHERE ORDERNUMBER = " + removed.getOrderNumber());
            size--;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return removed;
    }

    public boolean removeOrder(int orderNum) {
        boolean orderRemoved = false;
        connection = null;
        statement = null;
        System.out.println(search(orderNum));
        if (search(orderNum)!= null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
                statement = connection.createStatement();
                result = statement.executeUpdate("DELETE FROM ORDER_ITEMS WHERE ORDERNUMBER = " + orderNum);
                result = statement.executeUpdate("DELETE FROM ORDERQUEUE WHERE ORDERNUMBER = " + orderNum);
                orderRemoved = true;
                size--;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return orderRemoved;
    }

    public void changeOrderStatus(int orderNum, Status newStatus) {

        connection = null;
        statement = null;
        result = 0;
        if(search(orderNum) != null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
                statement = connection.createStatement();
                result = statement.executeUpdate("UPDATE ORDERQUEUE SET STATUS = '" + newStatus.toString() + "' WHERE ORDERNUMBER = " + orderNum );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getList() {
        LinkedList<Order> values = new LinkedList<>();
        Order orderItem = new Order();
        connection = null;
        statement = null;
        ResultSet resultSet;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ORDERQUEUE");
            while (resultSet.next()) {
                orderItem.setOrderNumber(resultSet.getInt("ORDERNUMBER"));
                orderItem.setOrderPrice(resultSet.getDouble("PRICE"));
                orderItem.setSize(resultSet.getInt("ITEMCOUNT"));
                orderItem.setOrderStatus(Status.valueOf(resultSet.getString("STATUS")));
                values.add(orderItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }
    public void getList(List<Order> list)
    {
        // TODO: 2017-07-12 to be implemented further
        list.clear();
        list.addAll(getList());
    }

    public Order search(int orderNumber) {
        Order found = new Order();
        connection = null;
        statement = null;
        ResultSet resultSet;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ORDERQUEUE WHERE ORDERNUMBER =  " + orderNumber + "");
            boolean hasResult = resultSet.next();
            if (hasResult) {
                while (hasResult) {
                    found.setOrderNumber(resultSet.getInt("ORDERNUMBER"));
                    found.setOrderPrice(resultSet.getDouble("PRICE"));
                    found.setOrderStatus(Status.valueOf(resultSet.getString("STATUS")));
                    hasResult = false;
                }
            }

            else {
                found = null;
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    public String toString () {
        connection = null;
        statement = null;
        ResultSet resultSet;
        String output ="";

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ORDERQUEUE");
            while (resultSet.next()){
                output += resultSet.getInt("ORDERNUMBER") + " | " +
                        resultSet.getDouble("PRICE") + " | " +
                        resultSet.getInt("ITEMCOUNT") + " | " +
                        Status.valueOf(resultSet.getString("STATUS")) + "\n";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public int size()
    {
        return size;
    }

    private void initialize() {
        String sqlQuery = "DROP TABLE IF EXISTS ORDER_ITEMS\n" +
                "DROP TABLE IF EXISTS ORDERITEMS\n" +
                "DROP TABLE IF EXISTS ORDERQUEUE\n" +
                "\n" +
                "CREATE TABLE ORDERQUEUE (ORDERNUMBER INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1000000, INCREMENT BY 1) PRIMARY KEY, PRICE decimal(10,2) NOT NULL, ITEMCOUNT int NOT NULL, STATUS varchar(255) NOT NULL);\n" +
                "CREATE TABLE ORDERITEMS (CATEGORY varchar(255) NOT NULL, NAME varchar(255) NOT NULL, ID varchar(255) NOT NULL, QUANTITY int, PRICE decimal(10,2) NOT NULL, PRIMARY KEY (ID));\n" +
                "CREATE TABLE ORDER_ITEMS (ORDERID INTEGER IDENTITY PRIMARY KEY, ORDERNUMBER int, ITEMID varchar(255) NOT NULL, PRICE decimal(10,2) NOT NULL, FOREIGN KEY (ORDERNUMBER) REFERENCES ORDERQUEUE(ORDERNUMBER), FOREIGN KEY (ITEMID) REFERENCES ORDERITEMS(ID));\n" +
                "\n" +
                "INSERT INTO ORDERQUEUE VALUES(NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME1', '123456789', 10, 58.9);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY2', 'NAME2', '345678912', 5, 5.9);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY4', 'NAME3', '567891234', 1, 580.9);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME4', '456789987', 100, 0.99);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY2', 'NAME5', '678912644', 5, 5.9);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY2', 'NAME6', '951235689', 1, 4.96);\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000000, '123456789', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '123456789';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000000, '345678912', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '345678912';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000000, '567891234', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '567891234';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000000, '456789987', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '456789987';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000000, '678912644', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '678912644';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000000, '951235689', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '951235689';\n" +
                "UPDATE ORDERQUEUE SET PRICE = (SELECT SUM(ORDER_ITEMS.PRICE) FROM ORDER_ITEMS WHERE ORDER_ITEMS.ORDERNUMBER = 1000000) WHERE ORDERNUMBER = 1000000;\n" +
                "UPDATE ORDERQUEUE SET ITEMCOUNT = (SELECT COUNT(ORDERNUMBER) FROM ORDER_ITEMS WHERE ORDER_ITEMS.ORDERNUMBER = 1000000) WHERE ORDERNUMBER = 1000000;\n" +
                "\n" +
                "INSERT INTO ORDERQUEUE  VALUES(NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME1', '1', 1, 1);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME2', '2', 2, 2);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME3', '3', 3, 3);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME4', '4', 4, 4);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME5', '5', 5, 5);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME6', '6', 6, 6);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME7', '7', 7, 7);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME8', '8', 8, 8);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME9', '9', 9, 9);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME10', '10', 10, 10);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME11', '11', 11, 11);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME12', '12', 12, 12);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME13', '13', 13, 13);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME14', '14', 14, 14);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME15', '15', 15, 15);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME16', '16', 16, 16);\n" +
                "INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME17', '17', 17, 17); INSERT INTO ORDERITEMS VALUES('CATEGORY1', 'NAME18', '18', 18, 18);\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '1' , QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '1';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '2', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '2';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '3', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '3';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '4', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '4';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '5', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '5';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '6', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '6';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '7', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '7';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '8', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '8';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '9', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '9';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '10', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '10';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '11', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '11';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '12', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '12';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '13', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '13';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '14', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '14';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '15', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '15';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '16', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '16';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '17', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '17';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000001, '18', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '18';\n" +
                "UPDATE ORDERQUEUE SET PRICE = (SELECT SUM(ORDER_ITEMS.PRICE) FROM ORDER_ITEMS WHERE ORDER_ITEMS.ORDERNUMBER = 1000001) WHERE ORDERNUMBER = 1000001;\n" +
                "UPDATE ORDERQUEUE SET ITEMCOUNT = (SELECT COUNT(ORDERNUMBER) FROM ORDER_ITEMS WHERE ORDER_ITEMS.ORDERNUMBER = 1000001) WHERE ORDERNUMBER = 1000001;\n" +
                "\n" +
                "INSERT INTO ORDERQUEUE VALUES(NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000002, '123456789', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '123456789';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000002, '345678912', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '345678912';\n" +
                "INSERT INTO ORDER_ITEMS (ORDERNUMBER, ITEMID, PRICE) SELECT 1000002, '567891234', QUANTITY * PRICE FROM ORDERITEMS WHERE ORDERITEMS.ID = '567891234';\n" +
                "UPDATE ORDERQUEUE SET PRICE = (SELECT SUM(ORDER_ITEMS.PRICE) FROM ORDER_ITEMS WHERE ORDER_ITEMS.ORDERNUMBER = 1000002) WHERE ORDERNUMBER = 1000002;\n" +
                "UPDATE ORDERQUEUE SET ITEMCOUNT = (SELECT COUNT(ORDERNUMBER) FROM ORDER_ITEMS WHERE ORDER_ITEMS.ORDERNUMBER = 1000002) WHERE ORDERNUMBER = 1000002;\n" +
                "\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 106.99, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 77.80, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 93.50, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 106.99, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 107.80, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 58.50, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 0, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 79.59, 0, 'NEW');\n" +
                "INSERT INTO ORDERQUEUE VALUES (NULL, 55.80, 0, 'NEW');";

        connection = null;
        statement = null;
        result = 0;
        size = 15;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:orderqueue", "SA", "");
            statement = connection.createStatement();
            result = statement.executeUpdate(sqlQuery);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}