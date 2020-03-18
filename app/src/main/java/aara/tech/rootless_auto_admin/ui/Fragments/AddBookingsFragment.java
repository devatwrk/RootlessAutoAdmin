package aara.tech.rootless_auto_admin.ui.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_admin.R;
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


public class AddBookingsFragment extends Fragment {

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



    private void initViews(View view) {
        //EditText
        et_service_type = view.findViewById(R.id.et_service_type);

        //TextView
        tv_customer_name = view.findViewById(R.id.tv_customer_name);
        tv_product_required = view.findViewById(R.id.tv_product_required);
        tv_booking_date = view.findViewById(R.id.tv_booking_date);
        tv_booking_time = view.findViewById(R.id.tv_booking_time);
        tv_service_type = view.findViewById(R.id.tv_service_type);
        tv_assigned_service_center = view.findViewById(R.id.tv_assigned_service_center);

        //TextView Listener
        tv_booking_date_picker = view.findViewById(R.id.tv_booking_date_picker);
        tv_booking_time_picker = view.findViewById(R.id.tv_booking_time_picker);

        //Spinners
        customer_name_spinner = view.findViewById(R.id.customer_name_spinner);
        product_required_spinner = view.findViewById(R.id.product_required_spinner);
        assigned_service_center_spinner = view.findViewById(R.id.assigned_service_center_spinner);

        //Button
        submit = view.findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(getContext());
        apiService = ApiUtils.getApiService();
        context = getContext();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_bookings, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
//        textViewColor();

        //Dummy Data For Spinners


        //Spinner Network Call :- Assigned Service Center
        customerNameNetworkCall(view);
        requiredProductNetworkCall(view);
        assignedCenterListNetworkCall(view);

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
                addBookingNetworkCall(c_name, b_time, p_required, s_type, b_date, a_service, view);
            }
        });
        return view;
    }

//...............................Network Call Methods...............................................
    private void customerNameNetworkCall(final View view) {
        Call<CustomerNameResponse> call = apiService.getCustomerNameList();
        call.enqueue(new Callback<CustomerNameResponse>() {
            @Override
            public void onResponse(Call<CustomerNameResponse> call, Response<CustomerNameResponse> response) {
                if (response.body().getData() != null) {
                    customerNameListData = response.body().getData();
                    for (CustomerNameListData data : customerNameListData) {
                        custName.add(data.getC_name());
                    }
                    selectCustomerName(view, context);
                }

            }

            @Override
            public void onFailure(Call<CustomerNameResponse> call, Throwable t) {

            }
        });

    }

    private void requiredProductNetworkCall(final View view) {
        Call<RequiredProductResponse> call = apiService.getRequiredProductList();
        call.enqueue(new Callback<RequiredProductResponse>() {
            @Override
            public void onResponse(Call<RequiredProductResponse> call, Response<RequiredProductResponse> response) {
                if (response.body().getData() != null) {
                    requiredProductListData = response.body().getData();
                    for (RequiredProductListData data : requiredProductListData) {
                        product.add(data.getProduct_name());
                    }
                    selectRequiredProduct(view,context);
                }

            }

            @Override
            public void onFailure(Call<RequiredProductResponse> call, Throwable t) {

            }
        });

    }

    private void assignedCenterListNetworkCall(final View view) {
        Call<AssignedServiceCenterResponse> call = apiService.getAssignedServiceCenterList();
        call.enqueue(new Callback<AssignedServiceCenterResponse>() {
            @Override
            public void onResponse(Call<AssignedServiceCenterResponse> call, Response<AssignedServiceCenterResponse> response) {
                if (response.body().getData() != null) {
                    assignedServiceCenterListData = response.body().getData();
                    for (AssignedServiceCenterListData data : assignedServiceCenterListData) {
                        assignedCenter.add(data.getService_name());
                    }
                    selectAssignedServiceCenter(view, context);
                }

            }

            @Override
            public void onFailure(Call<AssignedServiceCenterResponse> call, Throwable t) {

            }
        });

    }

    private void addBookingNetworkCall(String c_name, String b_time, String p_required, String s_type, String b_date, String a_service, final View view) {
        Call<AddResponse> call = apiService.addBooking(c_name, b_time, p_required, s_type, b_date, a_service);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                try {
                    AddData data = response.body().getData();
                    //setting spinners to default value
                    selectCustomerName(view, context);
                    selectRequiredProduct(view,context);
                    selectAssignedServiceCenter(view, context);
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
            }
        });
    }
//..................................................................................................

    private void selectCustomerName(View view, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, custName);
        customer_name_spinner.setAdapter(adapter);
    }

    private void selectRequiredProduct(View view, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, product);
        product_required_spinner.setAdapter(adapter);
    }

    private void selectAssignedServiceCenter(View view, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, assignedCenter);
        assigned_service_center_spinner.setAdapter(adapter);
    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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

