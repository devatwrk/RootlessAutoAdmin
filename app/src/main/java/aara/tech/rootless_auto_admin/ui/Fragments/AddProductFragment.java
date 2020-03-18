package aara.tech.rootless_auto_admin.ui.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.AddData;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddProductFragment extends Fragment {

    private EditText et_product_name, et_man_hours, et_complexity, et_service_charge, et_description ;
    private TextView tv_product_name, tv_man_hours, tv_complexity, tv_currency, tv_service_charge, tv_description, tv_service_date, tv_service_date_picker;
    private Spinner currency_spinner;
    private Button submit;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private Context context;


    private void initViews(View view) {
        context = getContext();
        //EditText
        et_product_name = view.findViewById(R.id.et_product_name);
        et_man_hours = view.findViewById(R.id.et_man_hours);
        et_complexity = view.findViewById(R.id.et_complexity);
        et_service_charge = view.findViewById(R.id.et_service_charge);
        et_description = view.findViewById(R.id.et_description);
        //TextView
        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_man_hours = view.findViewById(R.id.tv_man_hours);
        tv_complexity = view.findViewById(R.id.tv_complexity);
        tv_currency = view.findViewById(R.id.tv_currency);
        tv_service_charge = view.findViewById(R.id.tv_service_charge);
        tv_description = view.findViewById(R.id.tv_description);
        tv_service_date = view.findViewById(R.id.tv_service_date);
        tv_service_date_picker = view.findViewById(R.id.tv_service_date_picker);
        //Spinner
        currency_spinner = view.findViewById(R.id.currency_spinner);

        //Button
        submit = view.findViewById(R.id.btn_update);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
//        textViewColor();
        //Dummy Data For Spinners
        selectCurrency(view, context);

        onClick(view);

        return view;
    }

    private void onClick(final View view) {
        tv_service_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar Show
                commonhelper.ShowLoader();
                //getting selected items from spinner
                String product_name = et_product_name.getText().toString();
                String man_hours = et_man_hours.getText().toString();
                String complexity = et_complexity.getText().toString();
                String service_charge = et_service_charge.getText().toString();
                String description = et_description.getText().toString();
                String service_time = tv_service_date_picker.getText().toString();
//                String currency = currency_spinner.getSelectedItem().toString();

                //Network Call to Post Data
                addProductNetworkCall(product_name, man_hours, complexity, service_charge, description, service_time, view);
            }
        });
    }

    private void addProductNetworkCall(String product_name, String man_hours, String complexity, String service_charge, String description, String service_time, final View view) {

        Call<AddResponse> call = apiService.addProduct(product_name, man_hours, complexity, service_charge, description, service_time);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        AddData addProductData = response.body().getData();
                        //setting spinners to default value

                        selectCurrency(view, context);
                        et_product_name.setText("");
                        et_man_hours.setText("");
                        et_complexity.setText("");
                        et_service_charge.setText("");
                        et_description.setText("");
                        tv_service_date_picker.setText("");

                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(addProductData.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {

            }
        });


    }

    private void selectCurrency(View view, Context context) {
        ArrayList<String> currency = new ArrayList<String>();
        currency.add("USh");
        currency.add("USD");
        currency.add("INR");
        currency.add("EUR");
        currency.add("GBP");
        currency.add("JPY");
        currency.add("AC AUD");
        currency.add("CAD");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, currency);
        currency_spinner.setAdapter(adapter);
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

}
