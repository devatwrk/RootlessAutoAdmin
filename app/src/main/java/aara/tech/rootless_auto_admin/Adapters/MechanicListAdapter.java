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

import aara.tech.rootless_auto_admin.MechanicCardEditActivity;
import aara.tech.rootless_auto_admin.ProductCardEditActivity;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.VehicleCardEditActivity;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.MechanicListData;
import aara.tech.rootless_auto_admin.repository.model.VehicleListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class MechanicListAdapter extends RecyclerView.Adapter<MechanicListAdapter.ViewHolder> {

    private Context context;
    private List<MechanicListData> mechanicDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;

    public MechanicListAdapter(Context context, List<MechanicListData> mechanicDataList) {
        this.context = context;
        this.mechanicDataList = mechanicDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_mechanics, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new MechanicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        /*String service_id,
        String f_name,
        String l_name,
        String country,
        String state,
        String city,
        String physical_ads,
        String location,
        String residence,
        String p_contact,
        String alt_contact,
        String email,
        String id_number,
        String id_image,
        String image,
        String model,
        String skills,
        String service_center,
        String start_time,
        String end_time*/

        holder.first_name_result_tv.setText(mechanicDataList.get(position).getF_name());
        holder.last_name_result_tv.setText(mechanicDataList.get(position).getL_name());
        holder.id_number_result_tv.setText(mechanicDataList.get(position).getId_number());
        holder.country_result_tv.setText(mechanicDataList.get(position).getCountry());
        holder.state_result_tv.setText(mechanicDataList.get(position).getState());
        holder.city_result_tv.setText(mechanicDataList.get(position).getCity());
        holder.address_result_tv.setText(mechanicDataList.get(position).getPhysical_ads());
        holder.residence_result_tv.setText(mechanicDataList.get(position).getResidence());
        holder.contact_result_tv.setText(mechanicDataList.get(position).getP_contact());
        holder.alt_contact_result_tv.setText(mechanicDataList.get(position).getAlt_contact());
        holder.email_result_tv.setText(mechanicDataList.get(position).getEmail());
        holder.model_result_tv.setText(mechanicDataList.get(position).getModel());
        holder.service_center_result_tv.setText(mechanicDataList.get(position).getService_center());
        holder.skills_result_tv.setText(mechanicDataList.get(position).getSkills());
        holder.start_time_result_tv.setText(mechanicDataList.get(position).getStart_time());
        holder.end_time_result_tv.setText(mechanicDataList.get(position).getEnd_time());

        final String service_id = mechanicDataList.get(position).getService_id();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f_name = mechanicDataList.get(position).getF_name();
                String l_name = mechanicDataList.get(position).getL_name();
                String physical_ads = mechanicDataList.get(position).getPhysical_ads();
                String residence = mechanicDataList.get(position).getResidence();
                String p_contact = mechanicDataList.get(position).getP_contact();
                String alt_contact = mechanicDataList.get(position).getAlt_contact();
                String email = mechanicDataList.get(position).getEmail();
                String id_number = mechanicDataList.get(position).getId_number();
                String start_time = mechanicDataList.get(position).getStart_time();
                String end_time = mechanicDataList.get(position).getEnd_time();

                alertEditPopUp(service_id, f_name, l_name, physical_ads, residence, p_contact, alt_contact, email, id_number, start_time, end_time);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(service_id, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mechanicDataList.size();
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
        private TextView residence_tv;
        private TextView contact_tv;
        private TextView alt_contact_tv;
        private TextView email_tv;
        private TextView model_tv;
        private TextView service_center_tv;
        private TextView skills_tv;
        private TextView start_time_tv;
        private TextView end_time_tv;

        private TextView first_name_result_tv;
        private TextView last_name_result_tv;
        private TextView id_number_result_tv;
        private TextView country_result_tv;
        private TextView state_result_tv;
        private TextView city_result_tv;
        private TextView address_result_tv;
        private TextView residence_result_tv;
        private TextView contact_result_tv;
        private TextView alt_contact_result_tv;
        private TextView email_result_tv;
        private TextView model_result_tv;
        private TextView service_center_result_tv;
        private TextView skills_result_tv;
        private TextView start_time_result_tv;
        private TextView end_time_result_tv;


        private ImageView delete_iv;
        private ImageView edit_iv;
        private ImageView mechanic_iv;
        private ImageView mechanic_id_iv;

        private void textViewColor() {
            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/

            Shader textShader = new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#000000"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);

            first_name_tv.getPaint().setShader(textShader);
            last_name_tv.getPaint().setShader(textShader);
            id_number_tv.getPaint().setShader(textShader);
            country_tv.getPaint().setShader(textShader);
            state_tv.getPaint().setShader(textShader);
            city_tv.getPaint().setShader(textShader);
            address_tv.getPaint().setShader(textShader);
            residence_tv.getPaint().setShader(textShader);
            contact_tv.getPaint().setShader(textShader);
            alt_contact_tv.getPaint().setShader(textShader);
            email_tv.getPaint().setShader(textShader);
            model_tv.getPaint().setShader(textShader);
            service_center_tv.getPaint().setShader(textShader);
            skills_tv.getPaint().setShader(textShader);
            start_time_tv.getPaint().setShader(textShader);
            end_time_tv.getPaint().setShader(textShader);


        }

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
            residence_tv = itemView.findViewById(R.id.residence_tv);
            contact_tv = itemView.findViewById(R.id.contact_tv);
            alt_contact_tv = itemView.findViewById(R.id.alt_contact_tv);
            email_tv = itemView.findViewById(R.id.email_tv);
            model_tv = itemView.findViewById(R.id.model_tv);
            service_center_tv = itemView.findViewById(R.id.service_center_tv);
            skills_tv = itemView.findViewById(R.id.skills_tv);
            start_time_tv = itemView.findViewById(R.id.start_time_tv);
            end_time_tv = itemView.findViewById(R.id.end_time_tv);


            //image
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);
            mechanic_iv = itemView.findViewById(R.id.mechanic_iv);
//            mechanic_id_iv = itemView.findViewById(R.id.mechanic_id_iv);

            //Result TextViews
            first_name_result_tv = itemView.findViewById(R.id.first_name_result_tv);
            last_name_result_tv = itemView.findViewById(R.id.last_name_result_tv);
            id_number_result_tv = itemView.findViewById(R.id.id_number_result_tv);
            country_result_tv = itemView.findViewById(R.id.country_result_tv);
            state_result_tv = itemView.findViewById(R.id.state_result_tv);
            city_result_tv = itemView.findViewById(R.id.city_result_tv);
            address_result_tv = itemView.findViewById(R.id.address_result_tv);
            residence_result_tv = itemView.findViewById(R.id.residence_result_tv);
            contact_result_tv = itemView.findViewById(R.id.contact_result_tv);
            alt_contact_result_tv = itemView.findViewById(R.id.alt_contact_result_tv);
            email_result_tv = itemView.findViewById(R.id.email_result_tv);
            model_result_tv = itemView.findViewById(R.id.model_result_tv);
            service_center_result_tv = itemView.findViewById(R.id.service_center_result_tv);
            skills_result_tv = itemView.findViewById(R.id.skills_result_tv);
            start_time_result_tv = itemView.findViewById(R.id.start_time_result_tv);
            end_time_result_tv = itemView.findViewById(R.id.end_time_result_tv);

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

        Call<DeleteResponse> call = apiService.deleteMechanic(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    mechanicDataList.remove(position);
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

    private void alertEditPopUp(final String service_id, final String f_name, final String l_name, final String physical_ads, final String residence, final String p_contact, final String alt_contact, final String email, final String id_number, final String start_time, final String end_time){

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
                commonhelper.setSharedPreferences("service_id", service_id);
                commonhelper.setSharedPreferences("f_name", f_name);
                commonhelper.setSharedPreferences("l_name", l_name);
                commonhelper.setSharedPreferences("physical_ads", physical_ads);
                commonhelper.setSharedPreferences("residence", residence);
                commonhelper.setSharedPreferences("p_contact", p_contact);
                commonhelper.setSharedPreferences("alt_contact", alt_contact);
                commonhelper.setSharedPreferences("email", email);
                commonhelper.setSharedPreferences("id_number", id_number);
                commonhelper.setSharedPreferences("start_time", start_time);
                commonhelper.setSharedPreferences("end_time", end_time);

                //Intent
                Intent intent = new Intent(context, MechanicCardEditActivity.class);
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
