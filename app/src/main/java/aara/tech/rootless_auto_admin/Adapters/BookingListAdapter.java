package aara.tech.rootless_auto_admin.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aara.tech.rootless_auto_admin.BookingCardEditActivity;
import aara.tech.rootless_auto_admin.InventoryCardEditActivity;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.BookingListData;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.InventoryListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    private Context context;
    private List<BookingListData> bookingDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;

    public BookingListAdapter(Context context, List<BookingListData> bookingDataList) {
        this.context = context;
        this.bookingDataList = bookingDataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_booking, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new BookingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.customer_name_result_tv.setText(bookingDataList.get(position).getC_name());
        holder.product_required_result_tv.setText(bookingDataList.get(position).getP_required());
        holder.booking_date_result_tv.setText(bookingDataList.get(position).getB_date());
        holder.booking_time_result_tv.setText(bookingDataList.get(position).getB_time());
        holder.service_type_result_tv.setText(bookingDataList.get(position).getS_type());
        holder.assigned_service_center_result_tv.setText(bookingDataList.get(position).getA_service());

        final String c_id = bookingDataList.get(position).getC_id();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String c_name = bookingDataList.get(position).getC_name();
                String b_time = bookingDataList.get(position).getB_time();
                String p_required = bookingDataList.get(position).getP_required();
                String s_type = bookingDataList.get(position).getS_type();
                String b_date = bookingDataList.get(position).getB_date();
                String a_service = bookingDataList.get(position).getA_service();

                alertEditPopUp(c_id, c_name, b_time, p_required, s_type, b_date, a_service);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(c_id, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingDataList.size();
    }


//    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        //TV Title
        private TextView customer_name_tv;
        private TextView product_required_tv;
        private TextView booking_date_tv;
        private TextView booking_time_tv;
        private TextView service_type_tv;
        private TextView assigned_service_center_tv;

        //TV Results
        private TextView customer_name_result_tv;
        private TextView product_required_result_tv;
        private TextView booking_date_result_tv;
        private TextView booking_time_result_tv;
        private TextView service_type_result_tv;
        private TextView assigned_service_center_result_tv;

        //IV
        private ImageView delete_iv;
        private ImageView edit_iv;

        private void textViewColor() {
            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/

            Shader textShader = new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#000000"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);

            customer_name_tv.getPaint().setShader(textShader);
            product_required_tv.getPaint().setShader(textShader);
            booking_date_tv.getPaint().setShader(textShader);
            booking_time_tv.getPaint().setShader(textShader);
            service_type_tv.getPaint().setShader(textShader);
            assigned_service_center_tv.getPaint().setShader(textShader);

        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Title TextViews
            customer_name_tv = itemView.findViewById(R.id.customer_name_tv);
            product_required_tv = itemView.findViewById(R.id.product_required_tv);
            booking_date_tv = itemView.findViewById(R.id.booking_date_tv);
            booking_time_tv = itemView.findViewById(R.id.booking_time_tv);
            service_type_tv = itemView.findViewById(R.id.service_type_tv);
            assigned_service_center_tv = itemView.findViewById(R.id.assigned_service_center_tv);


            //image
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);

            //Result TextViews
            customer_name_result_tv = itemView.findViewById(R.id.customer_name_result_tv);
            product_required_result_tv = itemView.findViewById(R.id.product_required_result_tv);
            booking_date_result_tv = itemView.findViewById(R.id.booking_date_result_tv);
            booking_time_result_tv = itemView.findViewById(R.id.booking_time_result_tv);
            service_type_result_tv = itemView.findViewById(R.id.service_type_result_tv);
            assigned_service_center_result_tv = itemView.findViewById(R.id.assigned_service_center_result_tv);

            textViewColor();

        }
    }

    private void alertDeletePopUp(final String id, final int position) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                commonhelper.ShowLoader();
                // Write your code here to invoke YES event
                deleteNetworkCall(id, position);

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void deleteNetworkCall(String id, final int position) {

        Call<DeleteResponse> call = apiService.deleteBooking(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    bookingDataList.remove(position);
                    notifyDataSetChanged();
                    commonhelper.ShowMesseage(deleteData.getMessage());
                    commonhelper.HideLoader();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                commonhelper.ShowMesseage(t.getMessage());
                commonhelper.HideLoader();
            }
        });
    }

    private void alertEditPopUp(final String c_id, final String c_name, final String b_time, final String p_required, final String s_type, final String b_date, final String a_service){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Confirm Edit...");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Edit this?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_edit);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                commonhelper.setSharedPreferences("c_id", c_id);
                commonhelper.setSharedPreferences("c_name", c_name);
                commonhelper.setSharedPreferences("b_time", b_time);
                commonhelper.setSharedPreferences("p_required", p_required);
                commonhelper.setSharedPreferences("s_type", s_type);
                commonhelper.setSharedPreferences("b_date", b_date);
                commonhelper.setSharedPreferences("a_service", a_service);


                //Intent
                Intent intent = new Intent(context, BookingCardEditActivity.class);
                context.startActivity(intent);
                // Write your code here to invoke YES event
                Toast.makeText(context, "Welcome to Edit Screen", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


}
