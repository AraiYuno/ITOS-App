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
import comp3350.g16.inventorytracking.business.ItemValidator;
import comp3350.g16.inventorytracking.objects.Item;

//------------------------------------------------------------------------------
//  ItemActivity
//      This displays the list of items in stock. It allows the creation,
//		deletion, and updating of items, with changes reflected in the inventory
//		and the view
//------------------------------------------------------------------------------
public class ItemActivity extends AppCompatActivity {

    private AccessInventory accessInventory;        // gets access to inventory
    private LinkedList<Item> itemList;              // keeps a list of the items to display
    private ArrayAdapter<Item> itemArrayAdapter;
    
    private int selectedItemPosition = -1;
    
    private ListView listView;
    private Button updateButton;
    private Button deleteButton;
    
    // for the search view in the toolbar; either by name or by ID
    private boolean searchByName = false; // default by ID
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        
        accessInventory = new AccessInventory();
        itemList = new LinkedList<>();
        accessInventory.getList(itemList);
        
        // get and set the array adapter for the list view
        itemArrayAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, itemList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView productNameID = (TextView) view.findViewById(android.R.id.text1);
                TextView productPrice = (TextView) view.findViewById(android.R.id.text2);
                
                Item currItem = itemList.get(position);

                // Displays product name and id for an item
                productNameID.setText(currItem.getProductID() + ": " + currItem.getName());
                productPrice.setText("$ "+PresentationFormatter.formatPrice(currItem.getPrice()));

                return view;
            }
        };

        listView = (ListView) findViewById(R.id.listItems);
        listView.setAdapter(itemArrayAdapter);
        
        // buttons & default settings
        updateButton = (Button) findViewById(R.id.buttonItemUpdate);
        deleteButton = (Button) findViewById(R.id.buttonItemDelete);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == selectedItemPosition) {
                    setItemEnabled(position, false);

                    selectedItemPosition = -1;
                    editInputFields(null);
                } else {
                    setItemEnabled(position, true);

                    selectedItemPosition = position;
                    editInputFields(itemArrayAdapter.getItem(position));

                    hideKeyboard(view);
                }
            }
        });
    }
    //=========================================================================================
    //  methods for the search bar
    //=========================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        
        if( searchByName )
            searchView.setQueryHint("Search by Name . . .");
        else
            searchView.setQueryHint("Search by ID . . .");
    
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchString = searchView.getQuery().toString();
                
                if( searchByName )
                    searchByName(searchString);
                else
                    searchByID(searchString);
                
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
                searchView.setQueryHint("Search by ID . . .");
                searchByName = false;
                return true;
            case R.id.menuSearchName:
                searchView.setQueryHint("Search by name . . .");
                searchByName = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void searchByID( String searchID )
    {
        if( searchID.length() > 0 )
        {
            Item itemExists = accessInventory.search(searchID);
        
            if( itemExists != null )
            {
                int position = itemArrayAdapter.getPosition(itemExists);
            
                if( position >= 0 )
                {
                    setItemEnabled(position, true);
                    editInputFields(itemExists);
                }
            }
            else
                Messages.notification(this, "Product ID'" + searchID +"' does not exist.");
        }
        else
            Messages.warning(this, "Enter a product ID!");
    }
    
    private void searchByName( String searchName )
    {
        if( searchName.length() > 0 )
        {
            // search and get a list of items back
            LinkedList<Item> searchList = (LinkedList<Item>) accessInventory.searchByName(searchName);
            
            if( searchList != null )
            {
                setItemEnabled(selectedItemPosition, false);
                selectedItemPosition = -1;
                editInputFields(null);
                
                itemArrayAdapter.clear();
                itemArrayAdapter.addAll(searchList);
            }
            else
                Messages.notification(this, "Product Name '" + searchName +"' does not exist.");
        }
        else
            Messages.warning(this, "Enter a product name!");
    }
    
    
	
	//------------------------------------------------------------------------------
    //  buttonItemCreateOnClick				[ Completed By KYLE 06.27.2017 ]
    //      Takes the text that user has entered and attempts to create an item
    //      to add to the inventory and view list.
    //
    //      ADDITIONAL: The button does not create any new order unless all the empty
    //                   spaces are filled out.
    //------------------------------------------------------------------------------
    public void buttonItemCreateOnClick( View v )
    {
        String fieldsFilledMessage = checkEmptyBoxesBeforeCreate();
        
        if( fieldsFilledMessage == null )
        {
            Item item = createItemFromEditText();
            String result = validateItemData(item);
    
            boolean added;
    
            if ( result == null ) {
                added = accessInventory.addItem(item);
        
                if ( added ) {
                    accessInventory.getList(itemList);
                    itemArrayAdapter.notifyDataSetChanged();
            
                    int pos = itemList.indexOf(item);
                    if ( pos >= 0 ) {
                        ListView listView = (ListView) findViewById(R.id.listItems);
                        listView.setSelection(pos);
                    }

                    hideKeyboard(v);
                }
            } else
                Messages.warning(this, result);
        }
        else
            Messages.warning(this, fieldsFilledMessage);
    }

    //------------------------------------------------------------------------------
    //  buttonItemUpdateOnClick			[ Completed By KYLE 06.27.2017 ]
    //      Takes the text that user has entered and attempts to update the existing
    //      item's information
    //------------------------------------------------------------------------------
    public void buttonItemUpdateOnClick(View v)
    {
        // First we find the item with the productID
        String productID = ((EditText) findViewById(R.id.editProductID)).getText().toString();
        Item theItem = accessInventory.search( productID );
        
        if( productID.length() > 0 )
        {
            if( theItem != null )
            {
                // update infos if any information has changed && editText info is not empty
                theItem.setQuantity( Integer.parseInt( ( (EditText) findViewById(R.id.editQuantity) ).getText().toString()) );
                theItem.setCategory( ( ((EditText) findViewById(R.id.editCategory) ).getText()).toString() );
                theItem.setName( ( ((EditText) findViewById(R.id.editProductName) ).getText()).toString() );
                theItem.setPrice( Double.parseDouble(( (EditText) findViewById(R.id.editPrice) ).getText().toString()) );
                
                
                accessInventory.getList(itemList);
                itemArrayAdapter.notifyDataSetChanged();

                deselect();
                hideKeyboard(v);
            }
            else
                Messages.warning(this, "You cannot edit product ids!");
        }
        else
            Messages.warning(this, "Select an item!");
    }

    //------------------------------------------------------------------------------
    //  buttonItemDeleteOnClick
    //      Deletes the selected item from the inventory
    //------------------------------------------------------------------------------
    public void buttonItemDeleteOnClick(View v)
    {
        String productID = ((EditText) findViewById(R.id.editProductID)).getText().toString();
    
        if( productID.length() > 0 )
        {
            Item itemExists = accessInventory.search(productID);
            
            if( itemExists != null )
            {
                // // TODO: 2017-07-09 would be nice to have a confirmation dialog
                // remove the item
                itemExists = accessInventory.removeItem(productID);
                itemArrayAdapter.remove(itemExists);
    
                if( accessInventory.size() == 0 )
                {
                    // final item deleted, enable nothing
                    editInputFields(null);
                    setItemEnabled(-1, false);
                    selectedItemPosition = -1;
                }
                else
                {
                    // deleting the item in the final position requires the previous item to be selected
                    if(selectedItemPosition == accessInventory.size() )
                        selectedItemPosition -= 1;
    
                    setItemEnabled(selectedItemPosition, true);
    
                    Item newlySelected = itemArrayAdapter.getItem(selectedItemPosition);
                    editInputFields(newlySelected);

                    hideKeyboard(v);
                }
            }
            else
                Messages.warning(this, "Item '" + productID + "' not found.");
        }
        else
            Messages.warning(this, "Select an item!");
    }
    
    //------------------------------------------------------------------------------
    //  buttonClearFieldsOnClick
    //      Clears the fields of the product information, and deselects the item
    //      It will reset the full item list
    //------------------------------------------------------------------------------
    public void buttonClearFieldsOnClick(View v)
    {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setIconified(true);
        
        editInputFields(null);
        hideKeyboard(v);
        setItemEnabled(selectedItemPosition, false);

        selectedItemPosition = -1;
        itemArrayAdapter.clear();
        accessInventory.getList(itemList);
        
        itemArrayAdapter.addAll(itemList);
        itemArrayAdapter.notifyDataSetChanged();
    }
    
    
    //------------------------------------------------------------------------------
    //  editInputFields
    //      Edits the input fields to display the information of the selected item
    //      and clears them if passed null
    //------------------------------------------------------------------------------
    private void editInputFields( Item selected )
    {
        TextView textProductID = (TextView) findViewById( R.id.editProductID );
        EditText editName = (EditText) findViewById(R.id.editProductName);
        EditText editCategory = (EditText) findViewById(R.id.editCategory);
        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        EditText editQuantity = (EditText) findViewById(R.id.editQuantity);
    
        if( selected != null )
        {
            textProductID.setText( selected.getProductID() );
            editName.setText(selected.getName());
            editCategory.setText(selected.getCategory());
            editPrice.setText(PresentationFormatter.formatPrice(selected.getPrice()));
            editQuantity.setText(selected.getQuantity()+"");
        }
        else
        {
            textProductID.setText("");
            editName.setText("");
            editCategory.setText("");
            editPrice.setText("");
            editQuantity.setText("");
        }

        deselect();
    }


    private void deselect()
    {
        ((EditText)findViewById(R.id.editProductID)).clearFocus();
        ((EditText)findViewById(R.id.editProductName)).clearFocus();
        ((EditText)findViewById(R.id.editCategory)).clearFocus();
        ((EditText)findViewById(R.id.editPrice)).clearFocus();
        ((EditText)findViewById(R.id.editQuantity)).clearFocus();
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
    //  setItemEnabled
    //      Sets an item at a certain position to be checked based on the passed
    //      boolean. This corresponds to whether the update/delete buttons should be
    //      enabled too. 
    //------------------------------------------------------------------------------
    private void setItemEnabled( int position, boolean isEnabled )
    {
        deleteButton.setEnabled(isEnabled);
        updateButton.setEnabled(isEnabled);
        
        if( position >= 0 )
        {
            listView.setItemChecked(position, isEnabled );
            listView.smoothScrollToPosition(position);
        }
        
        if( isEnabled )
            selectedItemPosition = position;
    }
    
    //------------------------------------------------------------------------------
    //  createItemFromEditText
    //      creates an item from the text provided in the input fields
    //------------------------------------------------------------------------------
    private Item createItemFromEditText()
    {
        String category =  ( (EditText) findViewById(R.id.editCategory) ).getText().toString();
        String name =  ( (EditText) findViewById(R.id.editProductName) ).getText().toString();
        String productID =  ( (EditText) findViewById(R.id.editProductID) ).getText().toString().toLowerCase();
        String priceStr = ( (EditText) findViewById(R.id.editPrice) ).getText().toString();
        String quantityStr = ( (EditText) findViewById(R.id.editQuantity) ).getText().toString();
    
        double price = Double.parseDouble(priceStr);
        int quantity = Integer.parseInt(quantityStr);
        
        return  new Item(category, name, productID, quantity, price);
    }
    
    //------------------------------------------------------------------------------
    //  checkEmptyBoxesBeforeCreate			[ Completed By KYLE 06.27.2017 ]
    //      Checks if there's any empty slot before crating order.  [ KYLE ]
    //------------------------------------------------------------------------------
    private String checkEmptyBoxesBeforeCreate()
    {
        String fieldsEmpty = "Please fill out all the empty spaces to create a new item.";
        
        if( ( (EditText) findViewById(R.id.editProductID) ).getText().toString().length() > 0
                &&  ( (EditText) findViewById(R.id.editCategory) ).getText().toString().length() > 0
                &&  ( (EditText) findViewById(R.id.editProductName) ).getText().toString().length() > 0
                &&  ( ( (EditText) findViewById(R.id.editPrice) ).getText().toString().length() > 0
                &&  ( (EditText) findViewById(R.id.editQuantity) ).getText().toString().length() > 0 ))
        {
            fieldsEmpty = null;
        }
        
        return fieldsEmpty;
    }
    
    private String validateItemData(Item item)
    {
        /*
        *** This is already kind of in checkEmptyBoxesBeforeCreate method ***
        
        if( !ItemValidator.validateID(item))
            return "Product id required.";
        
        if( !ItemValidator.validateName(item))
            return "Product name required.";
        
        if ( !ItemValidator.validateCategory(item) )
            return "Category required.";
        
        if( !ItemValidator.validatePrice(item))
            return "Item price must be greater than or equal to 0.";
        
        if( !ItemValidator.validateQuantity(item) )
            return "Item quantity must be greater than or equal to 0.";
            */
        
        if( ItemValidator.itemExists(item) )
            return "Product ID " + item.getProductID() + " already Exists!";
        
        return null;
    }
	
}
