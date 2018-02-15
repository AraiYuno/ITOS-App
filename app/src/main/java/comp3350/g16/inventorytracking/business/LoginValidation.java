package comp3350.g16.inventorytracking.business;

//-----------------------------------------------------------------------------
//  LoginValidation
//      Checks for validity of username and password
//-----------------------------------------------------------------------------

import comp3350.g16.inventorytracking.objects.User;

public class LoginValidation {

    String username;
    String password;
    User toValidate;

    public LoginValidation(User toValidate) {
        this.toValidate = toValidate;
    }
    
    //-----------------------------------------------------------------------------
    //  validation method
    //      Checks if user is valid
    //      Dummy accounts of Manager and Accountant both with passwords "1234"
    //      Returns the username to determine what kind of account is being used
    //-----------------------------------------------------------------------------
    public String validation(User userToCheck){
        username = userToCheck.getUsername();
        password = userToCheck.getPassword();
        String result;

        if (username.equalsIgnoreCase("Manager") || username.equalsIgnoreCase("Accountant")){
            if (password.equals("1234")){
                result = username;
            }
            else {
                result = "Wrong password.";
            }
        }
        else {
            result = "Invalid account.";
        }
        return result;
    }

}
