package aara.tech.rootless_auto_admin.ui.Fragments;

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

import aara.tech.rootless_auto_admin.Adapters.CenterListAdapter;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.CenterListData;
import aara.tech.rootless_auto_admin.repository.model.CenterListResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListServiceCenterFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CenterListAdapter adapter;
    private List<CenterListData> centerListData;
    private ImageView edit_iv, delete_iv;
    ApiService apiService;
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_service_center, container, false);

        initViews(view);
        commonhelper.ShowLoader();

        centerListNetworkCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                centerListData.clear();
                commonhelper.ShowLoader();
                centerListNetworkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void centerListNetworkCall() {
        Call<CenterListResponse> call = apiService.getCenterList();

        call.enqueue(new Callback<CenterListResponse>() {
            @Override
            public void onResponse(Call<CenterListResponse> call, Response<CenterListResponse> response) {
                if (response.body().getData() != null) {
                    centerListData = response.body().getData();
                    adapter = new CenterListAdapter(getActivity(), centerListData);
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
            public void onFailure(Call<CenterListResponse> call, Throwable t) {

            }
        });

    }

}
