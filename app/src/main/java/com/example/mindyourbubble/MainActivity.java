package com.example.mindyourbubble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.Utils.Keys;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private BottomNavigationView navBar;
    private EditText username;
    private EditText password;
    private TextView error;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        navBar = findViewById( R.id.bottom_navigation );
        username = findViewById( R.id.UsernameLogin );
        password = findViewById( R.id.PasswordLogin );
        error = findViewById( R.id.errorLogin );
    }

    public void switchLogin( View view ) {
        if ( username.getText().toString().equals( "WeeDavey" ) &&
             password.getText().toString().equals( "testPass" ) ) {

            error.setVisibility( View.INVISIBLE );
            Intent intent = new Intent( this, ProfileActivity.class );
            intent.putExtra( Keys.USERNAME, "WeeDavey");
            intent.putExtra(Keys.ACTIVITY, Keys.LOGIN);
            startActivity( intent );
        } else {
            error.setVisibility( View.VISIBLE );
        }

    }

    public void switchRegister( View view ) {
        Intent intent = new Intent( this, RegisterActivity.class );
        startActivity( intent );
    }
}