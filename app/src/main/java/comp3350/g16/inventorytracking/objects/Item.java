package comp3350.g16.inventorytracking.objects;

// --------------------------------------------------------------
// Item Class contains attributes relevant to an item,
//		and methods that define how it's compared or updated
//---------------------------------------------------------------
public class Item implements Comparable<Item>
{
	private String id;		// unique id
	private String category;
	private String name;
	private int quantity;
	private double price; 		// unit price

	// --------------------------------------------------------------
	// Item Constructor
	//---------------------------------------------------------------
	public Item( String category, String name, String id, int quantity, double price )
	{
		this.category = category;
		this.name = name;
		this.id = id;
		this.price = price;
		this.quantity = quantity;
	}
	public Item() {

	}

	//------------------------------------------------------------
	// updateQuantity:	takes a positive or negative int, and
	//					updates item quantity accordingly.
	// Returns:			false if value > quantity and doesn't update
	//------------------------------------------------------------
	public boolean updateQuantity(int value)
	{
		boolean valid = ((quantity + value) >= 0);

		if( valid )
			quantity += value;

		return valid;
	}
	
	//------------------------------------------------------------
	// setQuantity:		Instead of adding or subtracting a quantity,
	//					this just changes the items's quantity
	//					to a new value
	//------------------------------------------------------------
	public boolean setQuantity(int newQuantity)
	{
		boolean quantitySet = false;
		
		if( newQuantity >= 0 )
			this.quantity = newQuantity;
		
		return quantitySet;
	}

	//------------------------------------------------------------
	// compareTo:	compares product IDs,
	// Returns:		0 if equal, < 0 if this.Item < cmpItem
	//------------------------------------------------------------
	public int compareTo(Item cmpItem)
	{
		String cmpID = cmpItem.getProductID();
		return id.compareTo( cmpID );
	}// end compareTo

	public String toString()
	{
		return category+" | "+name+" | " +id+ " | " +quantity+ " | $" +price ;
	}
	
	public Item createCopy()
	{
		return new Item(category, name, id, quantity, price);
	}
	
	
	//SETTERS
	public void setProductID( String newID ){ this.id = newID; }
	
	public void setPrice( double newPrice ){ this.price = newPrice; }

	public void setName( String newName ){ this.name = newName; }

	public void setCategory( String newCategory ){ this.category = newCategory; }
	
	//GETTERS

	public String getProductID() {	return id;	}

	public double getPrice() {	return price;	}

	public int getQuantity() {	return quantity;	}

	public String getName() { 	return name;	}

	public String getCategory() {	return category; }

}
