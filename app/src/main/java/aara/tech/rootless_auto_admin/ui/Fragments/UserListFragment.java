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

import aara.tech.rootless_auto_admin.Adapters.UserListAdapter;
import aara.tech.rootless_auto_admin.Adapters.VehicleListAdapter;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.UserListData;
import aara.tech.rootless_auto_admin.repository.model.UserListResponse;
import aara.tech.rootless_auto_admin.repository.model.VehicleListData;
import aara.tech.rootless_auto_admin.repository.model.VehicleListResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserListAdapter adapter;
    private List<UserListData> userListData;
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list_users, container, false);


        initViews(view);
        commonhelper.ShowLoader();

        userNetworkCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userListData.clear();
                commonhelper.ShowLoader();
                userNetworkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });




        return view;
    }

    private void userNetworkCall() {
        Call<UserListResponse> call = apiService.getUserList();

        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.body().getData() != null) {
                    userListData = response.body().getData();
                    adapter = new UserListAdapter(getActivity(), userListData);
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
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });
    }
}
