package comp3350.g16.inventorytracking.acceptance;

import junit.framework.Assert;

import com.robotium.solo.Solo;

import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.presentation.HomeActivity;
import android.test.ActivityInstrumentationTestCase2;

//------------------------------------------------------------------------
//========================================================================
//ItemsTest class                           [ Completed by Kyle. July 17, 2017 ]
// Acceptance testing for Inventory page
//
//========================================================================
public class ItemsTest extends ActivityInstrumentationTestCase2<HomeActivity>
{
	private Solo solo;
	
	public ItemsTest()
	{
		super(HomeActivity.class);
	}
	
	public void setUp() throws Exception
	{
		solo = new Solo(getInstrumentation(), getActivity());
	}

	//------------------------------------------------------------------------------
	//  testCreateItem    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and
	// 		1. creates an item in the inventory
	//------------------------------------------------------------------------------
	public void testCreateItem()
	{

		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		// to write all the fields required to create an item.
		solo.clearEditText(0);
		solo.enterText(0,"12341231" );
		solo.clearEditText(1);
		solo.enterText(1, "Europa Universalis XI");
		solo.clearEditText(2);
		solo.enterText(2, "Strategy Game");
		solo.clearEditText(3);
		solo.enterText(3, "49.99");
		solo.clearEditText(4);
		solo.enterText(4, "10");
		solo.clickOnButton("Create");
		Assert.assertTrue(solo.searchText("Europa Universalis XI"));		// click the created item
		solo.clickOnText("Europa Universalis XI");

		solo.goBack();    //back to the main page.
	}

	//------------------------------------------------------------------------------
	//  testCreateInvalidItems()    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and
	// 		 tries to create an invalid item with some missing fields.
	//------------------------------------------------------------------------------
	public void testCreateInvalidItems()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		// create a duplicate item
		solo.clickOnText("Grand Theft Auto V");
		solo.clickOnButton("Create");
		solo.clickOnScreen(200,400);

		// leave product ID empty.
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(1, "Hearts of Iron IV");
		solo.clearEditText(2);
		solo.enterText(2, "Strategy Game");
		solo.clearEditText(3);
		solo.enterText(3, "21.99");
		solo.clearEditText(4);
		solo.enterText(4, "14");
		solo.clickOnButton("Create");
		solo.clickOnScreen(200,400);


		// leave product name empty.
		solo.clearEditText(0);
		solo.enterText(0,"00100110" );
		solo.clearEditText(1);
		solo.clickOnButton("Create");
		solo.clickOnScreen(200,400);

		// leave the category empty.
		solo.clearEditText(1);
		solo.enterText(1, "Hearts of Iron IV");
		solo.clearEditText(2);
		solo.clickOnButton("Create");
		solo.clickOnScreen(200,400);

		// leave the price empty.
		solo.clearEditText(2);
		solo.enterText(2, "Strategy Game");
		solo.clearEditText(3);
		solo.clickOnButton("Create");
		solo.clickOnScreen(200,400);

		// leave the quantity empty.
		solo.clearEditText(3);
		solo.enterText(3, "49.99");
		solo.clearEditText(4);
		solo.clickOnButton("Create");
		solo.clickOnScreen(200,400);


		solo.goBack();
	}

	//------------------------------------------------------------------------------
	//  testUpdateItem    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and
	// 		 tries to update an item.
	//------------------------------------------------------------------------------
	public void testUpdateItems()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		// Now we search for an item and update the item's information.
		Assert.assertTrue(solo.searchText("Mass Effect"));		// Search for Mass Effect
		solo.clickOnText("Mass Effect");							// and edit the price and quantity.
		solo.clearEditText(1);
		solo.enterText(1, "Franklin The Best Professor");
		solo.clearEditText(2);
		solo.enterText(2, "Professors");
		solo.clearEditText(3);
		solo.enterText(3, "10.99");
		solo.clearEditText(4);
		solo.enterText(4, "400");
		solo.clickOnButton("Update");

		solo.goBack();							// back to the main page.
	}

	//------------------------------------------------------------------------------
	//  testUpdateInvalidItem    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and
	// 		 tries to update an item with
	//			1. an invalid productID.
	//			2. a missing field.
	//------------------------------------------------------------------------------
	public void testUpdateInvalidItem()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		// We update the product ID, which should be prohibited.
		Assert.assertTrue(solo.searchText("Overpriced Textbook"));
		solo.clickOnText("Overpriced Textbook");
		solo.clearEditText(0);
		solo.enterText(0, "12312319");
		solo.clickOnButton("Update");
		solo.clickOnScreen(200,400);

		// try updating with some fields missing.
		Assert.assertTrue(solo.searchText("Stellaris"));
		solo.clickOnText("Stellaris");
		solo.clearEditText(2);
		solo.clickOnButton("Update");
		solo.clickOnScreen(200,400);

		solo.goBack();
	}



	//------------------------------------------------------------------------------
	//  testDeleteItem    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and
	// 		3. deletes a few selected items.
	//------------------------------------------------------------------------------
	public void testDeleteItem()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		Assert.assertTrue(solo.searchText("01380038:"));		// We delete the first 3 items in the inventory
		solo.clickOnText("01380038:");
		solo.clickOnButton("Delete");

		Assert.assertTrue(solo.searchText("13024523:"));		// We delete the first 3 items in the inventory
		solo.clickOnText("13024523:");
		solo.clickOnButton("Delete");

		Assert.assertTrue(solo.searchText("33306682:"));		// We delete the first 3 items in the inventory
		solo.clickOnText("33306682:");
		solo.clickOnButton("Delete");

		solo.goBack();    // back to the main page
	}

	//------------------------------------------------------------------------------
	//  testInvalidDeleteItem()    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and;
	// 			1. tries to delete a non existing item.
	//			2. tries to delete with a missing product ID.
	//------------------------------------------------------------------------------
	public void testInvalidDeleteItem()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		solo.clickOnButton("Reset");			// unselect any items selected.
		solo.clickOnButton("Delete");			// test if any item is deleted.

		// try to delete non-existing item
		solo.clearEditText(0);
		solo.enterText(0,"00102312131" );
		solo.clearEditText(1);
		solo.enterText(1, "Invalid Item");
		solo.clearEditText(2);
		solo.enterText(2, "Strategy Game");
		solo.clearEditText(3);
		solo.enterText(3, "49.99");
		solo.clearEditText(4);
		solo.clickOnButton("Delete");
		solo.clickOnScreen(200,400);

		// try to delete with a missing productID.
		Assert.assertTrue(solo.searchText("Stellaris"));
		solo.clickOnText("Stellaris");
		solo.clearEditText(0);
		solo.clickOnButton("Delete");
		solo.clickOnScreen(200,400);

		solo.goBack();
	}

	//------------------------------------------------------------------------------
	//  testResetItem    				[ Completed By KYLE 07.17.2017 ]
	//      Automatically clicks inventory button and
	// 		4. clears out the item information fields
	//------------------------------------------------------------------------------
	public void testResetItem()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		Assert.assertTrue(solo.searchText("36025341:"));		// select an item
		solo.clickOnText("36025341:");
		solo.clickOnButton("Reset");							// clears out all the fields.

		solo.goBack();			// back to the main page
	}

	public void testSearchItem()
	{
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Inventory");		// going to the inventory by clicking inventory button
		solo.assertCurrentActivity("Expected activity ItemActivity", "ItemActivity");

		solo.clickOnActionBarItem(R.id.menu_search);
		solo.enterText(0,"War and Peace");

		solo.goBack();
	}
}

