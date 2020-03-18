package aara.tech.rootless_auto_admin.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import aara.tech.rootless_auto_admin.CenterCardEditActivity;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.CenterListData;
import aara.tech.rootless_auto_admin.repository.model.DeleteCenterData;
import aara.tech.rootless_auto_admin.repository.model.DeleteCenterResponse;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CenterListAdapter extends RecyclerView.Adapter<CenterListAdapter.ViewHolder> {

    private Context context;
    private List<CenterListData> centerListData;
    private ApiService apiService;
    private ApiUtils apiUtils;
    private String IMG_URL;
    private Commonhelper commonhelper;

    public CenterListAdapter(Context context, List<CenterListData> centerListData) {
        this.context = context;
        this.centerListData = centerListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_center, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        apiUtils = new ApiUtils();
        return new CenterListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.center_name_tv.setText(centerListData.get(position).getService_name());
        holder.country_result_tv.setText(centerListData.get(position).getCountry());
        holder.state_result_tv.setText(centerListData.get(position).getState());
        holder.city_result_tv.setText(centerListData.get(position).getCity());
        holder.address_result_tv.setText(centerListData.get(position).getP_address());
        holder.contact_result_tv.setText(centerListData.get(position).getP_contact());
        holder.alt_contact_result_tv.setText(centerListData.get(position).getCont());
        holder.email_result_tv.setText(centerListData.get(position).getEmail());
        holder.model_result_tv.setText(centerListData.get(position).getModel());
        holder.location_result_tv.setText(centerListData.get(position).getGpscoordinate());
        holder.services_offered_result_tv.setText(centerListData.get(position).getService());
        holder.start_time_result_tv.setText(centerListData.get(position).getStart_time());
        holder.end_time_result_tv.setText(centerListData.get(position).getEnd_time());
        IMG_URL = ApiUtils.IMAGE_BASE_URL_SERVICE_CENTER + centerListData.get(position).getService_photo();
        //Picasso
        Picasso.get()
                .load(IMG_URL)
                .placeholder(R.drawable.ic_car_repair_four)
                .error(R.drawable.ic_car_repair_six)
                .into(holder.center_photo_iv);

//        String imgbit = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB\\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB\\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCBBADDADASIA\\nAhEBAxEB/8QAHwAAAgIDAAMB";
//        //Image into imageview
//        Bitmap bm = StringToBitMap(imgbit);
//        holder.center_photo_iv.setImageBitmap(bm);



        final String Service_id = centerListData.get(position).getService_id();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = centerListData.get(position).getService_name();
                alertEditPopUp(Service_id, name);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(Service_id, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return centerListData.size();
    }


//    public Bitmap StringToBitMap(String encodedString) {
//        try {
//            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch (Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }

    //    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView center_name_tv;
        private TextView country_tv;
        private TextView state_tv;
        private TextView city_tv;
        private TextView address_tv;
        private TextView contact_tv;
        private TextView alt_contact_tv;
        private TextView email_tv;
        private TextView model_tv;
        private TextView location_tv;
        private TextView services_offered_tv;
        private TextView start_time_tv;
        private TextView end_time_tv;

        private TextView country_result_tv;
        private TextView state_result_tv;
        private TextView city_result_tv;
        private TextView address_result_tv;
        private TextView contact_result_tv;
        private TextView alt_contact_result_tv;
        private TextView email_result_tv;
        private TextView model_result_tv;
        private TextView location_result_tv;
        private TextView services_offered_result_tv;
        private TextView start_time_result_tv;
        private TextView end_time_result_tv;

        private ImageView delete_iv;
        private ImageView edit_iv;
        private ImageView center_photo_iv;

        private void textViewColor() {
            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/

            Shader textShader = new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#000000"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);
            country_tv.getPaint().setShader(textShader);
            state_tv.getPaint().setShader(textShader);
            city_tv.getPaint().setShader(textShader);
            address_tv.getPaint().setShader(textShader);
            contact_tv.getPaint().setShader(textShader);
            alt_contact_tv.getPaint().setShader(textShader);
            email_tv.getPaint().setShader(textShader);
            model_tv.getPaint().setShader(textShader);
            location_tv.getPaint().setShader(textShader);
            services_offered_tv.getPaint().setShader(textShader);
            start_time_tv.getPaint().setShader(textShader);
            end_time_tv.getPaint().setShader(textShader);

        }


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            country_result_tv = itemView.findViewById(R.id.country_result_tv);
            state_result_tv = itemView.findViewById(R.id.state_result_tv);
            city_result_tv = itemView.findViewById(R.id.city_result_tv);
            address_result_tv = itemView.findViewById(R.id.address_result_tv);
            contact_result_tv = itemView.findViewById(R.id.contact_result_tv);
            alt_contact_result_tv = itemView.findViewById(R.id.alt_contact_result_tv);
            email_result_tv = itemView.findViewById(R.id.email_result_tv);
            model_result_tv = itemView.findViewById(R.id.model_result_tv);
            location_result_tv = itemView.findViewById(R.id.location_result_tv);
            services_offered_result_tv = itemView.findViewById(R.id.services_offered_result_tv);
            start_time_result_tv = itemView.findViewById(R.id.start_time_result_tv);
            end_time_result_tv = itemView.findViewById(R.id.end_time_result_tv);


            country_tv = itemView.findViewById(R.id.country_tv);
            state_tv = itemView.findViewById(R.id.state_tv);
            city_tv = itemView.findViewById(R.id.city_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            contact_tv = itemView.findViewById(R.id.contact_tv);
            alt_contact_tv = itemView.findViewById(R.id.alt_contact_tv);
            email_tv = itemView.findViewById(R.id.email_tv);
            model_tv = itemView.findViewById(R.id.model_tv);
            location_tv = itemView.findViewById(R.id.location_tv);
            services_offered_tv = itemView.findViewById(R.id.services_offered_tv);
            start_time_tv = itemView.findViewById(R.id.start_time_tv);
            end_time_tv = itemView.findViewById(R.id.end_time_tv);

            center_name_tv = itemView.findViewById(R.id.center_name_tv);

            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);
            center_photo_iv = itemView.findViewById(R.id.center_iv);

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
                Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void deleteNetworkCall(String id, final int position) {

        Call<DeleteCenterResponse> call = apiService.deleteCenter(id);
        call.enqueue(new Callback<DeleteCenterResponse>() {
            @Override
            public void onResponse(Call<DeleteCenterResponse> call, Response<DeleteCenterResponse> response) {
                if (response.isSuccessful()) {
                    DeleteCenterData deleteCenterData = response.body().getData();
                    centerListData.remove(position);
                    notifyDataSetChanged();
                    commonhelper.ShowMesseage(deleteCenterData.getMessage());
                    commonhelper.HideLoader();

                }
            }

            @Override
            public void onFailure(Call<DeleteCenterResponse> call, Throwable t) {
                commonhelper.ShowMesseage(t.getMessage());
                commonhelper.HideLoader();
            }
        });
    }

    private void alertEditPopUp(final String Service_id, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Edit...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Edit this?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_edit);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, CenterCardEditActivity.class);
                intent.putExtra("Service_id", Service_id);
                intent.putExtra("name", name);
               /* intent.putExtra("name",year);
                intent.putExtra("name",model);
                intent.putExtra("name",engine);*/

                context.startActivity(intent);
                // Write your code here to invoke YES event
                Toast.makeText(context, "Welcome to Edit Screen", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


}
