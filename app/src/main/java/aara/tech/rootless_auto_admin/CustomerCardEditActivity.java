package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aara.tech.rootless_auto_admin.repository.model.AddData;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.model.CarListData;
import aara.tech.rootless_auto_admin.repository.model.CarListResponse;
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

public class CustomerCardEditActivity extends AppCompatActivity {

    private TextView first_name_tv, last_name_tv, email_tv, country_tv, state_tv, city_tv, address_tv, location_tv, contact_tv, alt_contact_tv, id_number_tv, photo_tv, vehicle_tv;
    private TextView location_picker_tv, profile_image_picker_tv, id_image_picker_tv, tv_country_code, tv_country_code2, tv_state_default, tv_city_default;
    private EditText first_name_et, last_name_et, email_et, address_et, contact_et, alt_contact_et, id_number_et;
    private Spinner country_spinner, state_spinner, city_spinner, vehicle_spinner;

    private ImageView iv_profile_image_picker, iv_id_image_picker;

    private String customer_id, f_name, l_name, photo, country, state, city, iD_no, iD_card_image, address, p_contact, altr_contact, email, vehicle, gps_location;

    private boolean mCountrySpinnerInitialized, mStateSpinnerInitialized;

    private List<CountryData> countryData;
    private List<StateData> stateData;
    private List<CityData> cityData;
    private List<CountryCodeData> codeData;

    private ArrayList<String> models;
    private ArrayList<String> countryList;
    private ArrayList<String> stateList;
    private ArrayList<String> cityList;

    private Button submit;
    private List<CarListData> carListData;
    private ArrayList<String> vehicleList;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private Context context;


    private void initViews() {
        context = CustomerCardEditActivity.this;

        //EditText
        first_name_et = findViewById(R.id.et_first_name);
        last_name_et = findViewById(R.id.et_last_name);
        email_et = findViewById(R.id.et_email);
        address_et = findViewById(R.id.et_physical_address);
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
//        address_tv = findViewById(R.id.tv_address);
        location_tv = findViewById(R.id.tv_location_pick);
        contact_tv = findViewById(R.id.tv_number);
        alt_contact_tv = findViewById(R.id.tv_alt_number);
        id_number_tv = findViewById(R.id.tv_id_no);
//        photo_tv = findViewById(R.id.tv_photo);
        vehicle_tv = findViewById(R.id.tv_vehicle);

        tv_state_default = findViewById(R.id.tv_state_default);
        tv_city_default = findViewById(R.id.tv_city_default);


        //TextView Listeners

        location_picker_tv = findViewById(R.id.tv_gps_location_picker);
        tv_country_code = findViewById(R.id.tv_country_code);
        tv_country_code2 = findViewById(R.id.tv_country_code2);

        //ImageView
        iv_profile_image_picker = findViewById(R.id.iv_profile_image_picker);
        iv_id_image_picker = findViewById(R.id.iv_id_image_picker);


        //Spinners
        country_spinner = findViewById(R.id.select_country_spinner);
        state_spinner = findViewById(R.id.select_state_spinner);
        city_spinner = findViewById(R.id.select_city_spinner);
        vehicle_spinner = findViewById(R.id.select_vehicle_spinner);


        //Button
        submit = findViewById(R.id.btn_update);

        //Common Helper
        commonhelper = new Commonhelper(getApplicationContext());
        apiService = ApiUtils.getApiService();

//..................................................................................................
        vehicleList = new ArrayList<>();
        models = new ArrayList<>();
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
        setContentView(R.layout.activity_customer_card_edit);

        initViews();

        //Spinner Network Calls
        countryNetworkCall();

        vehicleListNetworkCall();

        setReceivedData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commonhelper.ShowLoader();

                String f_name = first_name_et.getText().toString();
                String l_name = last_name_et.getText().toString();
                String photo = "default";

                String country = country_spinner.getSelectedItem().toString();
                String state = state_spinner.getSelectedItem().toString();
                String city = city_spinner.getSelectedItem().toString();

                String iD_no = id_number_et.getText().toString();
                String iD_card_image = "default";
                String address = address_et.getText().toString();
                String p_contact =  tv_country_code.getText().toString() + contact_et.getText().toString();
                String altr_contact =  tv_country_code2.getText().toString() +  alt_contact_et.getText().toString();
                String email = email_et.getText().toString();

                String vehicle = vehicle_spinner.getSelectedItem().toString();

                String gps_location = "0.254563,0.255663";

                updateCustomerNetworkCall(customer_id, f_name, l_name, photo, country, state, city, iD_no, iD_card_image, address, p_contact, altr_contact, email, vehicle, gps_location);

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

    private void setReceivedData() {

        customer_id = commonhelper.getSharedPreferences("customer_id", null);
        f_name = commonhelper.getSharedPreferences("f_name", null);
        l_name = commonhelper.getSharedPreferences("l_name", null);
//        country = commonhelper.getSharedPreferences("country", null);
//        state = commonhelper.getSharedPreferences("state", null);
//        city = commonhelper.getSharedPreferences("city", null);
        address = commonhelper.getSharedPreferences("address", null);
//        location = commonhelper.getSharedPreferences("location", null);
        p_contact = commonhelper.getSharedPreferences("p_contact", null);
        altr_contact = commonhelper.getSharedPreferences("altr_contact", null);
        email = commonhelper.getSharedPreferences("email", null);
        iD_no = commonhelper.getSharedPreferences("iD_no", null);

        //Set received data on Views
        first_name_et.setText(f_name);
        last_name_et.setText(l_name);
        email_et.setText(email);
        address_et.setText(address);
//        contact_et.setText(p_contact);
//        alt_contact_et.setText(altr_contact);
        id_number_et.setText(iD_no);


    }

    private void updateCustomerNetworkCall(String customer_id, String f_name, String l_name, String photo,
                                           String country, String state, String city, String iD_no, String iD_card_image,
                                           String address, String p_contact, String altr_contact, String email,
                                           String vehicle, String gps_location) {
        Call<AddResponse> call = apiService.updateCustomer(customer_id, f_name, l_name, photo, country, state, city, iD_no, iD_card_image, address, p_contact, altr_contact, email, vehicle, gps_location);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {

                try {
                    AddData addData = response.body().getData();
                    //setting spinners to default value
//                    selectCountry(context);
//                    selectState(context);
//                    selectCity(context);
//                    selectVehicle(context);

                    //Setting EditTexts to default

                    first_name_et.setText("");
                    last_name_et.setText("");
                    email_et.setText("");
                    address_et.setText("");
                    contact_et.setText("");
                    alt_contact_et.setText("");
                    id_number_et.setText("");

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

    private void vehicleListNetworkCall() {
        Call<CarListResponse> call = apiService.getCarList();
        call.enqueue(new Callback<CarListResponse>() {
            @Override
            public void onResponse(Call<CarListResponse> call, Response<CarListResponse> response) {
                if (response.body().getData() != null) {
                    carListData = response.body().getData();
                    for (CarListData data : carListData) {
                        vehicleList.add(data.getCars());
                    }
                    selectVehicle(context);
                }

            }

            @Override
            public void onFailure(Call<CarListResponse> call, Throwable t) {

            }
        });

    }

    private void selectVehicle(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, vehicleList);
        vehicle_spinner.setAdapter(adapter);
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


}
