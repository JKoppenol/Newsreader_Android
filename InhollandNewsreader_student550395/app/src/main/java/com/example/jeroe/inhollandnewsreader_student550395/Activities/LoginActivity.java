package com.example.jeroe.inhollandnewsreader_student550395.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jeroe.inhollandnewsreader_student550395.AuthToken;
import com.example.jeroe.inhollandnewsreader_student550395.R;
import com.example.jeroe.inhollandnewsreader_student550395.Services.UserService;
import com.example.jeroe.inhollandnewsreader_student550395.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.jeroe.inhollandnewsreader_student550395.R.id.login_button;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<AuthToken> {

    private EditText mUsernameText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private Retrofit retrofit;
    private User mUser;

    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        setContentView(R.layout.activity_login);

        mUsernameText = findViewById(R.id.Username);
        mPasswordText = findViewById(R.id.Password);

        mLoginButton = findViewById(login_button);
        mLoginButton.setOnClickListener(this);

        mUserService = retrofit.create(UserService.class);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == login_button) {
            mLoginButton.setEnabled(false);

            mUser = new User();
            mUser.setUsername(mUsernameText.getText().toString());
            mUser.setPassword(mPasswordText.getText().toString());

            mUserService.login(mUser).enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
        if (response.body() != null && response.isSuccessful()) {
            AuthToken result = response.body();

            if(result != null)
            {
                mUser.setAuthToken(result);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.USER, mUser);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("LoggedInUser", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_name", mUser.getUsername());
                editor.putString("authtoken", mUser.getAuthToken().AuthToken);
                editor.commit();

                startActivity(intent);
            }

            else
                mLoginButton.setEnabled(true);
        }

        else
            mLoginButton.setEnabled(true);
    }

    @Override
    public void onFailure(Call<AuthToken> call, Throwable t) {
        mLoginButton.setEnabled(true);
    }
}
