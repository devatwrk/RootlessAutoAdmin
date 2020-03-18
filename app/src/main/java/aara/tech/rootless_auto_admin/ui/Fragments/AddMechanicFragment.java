package aara.tech.rootless_auto_admin.ui.Fragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_admin.R;
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
import aara.tech.rootless_auto_admin.repository.remote.Serverurl;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class AddMechanicFragment extends Fragment {

    private TextView first_name_tv, last_name_tv, email_tv, country_tv, state_tv, city_tv, address_tv, location_tv, residence_tv, working_hours_tv, contact_tv, alt_contact_tv, id_number_tv, photo_tv, model_tv, skills_tv, service_center_tv;
    private TextView start_time_picker_tv, end_time_picker_tv, location_picker_tv, id_image_picker_tv, profile_image_picker_tv, tv_country_code, tv_country_code2, tv_state_default, tv_city_default;
    private EditText first_name_et, last_name_et, email_et, address_et, residence_et, contact_et, alt_contact_et, id_number_et;

    private Spinner country_spinner, state_spinner, city_spinner, select_model_spinner,
            skills_spinner, assigned_service_center_spinner;

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
    private Context context;
    private Commonhelper commonhelper;
    private ApiService apiService;

    //......................Async OkHTTP CALL...........................................................

    private static final int PICK_IMAGE_CAMERA = 901;
    private static final int PICK_IMAGE_Gallery = 900;
    private static final int PICK_IMAGE_CAMERA_ID = 902;
    private static final int PICK_IMAGE_Gallery_ID = 903;
    private Uri Profile_Image_path = null;
    private Uri Id_Image_path = null;
    private String mPhotoPath;
    private ImageView iv_profile_image_picker, iv_id_image_picker;

    public AddMechanicFragment() {
        // Required empty public constructor
    }


//..................................................................................................



    private int mYear, mMonth, mDay, mHour, mMinute;

    private void initViews(View view) {
        context = getContext();
        //EditText
        first_name_et = view.findViewById(R.id.et_first_name);
        last_name_et = view.findViewById(R.id.et_last_name);
        email_et = view.findViewById(R.id.et_email);
        address_et = view.findViewById(R.id.et_physical_address);
        residence_et = view.findViewById(R.id.et_residence);
        contact_et = view.findViewById(R.id.et_number);
        alt_contact_et = view.findViewById(R.id.et_alt_number);
        id_number_et = view.findViewById(R.id.et_id_no);


        //TextView Titles
        first_name_tv = view.findViewById(R.id.tv_first_name);
        last_name_tv = view.findViewById(R.id.tv_last_name);
        email_tv = view.findViewById(R.id.tv_email);
        country_tv = view.findViewById(R.id.tv_select_country);
        state_tv = view.findViewById(R.id.tv_state);
        city_tv = view.findViewById(R.id.tv_City);
        address_tv = view.findViewById(R.id.tv_physical_address);
        location_tv = view.findViewById(R.id.tv_location_pick);
        residence_tv = view.findViewById(R.id.tv_residence);
//        working_hours_tv = view.findViewById(R.id.tv_working_hours);
        contact_tv = view.findViewById(R.id.tv_number);
        alt_contact_tv = view.findViewById(R.id.tv_alt_number);
        id_number_tv = view.findViewById(R.id.tv_id_no);
//        photo_tv = view.findViewById(R.id.tv_photo);
        model_tv = view.findViewById(R.id.tv_model);
        skills_tv = view.findViewById(R.id.tv_skills);
        service_center_tv = view.findViewById(R.id.tv_assigned_service_center);

        tv_country_code = view.findViewById(R.id.tv_country_code);
        tv_country_code2 = view.findViewById(R.id.tv_country_code2);

        tv_state_default = view.findViewById(R.id.tv_state_default);
        tv_city_default = view.findViewById(R.id.tv_city_default);

        //TextView Listeners
        start_time_picker_tv = view.findViewById(R.id.tv_start_time_picker);
        end_time_picker_tv = view.findViewById(R.id.tv_end_time_picker);
        location_picker_tv = view.findViewById(R.id.tv_location_picker);
//        id_image_picker_tv = view.findViewById(R.id.tv_id_image_picker);
//        profile_image_picker_tv = view.findViewById(R.id.tv_profile_image_picker);


        //Spinners
        country_spinner = view.findViewById(R.id.select_country_spinner);
        state_spinner = view.findViewById(R.id.select_state_spinner);
        city_spinner = view.findViewById(R.id.select_city_spinner);
        skills_spinner = view.findViewById(R.id.select_skills_spinner);
        assigned_service_center_spinner = view.findViewById(R.id.assigned_service_center_spinner);
        select_model_spinner = view.findViewById(R.id.select_model_spinner);

        //ImageView
        iv_profile_image_picker = view.findViewById(R.id.iv_profile_image_picker);
        iv_id_image_picker = view.findViewById(R.id.iv_id_image_picker);

        //Button
        submit = view.findViewById(R.id.btn_update);

        //Common Helper
        commonhelper = new Commonhelper(context);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_mechanic, container, false);

        initViews(view);


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

        selectSkills(context);
        selectModels(context);

        //Network Calls
        assignedCenterListNetworkCall();
        //Spinner Network Calls
        countryNetworkCall();

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

                addMechanicNetworkCall(f_name, l_name, country, state, city, physical_ads, location, residence, p_contact, alt_contact, email, id_number, id_image, image, model, skills, service_center, start_time, end_time);
//                f_name, l_name, country, state, city, physical_ads, location, residence, p_contact, alt_contact, email, id_number, id_image, image, model, skills, service_center, start_time, end_time

            }
        });

        //......................Async OkHTTP CALL...........................................................
        iv_profile_image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), BrowseImage.class));

                checkAndRequestPermissions();
                if (commonhelper.isStoragePermissionGranted(getActivity())) {
                    if (commonhelper.isCameraPermissionGranted(getActivity())) {
                        askForCameraAndGallery();
                    }
                }
            }
        });

        iv_id_image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), BrowseImage.class));

                checkAndRequestPermissions();
                if (commonhelper.isStoragePermissionGranted(getActivity())) {
                    if (commonhelper.isCameraPermissionGranted(getActivity())) {
                        askForCameraAndGalleryId();
                    }
                }
            }
        });

//..................................................................................................


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

        return view;
    }


    //......................Async OkHTTP CALL...........................................................
    //Async OkHttp image upload working Method
    private void addMechanicNetworkCall(String f_name, String l_name, String country, String state, String city, String physical_ads, String location, String residence, String p_contact, String alt_contact, String email, String id_number, String id_image, String image, String model, String skills, String service_center, String start_time, String end_time) {
        try {

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setTimeout(20000);

            RequestParams requestParams = new RequestParams();

            requestParams.put("f_name", f_name);
            requestParams.put("l_name", l_name);
            requestParams.put("country", country);
            requestParams.put("state", state);
            requestParams.put("city", city);
            requestParams.put("physical_ads", physical_ads);
            requestParams.put("location", location);
            requestParams.put("residence", residence);
            requestParams.put("p_contact", p_contact);
            requestParams.put("alt_contact", alt_contact);
            requestParams.put("email", email);
            requestParams.put("id_number", id_number);
            requestParams.put("id_image", new File(getPath(getActivity(), Id_Image_path)));
            requestParams.put("image", new File(getPath(getActivity(), Profile_Image_path)));
            requestParams.put("model", model);
            requestParams.put("skills", skills);
            requestParams.put("service_center", service_center);
            requestParams.put("start_time", start_time);
            requestParams.put("end_time", end_time);

            asyncHttpClient.post(Serverurl.ADD_MECHANIC_API, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    commonhelper.ShowLoader();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        commonhelper.HideLoader();
                        String status = response.getString("Status");
                        String message = response.getJSONObject("data").getString("message");
                        if (message != null) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }


//                        commonhelper.ShowMesseage(status);
                       /* Log.d("KS", response.toString());


                        if (status.equals("true")) {
                            String repid = jsobj.getString("id");
                            AddReportImage(repid);
                        } else {
                            String message = jsobj.getString("Message");
                            commonhelper.ShowMesseage(message);
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    commonhelper.HideLoader();
                    Log.e("tagg", errorResponse.toString());
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    commonhelper.HideLoader();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.e("tagg", throwable.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    commonhelper.HideLoader();
                    Log.e("tagg", throwable.toString());
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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

        int writepermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    101);
            return false;
        }
        return true;
    }

    private void askForCameraAndGallery() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {

//                final CharSequence[] options = {"Take Photo From Camera", "Choose From Gallery", "Cancel"};
                final CharSequence[] options = {"Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void askForCameraAndGalleryId() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {

//                final CharSequence[] options = {"Take Photo From Camera", "Choose From Gallery", "Cancel"};
                final CharSequence[] options = {"Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo From Camera")) {
                            dialog.dismiss();

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA_ID);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_Gallery_ID);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //For Image Pick
        try {
            // When  Image is picked form gallery
            if (requestCode == PICK_IMAGE_Gallery && resultCode == RESULT_OK
                    && null != data) {
                Profile_Image_path = data.getData();
                iv_profile_image_picker.setImageURI(Profile_Image_path);
            }
            //when image picked from camera
            if (requestCode == PICK_IMAGE_CAMERA) {
                try {
                    Profile_Image_path = data.getData();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    iv_profile_image_picker.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == PICK_IMAGE_Gallery_ID && resultCode == RESULT_OK
                    && null != data) {
                Id_Image_path = data.getData();
                iv_id_image_picker.setImageURI(Id_Image_path);
            }
            //when image picked from camera
            if (requestCode == PICK_IMAGE_CAMERA_ID) {
                try {
                    Id_Image_path = data.getData();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    iv_id_image_picker.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        } catch (Exception e) {
            Log.e("MJ", "onActivityResult: " + e.toString());
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

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

/*    private void addMechanicNetworkCall(String f_name, String l_name, String country, String state, String city, String physical_ads, String location, String residence, String p_contact, String alt_contact, String email, String id_number, String id_image, String image, String model, String skills, String service_center, String start_time, String end_time) {
        Call<AddResponse> call = apiService.addMechanic(f_name, l_name, country, state, city, physical_ads, location, residence, p_contact, alt_contact, email, id_number, id_image, image, model, skills, service_center, start_time, end_time);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                try {
                    AddData addData = response.body().getData();
                    //setting spinners to default value
                    selectCountry(context);
                    *//*selectState(context);
                    selectCity(context);
                    selectSkills(context);
                    selectAssignedServiceCenter(context);*//*
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
