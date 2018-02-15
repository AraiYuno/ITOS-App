package comp3350.g16.inventorytracking.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.g16.inventorytracking.tests.business.LoginValidationTest;
import comp3350.g16.inventorytracking.tests.persistence.InventorySQLTest;
import comp3350.g16.inventorytracking.tests.persistence.InventoryTesting;
import comp3350.g16.inventorytracking.tests.persistence.OrderQueueSQLTest;
import comp3350.g16.inventorytracking.tests.persistence.OrderQueueTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//        InventorySQLTest.class,
        InventoryTesting.class,
        LoginValidationTest.class,
        OrderQueueTest.class,
        OrderQueueSQLTest.class,
})
public class AllTests {
}