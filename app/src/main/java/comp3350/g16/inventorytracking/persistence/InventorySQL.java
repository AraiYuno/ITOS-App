package comp3350.g16.inventorytracking.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import comp3350.g16.inventorytracking.objects.Item;

public class InventorySQL implements InventoryInterface {

    private Connection connection;
    private Statement statement;
    private int result;
    private int size;

    public InventorySQL() {
        initialize();
    }

    public boolean addItem(Item item) {
        boolean added = false;
        connection = null;
        statement = null;
        result = 0;
        if (item != null) {
            if (search(item.getProductID()) == null) {
                try {
                    Class.forName("org.hsqldb.jdbcDriver");
                    connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
                    statement = connection.createStatement();
                    result = statement.executeUpdate("INSERT INTO ITEMS VALUES ('" +
                            item.getCategory() + "', '" +
                            item.getName() + "', '" +
                            item.getProductID() + "', " +
                            item.getQuantity() + ", " +
                            item.getPrice() + ");");
                    connection.commit();
                    added = true;
                    size++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return added;
    }

    public Item removeItem (String itemID) {
        Item removed = new Item();
        connection = null;
        statement = null;
        ResultSet resultSet;
        if (itemID != null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM ITEMS WHERE ID = '" + itemID + "'");
                while (resultSet.next()) {
                    removed.setCategory(resultSet.getString("CATEGORY"));
                    removed.setName(resultSet.getString("NAME"));
                    removed.setProductID(resultSet.getString("ID"));
                    removed.setQuantity(resultSet.getInt("QUANTITY"));
                    removed.setPrice(resultSet.getDouble("PRICE"));
                }
                result = statement.executeUpdate("DELETE FROM ITEMS WHERE ID = '" + itemID + "'");
                size--;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return removed;
    }

    public void getList(List<Item> list) {
        connection = null;
        statement = null;
        ResultSet resultSet;
    
        LinkedList<Item> values = new LinkedList<>();
        
        String category, id, name;
        int quantity;
        double price;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ITEMS");
            
            while (resultSet.next()) {
                category = resultSet.getString("CATEGORY");
                name = resultSet.getString("NAME");
                id = resultSet.getString("ID");
                quantity = resultSet.getInt("QUANTITY");
                price = resultSet.getDouble("PRICE");
                
                values.add(new Item(category, name, id, quantity, price));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(values);
     	
		list.clear();
		list.addAll(values);
    }

    public boolean updateItem (String productID, int value){
        boolean updated = false;
        connection = null;
        statement = null;
        result = 0;
        if (productID != null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
                statement = connection.createStatement();
                result = statement.executeUpdate("UPDATE ITEMS SET QUANTITY = " + value + " WHERE ID = '" + productID + "'");
                updated = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updated;
    }
    
    // returns item if found, null if not found
    public Item search (String itemID) {
        Item found = null;
        connection = null;
        statement = null;
        ResultSet resultSet;
        
        String category, name, id;
        double price;
        int quantity;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ITEMS WHERE ID =  '" + itemID + "'");
            boolean hasResult = resultSet.next();
            
            if (hasResult) {
                while (hasResult) {
                    category = resultSet.getString("CATEGORY");
                    name = resultSet.getString("NAME");
                    id = resultSet.getString("ID");
                    quantity = resultSet.getInt("QUANTITY");
                    price = resultSet.getDouble("PRICE");
                    
                    found = new Item(category, name, id, quantity, price);
                    
                    hasResult =  resultSet.next();
                }
            }
            else
                found = null;
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
            connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ITEMS");
            while (resultSet.next()){
                output += resultSet.getString("CATEGORY") + " | " +
                        resultSet.getString("NAME") + " | " +
                        resultSet.getString("ID") + " | " +
                        resultSet.getInt("QUANTITY") + " | " +
                        resultSet.getDouble("PRICE") + "\n";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public int size(){
        return size;
    }
    
    private void initialize() {
        String sqlQuery = "DROP TABLE IF EXISTS ITEMS ;\n" +
                "CREATE TABLE ITEMS (\n" +
                "\tCATEGORY varchar(255) NOT NULL,\n" +
                "\tNAME varchar(255) NOT NULL, \n" +
                "\tID varchar(255) NOT NULL, \n" +
                "\tQUANTITY int, \n" +
                "\tPRICE decimal(10,2) NOT NULL,\n" +
                "\tPRIMARY KEY (ID)\n" +
                ");\n" +
                "INSERT INTO ITEMS VALUES ('Book', 'Catcher', '123-abc', 0, 20.99); \n" +
                "INSERT INTO ITEMS VALUES ('Book', 'In the Rye', '124-abc', 10, 20.99); \n" +
                "INSERT INTO ITEMS VALUES ('Book', 'A Really Long and Pretentious Title Because We R Mart', '777-ooo', 15, 11.99);\n" +
                "INSERT INTO ITEMS VALUES ('Book', 'Jane', '456-efg', 0,10.99);\n" +
                "INSERT INTO ITEMS VALUES ('Book', 'Eyre', '789-hij',19, 8.99); \n" +
                "INSERT INTO ITEMS VALUES ('Book', 'Catcher', '123-acc', 0, 20.99);\n" +
                "INSERT INTO ITEMS VALUES ('Game', 'CK2', '769-ck2', 0, 59.99);\n" +
                "INSERT INTO ITEMS VALUES ('Game', 'Stellaris', '7854-a', 6, 79.99);  \n" +
                "INSERT INTO ITEMS VALUES ('Game', 'MassEffect', '1107-n', 10, 40.99);  \n" +
                "INSERT INTO ITEMS VALUES ('Game', 'Some Other Video Game', '789-qwe', 0, 79.99);  \n" +
                "INSERT INTO ITEMS VALUES ('Game', 'Super Deluxe Edition', '345-asdc', 19, 129.99);  \n" +
                "INSERT INTO ITEMS VALUES ('Game', 'Indie', '465-gjs', 0, 1.99);";

        connection = null;
        statement = null;
        result = 0;
        size = 12;

        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            connection = DriverManager.getConnection("jdbc:hsqldb:file:inventory", "SA", "");
            statement = connection.createStatement();
            result = statement.executeUpdate(sqlQuery);
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public List<Item> searchByName(String name)
    {
        // TODO: 2017-07-13 to implement
        // returns a list containing items with names matching the String given
        // this will return null if the search list is empty
        
        return null;
    }
}
