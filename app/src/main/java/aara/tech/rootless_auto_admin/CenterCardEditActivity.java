package aara.tech.rootless_auto_admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_admin.repository.model.AddCenterData;
import aara.tech.rootless_auto_admin.repository.model.AddCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.CarModelListData;
import aara.tech.rootless_auto_admin.repository.model.CarModelResponse;
import aara.tech.rootless_auto_admin.repository.model.CityData;
import aara.tech.rootless_auto_admin.repository.model.CityResponse;
import aara.tech.rootless_auto_admin.repository.model.CountryCodeData;
import aara.tech.rootless_auto_admin.repository.model.CountryCodeResponse;
import aara.tech.rootless_auto_admin.repository.model.CountryData;
import aara.tech.rootless_auto_admin.repository.model.CountryResponse;
import aara.tech.rootless_auto_admin.repository.model.StateData;
import aara.tech.rootless_auto_admin.repository.model.StateResponse;
import aara.tech.rootless_auto_admin.repository.model.UpdateCenterData;
import aara.tech.rootless_auto_admin.repository.model.UpdateCenterResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.repository.remote.Serverurl;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class CenterCardEditActivity extends AppCompatActivity {

    private EditText etCenterName, etAddress, etNumber, etAltNumber, etEmail;
    private TextView tvCenterPhoto, tvCenterName, tvCountry, tvState, tvCity, tvAddress, tvLocation,
            tvNumber, tvAltNumber, tvEmail, tvModel, tvOffered, tvWorkingHours,
            tvLocationPicker, tvStartTimePicker, tvEndTimePicker, tv_country_code, tv_country_code2, tv_state_default, tv_city_default;
    private Spinner country_spinner, state_spinner, city_spinner, model_spinner, service_offered_spinner;

    private boolean mCountrySpinnerInitialized, mStateSpinnerInitialized;

    private List<CarModelListData> carModelListData;
    private List<CountryData> countryData;
    private List<StateData> stateData;
    private List<CityData> cityData;
    private List<CountryCodeData> codeData;

    private ArrayList<String> models;
    private ArrayList<String> countryList;
    private ArrayList<String> stateList;
    private ArrayList<String> cityList;

    private Button update_btn;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private Context context;
    private Intent intent;

    //Loader Progress
    private Dialog dialog;

/*
    //To upload Image
    Bitmap bitmap;
    private ImageView center_photo_iv;
    private  static final int IMAGE = 100;
*/

    //Time Utils
    private int mYear, mMonth, mDay, mHour, mMinute;

//......................Async OkHTTP CALL...........................................................

    private static final int PICK_IMAGE_CAMERA = 901;
    private static final int PICK_IMAGE_Gallery = 900;
    private Uri Image_path = null;
    private ImageView center_photo_iv;



//..................................................................................................

    private void initViews() {

        context = CenterCardEditActivity.this;
        //EditText
        etCenterName = findViewById(R.id.et_center_name);
        etAddress = findViewById(R.id.et_physical_address);
        etNumber = findViewById(R.id.et_number);
        etAltNumber = findViewById(R.id.et_alt_number);
        etEmail = findViewById(R.id.et_email);

        //ImageView
        center_photo_iv = findViewById(R.id.center_photo_iv);

        //TextView
        tvCenterPhoto = findViewById(R.id.tv_center_photo);
        tvCenterName = findViewById(R.id.tv_center_name);
        tvCountry = findViewById(R.id.tv_select_country);
        tvState = findViewById(R.id.tv_state);
        tvCity = findViewById(R.id.tv_City);
        tvAddress = findViewById(R.id.tv_physical_address);
        tvLocation = findViewById(R.id.tv_location_pick);
        tvNumber = findViewById(R.id.tv_number);
        tvAltNumber = findViewById(R.id.tv_alt_number);
        tvEmail = findViewById(R.id.tv_email);
        tvModel = findViewById(R.id.tv_select_model);
        tvOffered = findViewById(R.id.tv_service_offered);
//        tvWorkingHours = findViewById(R.id.tv_working_hours);
        tv_state_default = findViewById(R.id.tv_state_default);
        tv_city_default = findViewById(R.id.tv_city_default);

        tv_country_code = findViewById(R.id.tv_country_code);
        tv_country_code2 = findViewById(R.id.tv_country_code2);

        tvLocationPicker = findViewById(R.id.tv_location_picker);
        tvStartTimePicker = findViewById(R.id.tv_start_time_picker);
        tvEndTimePicker = findViewById(R.id.tv_end_time_picker);

        //Spinner
        country_spinner = findViewById(R.id.select_country_spinner);
        state_spinner = findViewById(R.id.select_state_spinner);
        city_spinner = findViewById(R.id.select_city_spinner);
        model_spinner = findViewById(R.id.select_model_spinner);
        service_offered_spinner = findViewById(R.id.service_offered_spinner);

        //Button
        update_btn = findViewById(R.id.btn_update);

        //Common Helper
        commonhelper = new Commonhelper(getApplicationContext());
        apiService = ApiUtils.getApiService();
        intent = getIntent();


        models = new ArrayList<>();
        countryList = new ArrayList<>();
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        //Boolean for Spinner
        mCountrySpinnerInitialized = false;
        mStateSpinnerInitialized = false;

        tv_country_code.setVisibility(View.GONE);
        tv_country_code2.setVisibility(View.GONE);

        //Loader Progress
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvCenterPhoto.getPaint().setShader(textShader);
        tvCenterName.getPaint().setShader(textShader);
        tvCountry.getPaint().setShader(textShader);
        tvState.getPaint().setShader(textShader);
        tvCity.getPaint().setShader(textShader);
        tvAddress.getPaint().setShader(textShader);
        tvLocation.getPaint().setShader(textShader);
        tvNumber.getPaint().setShader(textShader);
        tvAltNumber.getPaint().setShader(textShader);
        tvEmail.getPaint().setShader(textShader);
        tvModel.getPaint().setShader(textShader);
        tvOffered.getPaint().setShader(textShader);
        tvWorkingHours.getPaint().setShader(textShader);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_card_edit);

        //Initializing Views
        initViews();

//        // Setting TextView Color Dynamically
//        textViewColor();

        //Spinner Network Calls
        countryNetworkCall();

        /*selectState(view, context);
        selectCity(view, context);*/
        carModelSpinnerListNetworkCall();
        selectServicesOffered(context);

        //Visibility
        tvLocation.setVisibility(View.GONE);
        tvLocationPicker.setVisibility(View.GONE);

        //Getting Intent Data
        setReceivedData();

       /* center_photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/

        tvStartTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker();
            }
        });


        tvEndTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePicker();
            }
        });

        //Submit button clicked
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state, city;
                //getting selected items from spinner
                View stateView = state_spinner.getSelectedView();
                View cityView = city_spinner.getSelectedView();
                if (stateView != null) {
                    state = state_spinner.getSelectedItem().toString();
                }else {
                    state = "";
                }
                if (cityView != null) {
                    city = city_spinner.getSelectedItem().toString();
                }else {
                    city = "";
                }
                String Service_id = intent.getStringExtra("Service_id");
                String Service_name = etCenterName.getText().toString();
                String p_contact = tv_country_code.getText().toString() + etNumber.getText().toString();
                String Country = country_spinner.getSelectedItem().toString();
                String Cont = tv_country_code2.getText().toString() + etAltNumber.getText().toString();
//                String state = state_spinner.getSelectedItem().toString();
                String email = etEmail.getText().toString();
//                String city = city_spinner.getSelectedItem().toString();
//                String service_photo = "default";
//                String service_photo = convertToString();
                String p_address =  etAddress.getText().toString();
                String model = model_spinner.getSelectedItem().toString();
                String location = "53533.12, 12313.35";
                String service = service_offered_spinner.getSelectedItem().toString();
                String start_time = tvStartTimePicker.getText().toString();
                String end_time = tvEndTimePicker.getText().toString();

                boolean validate = validationCheck(Service_name, p_contact, Country, Cont, state, email, city, p_address, model, service, start_time, end_time);

                if (validate) {
                    //Network Call to Post Data
                    updateCenterNetworkCall(Service_id, Service_name, p_contact, Country, Cont, state, email,
                            city, p_address, model, location, service, start_time, end_time
                    );
                } else {
//                    commonhelper.HideLoader();
                }
               /* //Network Call to Post Data
                updateCenterNetworkCall(Service_id, Service_name, p_contact, Country, Cont, state, email,
                        city, service_photo, p_address, model, location, service, start_time, end_time
                        );*/

            }
        });

        //......................Async OkHTTP CALL...........................................................
        center_photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(CenterCardEditActivity.this, BrowseImage.class));

                checkAndRequestPermissions();
                if (commonhelper.isStoragePermissionGranted(CenterCardEditActivity.this)) {
                    if (commonhelper.isCameraPermissionGranted(CenterCardEditActivity.this)) {
                        askForCameraAndGallery();
                    }
                }
            }
        });

//..................................................................................................

       /* center_photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/
        tvStartTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker();
            }
        });


        tvEndTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePicker();
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

    private boolean validationCheck(String Service_name, String p_contact, String Country, String Cont, String state, String email, String city, String p_address, String model, String service, String start_time, String end_time) {
        if (Image_path == null ) {
            commonhelper.ShowMesseage("Select Service Photo");
            return false;
        }
        if (TextUtils.isEmpty(Service_name) ) {
            etCenterName.setError("Name is required");
            etCenterName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(Country) ) {
            commonhelper.ShowMesseage("Country is not selected");
            return false;
        }
        if (TextUtils.isEmpty(state) ) {
            commonhelper.ShowMesseage("State is not selected");
            return false;

        }
        if (TextUtils.isEmpty(city) ) {
            commonhelper.ShowMesseage("City is not selected");
            return false;
        }

        if (TextUtils.isEmpty(p_address) ) {
            etAddress.setError("Address required");
            etAddress.requestFocus();
            return false;
        }
        /*if (TextUtils.isEmpty(gpscoordinate) ) {
            et_location.setError("Location required");
            et_location.requestFocus();
            return false;
        }*/
        if (TextUtils.isEmpty(etNumber.getText().toString()) ) {
            etNumber.setError("Contact required");
            etNumber.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etAltNumber.getText().toString()) ) {
            etAltNumber.setError("Contact required");
            etAltNumber.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email) ) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(start_time) ) {
            commonhelper.ShowMesseage("Select Start Time");
            return false;
        }
        if (TextUtils.isEmpty(end_time) ) {
            commonhelper.ShowMesseage("Select End Time");
            return false;

        }
        return true;
    }

    private void setReceivedData() {

        final String name = intent.getStringExtra("name");

        /*final String year = intent.getStringExtra("year");
        final String model = intent.getStringExtra("model");
        final String engine = intent.getStringExtra("engine");*/

        etCenterName.setText(name);
        /* etEngine.setText(engine);*/

    }

   /* private void updateCenterNetworkCall(String Service_id, String Service_name, String p_contact, String Country,
            String Cont, String state, String email, String city, String service_photo, String p_address,
            String model, String location, String service, String start_time, String end_time
    ) {
        Call<UpdateCenterResponse> call = apiService.updateServiceCenter(
                Service_id,
                Service_name,
                p_contact,
                Country,
                Cont,
                state,
                email,
                city,
                service_photo,
                p_address,
                model,
                location,
                service,
                start_time,
                end_time
        );
        call.enqueue(new Callback<UpdateCenterResponse>() {
            @Override
            public void onResponse(Call<UpdateCenterResponse> call, Response<UpdateCenterResponse> response) {

                if (response.isSuccessful()) {
                        UpdateCenterData updateCenterData = response.body().getData();
                        //setting spinners to default value
                   *//* try {
                        selectCountry(context);
                        selectState(context);
                        selectCity(context);
                        selectModel(context);
                        selectServicesOffered(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*//*
                    //Setting EditTexts to default
                        etCenterName.setText("");
                        etNumber.setText("");
                        etAltNumber.setText("");
                        etEmail.setText("");
                        etAddress.setText("");

                        //ProgressBar Hide
//                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(updateCenterData.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UpdateCenterResponse> call, Throwable t) {
                //ProgressBar Hide
//                commonhelper.HideLoader();
                //Toast
                commonhelper.ShowMesseage(t.getMessage());
            }
        });

    }*/


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

    private void selectModel(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, models);
        model_spinner.setAdapter(adapter);
    }

    private void selectServicesOffered(Context context) {
        ArrayList<String> services = new ArrayList<String>();
        services.add("Free Washing");
        services.add("24x7 Helpline");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, services);
        service_offered_spinner.setAdapter(adapter);
    }

    private void carModelSpinnerListNetworkCall() {
        Call<CarModelResponse> call = apiService.getCarModelList();
        call.enqueue(new Callback<CarModelResponse>() {
            @Override
            public void onResponse(Call<CarModelResponse> call, Response<CarModelResponse> response) {
                if (response.body().getData() != null) {
                    carModelListData = response.body().getData();
                    for (CarModelListData carModel : carModelListData) {
                        models.add(carModel.getModel());
                    }
                    selectModel(context);
                }

            }

            @Override
            public void onFailure(Call<CarModelResponse> call, Throwable t) {

            }
        });

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
                        if (hourOfDay< 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
//                        tvStartTimePicker.setText(hourOfDay + ":" + minute + " " + AM_PM);
                        tvStartTimePicker.setText(hourOfDay + ":" + minute);
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
                        if (hourOfDay< 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
//                        tvEndTimePicker.setText(hourOfDay + ":" + minute + " " + AM_PM);
                        tvEndTimePicker.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

   /* private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE);
    }

    private String convertToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== IMAGE && resultCode==RESULT_OK && data!=null)
        {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(CenterCardEditActivity.this.getContentResolver(),path);
                center_photo_iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/


//......................Async OkHTTP CALL...........................................................
//Async OkHttp image upload working Method
    private void updateCenterNetworkCall(String Service_id,String Service_name, String p_contact, String Country, String Cont, String state, String email, String city, String p_address, String model, String gpscoordinate, String service, String start_time, String end_time) {
        try {

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setTimeout(20000);

            RequestParams requestParams = new RequestParams();

//            requestParams.put("Service_id", Service_id);
            requestParams.put("Service_name", Service_name);
            requestParams.put("p_contact", p_contact);
            requestParams.put("Country", Country);
            requestParams.put("Cont", Cont);
            requestParams.put("state", state);
            requestParams.put("email", email);
            requestParams.put("city", city);
            requestParams.put("service_photo", new File(getPath(CenterCardEditActivity.this, Image_path)));
            requestParams.put("p_address", p_address);
            requestParams.put("model", model);
            requestParams.put("gpscoordinate", gpscoordinate);
            requestParams.put("service", service);
            requestParams.put("start_time", start_time);
            requestParams.put("end_time", end_time);
            String url = Serverurl.UPDATE_SERVICE_API+Service_id;
            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        dialog.hide();
                        String status = response.getString("Status");
                        String message = response.getJSONObject("data").getString("message");
                        if (message != null) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dialog.hide();
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.hide();
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    dialog.hide();
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    long percentage = (bytesWritten / totalSize) * 100;
                    //progressDialog.setProgress(((int) percentage));
                }
            });
        } catch (Exception e) {

        }
    }

    //Required Things for Image Upload
    private boolean checkAndRequestPermissions() {

        int writepermission = ContextCompat.checkSelfPermission(CenterCardEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(CenterCardEditActivity.this,
                Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(CenterCardEditActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    101);
            return false;
        }
        return true;
    }

    private void askForCameraAndGallery() {
        try {
            PackageManager pm = CenterCardEditActivity.this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, CenterCardEditActivity.this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {

//                final CharSequence[] options = {"Take Photo From Camera", "Choose From Gallery", "Cancel"};
                final CharSequence[] options = {"Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CenterCardEditActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo From Camera")) {
                            dialog.dismiss();

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_Gallery);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(CenterCardEditActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(CenterCardEditActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        // For location Picker
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String latlng = latitude + ", " + longitude;
                et_location.setText(latlng);
            }
        }*/

        //For Image Pick
        try {
            // When  Image is picked form gallery
            if (requestCode == PICK_IMAGE_Gallery && resultCode == RESULT_OK
                    && null != data) {
                Image_path = data.getData();
                center_photo_iv.setImageURI(Image_path);
            }
            //when image picked from camera
            if (requestCode == PICK_IMAGE_CAMERA) {
                try {
                    Image_path = data.getData();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    center_photo_iv.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            Log.e("MJ", "onActivityResult: " + e.toString());
            Toast.makeText(CenterCardEditActivity.this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

       /* // For location Picker
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String latlng = latitude + ", " + longitude;
                et_location.setText(latlng);
            }
        }*/
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public void FromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

//..................................................................................................

}
