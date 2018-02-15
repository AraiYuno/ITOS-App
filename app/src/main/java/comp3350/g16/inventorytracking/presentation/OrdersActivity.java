package comp3350.g16.inventorytracking.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.business.AccessOrders;
import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;

//-----------------------------------------------------------------------------
//  OrdersActivity
//      This manages the displaying of the list of orders. A user can change
//		the status of an order with the appropriate buttons. A user can also
//		view the details of a specific order.
//-----------------------------------------------------------------------------
public class OrdersActivity extends Activity
{
	private AccessOrders accessOrder;
	private LinkedList<Order> orderList;
	
	private ArrayAdapter<Order> orderArrayAdapter;
	private ListView listView;
	
	private int selectedOrderPosition = -1;
	
	// various buttons user can click
	private Button viewNonCancelledButton;
	private Button viewAllOrdersButton;
	private Button approveButton;
	private Button rejectButton;
	private Button cancelButton;
	private Button detailsButton;
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_orders );
		
		// quick toolbar fix
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.title_activity_orders);
		
		accessOrder = new AccessOrders();
		orderList = new LinkedList<Order>();
		
		accessOrder.getList(orderList);
		
		// list view of orders
		orderArrayAdapter = new ArrayAdapter<Order>( this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, orderList )
		{
			@Override
			public View getView( int position, View convertView, ViewGroup parent )
			{
				View view = super.getView( position, convertView, parent );
				
				Order currentOrder = orderList.get( position );
				displayOrderDetails( currentOrder, view );
				
				return view;
			}
		};
		
		listView = (ListView) findViewById( R.id.listOrders );
		listView.setAdapter( orderArrayAdapter );
		
		listView.setOnItemClickListener( new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id )
			{
				if ( position == selectedOrderPosition ) {
					listView.setItemChecked( position, false );
					setSelectedOrderButtonsEnabled(false);
					selectedOrderPosition = -1;
				} else {
					listView.setItemChecked( position, true );
					setSelectedOrderButtonsEnabled(true);
					selectedOrderPosition = position;
				}
			}
			
		} );
		
		// setting buttons and default disabling
		viewNonCancelledButton = (Button) findViewById( R.id.buttonNonCancelled );
		viewAllOrdersButton = (Button) findViewById( R.id.buttonAllOrders );
		approveButton = (Button) findViewById( R.id.buttonApproveOrder );
		rejectButton = (Button) findViewById( R.id.buttonRejectOrder );
		cancelButton = (Button) findViewById( R.id.buttonCancelOrder );
		detailsButton = (Button) findViewById( R.id.buttonOrderDetails );
		
		viewAllOrdersButton.setEnabled(false);
		setSelectedOrderButtonsEnabled(false);
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		accessOrder.getList(orderList);
		orderArrayAdapter.notifyDataSetChanged();
	}
	
	// TODO: 2017-07-17 If there is time; fix the menu so that it may have a sort function
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu_search.menu_search, menu_search);
		
		// Inflate the options menu_search from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_sort, menu);
		
		return true;
	}
	

	//
	private SortOption sortOption = SortOption.ALL;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuSortAll :
				sortOption = SortOption.ALL;
				viewAllOrders();
				return true;
			case R.id.menuSortApproved :
				sortOption = SortOption.APPROVED;
				return true;
			case R.id.menuSortCancelled :
				sortOption = SortOption.CANCELLED;
				return true;
			case R.id.menuSortNonCancelled :
				sortOption = SortOption.NONCANCELLED;
				viewNonCancelled();
				return true;
			case R.id.menuSortRejected :
				sortOption = SortOption.REJECTED;
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	//*/
	
	//------------------------------------------------------------------------------
	//  setSelectedOrderButtonsEnabled
	//      enables buttons according to passed boolean; for buttons that are
	//		require an order to be selected
	//------------------------------------------------------------------------------
	private void setSelectedOrderButtonsEnabled(boolean isEnabled)
	{
		approveButton.setEnabled(isEnabled);
		rejectButton.setEnabled(isEnabled);
		cancelButton.setEnabled(isEnabled);
		detailsButton.setEnabled(isEnabled);
	}
	
	//------------------------------------------------------------------------------
	//  ButtonOrderApproveOnClick
	//      On a new activity, this change the status of an order to APPROVED.
	//------------------------------------------------------------------------------
	public void buttonOrderApproveOnClick( View v )
	{
		changeOrderStatus(Status.APPROVED);
	}
	
	//------------------------------------------------------------------------------
	//  ButtonOrderRejectOnClick
	//      On a new activity, this change the status of an order to REJECTED.
	//------------------------------------------------------------------------------
	public void buttonOrderRejectOnClick( View v )
	{
		changeOrderStatus(Status.REJECTED);
	}
	
	//------------------------------------------------------------------------------
	//  ButtonOrderCancelOnClick
	//      On a new activity, this change the status of an order to CANCELLED.
	//------------------------------------------------------------------------------
	public void buttonOrderCancelOnClick( View v )
	{
		changeOrderStatus(Status.CANCELLED);
	}
	
	//------------------------------------------------------------------------------
	//  changeOrderStatus
	//      Changes the status of a selected order based on passed Status parameter
	//------------------------------------------------------------------------------
	private void changeOrderStatus( Status newStatus )
	{
		if( selectedOrderPosition >= 0 )
		{
			Order order = orderList.get( selectedOrderPosition );
			
			if ( order != null ) {
				order.setOrderStatus(newStatus);
				
				orderArrayAdapter.notifyDataSetChanged();
				
				// todo if we are viewing all non-cancelled and the newStatus is "CANCELLED",
				// this order will need to be hidden from the list
			}
			else
				Messages.fatalError(this, "Something went wrong when getting the order.");
		}
		else
			Messages.warning(this, "Select an order!");
	}
	
	//------------------------------------------------------------------------------
	//  ButtonDetailsOnClick
	//      On a new activity, this will display the order details
	//------------------------------------------------------------------------------
	public void buttonDetailsOnClick( View v )
	{
		Intent orderDetailsIntent = new Intent( OrdersActivity.this, OrderDetailsActivity.class );
		
		Order currentOrder;
		String[] putExtras = new String[1];
		
		if ( selectedOrderPosition >= 0 ) {
			currentOrder = orderList.get( selectedOrderPosition );
			
			if( currentOrder != null )
			{
				putExtras[0] = "" + currentOrder.getOrderNumber();
				orderDetailsIntent.putExtra( "ORDER_DETAILS", putExtras );
				OrdersActivity.this.startActivity( orderDetailsIntent );
			}
			else
				Messages.fatalError(this, "Something went wrong when getting the order.");
		}
		else
			Messages.warning(this, "Select an order!");
	}
	
	public void buttonNonCancelledOnClick(View v)
	{
		viewNonCancelled();
	}
	private void viewNonCancelled()
	{
		orderList = (LinkedList<Order>) accessOrder.getStatusOrdersWithout(Status.CANCELLED );
		
		orderArrayAdapter.clear();
		orderArrayAdapter.addAll(orderList);
		orderArrayAdapter.notifyDataSetChanged();
		
		viewNonCancelledButton.setEnabled(false);
		viewAllOrdersButton.setEnabled(true);
	}
	
	public void buttonViewAllOrders( View v )
	{
		viewAllOrders();
	}
	
	private void viewAllOrders()
	{
		accessOrder.getList(orderList);
		
		orderArrayAdapter.clear();
		orderArrayAdapter.addAll(orderList);
		orderArrayAdapter.notifyDataSetChanged();
		
		viewNonCancelledButton.setEnabled(true);
		viewAllOrdersButton.setEnabled(false);
	}
	
	//------------------------------------------------------------------------------
	//  displayOrderDetails
	//      Displays the details of a current order for a current view.
	//------------------------------------------------------------------------------
	private void displayOrderDetails( Order currentOrder, View view )
	{
		TextView orderNumber = (TextView) view.findViewById( android.R.id.text1 );
		TextView orderPrice = (TextView) view.findViewById( android.R.id.text2 );
		
		orderNumber.setText( "Order No. " + currentOrder.getOrderNumber() + "" );
		orderPrice.setText( "$ " + PresentationFormatter.formatPrice( currentOrder.getOrderPrice() )
				+ "\t" + currentOrder.getStatus() );
	}
	
	private enum SortOption
	{
		ALL, APPROVED, REJECTED, CANCELLED, NONCANCELLED;
	}
	
	
}
