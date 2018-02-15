package comp3350.g16.inventorytracking.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.application.Services;

//------------------------------------------------------------------------------
//  HomeActivity
//      This is the home screen. It contains buttons to go to other screens.
//------------------------------------------------------------------------------
public class HomeActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Services.createDatabaseAccess();
        setContentView(R.layout.activity_home);
    }

    //------------------------------------------------------------------------------
    //  buttonInventoryOnClick
    //      This starts the ItemActivity. It takes it to the inventory screen where
    //      we can see a list of items, and create more.
    //------------------------------------------------------------------------------
    public void buttonInventoryOnClick(View v)
    {
        Intent itemIntent = new Intent(HomeActivity.this, ItemActivity.class);
        HomeActivity.this.startActivity(itemIntent);
    }

    //------------------------------------------------------------------------------
    //  buttonOrdersOnClick
    //      This starts the OrderActivity. It takes it to the order list screen
    //      where we can see a list of orders, change their status, and then see
    //      the order details.
    //------------------------------------------------------------------------------
    public void buttonOrdersOnClick(View v)
    {
        Intent orderIntent = new Intent(HomeActivity.this, OrdersActivity.class);
        HomeActivity.this.startActivity(orderIntent);
    }
    
    //------------------------------------------------------------------------------
    //  buttonOrdersOnClick
    //      This starts the OrderActivity. It takes it to the order list screen
    //      where we can see a list of orders, change their status, and then see
    //      the order details.
    //------------------------------------------------------------------------------
    public void buttonCreateOrdersOnClick(View v)
    {
        Intent createOrderIntent = new Intent(HomeActivity.this, CreateOrdersActivity.class);
        HomeActivity.this.startActivity(createOrderIntent);
    }

}