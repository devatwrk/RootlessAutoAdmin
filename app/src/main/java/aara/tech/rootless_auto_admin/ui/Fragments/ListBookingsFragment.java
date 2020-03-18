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

import aara.tech.rootless_auto_admin.Adapters.BookingListAdapter;
import aara.tech.rootless_auto_admin.Adapters.CustomerListAdapter;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.BookingListData;
import aara.tech.rootless_auto_admin.repository.model.BookingListResponse;
import aara.tech.rootless_auto_admin.repository.model.CustomerListData;
import aara.tech.rootless_auto_admin.repository.model.CustomerResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BookingListAdapter adapter;
    private List<BookingListData> bookingDataList;
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
        View view = inflater.inflate(R.layout.fragment_list_bookings, container, false);

        initViews(view);
        commonhelper.ShowLoader();

        bookingNetworkCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bookingDataList.clear();
                commonhelper.ShowLoader();
                bookingNetworkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void bookingNetworkCall() {
        Call<BookingListResponse> call = apiService.getBookingList();

        call.enqueue(new Callback<BookingListResponse>() {
            @Override
            public void onResponse(Call<BookingListResponse> call, Response<BookingListResponse> response) {
                if (response.body().getData() != null) {
                    bookingDataList = response.body().getData();
                    adapter = new BookingListAdapter(getActivity(), bookingDataList);
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
            public void onFailure(Call<BookingListResponse> call, Throwable t) {

            }
        });

    }


}
