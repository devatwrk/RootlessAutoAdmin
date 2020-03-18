package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import aara.tech.rootless_auto_admin.repository.model.AddData;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryCardEditActivity extends AppCompatActivity {

    EditText et_item_name, et_brand, et_description, et_quantity, et_vendor, et_item_bar_code;
    TextView tv_item_name, tv_brand, tv_item_picture, tv_description, tv_quantity, tv_vendor, tv_item_bar_code;
    ImageView iv_pick_image;

    String inventory_id, item_name, brand, description, quantity, vendor, photo, bar_code;

    Button submit;
    Commonhelper commonhelper;
    ApiService apiService;

    private void initViews() {
        //EditText
        et_item_name = findViewById(R.id.et_item_name);
        et_brand = findViewById(R.id.et_brand);
        et_description = findViewById(R.id.et_description);
        et_quantity = findViewById(R.id.et_quantity);
        et_vendor = findViewById(R.id.et_vendor);
        et_item_bar_code = findViewById(R.id.et_item_bar_code);

        //TextView
        tv_item_name = findViewById(R.id.tv_item_name);
        tv_brand = findViewById(R.id.tv_brand);
        tv_item_picture = findViewById(R.id.tv_item_picture);
        tv_description = findViewById(R.id.tv_description);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_vendor = findViewById(R.id.tv_vendor);
        tv_item_bar_code = findViewById(R.id.tv_item_bar_code);

        //ImageView
        iv_pick_image = findViewById(R.id.iv_pick_image);

        //Button
        submit = findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(getApplicationContext());
        apiService = ApiUtils.getApiService();

    }

    private void textViewColor() {
        Shader textShader = new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tv_item_name.getPaint().setShader(textShader);
        tv_brand.getPaint().setShader(textShader);
        tv_item_picture.getPaint().setShader(textShader);
        tv_description.getPaint().setShader(textShader);
        tv_quantity.getPaint().setShader(textShader);
        tv_vendor.getPaint().setShader(textShader);
        tv_item_bar_code.getPaint().setShader(textShader);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_card_edit);

        //Initializing Views
        initViews();

        // Setting TextView Color Dynamically
//        textViewColor();

        //Getting Intent Data
        setReceivedData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commonhelper.ShowLoader();
                //getting selected items from spinner
                String item_name = et_item_name.getText().toString();
                String brand = et_brand.getText().toString();
                String description = et_description.getText().toString();
                String quantity = et_quantity.getText().toString();
                String vendor = et_vendor.getText().toString();
                String photo = "default";
                String bar_code = et_item_bar_code.getText().toString();

                //Network Call to Post Data
                updateInventoryNetworkCall(inventory_id, item_name, brand, description, quantity, vendor, photo, bar_code);
            }
        });

    }

    private void updateInventoryNetworkCall(String inventory_id, String item_name, String brand, String description, String quantity, String vendor, String photo, String bar_code) {
        Call<AddResponse> call = apiService.updateInventory(inventory_id, item_name, brand, description, quantity, vendor, photo, bar_code);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        AddData data = response.body().getData();
                        //setting spinners to default value
                        et_item_name.setText("");
                        et_brand.setText("");
                        et_description.setText("");
                        et_quantity.setText("");
                        et_vendor.setText("");
                        et_item_bar_code.setText("");
                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(data.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                //ProgressBar Hide
                commonhelper.HideLoader();
                //Toast
                commonhelper.ShowMesseage(t.getMessage());
            }
        });
    }

    private void setReceivedData() {
        inventory_id = commonhelper.getSharedPreferences("inventory_id", null);
        item_name = commonhelper.getSharedPreferences("item_name", null);
        brand = commonhelper.getSharedPreferences("brand", null);
        description = commonhelper.getSharedPreferences("description", null);
        quantity = commonhelper.getSharedPreferences("quantity", null);
        vendor = commonhelper.getSharedPreferences("vendor", null);
        bar_code = commonhelper.getSharedPreferences("bar_code", null);

        //Set Data to et
        et_item_name.setText(item_name);
        et_brand.setText(brand);
        et_description.setText(description);
        et_quantity.setText(quantity);
        et_vendor.setText(vendor);
        et_item_bar_code.setText(bar_code);
    }


}
