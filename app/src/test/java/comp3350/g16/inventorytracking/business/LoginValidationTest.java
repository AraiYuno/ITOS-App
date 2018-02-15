package comp3350.g16.inventorytracking.tests.business;

/**
 * Created by vaibhavarora on 2017-06-29.
 */


import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

import comp3350.g16.inventorytracking.business.LoginValidation;
import comp3350.g16.inventorytracking.objects.User;


public class LoginValidationTest {

    User myUser;
    LoginValidation validator;


    @Before
    public void init() throws Exception
    {
        myUser = new User("Manager","1234");
        validator= new LoginValidation(myUser);
    }

    @After
    public void tearDown() throws Exception
    {
        myUser = null;
        validator = null;
    }

    @Test
    public void testValidation() throws Exception
    {
        assertFalse(validator.validation(new User("simran","1234")).compareTo("go")==0);
    }

}
