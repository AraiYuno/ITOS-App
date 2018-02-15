README.txt
____________________________________________________________

LOGINS:
	Manager (Password: 1234)
	Accountant (Password: 1234)

The Inventory Tracking and Ordering System (ITOS) will be used to oversee inventory levels of a company’s goods for sale. 

Currently, the system can display the inventory, order list, and order details. It allows the user to edit items in the inventory, orders, and items in a specific order.  

Log.txt exists in the main folder.

====Packages====

comp3350.g16.inventorytracking
	application
		-	Services for starting the database
	business
		-	Classes that specifiy access to inventory or orders
		-	Validation logic
	objects
		-	Inventory item, orders
		-	A User class
	persistence
		-	SQL Databases
		-	Non-Persistence stub databases
		-	Interfaces for these
	presentation
		-	Several Activities for GUI
				-	Home page, login page
				-	View Inventory, Order List, Specific Order Details, create order page
				-	User messages, presentation formatter

==== Changes ====

For this iteration, we have made the following changes:
	-	Our code is actually placed into packages
	-	Entirely revised GUI
			> Before it was a simple view of items added. 


==== Issues ====

>>>Currently we lack unique credentials, and we lack the ability to limit certain functions to certain users (ex. only accountants can change the status of an order, only managers can create/edit items . . . ) 

>>>For now, users need to look and choose from a whole list of items to be able to do what they want with them (add them to orders, edit their properties, etc). For future, we will be adding a search function that allows the user to search for an item, and choose the one they want. 

>>>Builder pattern for objects/Item.java: For now, we have decided not to implement a builder for Item. We had decided on a constructor, but for a future builder pattern, we can make the setting of quantities optional, and getting a copy from the builder rather than a method in Item. 

>>>The process of sending the user messages can be done better; either with the Strings, with the contents of the messages, or how/when they receive them.

>>>Some of the Activities are repetitive. For future, team needs to discuss how we can reduce this. 
