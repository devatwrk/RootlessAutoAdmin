package aara.tech.rootless_auto_admin.ui.Fragments;

import android.content.Context;
import android.net.Uri;
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

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import aara.tech.rootless_auto_admin.R;


public class ServiceCenterFragment extends Fragment {


    private void initViews(View view) {


        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tablayout_gradient));

        viewPagerAdapter.addFragment(new AddServiceCenterFragment(), "Add Service");
        viewPagerAdapter.addFragment(new ListServiceCenterFragment(), "List Service Center");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
       /* //TextView
        tvYear = view.findViewById(R.id.tv_Year);
        tvChooseCar = view.findViewById(R.id.tv_ChooseCar);
        tvSelectYourModel = view.findViewById(R.id.tv_select_model);
        tvChooseEngine = view.findViewById(R.id.tv_choose_engine);
        tvZipCode = view.findViewById(R.id.tv_zip_code);*/

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_service_center, container, false);

        //Initializing Views
        initViews(view);


        return view;
    }



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
