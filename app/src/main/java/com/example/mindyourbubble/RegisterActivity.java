package com.example.mindyourbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mindyourbubble.Utils.Keys;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirm;
    private EditText fullname;
    private EditText postcode;
    private TextView error;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        username = findViewById( R.id.UsernameRegister );
        password = findViewById( R.id.PasswordRegister );
        confirm = findViewById( R.id.ConfirmPasswordRegister );
        fullname = findViewById( R.id.FullNameRegister );
        postcode = findViewById( R.id.PostcodeRegister );
        error = findViewById( R.id.registererror );
    }

    public void switchRegister( View view ) {
        if (password.getText().toString().equals(confirm.getText().toString())) {
            error.setVisibility( View.INVISIBLE );
            Intent intent = new Intent( this, ProfileActivity.class );
            intent.putExtra(Keys.FULLNAME, fullname.getText().toString());
            intent.putExtra(Keys.USERNAME, username.getText().toString());
            intent.putExtra(Keys.PASSWORD, password.getText().toString());
            intent.putExtra(Keys.POSTCODE, postcode.getText().toString());
            intent.putExtra( Keys.ACTIVITY, Keys.REGISTER);
            startActivity( intent );
        } else {
            error.setVisibility( View.VISIBLE );
        }


    }

    public void switchLogin( View view ) {
        Intent intent = new Intent( this, MainActivity.class );
        startActivity( intent );
    }
}