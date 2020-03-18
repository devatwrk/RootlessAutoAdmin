package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import aara.tech.rootless_auto_admin.repository.model.Admin;
import aara.tech.rootless_auto_admin.repository.model.LoginResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email_et, password_et;
    private TextView tv_signup, forgot_password;
    private Button login_btn, google_login;

    ApiService apiService;
    private Commonhelper commonhelper;

    private void initview() {
        setContentView(R.layout.activity_login);
        email_et = findViewById(R.id.etx_email);
        password_et = findViewById(R.id.etx_password);
        forgot_password = findViewById(R.id.txt_forgot_password);
        login_btn = findViewById(R.id.btn_login);
        google_login = findViewById(R.id.btn_ggl_cust);

        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(this);
    }

    private void onclick(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();

            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , ForgetPassword.class));
            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Still in development ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }

        });
        onclick();
    }

    private void loginClicked() {
        commonhelper.ShowLoader();
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        //Validation
        validationCheck(email, password);

    }

    private void validationCheck(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "All the fields are required", Toast.LENGTH_SHORT).show();
            commonhelper.HideLoader();
        } else if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, "Password must be of at least 6 characters", Toast.LENGTH_SHORT).show();
            commonhelper.HideLoader();
        } else {
//            commonhelper.callintent(LoginActivity.this, MainActivity.class);
            doLogin(email, password);
        }
    }

    private void doLogin(String email, String password) {
        Call<LoginResponse> call = apiService.login(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {

//                    String error;
                    email_et.setText("");
                    password_et.setText("");

                    LoginResponse loginResponse = response.body();
                    if (!loginResponse.isError()) {
                        Admin admin = loginResponse.getAdmin();

                        //Saving LoggedIn Admin's Data into Shared Preference
                        saveUserDataSharedPref(admin);
                        //Intent to mainActivity
                        gotoMainScreen();


                    } else {
                        commonhelper.HideLoader();
                        Toast.makeText(LoginActivity.this, "Credentials are incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    private void gotoMainScreen() {


        // getting boolean
        if (commonhelper.getSharedPreferences("error", null).equals("false")){
            commonhelper.ShowMesseage("Login Successful \n Welcome\n"  + commonhelper.getSharedPreferences("admin_email", null));
            commonhelper.callintent(LoginActivity.this, MainActivity.class);
        }
        commonhelper.HideLoader();
    }

    public void saveUserDataSharedPref(Admin admin) {
        String error = "false";
        try {

            commonhelper.setSharedPreferences("error", error);
            commonhelper.setSharedPreferences("admin_email", admin.getEmail());
            commonhelper.setSharedPreferences("password", admin.getPassword());
            commonhelper.callintent(LoginActivity.this, MainActivity.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfUserLoggedIn() {
        // getting boolean
        try {
            String error = commonhelper.getSharedPreferences("error", null);

            if (error.equals("false")){
                String username = commonhelper.getSharedPreferences("uname", null);
                commonhelper.ShowMesseage("Welcome Back\n" + username);
                commonhelper.callintent(LoginActivity.this, MainActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (pref.getString("error", null).equals("false")){
            String username = pref.getString("uname", null);
            commonhelper.ShowMesseage("Welcome Back\n" + username);
            commonhelper.callintent(LoginActivity.this, MainActivity.class);
        }*/


    }
}
