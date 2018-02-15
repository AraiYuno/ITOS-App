package comp3350.g16.inventorytracking.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import comp3350.g16.inventorytracking.R;

//------------------------------------------------------------------------------
//  Messages
//      Displays error, warning, and notification messages
//------------------------------------------------------------------------------
public class Messages
{
    // Something horrible and unexpected has gone wrong!
    public static void fatalError(final Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();

        alertDialog.setTitle(owner.getString(R.string.fatalError));
        alertDialog.setMessage(message);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                owner.finish();
            }
        });

        alertDialog.show();
    }
    
    //  A not so friendly warning that the user should pay attention to
    public static void warning(Activity owner, String message) {
        String type = owner.getString(R.string.warning);
        displayDialog(owner, message, type);
    }
    
    //  A friendly little message
    public static void notification( Activity owner, String message) {
        String type = owner.getString(R.string.notification);
        displayDialog(owner, message, type);
    }
    
    // displays the dialog for the user
    private static void displayDialog(Activity owner, String message, String type)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();
    
        alertDialog.setTitle(type);
        alertDialog.setMessage(message);
    
        alertDialog.show();
    }

}
