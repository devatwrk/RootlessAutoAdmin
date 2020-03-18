package aara.tech.rootless_auto_admin.ui.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import aara.tech.rootless_auto_admin.Adapters.VehicleListAdapter;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.VehicleListData;
import aara.tech.rootless_auto_admin.repository.model.VehicleListResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListVehicleFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private VehicleListAdapter adapter;
    private List<VehicleListData> vehicleDataList;
    private ImageView edit_iv, delete_iv;
    private ApiService apiService;
    private Commonhelper commonhelper;
    private TextView oops_tv;

    private void initViews(View view) {

        edit_iv = view.findViewById(R.id.edit_iv);
        delete_iv = view.findViewById(R.id.delete_iv);
        oops_tv = view.findViewById(R.id.oops_tv);

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_vehicle, container, false);


        initViews(view);
        commonhelper.ShowLoader();

        vehicleNetworkCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vehicleDataList.clear();
                commonhelper.ShowLoader();
                vehicleNetworkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void vehicleNetworkCall() {
        Call<VehicleListResponse> call = apiService.getVehicleList();

        call.enqueue(new Callback<VehicleListResponse>() {
            @Override
            public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                if (response.body().getData() != null) {
                    vehicleDataList = response.body().getData();
                    adapter = new VehicleListAdapter(getActivity(), vehicleDataList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    commonhelper.HideLoader();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    oops_tv.setVisibility(View.VISIBLE);
                    commonhelper.HideLoader();
                }

            }

            @Override
            public void onFailure(Call<VehicleListResponse> call, Throwable t) {

            }
        });
    }


}
