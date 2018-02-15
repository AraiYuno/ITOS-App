package comp3350.g16.inventorytracking.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.*;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.business.AccessOrders;
import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;

//-----------------------------------------------------------------------------
//  OrderDetailsActivity
//		This displays the details of a specific order. It allows the user to
//	change the quantities of items to order, and to remove items from the order.
//-----------------------------------------------------------------------------
public class OrderDetailsActivity extends AppCompatActivity
{
    private AccessOrders orderAccess;
    private ArrayAdapter<Item> itemArrayAdapter;
    private LinkedList<Item> itemList;

    private int selectedItemPosition = -1;
	
	private Order currentOrder = null;
	private String orderNumber;
	
	private ListView listView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderAccess = new AccessOrders();
		
		// grab stuff from previous activity
		Bundle extras = getIntent().getExtras();
	
		if( extras != null )
        {
			String[] values = extras.getStringArray("ORDER_DETAILS");
			orderNumber = values[0];
			
            currentOrder = orderAccess.search(Integer.parseInt(orderNumber));
            itemList = (LinkedList<Item>)currentOrder.getList();
			
			// display over all order details
            displayOrderDetails();
			displayTotalPrice();

            // display items in a list
            itemArrayAdapter = getArrayAdapter();
			
			listView = (ListView)findViewById(R.id.listOrderItems);
            listView.setAdapter(itemArrayAdapter);
			setOnItemClickListener();
        }
        else
        	Messages.fatalError(this, "Could not get receive order details");
    }
	
	//------------------------------------------------------------------------------
	//  ButtonUpdateQuantityOnClick
	//      Updates the order quantity of a selected item. If the entered quantity
	//		is 0, then the item is removed from the order.
	//-----------------------------------------------------------------------------
    public void buttonUpdateQuantityOnClick(View v)
    {
		if(selectedItemPosition >= 0 )
		{
			Item item = itemList.get( selectedItemPosition );
			
			if( item != null )
			{
				EditText editQuantity = (EditText) findViewById(R.id.editUpdateQuantity);
				int newQuantity = Integer.parseInt( editQuantity.getText().toString() );
				
				if(newQuantity == 0 )
				{
					removeFromOrder();
				}
				else
				{
					currentOrder.changeItemQuantity( item, newQuantity );
					updateListView(item);
				}

				editQuantity.clearFocus();
				hideKeyboard(v);
			}
			else
				Messages.fatalError(this, "Something went wrong when getting the item.");
		}
		else
			Messages.warning(this, "Select an item!");
    }


	//------------------------------------------------------------------------------
	//  hideKeyboard
	//      Helper method to hide keyboard and deselect textfield
	//------------------------------------------------------------------------------
	private void hideKeyboard (View v)
	{
		InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	//------------------------------------------------------------------------------
	//  buttonRemoveFromOrderOnClick
	//      This removes the selected item from the order
	//-----------------------------------------------------------------------------
	public void buttonRemoveFromOrderOnClick(View v)
	{
		if( selectedItemPosition >= 0 )
		{
            removeFromOrder();
            ((EditText)findViewById(R.id.editUpdateQuantity)).setText("");
        }
		else
			Messages.warning(this, "Select an item from the order!");

		((EditText)findViewById(R.id.editUpdateQuantity)).clearFocus();
		hideKeyboard(v);
	}
	
	//------------------------------------------------------------------------------
	//  removeFromOrder
	//      This does the actual removing of the item from the list/view. It is
	//		currently called by two other methods.
	//-----------------------------------------------------------------------------
	private void removeFromOrder()
	{
		String productID = (itemArrayAdapter.getItem(selectedItemPosition)).getProductID();
		Item removedItem = currentOrder.removeOrderItem(productID);
		
		if (removedItem != null )
		{
			itemArrayAdapter.remove(removedItem);
			displayTotalPrice();
			
			// when an order is empty, order should be cancelled and activity finished
			if( currentOrder.getSize() == 0 )
			{
				currentOrder.setOrderStatus(Status.CANCELLED);
				((TextView) findViewById(R.id.textViewStatus)).setText(Status.CANCELLED.toString());
				Messages.notification(this, "Order cancelled.");
			}
		}
		else
			Messages.fatalError(this, "Item not found in the order!");
	}
    
    //------------------------------------------------------------------------------
    //  displayDetailsAtPosition
    //      Edits the input fields to display the information of the selected item
    //------------------------------------------------------------------------------
    private void displayDetailsAtPosition( int position)
    {
        Item selected = itemArrayAdapter.getItem(position);
		EditText editQuantity = (EditText) findViewById(R.id.editUpdateQuantity);
		
		editQuantity.setText(selected.getQuantity() + "");
    }
	
	//------------------------------------------------------------------------------
	//  updateListView
	//      Adjusts the list viewing to reflect any changes
	//------------------------------------------------------------------------------
	private void updateListView( Item updateItem )
	{
		int pos = itemList.indexOf( updateItem );
		
		itemList = (LinkedList<Item>)currentOrder.getList();
		itemArrayAdapter.notifyDataSetChanged();
		
		if ( pos >= 0 ) {
			ListView listView = (ListView) findViewById( R.id.listOrderItems );
			listView.setSelection( pos );
			
			displayTotalPrice();
		}
	}
	
	//------------------------------------------------------------------------------
	//  displayOrderDetails
	//      This displays the orderNumber and Order Status of the current order
	//-----------------------------------------------------------------------------
	private void displayOrderDetails()
	{
		String orderNumberDisplay = getText(R.string.OrderNumberDisplay).toString() + orderNumber;
		String orderedDate = PresentationFormatter.formatDate( currentOrder.getOrderDate() );
		String arrivalDate = PresentationFormatter.formatDate( currentOrder.getArrivalDate() );
		
		(( TextView ) findViewById(R.id.textViewOrderNo)).setText(orderNumberDisplay);
		(( TextView ) findViewById(R.id.textViewStatus)).setText(currentOrder.getStatus().toString());
		(( TextView ) findViewById(R.id.textArrivalDate)).setText(arrivalDate);
		(( TextView ) findViewById(R.id.textOrderDate)).setText(orderedDate);
	}
	
	//------------------------------------------------------------------------------
	//  displayTotalPrice
	//		This updates the viewing for the order's current total price.
	//-----------------------------------------------------------------------------
	private void displayTotalPrice()
	{
		String orderPrice = PresentationFormatter.formatPrice( currentOrder.getOrderPrice() );
		String priceDisplay = "$ " + orderPrice;
		
		(( TextView ) findViewById(R.id.textViewOrderPrice)).setText(priceDisplay);
	}
	
	// these two methods are here so that they are out of the onCreate() method
	private ArrayAdapter<Item> getArrayAdapter()
	{
		return new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, itemList)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				
				TextView itemName = (TextView) view.findViewById(android.R.id.text1);
				TextView itemDetails = (TextView) view.findViewById(android.R.id.text2);
				
				Item currentItem = itemList.get(position);
				String itemTotalPrice = PresentationFormatter.formatPrice(currentItem.getQuantity()*currentItem.getPrice());
				
				//	displays information for current item
				itemName.setText( currentItem.getName() );
				itemDetails.setText( currentItem.getProductID() + "\t\t\t$ "
						+ PresentationFormatter.formatPrice(currentItem.getPrice())
						+ "\t\t\tQuantity: " + currentItem.getQuantity()
						+ "\t\t\t("+itemTotalPrice+")");
				
				return view;
			}
		};
	}
	
	private void setOnItemClickListener()
	{
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Button updateButton = (Button)findViewById(R.id.buttonUpdateQuantity);
				
				if (position == selectedItemPosition) {
					listView.setItemChecked(position, false);
					updateButton.setEnabled(false);
					selectedItemPosition = -1;
				} else {
					listView.setItemChecked(position, true);
					updateButton.setEnabled(true);
					selectedItemPosition = position;
					
					displayDetailsAtPosition(position);
				}
			}
		});
	}
}
