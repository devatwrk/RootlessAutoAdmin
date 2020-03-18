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

import aara.tech.rootless_auto_admin.Adapters.InventoryListAdapter;
import aara.tech.rootless_auto_admin.Adapters.MechanicListAdapter;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.InventoryListData;
import aara.tech.rootless_auto_admin.repository.model.InventoryResponse;
import aara.tech.rootless_auto_admin.repository.model.MechanicListData;
import aara.tech.rootless_auto_admin.repository.model.MechanicListResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListInventoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private InventoryListAdapter adapter;
    private List<InventoryListData> inventoryDataList;
    private ImageView edit_iv, delete_iv, item_pic_iv;
    private ApiService apiService;
    private Commonhelper commonhelper;
    private TextView oops_tv;

    private void initViews(View view) {

        edit_iv = view.findViewById(R.id.edit_iv);
        delete_iv = view.findViewById(R.id.delete_iv);
        item_pic_iv = view.findViewById(R.id.item_pic_iv);
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
        View view = inflater.inflate(R.layout.fragment_list_inventory, container, false);

        initViews(view);
        commonhelper.ShowLoader();

        inventoryNetworkCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inventoryDataList.clear();
                commonhelper.ShowLoader();
                inventoryNetworkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void inventoryNetworkCall() {
        Call<InventoryResponse> call = apiService.getInventoryList();

        call.enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                if (response.body().getData() != null) {
                    inventoryDataList = response.body().getData();
                    adapter = new InventoryListAdapter(getActivity(), inventoryDataList);
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
            public void onFailure(Call<InventoryResponse> call, Throwable t) {

            }
        });
    }


}
