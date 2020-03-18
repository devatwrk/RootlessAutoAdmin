package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import aara.tech.rootless_auto_admin.utils.Commonhelper;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    Commonhelper commonhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                commonhelper = new Commonhelper(getApplicationContext());

                if (commonhelper.getSharedPreferences("admin_email", null) != null) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    commonhelper.ShowMesseage("Welcome Back " + commonhelper.getSharedPreferences("admin_email", null));
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }


            }
        }, 3000);

    }
}
