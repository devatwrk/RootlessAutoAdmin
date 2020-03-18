package aara.tech.rootless_auto_admin.ui.Fragments;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import aara.tech.rootless_auto_admin.R;


public class MoreInfoFragment extends Fragment {

    EditText etName, etNumber, etSubject, etEmail, etMessage;
    TextView tvName, tvNumber, tvSubject, tvEmail , tvDate, tvMessage;


    private void initViews(View view) {
        //EditText
        etName = view.findViewById(R.id.et_your_name);
        etNumber = view.findViewById(R.id.et_number);
        etSubject = view.findViewById(R.id.et_subject);
        etEmail = view.findViewById(R.id.et_email);
        etMessage = view.findViewById(R.id.et_message);
        //TextView
        tvName = view.findViewById(R.id.tv_vehicle_plate);
        tvNumber = view.findViewById(R.id.tv_number);
        tvSubject = view.findViewById(R.id.tv_subject);
        tvEmail = view.findViewById(R.id.tv_email);
        tvDate = view.findViewById(R.id.tv_pick_date);
        tvMessage = view.findViewById(R.id.tv_message);


    }
    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvName.getPaint().setShader(textShader);
        tvNumber.getPaint().setShader(textShader);
        tvSubject.getPaint().setShader(textShader);
        tvEmail.getPaint().setShader(textShader);
        tvDate.getPaint().setShader(textShader);
        tvMessage.getPaint().setShader(textShader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
        textViewColor();

        return view;
    }

}
