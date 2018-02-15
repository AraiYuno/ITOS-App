package comp3350.g16.inventorytracking.presentation;

import java.text.DecimalFormat;
import java.util.Calendar;

//-----------------------------------------------------------------------------
//  PresentationFormatter
//      Currently this exists to format the decimal values for presentation
//-----------------------------------------------------------------------------
public class PresentationFormatter
{
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    
    public static String formatPrice( double price )
    {
        return decimalFormat.format(price);
    }
    
    public static String formatDate( Calendar date )
    {
        String dateString = "- no arrival date set -";
    
        if( date != null )
        {
            String[] tokens = (date.getTime().toString()).split(" ");
            dateString = (tokens[1] + " " + tokens[2] + " " + tokens[5]);
        }
        
        return dateString;
    }
}
