package iss.workshop.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;

import iss.workshop.adproject.Model.BlogUser;
import retrofit2.Call;
import iss.workshop.adproject.DataService.UserDataService;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    Button loginBtn;
    Button signUpBtn;
    EditText userNameText;
    EditText passwordText;
    private TextInputLayout usernameLayout, passwordLayout;
    private TextInputEditText usernameEditText, passwordEditText;
    private UserDataService uDService;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        loginBtn = findViewById(R.id.buttonLogin);
        signUpBtn = findViewById(R.id.buttonSignUp);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.249.155.87:8080/") // 替换为您的API的基础URL,必须以斜杠结尾
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uDService = retrofit.create(UserDataService.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName =  usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                SharedPreferences.Editor editor = pref.edit();
                editor.commit();
                ifExist(userName, password);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inSignUp();
            }
        });
    }



    public void ifExist(String userName, String password) {
        Call<List<BlogUser>> call = uDService.findAllActiveUser();
        call.enqueue(new Callback<List<BlogUser>>() {
            @Override
            public void onResponse(Call<List<BlogUser>> call, Response<List<BlogUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BlogUser> users = response.body();
                    for (BlogUser user : users) {
                        if (user.getDisplayName().equals(userName) && user.getPassword().equals(password)) {
                            id = user.getUserId();
                            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("userId",id);
                            editor.putString("username",user.getDisplayName());
                            editor.commit();//不能少
                            inHomePage();
                            return;
                        } else if (user.getDisplayName().equals(userName)&&!user.getPassword().equals(password)) {
                            passwordLayout.setError("password is wrong");
                            return;
                        }
                    }
                    usernameLayout.setError("username is wrong");
                }else {
                    Log.d("LoginRetrofit", "Response Body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BlogUser>> call, Throwable t) {
            }
        });
    }//验证是否存在该用户

    public void inSignUp(){
        Intent intent = new Intent(this,signUp.class);
        startActivity(intent);
    }//进入注册页面

    public void inHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }//进入主界面
}