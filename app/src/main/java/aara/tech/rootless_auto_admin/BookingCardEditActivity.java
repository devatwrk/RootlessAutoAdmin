package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_admin.repository.model.AddData;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.model.AssignedServiceCenterListData;
import aara.tech.rootless_auto_admin.repository.model.AssignedServiceCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.CustomerNameListData;
import aara.tech.rootless_auto_admin.repository.model.CustomerNameResponse;
import aara.tech.rootless_auto_admin.repository.model.RequiredProductListData;
import aara.tech.rootless_auto_admin.repository.model.RequiredProductResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker;

public class BookingCardEditActivity extends AppCompatActivity {

    private String c_id, b_time, s_type, b_date;

    private EditText et_service_type;
    private TextView tv_customer_name, tv_product_required, tv_booking_date, tv_booking_time, tv_service_type, tv_assigned_service_center;
    private TextView tv_booking_date_picker, tv_booking_time_picker;
    private Spinner customer_name_spinner, product_required_spinner, assigned_service_center_spinner;
    private Button submit;

    private List<AssignedServiceCenterListData> assignedServiceCenterListData;
    private List<RequiredProductListData> requiredProductListData;
    private List<CustomerNameListData> customerNameListData;
    private ArrayList<String> assignedCenter;
    private ArrayList<String> custName;
    private ArrayList<String> product;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private Context context;


    //Time Utils
    private int mYear, mMonth, mDay, mHour, mMinute;

    private void initViews() {
        context = BookingCardEditActivity.this;
        //EditText
        et_service_type = findViewById(R.id.et_service_type);

        //TextView
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_product_required = findViewById(R.id.tv_product_required);
        tv_booking_date = findViewById(R.id.tv_booking_date);
        tv_booking_time = findViewById(R.id.tv_booking_time);
        tv_service_type = findViewById(R.id.tv_service_type);
        tv_assigned_service_center = findViewById(R.id.tv_assigned_service_center);

        //TextView Listener
        tv_booking_date_picker = findViewById(R.id.tv_booking_date_picker);
        tv_booking_time_picker = findViewById(R.id.tv_booking_time_picker);

        //Spinners
        customer_name_spinner = findViewById(R.id.customer_name_spinner);
        product_required_spinner = findViewById(R.id.product_required_spinner);
        assigned_service_center_spinner = findViewById(R.id.assigned_service_center_spinner);

        //Button
        submit = findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(getApplicationContext());
        apiService = ApiUtils.getApiService();

        assignedCenter = new ArrayList<>();
        custName = new ArrayList<>();
        product = new ArrayList<>();

    }

    private void textViewColor() {
        Shader textShader = new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tv_customer_name.getPaint().setShader(textShader);
        tv_product_required.getPaint().setShader(textShader);
        tv_booking_date.getPaint().setShader(textShader);
        tv_booking_time.getPaint().setShader(textShader);
        tv_service_type.getPaint().setShader(textShader);
        tv_assigned_service_center.getPaint().setShader(textShader);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_card_edit);

        //Initializing Views
        initViews();

        // Setting TextView Color Dynamically
//        textViewColor();

        setReceivedData();

        //Spinner Network Call :- Assigned Service Center
        customerNameNetworkCall();
        requiredProductNetworkCall();
        assignedCenterListNetworkCall();

        tv_booking_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        tv_booking_time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTimePicker();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commonhelper.ShowLoader();

                //getting selected items from spinner
                String c_name = customer_name_spinner.getSelectedItem().toString();
                String b_time = tv_booking_time_picker.getText().toString();
                String p_required = product_required_spinner.getSelectedItem().toString();
                String s_type = et_service_type.getText().toString();
                String b_date = tv_booking_date_picker.getText().toString();
                String a_service = assigned_service_center_spinner.getSelectedItem().toString();

                //Network Call to Post Data
                updateBookingNetworkCall(c_id, c_name, b_time, p_required, s_type, b_date, a_service);
            }
        });


    }


    private void setReceivedData() {
        c_id = commonhelper.getSharedPreferences("c_id", null);
        s_type = commonhelper.getSharedPreferences("s_type", null);
        b_time = commonhelper.getSharedPreferences("b_time", null);
        b_date = commonhelper.getSharedPreferences("b_date", null);

        //Set Data to et
        et_service_type.setText(s_type);
        tv_booking_date_picker.setText(b_date);
        tv_booking_time_picker.setText(b_time);

    }

    //...............................Network Call Methods...............................................

    private void updateBookingNetworkCall(String c_id, String c_name, String b_time, String p_required, String s_type, String b_date, String a_service) {
        Call<AddResponse> call = apiService.updateBooking(c_id, c_name, b_time, p_required, s_type, b_date, a_service);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                try {
                    AddData data = response.body().getData();
                    //setting spinners to default value
                    selectCustomerName(context);
                    selectRequiredProduct(context);
                    selectAssignedServiceCenter(context);
                    //Setting EditTexts to default
                    et_service_type.setText("");
                    tv_booking_date_picker.setText("");
                    tv_booking_time_picker.setText("");

                    //ProgressBar Hide
                    commonhelper.HideLoader();
                    //Toast
                    commonhelper.ShowMesseage(data.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
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


    private void customerNameNetworkCall() {
        Call<CustomerNameResponse> call = apiService.getCustomerNameList();
        call.enqueue(new Callback<CustomerNameResponse>() {
            @Override
            public void onResponse(Call<CustomerNameResponse> call, Response<CustomerNameResponse> response) {
                if (response.body().getData() != null) {
                    customerNameListData = response.body().getData();
                    for (CustomerNameListData data : customerNameListData) {
                        custName.add(data.getC_name());
                    }
                    selectCustomerName(context);
                }

            }

            @Override
            public void onFailure(Call<CustomerNameResponse> call, Throwable t) {

            }
        });

    }

    private void requiredProductNetworkCall() {
        Call<RequiredProductResponse> call = apiService.getRequiredProductList();
        call.enqueue(new Callback<RequiredProductResponse>() {
            @Override
            public void onResponse(Call<RequiredProductResponse> call, Response<RequiredProductResponse> response) {
                if (response.body().getData() != null) {
                    requiredProductListData = response.body().getData();
                    for (RequiredProductListData data : requiredProductListData) {
                        product.add(data.getProduct_name());
                    }
                    selectRequiredProduct(context);
                }

            }

            @Override
            public void onFailure(Call<RequiredProductResponse> call, Throwable t) {

            }
        });

    }

    private void assignedCenterListNetworkCall() {
        Call<AssignedServiceCenterResponse> call = apiService.getAssignedServiceCenterList();
        call.enqueue(new Callback<AssignedServiceCenterResponse>() {
            @Override
            public void onResponse(Call<AssignedServiceCenterResponse> call, Response<AssignedServiceCenterResponse> response) {
                if (response.body().getData() != null) {
                    assignedServiceCenterListData = response.body().getData();
                    for (AssignedServiceCenterListData data : assignedServiceCenterListData) {
                        assignedCenter.add(data.getService_name());
                    }
                    selectAssignedServiceCenter( context);
                }

            }

            @Override
            public void onFailure(Call<AssignedServiceCenterResponse> call, Throwable t) {

            }
        });

    }
//..................................................................................................

    private void selectCustomerName( Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, custName);
        customer_name_spinner.setAdapter(adapter);
    }

    private void selectRequiredProduct(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, product);
        product_required_spinner.setAdapter(adapter);
    }

    private void selectAssignedServiceCenter( Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, assignedCenter);
        assigned_service_center_spinner.setAdapter(adapter);
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
                tv_booking_date_picker.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }

    private void myTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay< 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
//                        tvStartTimePicker.setText(hourOfDay + ":" + minute + " " + AM_PM);
                        tv_booking_time_picker.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
