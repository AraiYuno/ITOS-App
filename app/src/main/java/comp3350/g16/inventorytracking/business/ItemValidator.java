package comp3350.g16.inventorytracking.business;

import comp3350.g16.inventorytracking.objects.Item;

//-----------------------------------------------------------------------------
//  ItemValidator
//			Validates an item for business and presentation classes.
//-----------------------------------------------------------------------------
public class ItemValidator
{
	private static AccessInventory accessInventory = new AccessInventory();
	
	public static boolean validItem(final Item item)
	{
		return validName(item) && validID(item) && validCategory(item) && validPrice(item) && validQuantity(item);
	}
	
	// true if name is > 0
	public static boolean validName(final Item item) {
		boolean valid = false;
		
		if( item != null )
			valid = (item.getName().length() != 0);
		
		return valid;
	}
	
	public static boolean validID(final Item item ) {
		boolean valid = false;
		
		if( item != null )
			valid = (item.getProductID().length() != 0);
		
		return valid;
	}
	
	// true: item exists
	public static boolean itemExists(Item item) {
		boolean valid = false;
		
		if( item != null )
			valid = (accessInventory.search(item.getProductID() ) != null);
		
		return valid;
	}
	
	public static boolean validCategory(Item item) {
		boolean valid = false;
		
		if( item != null )
			valid = (item.getCategory().length() != 0);
		
		return valid;
	}
	
	public static boolean validPrice(Item item) {
		boolean valid = false;
		
		if( item != null )
			valid = (item.getPrice() >= 0);
		
		return valid;
	}
	
	public static boolean validQuantity(Item item) {
		boolean valid = false;
		
		if( item != null )
			valid = (item.getQuantity() >= 0);
		
		return  valid;
	}
}
