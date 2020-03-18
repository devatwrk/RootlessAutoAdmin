package aara.tech.rootless_auto_admin.ui.Fragments;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.AddVehicleData;
import aara.tech.rootless_auto_admin.repository.model.AddVehicleResponse;
import aara.tech.rootless_auto_admin.repository.model.CarModelListData;
import aara.tech.rootless_auto_admin.repository.model.CarModelResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddVehicleFragment extends Fragment {

    private EditText etPlateNumber, etEngine, etMileage, etColour;
    private TextView tvPlateNumber, tvYear, tvSelectYourModel, tvEngine, tvMileage, tvColour;
    private Spinner year_spinner, model_spinner;
    private Button submit;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private List<CarModelListData> carModelListData;
    private ArrayList<String> models;
    private Context context;


    private void initViews(View view) {
        context = getContext();
        //EditText
        etPlateNumber = view.findViewById(R.id.et_vehicle_plate);
        etEngine = view.findViewById(R.id.et_engine);
        etMileage = view.findViewById(R.id.et_mileage);
        etColour = view.findViewById(R.id.et_colour);
        //TextView
        tvPlateNumber = view.findViewById(R.id.tv_vehicle_plate);
        tvYear = view.findViewById(R.id.tv_Year);
        tvSelectYourModel = view.findViewById(R.id.tv_select_model);
        tvEngine = view.findViewById(R.id.tv_engine);
        tvMileage = view.findViewById(R.id.tv_mileage);
        tvColour = view.findViewById(R.id.tv_colour);
        //Spinner
        year_spinner = view.findViewById(R.id.year_spinner);
        model_spinner = view.findViewById(R.id.select_model_spinner);
        //Button
        submit = view.findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(context);
        apiService = ApiUtils.getApiService();
        models = new ArrayList<>();

    }
    private void textViewColor() {
        Shader textShader = new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvPlateNumber.getPaint().setShader(textShader);
        tvYear.getPaint().setShader(textShader);
        tvSelectYourModel.getPaint().setShader(textShader);
        tvEngine.getPaint().setShader(textShader);
        tvMileage.getPaint().setShader(textShader);
        tvColour.getPaint().setShader(textShader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
//        textViewColor();

        //Dummy Data For Spinners
        selectYear(context);
        carModelSpinnerListNetworkCall();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar Show
                commonhelper.ShowLoader();
                //getting selected items from spinner
                String name = etPlateNumber.getText().toString();
                String year = year_spinner.getSelectedItem().toString();
                String model = model_spinner.getSelectedItem().toString();
                String engine = etEngine.getText().toString();
                String colour = etColour.getText().toString();
                String mileage = etMileage.getText().toString();

                //Network Call to Post Data
                addVehicleNetworkCall(name, model, year, engine, colour, mileage, view);
            }
        });

        return view;
    }

    private void addVehicleNetworkCall(String name, String model, String year, String engine, String colour, String mileage, final View view) {

        Call<AddVehicleResponse> call = apiService.addVehicle(name, model, year, engine, colour, mileage);

        call.enqueue(new Callback<AddVehicleResponse>() {
            @Override
            public void onResponse(Call<AddVehicleResponse> call, Response<AddVehicleResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        AddVehicleData addVehicleData = response.body().getData();
                        //setting spinners to default value
                        selectYear(context);
                        selectModel(context);
                        etPlateNumber.setText("");
                        etEngine.setText("");
                        etMileage.setText("");
                        etColour.setText("");
                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(addVehicleData.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                       
                    }
                }
            }

            @Override
            public void onFailure(Call<AddVehicleResponse> call, Throwable t) {
                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(t.getMessage());
            }
        });

    }

    private void selectYear(Context context) {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Your Year:-");
        years.add(Integer.toString(1900));
        for (int i = 1991; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years);
        year_spinner.setAdapter(adapter);
    }

    private void selectModel(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, models);
        model_spinner.setAdapter(adapter);
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

}
