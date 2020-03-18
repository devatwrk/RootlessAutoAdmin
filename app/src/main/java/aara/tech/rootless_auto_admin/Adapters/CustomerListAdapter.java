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

import aara.tech.rootless_auto_admin.CustomerCardEditActivity;
import aara.tech.rootless_auto_admin.MechanicCardEditActivity;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.CustomerListData;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.MechanicListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private Context context;
    private List<CustomerListData> customerDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;

    public CustomerListAdapter(Context context, List<CustomerListData> customerDataList) {
        this.context = context;
        this.customerDataList = customerDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_customer, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new CustomerListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.first_name_result_tv.setText(customerDataList.get(position).getF_name());
        holder.last_name_result_tv.setText(customerDataList.get(position).getL_name());
        holder.id_number_result_tv.setText(customerDataList.get(position).getiD_no());
        holder.country_result_tv.setText(customerDataList.get(position).getCountry());
        holder.state_result_tv.setText(customerDataList.get(position).getState());
        holder.city_result_tv.setText(customerDataList.get(position).getCity());
        holder.address_result_tv.setText(customerDataList.get(position).getAddress());
        holder.contact_result_tv.setText(customerDataList.get(position).getP_contact());
        holder.alt_contact_result_tv.setText(customerDataList.get(position).getAltr_contact());
        holder.email_result_tv.setText(customerDataList.get(position).getEmail());
        holder.vehicle_result_tv.setText(customerDataList.get(position).getVehicle());
        holder.gps_result_tv.setText(customerDataList.get(position).getGps_location());


        final String customer_id = customerDataList.get(position).getCustomer_id();


        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f_name = customerDataList.get(position).getF_name();
                String l_name = customerDataList.get(position).getL_name();
                String address = customerDataList.get(position).getAddress();
                String p_contact = customerDataList.get(position).getP_contact();
                String altr_contact = customerDataList.get(position).getAltr_contact();
                String email = customerDataList.get(position).getEmail();
                String iD_no = customerDataList.get(position).getiD_no();


                alertEditPopUp(customer_id, f_name, l_name, address, p_contact, altr_contact, email, iD_no);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(customer_id, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerDataList.size();
    }


    //    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView first_name_tv;
        private TextView last_name_tv;
        private TextView id_number_tv;
        private TextView country_tv;
        private TextView state_tv;
        private TextView city_tv;
        private TextView address_tv;
        private TextView contact_tv;
        private TextView alt_contact_tv;
        private TextView email_tv;
        private TextView vehicle_tv;
        private TextView gps_tv;

        private TextView first_name_result_tv;
        private TextView last_name_result_tv;
        private TextView id_number_result_tv;
        private TextView country_result_tv;
        private TextView state_result_tv;
        private TextView city_result_tv;
        private TextView address_result_tv;
        private TextView contact_result_tv;
        private TextView alt_contact_result_tv;
        private TextView email_result_tv;
        private TextView vehicle_result_tv;
        private TextView gps_result_tv;


        private ImageView delete_iv;
        private ImageView edit_iv;


//        private void textViewColor() {
//            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
//                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
//                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/
//
//            Shader textShader = new LinearGradient(0, 0, 150, 20,
//                    new int[]{Color.parseColor("#000000"), Color.BLUE},
//                    new float[]{0, 1}, Shader.TileMode.CLAMP);
//
//            first_name_tv.getPaint().setShader(textShader);
//            last_name_tv.getPaint().setShader(textShader);
//            id_number_tv.getPaint().setShader(textShader);
//            country_tv.getPaint().setShader(textShader);
//            state_tv.getPaint().setShader(textShader);
//            city_tv.getPaint().setShader(textShader);
//            address_tv.getPaint().setShader(textShader);
//            residence_tv.getPaint().setShader(textShader);
//            contact_tv.getPaint().setShader(textShader);
//            alt_contact_tv.getPaint().setShader(textShader);
//            email_tv.getPaint().setShader(textShader);
//            model_tv.getPaint().setShader(textShader);
//            service_center_tv.getPaint().setShader(textShader);
//            skills_tv.getPaint().setShader(textShader);
//            start_time_tv.getPaint().setShader(textShader);
//            end_time_tv.getPaint().setShader(textShader);
//
//
//        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Title TextViews
            first_name_tv = itemView.findViewById(R.id.first_name_tv);
            last_name_tv = itemView.findViewById(R.id.last_name_tv);
            id_number_tv = itemView.findViewById(R.id.id_number_tv);
            country_tv = itemView.findViewById(R.id.country_tv);
            state_tv = itemView.findViewById(R.id.state_tv);
            city_tv = itemView.findViewById(R.id.city_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            contact_tv = itemView.findViewById(R.id.contact_tv);
            alt_contact_tv = itemView.findViewById(R.id.alt_contact_tv);
            email_tv = itemView.findViewById(R.id.email_tv);
            vehicle_tv = itemView.findViewById(R.id.vehicle_tv);
            gps_tv = itemView.findViewById(R.id.gps_tv);


            //image
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);

            //Result TextViews
            first_name_result_tv = itemView.findViewById(R.id.first_name_result_tv);
            last_name_result_tv = itemView.findViewById(R.id.last_name_result_tv);
            id_number_result_tv = itemView.findViewById(R.id.id_number_result_tv);
            country_result_tv = itemView.findViewById(R.id.country_result_tv);
            state_result_tv = itemView.findViewById(R.id.state_result_tv);
            city_result_tv = itemView.findViewById(R.id.city_result_tv);
            address_result_tv = itemView.findViewById(R.id.address_result_tv);
            contact_result_tv = itemView.findViewById(R.id.contact_result_tv);
            alt_contact_result_tv = itemView.findViewById(R.id.alt_contact_result_tv);
            email_result_tv = itemView.findViewById(R.id.email_result_tv);
            vehicle_result_tv = itemView.findViewById(R.id.vehicle_result_tv);
            gps_result_tv = itemView.findViewById(R.id.gps_result_tv);
//            textViewColor();

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

        Call<DeleteResponse> call = apiService.deleteMechanic(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    customerDataList.remove(position);
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

    private void alertEditPopUp(final String customer_id, final String f_name, final String l_name, final String address, final String p_contact, final String altr_contact, final String email, final String iD_no){

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
                commonhelper.setSharedPreferences("customer_id", customer_id);
                commonhelper.setSharedPreferences("f_name", f_name);
                commonhelper.setSharedPreferences("l_name", l_name);
                commonhelper.setSharedPreferences("address", address);
                commonhelper.setSharedPreferences("p_contact", p_contact);
                commonhelper.setSharedPreferences("altr_contact", altr_contact);
                commonhelper.setSharedPreferences("email", email);
                commonhelper.setSharedPreferences("iD_no", iD_no);

                //Intent
                Intent intent = new Intent(context, CustomerCardEditActivity.class);
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
