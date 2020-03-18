package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPassword extends AppCompatActivity {

    private EditText email;
    private Button reset;

    private void initview() {
        setContentView(R.layout.activity_forget_paasword);
        email = findViewById(R.id.etx_email);
        reset = findViewById(R.id.btn_reset);
    }

    private void OnClick() {
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        OnClick();
    }
}
