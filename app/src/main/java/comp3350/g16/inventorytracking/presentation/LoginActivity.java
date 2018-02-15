package comp3350.g16.inventorytracking.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import comp3350.g16.inventorytracking.R;
import comp3350.g16.inventorytracking.application.Services;
import comp3350.g16.inventorytracking.business.LoginValidation;
import comp3350.g16.inventorytracking.objects.User;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	
		// Starts up the database stub.
		Services.createDatabaseAccess();
		
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.textUser);
        password = (EditText)findViewById(R.id.textPassword);
        loginButton = (Button)findViewById(R.id.bLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pw = password.getText().toString();
                User user = new User(name, pw);
                LoginValidation check = new LoginValidation(user);
                String userAccount = check.validation(user); // checks if user entered is valid
    
                if (userAccount.equalsIgnoreCase("manager")){
                    Intent managerIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.startActivity(managerIntent);
                }

                else if (userAccount.equalsIgnoreCase("accountant")) {
                    Intent accountsIntent = new Intent(LoginActivity.this, OrdersActivity.class);
                    LoginActivity.this.startActivity(accountsIntent);
                }

                else{
                    Toast.makeText(LoginActivity.this, userAccount, Toast.LENGTH_SHORT).show();
                }
                //*/
                
            }
        });
        
    }
}
