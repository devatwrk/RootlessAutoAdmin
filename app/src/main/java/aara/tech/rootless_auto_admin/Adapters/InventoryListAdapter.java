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

import aara.tech.rootless_auto_admin.InventoryCardEditActivity;
import aara.tech.rootless_auto_admin.MechanicCardEditActivity;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.InventoryListData;
import aara.tech.rootless_auto_admin.repository.model.MechanicListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListAdapter.ViewHolder>{

    private Context context;
    private List<InventoryListData> inventoryDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;

    public InventoryListAdapter(Context context, List<InventoryListData> inventoryDataList) {
        this.context = context;
        this.inventoryDataList = inventoryDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_inventory, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new InventoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.item_name_result_tv.setText(inventoryDataList.get(position).getItem_name());
        holder.brand_result_tv.setText(inventoryDataList.get(position).getBrand());
        holder.quantity_result_tv.setText(inventoryDataList.get(position).getQuantity());
        holder.vendor_result_tv.setText(inventoryDataList.get(position).getVendor());
        holder.barcode_result_tv.setText(inventoryDataList.get(position).getBar_code());
        holder.description_result_tv.setText(inventoryDataList.get(position).getDescription());

        final String inventory_id = inventoryDataList.get(position).getInventory_id();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item_name = inventoryDataList.get(position).getItem_name();
                String brand = inventoryDataList.get(position).getBrand();
                String quantity = inventoryDataList.get(position).getQuantity();
                String vendor = inventoryDataList.get(position).getVendor();
                String bar_code = inventoryDataList.get(position).getBar_code();
                String description = inventoryDataList.get(position).getDescription();

                alertEditPopUp(inventory_id, item_name, brand, description, quantity, vendor, bar_code);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(inventory_id, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return inventoryDataList.size();
    }

//    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        //TV Title
        private TextView item_name_tv;
        private TextView brand_tv;
        private TextView quantity_tv;
        private TextView vendor_charge_tv;
        private TextView barcode_tv;
        private TextView description_tv;

        //TV Results
        private TextView item_name_result_tv;
        private TextView brand_result_tv;
        private TextView quantity_result_tv;
        private TextView vendor_result_tv;
        private TextView barcode_result_tv;
        private TextView description_result_tv;

        //IV
        private ImageView delete_iv;
        private ImageView edit_iv;
        private ImageView item_pic_iv;

        private void textViewColor() {
            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/

            Shader textShader = new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#000000"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);

            item_name_tv.getPaint().setShader(textShader);
            brand_tv.getPaint().setShader(textShader);
            quantity_tv.getPaint().setShader(textShader);
            vendor_charge_tv.getPaint().setShader(textShader);
            barcode_tv.getPaint().setShader(textShader);
            description_tv.getPaint().setShader(textShader);

        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Title TextViews
            item_name_tv = itemView.findViewById(R.id.item_name_tv);
            brand_tv = itemView.findViewById(R.id.brand_tv);
            quantity_tv = itemView.findViewById(R.id.quantity_tv);
            vendor_charge_tv = itemView.findViewById(R.id.vendor_charge_tv);
            barcode_tv = itemView.findViewById(R.id.barcode_tv);
            description_tv = itemView.findViewById(R.id.description_tv);



            //image
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);
            item_pic_iv = itemView.findViewById(R.id.item_pic_iv);
//            mechanic_id_iv = itemView.findViewById(R.id.mechanic_id_iv);

            //Result TextViews
            item_name_result_tv = itemView.findViewById(R.id.item_name_result_tv);
            brand_result_tv = itemView.findViewById(R.id.brand_result_tv);
            quantity_result_tv = itemView.findViewById(R.id.quantity_result_tv);
            vendor_result_tv = itemView.findViewById(R.id.vendor_result_tv);
            barcode_result_tv = itemView.findViewById(R.id.barcode_result_tv);
            description_result_tv = itemView.findViewById(R.id.description_result_tv);

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

        Call<DeleteResponse> call = apiService.deleteInventory(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    inventoryDataList.remove(position);
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

    private void alertEditPopUp(final String inventory_id, final String item_name, final String brand, final String description, final String quantity, final String vendor, final String bar_code){

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
                commonhelper.setSharedPreferences("inventory_id", inventory_id);
                commonhelper.setSharedPreferences("item_name", item_name);
                commonhelper.setSharedPreferences("brand", brand);
                commonhelper.setSharedPreferences("description", description);
                commonhelper.setSharedPreferences("quantity", quantity);
                commonhelper.setSharedPreferences("vendor", vendor);
                commonhelper.setSharedPreferences("bar_code", bar_code);


                //Intent
                Intent intent = new Intent(context, InventoryCardEditActivity.class);
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
