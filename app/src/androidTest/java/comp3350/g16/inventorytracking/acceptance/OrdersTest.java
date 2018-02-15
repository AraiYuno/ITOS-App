package comp3350.g16.inventorytracking.acceptance;

import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.presentation.HomeActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;
//------------------------------------------------------------------------
//========================================================================
//OrderTest class                           [ Completed by Kyle. July 17, 2017 ]
// Acceptance testing for Orders page
//
//========================================================================
public class OrdersTest extends ActivityInstrumentationTestCase2<HomeActivity>
{
    private Solo solo;

    public OrdersTest()
    {
        super(HomeActivity.class);
    }

    public void setUp() throws Exception
    {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    //------------------------------------------------------------------------------
    //  testApproveOrder    				[ Completed By KYLE 07.17.2017 ]
    //      Automatically clicks Orders button and approves, rejects and cancels an order.
    //------------------------------------------------------------------------------
    public void testEditStatus()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("Orders");		// going to orders page
        solo.assertCurrentActivity("Expected activity OrdersActivity", "OrdersActivity");

        Assert.assertTrue(solo.searchText("100000"));
        solo.clickOnText("100000");
        solo.clickOnButton("Approve");

        Assert.assertTrue(solo.searchText("100001"));
        solo.clickOnText("100001");
        solo.clickOnButton("Reject");

        Assert.assertTrue(solo.searchText("100002"));
        solo.clickOnText("100002");
        solo.clickOnButton("Cancel");

        solo.goBack();
    }

    //------------------------------------------------------------------------------
    //  testViewValidOrderArrivalDAte()    				[ Completed By KYLE 07.17.2017 ]
    //      automatically clicks Orders button and selects an approved order to view
    //      the calculated arrival date.
    //------------------------------------------------------------------------------
    public void testViewValidOrderArrivalDate()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("Orders");		// going to orders page
        solo.assertCurrentActivity("Expected activity OrdersActivity", "OrdersActivity");

        // we first select an order and approve it in order to view the arrivalTime for an order.
        Assert.assertTrue(solo.searchText("100000"));
        solo.clickOnText("100000");
        solo.clickOnButton("Approve");

        // click on details to view the order details.
        solo.clickOnButton("Details");

        solo.goBack();
    }

    //------------------------------------------------------------------------------
    //  testViewInvalidOrderArrivalDate()   				[ Completed By KYLE 07.17.2017 ]
    //      automatically clicks Orders button and selects a rejected or cancelled order to view
    //      undefined order arrival date.
    //------------------------------------------------------------------------------
    public void testViewInvalidOrderArrivalDate()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("Orders");		// going to orders page
        solo.assertCurrentActivity("Expected activity OrdersActivity", "OrdersActivity");

        // we first select a cancelled order to view that the arrival date is not set up yet.
        Assert.assertTrue(solo.searchText("100002"));
        solo.clickOnText("100002");
        solo.clickOnButton("Details");

        solo.goBack();
    }

    //------------------------------------------------------------------------------
    //  testQUpdateOrderInfo()   				[ Completed By KYLE 07.17.2017 ]
    //      automatically clicks Orders button and selects an approved order to update
    //      quantity information of items in the order.
    //------------------------------------------------------------------------------
    public void testQUpdateOrderInfo()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("Orders");		// going to orders page
        solo.assertCurrentActivity("Expected activity OrdersActivity", "OrdersActivity");
        // select an approved item and go to order details
        Assert.assertTrue(solo.searchText("100000"));
        solo.clickOnText("100000");
        solo.clickOnButton("Details");

        // it updates quantity information for some of the items in the order.
        Assert.assertTrue(solo.searchText("NAME1"));
        solo.clickOnText("NAME1");
        solo.clearEditText(0);
        solo.enterText(0, "5");
        solo.clickOnButton("Update");

        Assert.assertTrue(solo.searchText("NAME1"));
        solo.clickOnText("NAME2");
        solo.clearEditText(0);
        solo.enterText(0, "55");
        solo.clickOnButton("Update");

        solo.goBack();
    }

    //------------------------------------------------------------------------------
    //  testRemoveItemsInOrder()   				[ Completed By KYLE 07.17.2017 ]
    //      automatically clicks Orders button and selects an approved order to remove
    //      items from an order
    //------------------------------------------------------------------------------
    public void testRemoveItemsInOrder()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("Orders");		// going to orders page
        solo.assertCurrentActivity("Expected activity OrdersActivity", "OrdersActivity");

        Assert.assertTrue(solo.searchText("100000"));
        solo.clickOnText("100000");
        solo.clickOnButton("Approve");
        solo.clickOnButton("Details");

        // it removes 3 items from the top.
        Assert.assertTrue(solo.searchText("NAME3"));
        solo.clickOnText("NAME3");
        solo.clickOnButton("Remove Item");

        Assert.assertTrue(solo.searchText("NAME2"));
        solo.clickOnText("NAME2");
        solo.clickOnButton("Remove Item");

        Assert.assertTrue(solo.searchText("NAME1"));
        solo.clickOnText("NAME1");
        solo.clickOnButton("Remove Item");

        solo.goBack();
    }
}
