package aara.tech.rootless_auto_admin.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import aara.tech.rootless_auto_admin.ProductCardEditActivity;
import aara.tech.rootless_auto_admin.R;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.ProductListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{

    private Context context;
    private List<ProductListData> productDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;

    public ProductListAdapter(Context context, List<ProductListData> productDataList) {
        this.context = context;
        this.productDataList = productDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_product, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new ProductListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.product_name_result_tv.setText(productDataList.get(position).getProduct_name());
        holder.man_hours_result_tv.setText(productDataList.get(position).getMan_hours());
        holder.complexity_result_tv.setText(productDataList.get(position).getComplexity());
        holder.service_charge_result_tv.setText(productDataList.get(position).getService_charge());
        holder.description_result_tv.setText(productDataList.get(position).getDescription());
        holder.service_time_result_tv.setText(productDataList.get(position).getService_time());

        final String product_id = productDataList.get(position).getProduct_id();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String product_name = productDataList.get(position).getProduct_name();
                final String man_hours = productDataList.get(position).getMan_hours();
                final String complexity = productDataList.get(position).getComplexity();
                final String service_charge = productDataList.get(position).getService_charge();
                final String description = productDataList.get(position).getDescription();
                final String service_time = productDataList.get(position).getService_time();
                alertEditPopUp(product_id, product_name, man_hours, complexity, service_charge, description, service_time);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(product_id, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    //    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView product_name_result_tv;
        private TextView man_hours_result_tv;
        private TextView complexity_result_tv;
        private TextView service_charge_result_tv;
        private TextView description_result_tv;
        private TextView service_time_result_tv;
        private ImageView delete_iv;
        private ImageView edit_iv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name_result_tv = itemView.findViewById(R.id.product_name_result_tv);
            man_hours_result_tv = itemView.findViewById(R.id.man_hours_result_tv);
            complexity_result_tv = itemView.findViewById(R.id.complexity_result_tv);
            service_charge_result_tv = itemView.findViewById(R.id.service_charge_result_tv);
            description_result_tv = itemView.findViewById(R.id.description_result_tv);
            service_time_result_tv = itemView.findViewById(R.id.service_date_result_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);

        }
    }




    private void alertDeletePopUp(final String product_id, final int position){

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
                deleteNetworkCall(product_id, position);

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

    private void deleteNetworkCall(String product_id, final int position) {

        Call<DeleteResponse> call = apiService.deleteProduct(product_id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    productDataList.remove(position);
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

    private void alertEditPopUp(final String product_id, final String product_name, final String man_hours, final String complexity, final String service_charge, final String description, final String service_time){

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
                commonhelper.setSharedPreferences("product_id", product_id);
                commonhelper.setSharedPreferences("product_name", product_name);
                commonhelper.setSharedPreferences("man_hours", man_hours);
                commonhelper.setSharedPreferences("complexity", complexity);
                commonhelper.setSharedPreferences("service_charge", service_charge);
                commonhelper.setSharedPreferences("description", description);
                commonhelper.setSharedPreferences("service_time", service_time);
                //Intent
                Intent intent = new Intent(context, ProductCardEditActivity.class);
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
