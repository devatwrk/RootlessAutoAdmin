package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_admin.repository.model.AddData;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.model.AssignedServiceCenterListData;
import aara.tech.rootless_auto_admin.repository.model.AssignedServiceCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.CityData;
import aara.tech.rootless_auto_admin.repository.model.CityResponse;
import aara.tech.rootless_auto_admin.repository.model.CountryCodeData;
import aara.tech.rootless_auto_admin.repository.model.CountryCodeResponse;
import aara.tech.rootless_auto_admin.repository.model.CountryData;
import aara.tech.rootless_auto_admin.repository.model.CountryResponse;
import aara.tech.rootless_auto_admin.repository.model.StateData;
import aara.tech.rootless_auto_admin.repository.model.StateResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicCardEditActivity extends AppCompatActivity {

    private TextView first_name_tv, last_name_tv, email_tv, country_tv, state_tv, city_tv, address_tv, location_tv, residence_tv, working_hours_tv, contact_tv, alt_contact_tv, id_number_tv, photo_tv, model_tv, skills_tv, service_center_tv;
    private TextView start_time_picker_tv, end_time_picker_tv, location_picker_tv, id_image_picker_tv, profile_image_picker_tv, tv_country_code, tv_country_code2, tv_state_default, tv_city_default;
    private EditText first_name_et, last_name_et, email_et, address_et, residence_et, contact_et, alt_contact_et, id_number_et;

    private Spinner country_spinner, state_spinner, city_spinner, select_model_spinner,
            skills_spinner, assigned_service_center_spinner;

    private ImageView iv_profile_image_picker, iv_id_image_picker;


    private String service_id, f_name, l_name, country, state, city, physical_ads, location, residence, p_contact, alt_contact, email, id_number, id_image, image, model, skills, service_center, start_time, end_time;


    private List<AssignedServiceCenterListData> assignedServiceCenterListData;
    private ArrayList<String> assignedCenter;

    private boolean mCountrySpinnerInitialized, mStateSpinnerInitialized;

    private List<CountryData> countryData;
    private List<StateData> stateData;
    private List<CityData> cityData;
    private List<CountryCodeData> codeData;

    private ArrayList<String> countryList;
    private ArrayList<String> stateList;
    private ArrayList<String> cityList;


    private Button submit;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private Context context;



    private int mYear, mMonth, mDay, mHour, mMinute;

    private void initViews() {
        context = MechanicCardEditActivity.this;
        //EditText
        first_name_et = findViewById(R.id.et_first_name);
        last_name_et = findViewById(R.id.et_last_name);
        email_et = findViewById(R.id.et_email);
        address_et = findViewById(R.id.et_physical_address);
        residence_et = findViewById(R.id.et_residence);
        contact_et = findViewById(R.id.et_number);
        alt_contact_et = findViewById(R.id.et_alt_number);
        id_number_et = findViewById(R.id.et_id_no);


        //TextView Titles
        first_name_tv = findViewById(R.id.tv_first_name);
        last_name_tv = findViewById(R.id.tv_last_name);
        email_tv = findViewById(R.id.tv_email);
        country_tv = findViewById(R.id.tv_select_country);
        state_tv = findViewById(R.id.tv_state);
        city_tv = findViewById(R.id.tv_City);
        address_tv = findViewById(R.id.tv_physical_address);
        location_tv = findViewById(R.id.tv_location_pick);
        residence_tv = findViewById(R.id.tv_residence);
//        working_hours_tv = findViewById(R.id.tv_working_hours);
        contact_tv = findViewById(R.id.tv_number);
        alt_contact_tv = findViewById(R.id.tv_alt_number);
        id_number_tv = findViewById(R.id.tv_id_no);
//        photo_tv = findViewById(R.id.tv_photo);
        model_tv = findViewById(R.id.tv_model);
        skills_tv = findViewById(R.id.tv_skills);
        service_center_tv = findViewById(R.id.tv_assigned_service_center);

        tv_country_code = findViewById(R.id.tv_country_code);
        tv_country_code2 = findViewById(R.id.tv_country_code2);

        tv_state_default = findViewById(R.id.tv_state_default);
        tv_city_default = findViewById(R.id.tv_city_default);

        //TextView Listeners
        start_time_picker_tv = findViewById(R.id.tv_start_time_picker);
        end_time_picker_tv = findViewById(R.id.tv_end_time_picker);
        location_picker_tv = findViewById(R.id.tv_location_picker);
//        id_image_picker_tv = findViewById(R.id.tv_id_image_picker);
//        profile_image_picker_tv = findViewById(R.id.tv_profile_image_picker);

        //Spinners
        country_spinner = findViewById(R.id.select_country_spinner);
        state_spinner = findViewById(R.id.select_state_spinner);
        city_spinner = findViewById(R.id.select_city_spinner);
        skills_spinner = findViewById(R.id.select_skills_spinner);
        assigned_service_center_spinner = findViewById(R.id.assigned_service_center_spinner);
        select_model_spinner = findViewById(R.id.select_model_spinner);

        //ImageView
        iv_profile_image_picker = findViewById(R.id.iv_profile_image_picker);
        iv_id_image_picker = findViewById(R.id.iv_id_image_picker);
        //Button


        submit = findViewById(R.id.btn_update);

        //Common Helper
        commonhelper = new Commonhelper(this);
        apiService = ApiUtils.getApiService();

        assignedCenter = new ArrayList<>();
        countryList = new ArrayList<>();
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        //Boolean for Spinner
        mCountrySpinnerInitialized = false;
        mStateSpinnerInitialized = false;

        tv_country_code.setVisibility(View.GONE);
        tv_country_code2.setVisibility(View.GONE);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_card_edit);

        initViews();

        //Getting Intent Data
        setReceivedData();

        selectSkills(context);
        selectModels(context);

        //Network Calls
        assignedCenterListNetworkCall();
        //Spinner Network Calls
        countryNetworkCall();


        start_time_picker_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker();
            }
        });


        end_time_picker_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePicker();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonhelper.ShowLoader();
//                String service_id = etCenterName.getText().toString();

                String f_name = first_name_et.getText().toString();
                String l_name = last_name_et.getText().toString();
                String physical_ads = address_et.getText().toString();
                String location = "0.254153-02154587";
                String residence = residence_et.getText().toString();
                String p_contact = tv_country_code.getText().toString() + contact_et.getText().toString();
                String alt_contact = tv_country_code2.getText().toString() + alt_contact_et.getText().toString();
                String email = email_et.getText().toString();
                String id_number = id_number_et.getText().toString();
                String id_image = "default";
                String image = "default";
                String start_time = start_time_picker_tv.getText().toString();
                String end_time = end_time_picker_tv.getText().toString();


                //Spinners
                String model = select_model_spinner.getSelectedItem().toString();
                String country = country_spinner.getSelectedItem().toString();
                String state = state_spinner.getSelectedItem().toString();
                String city = city_spinner.getSelectedItem().toString();
                String skills = skills_spinner.getSelectedItem().toString();
                String service_center = assigned_service_center_spinner.getSelectedItem().toString();

                updateMechanicNetworkCall(service_id, f_name, l_name, country, state, city, physical_ads, location, residence, p_contact, alt_contact, email, id_number, id_image, image, model, skills, service_center, start_time, end_time);


            }
        });

//      Spinner Item Selected Listeners.............................................................

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mCountrySpinnerInitialized) {
                    mCountrySpinnerInitialized = true;
                    return;
                }
                // do stuff
                String item = country_spinner.getSelectedItem().toString();
                stateList.clear();
                for (CountryData data : countryData) {
                    if (item.equals(data.getCountry_name())) {
                        String country_id = data.getCountry_id();
                        countryCodeNetworkCall(country_id);
                        stateNetworkCall(country_id);
                        tv_state_default.setVisibility(View.GONE);
                        state_spinner.setVisibility(View.VISIBLE);
                        return;
                    }
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mStateSpinnerInitialized) {
                    mStateSpinnerInitialized = true;
                    return;
                }
                // do stuff
                String item = state_spinner.getSelectedItem().toString();
                cityList.clear();
                for (StateData data : stateData) {
                    if (item.equals(data.getState_name())) {
                        String state_id = data.getState_id();
                        cityNetworkCall(state_id);
                        tv_city_default.setVisibility(View.GONE);
                        city_spinner.setVisibility(View.VISIBLE);
                        return;
                    }
                }

            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

//..................................................................................................

    }

    private void updateMechanicNetworkCall(String service_id, String f_name, String l_name, String country, String state, String city, String physical_ads, String location, String residence, String p_contact, String alt_contact, String email, String id_number, String id_image, String image, String model, String skills, String service_center, String start_time, String end_time) {

        Call<AddResponse> call = apiService.updateMechanic(service_id, f_name, l_name, country, state, city, physical_ads, location, residence, p_contact, alt_contact, email, id_number, id_image, image, model, skills, service_center, start_time, end_time);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                try {
                    AddData addData = response.body().getData();
                    //setting spinners to default value
                    /*selectCountry(context);
                    selectState(context);
                    selectCity(context);
                    countryCode(context);
                    selectSkills(context);
                    selectAssignedServiceCenter(context);*/

                    //Setting EditTexts to default

                    first_name_et.setText("");
                    last_name_et.setText("");
                    email_et.setText("");
                    address_et.setText("");
                    residence_et.setText("");
                    contact_et.setText("");
                    alt_contact_et.setText("");
                    id_number_et.setText("");
                    start_time_picker_tv.setText("");
                    end_time_picker_tv.setText("");

                    //ProgressBar Hide
                    commonhelper.HideLoader();
                    //Toast
                    commonhelper.ShowMesseage(addData.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {

            }
        });


    }


    private void setReceivedData() {

        service_id = commonhelper.getSharedPreferences("service_id", null);
        f_name = commonhelper.getSharedPreferences("f_name", null);
        l_name = commonhelper.getSharedPreferences("l_name", null);
//        country = commonhelper.getSharedPreferences("country", null);
//        state = commonhelper.getSharedPreferences("state", null);
//        city = commonhelper.getSharedPreferences("city", null);
        physical_ads = commonhelper.getSharedPreferences("physical_ads", null);
//        location = commonhelper.getSharedPreferences("location", null);
        residence = commonhelper.getSharedPreferences("residence", null);
        p_contact = commonhelper.getSharedPreferences("p_contact", null);
        alt_contact = commonhelper.getSharedPreferences("alt_contact", null);
        email = commonhelper.getSharedPreferences("email", null);
        id_number = commonhelper.getSharedPreferences("id_number", null);
//        id_image = commonhelper.getSharedPreferences("id_image", null);
//        image = commonhelper.getSharedPreferences("image", null);
//        model = commonhelper.getSharedPreferences("model", null);
//        skills = commonhelper.getSharedPreferences("skills", null);
//        service_center = commonhelper.getSharedPreferences("service_center", null);
//        start_time = commonhelper.getSharedPreferences("start_time", null);
//        end_time = commonhelper.getSharedPreferences("end_time", null);


        //Set received data on Views
        first_name_et.setText(f_name);
        last_name_et.setText(l_name);
        email_et.setText(email);
        address_et.setText(physical_ads);
        residence_et.setText(residence);
//        contact_et.setText(p_contact);
//        alt_contact_et.setText(alt_contact);
        id_number_et.setText(id_number);


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
                    selectAssignedServiceCenter(context);
                }

            }

            @Override
            public void onFailure(Call<AssignedServiceCenterResponse> call, Throwable t) {

            }
        });

    }

    private void selectCountry(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countryList);
        country_spinner.setAdapter(adapter);
    }

    private void selectState(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, stateList);
        state_spinner.setAdapter(adapter);
    }

    private void selectCity(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cityList);
        city_spinner.setAdapter(adapter);
    }

    private void countryNetworkCall() {
        Call<CountryResponse> call = apiService.getCountry();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.body().getData() != null) {
                    countryData = response.body().getData();
                    for (CountryData data : countryData) {
                        countryList.add(data.getCountry_name());
                    }
                    selectCountry(context);
                }

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {

            }
        });

    }

    private void stateNetworkCall(String country_id) {
        Call<StateResponse> call = apiService.getState(country_id);
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                if (response.body().getData() != null) {
                    stateData = response.body().getData();
                    for (StateData data : stateData) {
                        stateList.add(data.getState_name());
                    }
                    selectState(context);
                }

            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {

            }
        });
    }

    private void cityNetworkCall(String state_id) {
        Call<CityResponse> call = apiService.getCity(state_id);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.body().getData() != null) {
                    cityData = response.body().getData();
                    for (CityData data : cityData) {
                        cityList.add(data.getCity_name());
                    }
                    selectCity(context);
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {

            }
        });

    }

    private void countryCodeNetworkCall(String country_id) {
        Call<CountryCodeResponse> call = apiService.getCountryCode(country_id);
        call.enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                if (response.body().getData() != null) {
                    codeData = response.body().getData();
                    tv_country_code.setVisibility(View.VISIBLE);
                    tv_country_code2.setVisibility(View.VISIBLE);
                    tv_country_code.setText(codeData.get(0).getCountries_isd_code());
                    tv_country_code2.setText(codeData.get(0).getCountries_isd_code());
                }
            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectSkills(Context context) {
        ArrayList<String> skills = new ArrayList<String>();
        skills.add("Washing");
        skills.add("Repair");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, skills);
        skills_spinner.setAdapter(adapter);
    }

    private void selectModels(Context context) {
        ArrayList<String> model = new ArrayList<String>();
        model.add("Car Model");
        model.add("Brand");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, model);
        select_model_spinner.setAdapter(adapter);
    }

    private void selectAssignedServiceCenter(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, assignedCenter);
        assigned_service_center_spinner.setAdapter(adapter);
    }



    private void startTimePicker() {
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
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
//                        tvStartTimePicker.setText(hourOfDay + ":" + minute + " " + AM_PM);
                        start_time_picker_tv.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void endTimePicker() {
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
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
//                        tvEndTimePicker.setText(hourOfDay + ":" + minute + " " + AM_PM);
                        end_time_picker_tv.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
