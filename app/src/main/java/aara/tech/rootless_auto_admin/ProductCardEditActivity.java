package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import aara.tech.rootless_auto_admin.repository.model.AddData;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.model.UpdateProductData;
import aara.tech.rootless_auto_admin.repository.model.UpdateProductResponse;
import aara.tech.rootless_auto_admin.repository.model.UpdateVehicleData;
import aara.tech.rootless_auto_admin.repository.model.UpdateVehicleResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCardEditActivity extends AppCompatActivity {

    EditText et_product_name, et_man_hours, et_complexity, et_service_charge, et_description;
    TextView tv_product_name, tv_man_hours, tv_complexity, tv_currency, tv_service_charge, tv_description, tv_service_date, tv_service_date_picker;
    Spinner currency_spinner;
    String product_id, product_name, man_hours, complexity, service_charge, description, service_time;
    Button update;
    Commonhelper commonhelper;
    ApiService apiService;
    Context context;


    private void initViews() {
        context = ProductCardEditActivity.this;
        //EditText
        et_product_name = findViewById(R.id.et_product_name);
        et_man_hours = findViewById(R.id.et_man_hours);
        et_complexity = findViewById(R.id.et_complexity);
        et_service_charge = findViewById(R.id.et_service_charge);
        et_description = findViewById(R.id.et_description);
        //TextView
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_man_hours = findViewById(R.id.tv_man_hours);
        tv_complexity = findViewById(R.id.tv_complexity);
        tv_currency = findViewById(R.id.tv_currency);
        tv_service_charge = findViewById(R.id.tv_service_charge);
        tv_description = findViewById(R.id.tv_description);
        tv_service_date = findViewById(R.id.tv_service_date);
        tv_service_date_picker = findViewById(R.id.tv_service_date_picker);
        //Spinner
        currency_spinner = findViewById(R.id.currency_spinner);

        //Button
        update = findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(context);
        apiService = ApiUtils.getApiService();

    }

    private void textViewColor() {
        Shader textShader = new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tv_product_name.getPaint().setShader(textShader);
        tv_man_hours.getPaint().setShader(textShader);
        tv_complexity.getPaint().setShader(textShader);
        tv_currency.getPaint().setShader(textShader);
        tv_service_charge.getPaint().setShader(textShader);
        tv_description.getPaint().setShader(textShader);
        tv_service_date.getPaint().setShader(textShader);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_card_edit);

        //Initializing Views
        initViews();

        // Setting TextView Color Dynamically
//        textViewColor();

        //Getting Data
        setReceivedData();

        selectCurrency(context);

        tv_service_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar Show
//                commonhelper.ShowLoader();
                //getting selected items from spinner
//                String product_id = intent.getI
                String product_name = et_product_name.getText().toString();
                String man_hours = et_man_hours.getText().toString();
                String complexity = et_complexity.getText().toString();
                String service_charge = et_service_charge.getText().toString();
                String description = et_description.getText().toString();
                String service_time = tv_service_date_picker.getText().toString();
//                String currency = currency_spinner.getSelectedItem().toString();

                //Network Call to Post Data
                updateProductNetworkCall(product_id, product_name, man_hours, complexity, service_charge, description, service_time);


            }
        });

    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)
                        + "/" + String.valueOf(year);
                tv_service_date_picker.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }

    private void updateProductNetworkCall(String product_id, String product_name, String man_hours, String complexity, String service_charge, String description, String service_time) {

        Call<UpdateProductResponse> call = apiService.updateProduct(product_id, product_name, man_hours, complexity, service_charge, description, service_time);
        call.enqueue(new Callback<UpdateProductResponse>() {
            @Override
            public void onResponse(Call<UpdateProductResponse> call, Response<UpdateProductResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        UpdateProductData updateProductData = response.body().getData();
                        //setting spinners to default value

                       /* selectCurrency(context);
                        et_product_name.setText("");
                        et_man_hours.setText("");
                        et_complexity.setText("");
                        et_service_charge.setText("");
                        et_description.setText("");
                        tv_service_date_picker.setText("");

                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast*/
                        commonhelper.ShowMesseage(updateProductData.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateProductResponse> call, Throwable t) {
                //ProgressBar Hide
                commonhelper.HideLoader();
                //Toast
                commonhelper.ShowMesseage(t.getMessage());
            }
        });

    }

    private void setReceivedData() {

        product_id = commonhelper.getSharedPreferences("product_id", null);
        product_name = commonhelper.getSharedPreferences("product_name", null);
        man_hours = commonhelper.getSharedPreferences("man_hours", null);
        complexity = commonhelper.getSharedPreferences("complexity", null);
        service_charge = commonhelper.getSharedPreferences("service_charge", null);
        description = commonhelper.getSharedPreferences("description", null);
        service_time = commonhelper.getSharedPreferences("service_time", null);

        //Set received data on Views
        et_product_name.setText(product_name);
        et_man_hours.setText(man_hours);
        et_complexity.setText(complexity);
        et_service_charge.setText(service_charge);
        et_description.setText(description);
        tv_service_date_picker.setText(service_time);


    }

    private void selectCurrency(Context context) {
        ArrayList<String> currency = new ArrayList<String>();
        currency.add("USh");
        currency.add("INR");
        currency.add("EUR");
        currency.add("GBP");
        currency.add("JPY");
        currency.add("AC AUD");
        currency.add("CAD");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, currency);
        currency_spinner.setAdapter(adapter);
    }

}
