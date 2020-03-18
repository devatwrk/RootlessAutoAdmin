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

import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.VehicleCardEditActivity;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.VehicleListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {

    private Context context;
    private List<VehicleListData> vehicleDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;


    public VehicleListAdapter(Context context, List<VehicleListData> vehicleDataList) {
        this.context = context;
        this.vehicleDataList = vehicleDataList;
    }


    //..............................................................................................
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_vehicle, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new VehicleListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //        VehicleData vehicleData = vehicleDataList.get(position);

        holder.plate_result_tv.setText(vehicleDataList.get(position).getName());
        holder.year_result_tv.setText(vehicleDataList.get(position).getYear());
        holder.model_result_tv.setText(vehicleDataList.get(position).getModel());
        holder.engine_result_tv.setText(vehicleDataList.get(position).getEngine());
        holder.colour_result_tv.setText(vehicleDataList.get(position).getColour());
        holder.mileage_result_tv.setText(vehicleDataList.get(position).getMileage());

        final String id = vehicleDataList.get(position).getId();

         holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = vehicleDataList.get(position).getName();
                alertEditPopUp(id, name);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(id, position);
            }
        });
    }

    @Override
    public int getItemCount() { return vehicleDataList.size(); }


//    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView plate_tv;
        private TextView year_tv;
        private TextView model_tv;
        private TextView engine_tv;
        private TextView colour_tv;
        private TextView mileage_tv;

        private TextView plate_result_tv;
        private TextView year_result_tv;
        private TextView model_result_tv;
        private TextView engine_result_tv;
        private TextView colour_result_tv;
        private TextView mileage_result_tv;
        private ImageView delete_iv;
        private ImageView edit_iv;

        private void textViewColor() {
            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/

            Shader textShader = new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#000000"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);

            plate_tv.getPaint().setShader(textShader);
            model_tv.getPaint().setShader(textShader);
            year_tv.getPaint().setShader(textShader);
            engine_tv.getPaint().setShader(textShader);
            colour_tv.getPaint().setShader(textShader);
            mileage_tv.getPaint().setShader(textShader);

        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Result TextViews
            plate_result_tv = itemView.findViewById(R.id.plate_result_tv);
            model_result_tv = itemView.findViewById(R.id.model_result_tv);
            year_result_tv = itemView.findViewById(R.id.year_result_tv);
            engine_result_tv = itemView.findViewById(R.id.engine_result_tv);
            colour_result_tv = itemView.findViewById(R.id.colour_result_tv);
            mileage_result_tv = itemView.findViewById(R.id.mileage_result_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);

            //Title TextViews
            plate_tv = itemView.findViewById(R.id.plate_tv);
            model_tv = itemView.findViewById(R.id.model_tv);
            year_tv = itemView.findViewById(R.id.year_tv);
            engine_tv = itemView.findViewById(R.id.engine_tv);
            colour_tv = itemView.findViewById(R.id.colour_tv);
            mileage_tv = itemView.findViewById(R.id.mileage_tv);
            textViewColor();

        }
    }




    private void alertDeletePopUp(final String id, final int position){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
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

        Call<DeleteResponse> call = apiService.deleteVehicle(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    vehicleDataList.remove(position);
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

    private void alertEditPopUp(final String id, final String name){

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
                Intent intent = new Intent(context, VehicleCardEditActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                /*intent.putExtra("name",year);
                intent.putExtra("name",model);
                intent.putExtra("name",engine);
                */
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
