package comp3350.g16.inventorytracking.objects;

//--------------------------------------------------------------------
//	Status:	Indicates the status of an order
//
//			NEW - for new orders
//			APPROVED - order has been approved by accounts
//			REJECTED - order has been rejected by accounts
//			CANCELLED - an approved order was cancelled for some reason
//			COMPLETED - an approved order was delivered
//--------------------------------------------------------------------
public enum Status
{
    NEW, APPROVED, REJECTED, CANCELLED, COMPLETED;

}