package comp3350.g16.inventorytracking.acceptance;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;

import comp3350.g16.inventorytracking.presentation.HomeActivity;

/**
 * Created by Owner on 7/18/2017.
 */

//------------------------------------------------------------------------
//========================================================================
//NewOerdersTest class                           [ Completed by Kyle. July 17, 2017 ]
// Acceptance testing for New Orders page
//
//========================================================================
public class NewOrdersTest extends ActivityInstrumentationTestCase2<HomeActivity>
{
    private Solo solo;

    public NewOrdersTest()
    {
        super(HomeActivity.class);
    }

    public void setUp() throws Exception
    {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    //------------------------------------------------------------------------------
    //  testPlaceOrder()    				[ Completed By KYLE 07.17.2017 ]
    //      automatically adds items to an order and saves the order.
    //------------------------------------------------------------------------------
    public void testPlaceOrder()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("New Order");		// going to the "NEW ORDERS" page.
        solo.assertCurrentActivity("Expected activity CreateORdersActivity", "CreateOrdersActivity");

        Assert.assertTrue(solo.searchText("72460260"));		// selects an item on the inventory list side.
        solo.clickOnText("72460260");

        // edits the quantity and adds it to the order.
        solo.clearEditText(0);
        solo.enterText(0, "222");
        solo.clickOnButton("Add");

        Assert.assertTrue(solo.searchText("82125680"));		// selects an item on the inventory list side.
        solo.clickOnText("82125680");

        // edits the quantity and adds it to the order.
        solo.clearEditText(0);
        solo.enterText(0, "999");
        solo.clickOnButton("Add");

        // re-select the item added earlier to modify the quantity
        solo.clickOnText("82125680",2);
        solo.clearEditText(0);
        solo.enterText(0, "66");
        solo.clickOnButton("Update");

        // saves the order
        solo.clickOnButton("Save Order");

        solo.goBack();
    }

    //------------------------------------------------------------------------------
    //  testInvalidPlaceORder    				[ Completed By KYLE 07.17.2017 ]
    //     tests the invalid cases when placing order
    //      1. tries to add without a quantitiy defined.
    //------------------------------------------------------------------------------
    public void testPlaceOrderInvalid()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("New Order");		// going to the "NEW ORDERS" page.
        solo.assertCurrentActivity("Expected activity CreateORdersActivity", "CreateOrdersActivity");

        Assert.assertTrue(solo.searchText("80883013"));		// selects an item on the inventory list side.
        solo.clickOnText("80883013");

        // clears out the quantity of the items to be added.
        solo.clearEditText(0);
        solo.enterText(0,"0");

        // clicks on add button without having quantity and ignores the pop-up warning afterwards.
        solo.clickOnButton("Add");
        solo.clickOnScreen( 200, 400 );

        solo.goBack();
    }

    //------------------------------------------------------------------------------
    //  testRemoveFromOrder()    				[ Completed By KYLE 07.17.2017 ]
    //     tests if any items can be removed from the order list in New Orders page.
    //------------------------------------------------------------------------------
    public void testRemoveFromOrder()
    {
        solo.waitForActivity("HomeActivity");
        solo.clickOnButton("New Order");		// going to the "NEW ORDERS" page.
        solo.assertCurrentActivity("Expected activity CreateORdersActivity", "CreateOrdersActivity");

        Assert.assertTrue(solo.searchText("72460260"));		// selects an item on the inventory list side.
        solo.clickOnText("72460260");

        // edits the quantity and adds it to the order.
        solo.clearEditText(0);
        solo.enterText(0, "222");
        solo.clickOnButton("Add");

        Assert.assertTrue(solo.searchText("82125680"));		// selects an item on the inventory list side.
        solo.clickOnText("82125680");

        // edits the quantity and adds it to the order.
        solo.clearEditText(0);
        solo.enterText(0, "999");
        solo.clickOnButton("Add");


        // now remove from the order list.
        solo.clickOnText("72460260",2);
        solo.clickOnButton("Remove");

        // lastly checks for the reset button.
        solo.clickOnButton("Reset");

        solo.goBack();
    }

}
