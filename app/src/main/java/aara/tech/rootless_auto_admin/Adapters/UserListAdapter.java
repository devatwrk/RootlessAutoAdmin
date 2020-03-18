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
import aara.tech.rootless_auto_admin.UserCardEditActivity;
import aara.tech.rootless_auto_admin.VehicleCardEditActivity;
import aara.tech.rootless_auto_admin.repository.model.DeleteData;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.UserListData;
import aara.tech.rootless_auto_admin.repository.remote.ApiService;
import aara.tech.rootless_auto_admin.repository.remote.ApiUtils;
import aara.tech.rootless_auto_admin.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    private Context context;
    private List<UserListData> userListData;
    private ApiService apiService;
    private Commonhelper commonhelper;


    public UserListAdapter(Context context, List<UserListData> userListData) {
        this.context = context;
        this.userListData = userListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_users, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.uname_result_tv.setText(userListData.get(position).getUname());
        holder.email_result_tv.setText(userListData.get(position).getEmail());
        holder.mobile_result_tv.setText(userListData.get(position).getMobile());
        holder.address_result_tv.setText(userListData.get(position).getAddress());

        final String id = userListData.get(position).getId();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userListData.get(position).getEmail();
                alertEditPopUp(id, email);
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
    public int getItemCount() {
        return userListData.size();
    }


    //    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView email_tv;
        private TextView uname_tv;
        private TextView mobile_tv;
        private TextView address_tv;
        private TextView email_result_tv;
        private TextView uname_result_tv;
        private TextView mobile_result_tv;
        private TextView address_result_tv;

        private ImageView delete_iv;
        private ImageView edit_iv;

        private void textViewColor() {
            /*Shader textShader=new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);*/

            Shader textShader = new LinearGradient(0, 0, 150, 20,
                    new int[]{Color.parseColor("#000000"), Color.BLUE},
                    new float[]{0, 1}, Shader.TileMode.CLAMP);

            email_tv.getPaint().setShader(textShader);
            uname_tv.getPaint().setShader(textShader);
            mobile_tv.getPaint().setShader(textShader);
            address_tv.getPaint().setShader(textShader);


        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Result TextViews
            email_result_tv = itemView.findViewById(R.id.email_result_tv);
            uname_result_tv = itemView.findViewById(R.id.uname_result_tv);
            mobile_result_tv = itemView.findViewById(R.id.mobile_result_tv);
            address_result_tv = itemView.findViewById(R.id.address_result_tv);

            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);

            //Title TextViews
            email_tv = itemView.findViewById(R.id.email_tv);
            uname_tv = itemView.findViewById(R.id.uname_tv);
            mobile_tv = itemView.findViewById(R.id.mobile_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            edit_iv.setVisibility(View.GONE);
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
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void deleteNetworkCall(String id, final int position) {

        Call<DeleteResponse> call = apiService.deleteUser(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful()) {
                    DeleteData deleteData = response.body().getData();
                    if (!deleteData.getMessage().equals("Failed Delete")) {
                        userListData.remove(position);
                        notifyDataSetChanged();
                        commonhelper.ShowMesseage(deleteData.getMessage());
                        commonhelper.HideLoader();

                    }else{
                        commonhelper.ShowMesseage(deleteData.getMessage());
                        commonhelper.HideLoader();
                    }


                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                commonhelper.ShowMesseage(t.getMessage());
                commonhelper.HideLoader();
            }
        });
    }

    private void alertEditPopUp(final String id, final String user_email){

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
                Intent intent = new Intent(context, UserCardEditActivity.class);
                commonhelper.setSharedPreferences("user_id", id);
                commonhelper.setSharedPreferences("user_email", user_email);
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
