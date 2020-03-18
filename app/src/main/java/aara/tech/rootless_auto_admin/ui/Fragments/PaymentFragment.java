package aara.tech.rootless_auto_admin.ui.Fragments;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import aara.tech.rootless_auto_admin.R;


public class PaymentFragment extends Fragment {


    TextView tvOrderId, tvAmountToPay;
    WebView webView;
    Button paytm;
    ScrollView scrollView;
    ProgressBar progressBar;
    Sprite chasingDots;


    private void initViews(View view) {
        //TextView
        tvOrderId = view.findViewById(R.id.tv_order_id);
        tvAmountToPay = view.findViewById(R.id.tv_amount);
        webView = view.findViewById(R.id.webview);
        paytm = view.findViewById(R.id.btn_paytm);
        scrollView = view.findViewById(R.id.payment_scrollView);

        progressBar = view.findViewById(R.id.spin_kit);
        chasingDots = new ChasingDots();
        progressBar.setIndeterminateDrawable(chasingDots);


    }

    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvOrderId.getPaint().setShader(textShader);
        tvAmountToPay.getPaint().setShader(textShader);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
        textViewColor();

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytmBtn();
//                progressBar.setVisibility(View.GONE);
            }
        });


        return view;
    }

    private void paytmBtn(){

        webView.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        webView.loadUrl("https://paytm.com/");



        /*tvOrderId.setVisibility(View.INVISIBLE);
        tvAmountToPay.setVisibility(View.INVISIBLE);*/

    }



}
