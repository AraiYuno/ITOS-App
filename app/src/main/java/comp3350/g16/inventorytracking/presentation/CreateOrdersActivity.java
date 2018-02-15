package comp3350.g16.inventorytracking.presentation;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.business.AccessInventory;
import comp3350.g16.inventorytracking.business.AccessOrders;
import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.objects.Order;

//-----------------------------------------------------------------------------
//  CreateOrdersActivity
//		Manages the function that go into creating orders. The user will be
//		able to select from the inventory list (without worry of making changes
// 		to it), and add a certain quantity to a new order.
//
//		The user must save the order before it is submitted into the Database.
//		Otherwise, it is cleared on exit.
//-----------------------------------------------------------------------------
public class CreateOrdersActivity extends AppCompatActivity
{
	private AccessOrders accessOrders;
	private AccessInventory accessInventory;
	
	private ArrayAdapter<Item> orderArrayAdapter;
	private ArrayAdapter<Item> inventoryArrayAdapter;
	
	private ListView inventoryListView;
	private ListView orderListView;
	
	private LinkedList<Item> orderItemList;
	private LinkedList<Item> inventoryList;
	
	// holds the positions of which item is selected at a time
	private int orderItemSelectedPosition = -1;
	private int inventoryItemSelectedPosition = -1;
	
	// variables regarding the new order
	private boolean orderSaved = false;
	private Order newOrder;
	
	// buttons for various functions
	private Button updateButton;
	private Button removeButton;
	private Button addButton;
	private Button saveButton;
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_create_order );
		
		// display order details
		newOrder = new Order();
//		initialize();
		
		TextView orderNumber = (TextView) findViewById( R.id.textViewOrderList );
		orderNumber.setText( "Order No. " + newOrder.getOrderNumber() );
		updateTotalPrice();
		
		// get access to the inventory and order list
		accessInventory = new AccessInventory();
		accessOrders = new AccessOrders();
		
		// get lists for inventory and this current order list
		inventoryList = new LinkedList<>();
		accessInventory.getList(inventoryList);
		orderItemList = (LinkedList<Item>) newOrder.getList();
		
		// get adapters
		orderArrayAdapter = getItemArrayAdapter(orderItemList);
		inventoryArrayAdapter = getItemArrayAdapter(inventoryList);
		
		// set adapters to ListViews
		inventoryListView = (ListView) findViewById(R.id.listViewInventoryOrderList);
		orderListView = (ListView) findViewById(R.id.listViewCreateOrderList);
		inventoryListView.setAdapter(inventoryArrayAdapter);
		orderListView.setAdapter(orderArrayAdapter);
		
		// set buttons
		updateButton = (Button)findViewById(R.id.buttonUpdateQuantity);
		removeButton = (Button) findViewById(R.id.buttonRemoveFromOrder);
		addButton = (Button)findViewById(R.id.buttonAddToOrder);
		saveButton = (Button) findViewById(R.id.buttonSaveOrder);
		
		// disable buttons for now
		updateButton.setEnabled(false);
		removeButton.setEnabled(false);
		addButton.setEnabled(false);
		saveButton.setEnabled(false);
		
		// set list listeners (stutter)
		listItemListeners(inventoryListView, orderListView, ItemBelongingTo.INVENTORY);
		listItemListeners(orderListView, inventoryListView, ItemBelongingTo.ORDER);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		// If a user creates an order, but doesn't save it, the counter will need be decremented so
		// so that the order number can be re-used for an order that will be saved
		if( !orderSaved )
			Order.decrementCounter();
	}
	
	//------------------------------------------------------------------------------
	//  buttonUpdateQuantityOnClick
	//      Updates the quantity of the selected item to order
	//------------------------------------------------------------------------------
	public void buttonUpdateQuantityOnClick( View v)
	{
		if( orderItemSelectedPosition >= 0 )
		{
			int newQuantity = Integer.parseInt( (((EditText) findViewById(R.id.editOrderItemQuantity)).getText().toString()));
			Item orderItem = orderArrayAdapter.getItem(orderItemSelectedPosition);
			
			if( newQuantity > 0 )
			{
				newOrder.changeItemQuantity(orderItem, newQuantity);
				
				orderItemList = (LinkedList<Item>) newOrder.getList();
				orderArrayAdapter.notifyDataSetChanged();
				
				updateTotalPrice();
			}
			else
				Messages.warning(this, "Quantity should be greater than 0");
		}
		else
			Messages.warning(this, "Select an item from the order!");
	}
	
	//------------------------------------------------------------------------------
	//  buttonRemoveFromOrderOnClick
	//      Removes the selected item from the order
	//------------------------------------------------------------------------------
	public void buttonRemoveFromOrderOnClick(View v)
	{
		if( orderItemSelectedPosition >= 0 )
		{
			String productID = (orderArrayAdapter.getItem(orderItemSelectedPosition)).getProductID();
			Item itemExists = newOrder.search(productID);
			
			
			if (itemExists != null )
			{
				System.out.println(newOrder.getSize());
				newOrder.removeOrderItem(productID);
				orderArrayAdapter.remove(itemExists);
				
				updateTotalPrice();
				checkForEmptyOrder();
			}
			else
				Messages.warning(this, "Item not found in the order!");
		}
		else
			Messages.warning(this, "Select an item from the order!");
	}
	
	//------------------------------------------------------------------------------
	//  buttonAddToOrderOnClick
	//      Adds the selected item to the order
	//------------------------------------------------------------------------------
	public void buttonAddToOrderOnClick(View v)
	{
		if( inventoryItemSelectedPosition >= 0 )
		{
			Item selectedItem = inventoryArrayAdapter.getItem(inventoryItemSelectedPosition);
			
			EditText editOrderItemQuantity = (EditText) findViewById(R.id.editOrderItemQuantity);
			int orderQuantity = Integer.parseInt( editOrderItemQuantity.getText().toString() );
			
			if( orderQuantity > 0 )
			{
				Item orderItem = selectedItem.createCopy();
				orderItem.setQuantity(orderQuantity);
				
				newOrder.placeOrder(orderItem);
				
				orderItemList = (LinkedList<Item>)newOrder.getList();
				orderArrayAdapter.notifyDataSetChanged();
				
				updateTotalPrice();
				checkForEmptyOrder();

				editOrderItemQuantity.setText("0");
				editOrderItemQuantity.clearFocus();
				InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			else
				Messages.warning(this, "Order quantities must be greater than 0!");
		}
		else
			Messages.warning(this, "Select an item from inventory!");
		
		
	}
	
	//------------------------------------------------------------------------------
	//  buttonSaveOrderOnClick
	//      Marks the order as being saved.
	//------------------------------------------------------------------------------
	public void buttonSaveOrderOnClick(View v)
	{
		if( newOrder.getSize() > 0 )
		{
			Order orderFound = accessOrders.search(newOrder.getOrderNumber());
			
			if( orderFound == null )
			{
				accessOrders.addOrder(newOrder);
				
				orderSaved = true;
				Messages.notification(this, "Order saved!");
			}
			else
			{
				// this is so one can save an order that has been saved already without creating
				// duplicate orders
				newOrder.setItems(orderItemList);
			}
		}
		else
			Messages.warning(this, "Order size must be greater than 0!");
		// i like yelling at the user
	}
	
	//------------------------------------------------------------------------------
	//  buttonClearFieldsOnClick
	//      Clears the fields of the product information, and deselects the item
	//      It will reset the full item list
	//------------------------------------------------------------------------------
	public void buttonClearFieldsOnClick(View v)
	{
		SearchView searchView = ((SearchView) findViewById(R.id.menu_search));
		searchView.setQuery("", false);
		searchView.clearFocus();
		searchView.setIconified(true);
		
		setInventoryItemEnabled(inventoryItemSelectedPosition, false);
		updateButton.setEnabled(false);
		removeButton.setEnabled(false);
		
		inventoryArrayAdapter.clear();
		accessInventory.getList(inventoryList);
		inventoryArrayAdapter.addAll(inventoryList);
		inventoryArrayAdapter.notifyDataSetChanged();
	}
	
	private void updateQuantityField(int newQuantity)
	{
		EditText editOrderItemQuantity = (EditText) findViewById(R.id.editOrderItemQuantity);
		editOrderItemQuantity.setText(newQuantity+"");
	}
	private void updateTotalPrice()
	{
		TextView tvOrderPrice = (TextView) findViewById(R.id.textViewOrderPrice);
		tvOrderPrice.setText( PresentationFormatter.formatPrice(newOrder.getOrderPrice()) );
	}
	
	// two different list views, need two different adapters; this method is an attempt to clean things up
	private ArrayAdapter<Item> getItemArrayAdapter( final LinkedList<Item> list )
	{
		ArrayAdapter<Item> itemArrayAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, list)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				
				TextView productNameID = (TextView) view.findViewById(android.R.id.text1);
				TextView productPriceQuantity = (TextView) view.findViewById(android.R.id.text2);
				
				Item currItem = list.get(position);
				
				// Displays product name, id and quantity for an item
				String priceStr = "$ "+PresentationFormatter.formatPrice(currItem.getPrice()) + "\t("+currItem.getQuantity()+")";
				productNameID.setText(currItem.getProductID() + ": " + currItem.getName());
				productPriceQuantity.setText(priceStr + "\t("+currItem.getCategory()+")");
				
				return view;
			}
		};
		
		return itemArrayAdapter;
	}
	
	// this is because we have two different list views that need liseners. It's an attempt to clean things up
	private void listItemListeners(final ListView mainListView, final ListView secondListView, final ItemBelongingTo whichList )
	{
		mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				
				int mainSelected = -1;
				
				if( whichList == ItemBelongingTo.INVENTORY)
				{
					alternateButtonsEnabled(true);
					secondListView.setItemChecked(orderItemSelectedPosition, false);
					
					mainSelected = inventoryItemSelectedPosition;
					orderItemSelectedPosition = -1;
					
					if( inventoryItemSelectedPosition == position )
						inventoryItemSelectedPosition = -1;
					else
					{
						inventoryItemSelectedPosition = position;
						
						// update the product search field
						Item itemSelected = inventoryArrayAdapter.getItem(position);
					}
				}
				else
				{
					alternateButtonsEnabled(false);
					secondListView.setItemChecked(inventoryItemSelectedPosition, false);
					
					mainSelected = orderItemSelectedPosition;
					inventoryItemSelectedPosition = -1;
					
					// updates the quantity field based on the order item selected
					Item selectedItem = orderArrayAdapter.getItem(position);
					updateQuantityField( selectedItem.getQuantity() );
					
					if( orderItemSelectedPosition == position )
						orderItemSelectedPosition = -1;
					else
					{
						orderItemSelectedPosition = position;
						
						// update the product search field
						Item itemSelected = orderArrayAdapter.getItem(position);
					}
				}
				
				if( mainSelected == position )
					mainListView.setItemChecked(position, false);
				else
					mainListView.setItemChecked(position, true);
				
				checkForEmptyOrder();
			}
		});
	}
	
	// used in searching.
	// this will go to the position in the inventory list and "select" it
	// then whatever is selected in order list should be un-selected
	private void setInventoryItemEnabled(int position, boolean isEnabled)
	{
		alternateButtonsEnabled(isEnabled);
		
		if( position >= 0 )
		{
			inventoryListView.setItemChecked(position, isEnabled );
			if( isEnabled )
			{
				inventoryItemSelectedPosition = position;
				inventoryListView.smoothScrollToPosition(position);
				orderListView.setItemChecked(orderItemSelectedPosition, !isEnabled);
				orderItemSelectedPosition = -1;
			}
		}
		
	}
	
	// this will reverse the enabling of the buttons based on which list is being selected
	private void alternateButtonsEnabled( boolean inventoryIsSelected)
	{
		addButton.setEnabled( inventoryIsSelected );
		
		updateButton.setEnabled( !inventoryIsSelected );
		removeButton.setEnabled( !inventoryIsSelected );
	}
	
	// checks if an order is empty, and if it is, disabled certain buttons
	
	private void checkForEmptyOrder()
	{
		if( newOrder.getSize() > 0 )
			saveButton.setEnabled(true);
		else
		{
			saveButton.setEnabled(false);
			removeButton.setEnabled(false);
			updateButton.setEnabled(false);
		}
	}
	
	private enum ItemBelongingTo
	{
		INVENTORY, ORDER
	}
	
	//=========================================================================================
	//  methods for the search bar
	//=========================================================================================
	private boolean searchByName = false;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu_search.menu_search, menu_search);
		
		// Inflate the options menu_search from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_search, menu);
		
		final SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		
		if( searchByName )
			search.setQueryHint("Search by Name . . .");
		else
			search.setQueryHint("Search by ID . . .");
		
		search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				if( searchByName )
					searchByName();
				else
					searchByID();
				
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuSearchID :
//                searchByID();
				((SearchView) findViewById(R.id.menu_search)).setQueryHint("Search by ID . . .");
				searchByName = false;
				return true;
			case R.id.menuSearchName:
//                searchByName();
				((SearchView) findViewById(R.id.menu_search)).setQueryHint("Search by name . . .");
				searchByName = true;
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void searchByID()
	{
		// // TODO: 2017-07-13 search methods need to be updated so that THEY enforce lower case
		String searchID = ((SearchView) findViewById(R.id.menu_search)).getQuery().toString();
		
		if( searchID.length() > 0 )
		{
			Item itemExists = accessInventory.search(searchID);
			
			if( itemExists != null )
			{
				int position = inventoryArrayAdapter.getPosition(itemExists);
				
				if( position >= 0 )
				{
					setInventoryItemEnabled(position, true);
				}
			}
			else
				Messages.warning(this, "Product ID '" + searchID +"' does not exist!");
		}
		else
			Messages.warning(this, "Enter a product ID!");
	}
	private void searchByName()
	{
		String searchName = ((SearchView) findViewById(R.id.menu_search)).getQuery().toString();
		
		if( searchName.length() > 0 )
		{
			// search and get a list of items back
			LinkedList<Item> searchList = (LinkedList<Item>) accessInventory.searchByName(searchName);
			
			if( searchList != null )
			{
				inventoryArrayAdapter.clear();
				inventoryArrayAdapter.addAll(searchList);
			}
		}
		else
			Messages.warning(this, "Enter a product name!");
	}
}
