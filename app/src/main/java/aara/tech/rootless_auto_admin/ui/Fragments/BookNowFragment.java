package aara.tech.rootless_auto_admin.ui.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;

import aara.tech.rootless_auto_admin.R;


public class BookNowFragment extends Fragment {

   /* TextView tvYear, tvChooseCar, tvSelectYourModel, tvChooseEngine, tvZipCode;*/

    private void initViews(View view) {


        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tablayout_gradient));

        viewPagerAdapter.addFragment(new PaymentFragment(), "Payment");
        viewPagerAdapter.addFragment(new MoreInfoFragment(), "More Info");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
       /* //TextView
        tvYear = view.findViewById(R.id.tv_Year);
        tvChooseCar = view.findViewById(R.id.tv_ChooseCar);
        tvSelectYourModel = view.findViewById(R.id.tv_select_model);
        tvChooseEngine = view.findViewById(R.id.tv_choose_engine);
        tvZipCode = view.findViewById(R.id.tv_zip_code);*/

    }

   /* private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvYear.getPaint().setShader(textShader);
        tvChooseCar.getPaint().setShader(textShader);
        tvSelectYourModel.getPaint().setShader(textShader);
        tvChooseEngine.getPaint().setShader(textShader);
        tvZipCode.getPaint().setShader(textShader);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_now, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
//        textViewColor();

        /*//Dummy Data For Spinners
        selectYear(view, getContext());
        selectCar(view, getContext());
        selectModel(view, getContext());*/


        return view;
    }


   /* private void selectYear(View view, Context context) {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Your Year :-");
        years.add(Integer.toString(1900));
        for (int i = 1991; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        Spinner spinYear = view.findViewById(R.id.year_spinner);
        spinYear.setAdapter(adapter);
    }

    private void selectCar(View view, Context context) {
        ArrayList<String> cars = new ArrayList<String>();
        cars.add("Your Car :-");
        cars.add("Audi");
        cars.add("Lamborghini");
        cars.add("Toyota");
        cars.add("Hero MotoCorp");
        cars.add("Chevrolet");
        cars.add("Ford");
        cars.add("BMW");
        cars.add("Ferrari");
        cars.add("Mahindra");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cars);
        Spinner spinYear = view.findViewById(R.id.choose_car_spinner);
        spinYear.setAdapter(adapter);
    }

    private void selectModel(View view, Context context) {
        ArrayList<String> models = new ArrayList<String>();
        models.add("Your Model :-");
        models.add("R8");
        models.add("A8");
        models.add("Aventador");
        models.add("BMW X5");
        models.add("DC Avanti");
        models.add("Honda City");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, models);
        Spinner spinYear = view.findViewById(R.id.select_model_spinner);
        spinYear.setAdapter(adapter);
    }*/

    //    Adapter Class For ViewPager

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



}
