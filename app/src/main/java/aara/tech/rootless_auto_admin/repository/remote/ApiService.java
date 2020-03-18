package aara.tech.rootless_auto_admin.repository.remote;

import aara.tech.rootless_auto_admin.repository.model.AddCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.AddResponse;
import aara.tech.rootless_auto_admin.repository.model.AddVehicleResponse;
import aara.tech.rootless_auto_admin.repository.model.AssignedServiceCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.BookingListResponse;
import aara.tech.rootless_auto_admin.repository.model.CarListResponse;
import aara.tech.rootless_auto_admin.repository.model.CarModelResponse;
import aara.tech.rootless_auto_admin.repository.model.CenterListResponse;
import aara.tech.rootless_auto_admin.repository.model.CityResponse;
import aara.tech.rootless_auto_admin.repository.model.CountryCodeData;
import aara.tech.rootless_auto_admin.repository.model.CountryCodeResponse;
import aara.tech.rootless_auto_admin.repository.model.CountryResponse;
import aara.tech.rootless_auto_admin.repository.model.CustomerNameResponse;
import aara.tech.rootless_auto_admin.repository.model.CustomerResponse;
import aara.tech.rootless_auto_admin.repository.model.DeleteCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.DeleteResponse;
import aara.tech.rootless_auto_admin.repository.model.InventoryResponse;
import aara.tech.rootless_auto_admin.repository.model.LoginResponse;
import aara.tech.rootless_auto_admin.repository.model.MechanicListResponse;
import aara.tech.rootless_auto_admin.repository.model.ProductListResponse;
import aara.tech.rootless_auto_admin.repository.model.RequiredProductResponse;
import aara.tech.rootless_auto_admin.repository.model.StateResponse;
import aara.tech.rootless_auto_admin.repository.model.UpdateCenterResponse;
import aara.tech.rootless_auto_admin.repository.model.UpdateProductResponse;
import aara.tech.rootless_auto_admin.repository.model.UpdateVehicleResponse;
import aara.tech.rootless_auto_admin.repository.model.UserListResponse;
import aara.tech.rootless_auto_admin.repository.model.VehicleListResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

//AUTH..............................................................................................
    @FormUrlEncoded
    @POST("Api.php?apicall=admin")
    Call<LoginResponse> login (
            @Field("email") String email,
            @Field("password") String password
    );


//GET...............................................................................................
    @GET("service_center/api.php")
    Call<CenterListResponse> getCenterList();

    @GET("product/api.php")
    Call<ProductListResponse> getProductList();

    @GET("vehicle/api.php")
    Call<VehicleListResponse> getVehicleList();

    @GET("mechanic/api.php")
    Call<MechanicListResponse> getMechanicList();

    @GET("customer/api.php")
    Call<CustomerResponse> getCustomerList();

    @GET("inventory/api.php")
    Call<InventoryResponse> getInventoryList();

    @GET("booking/api.php")
    Call<BookingListResponse> getBookingList();

    @GET("admin/userapi.php")
    Call<UserListResponse> getUserList();
//..................................................................................................
    @GET("objects/countryapi.php")
    Call<CountryResponse> getCountry();

    @GET("objects/stateapi.php")
    Call<StateResponse> getState(
            @Query("country_id") String country_id
    );

    @GET("objects/cityapi.php")
    Call<CityResponse> getCity(
            @Query("state_id") String state_id
    );

    @GET("objects/numberapi.php")
    Call<CountryCodeResponse> getCountryCode(
            @Query("country_id") String country_id
    );

    @GET("objects/modelapi.php")
    Call<CarModelResponse> getCarModelList();

    @GET("objects/serviceapi.php")
    Call<AssignedServiceCenterResponse> getAssignedServiceCenterList();

    @GET("objects/carapi.php")
    Call<CarListResponse> getCarList();

    @GET("objects/productapi.php")
    Call<RequiredProductResponse> getRequiredProductList();

    @GET("objects/customerapi.php")
    Call<CustomerNameResponse> getCustomerNameList();
//..................................................................................................

//POST..............................................................................................
   /* @FormUrlEncoded
    @POST("service_center/updateapi.php")
    Call<AddCenterResponse> addServiceCenter(
            @Field("Service_name") String Service_name,
            @Field("p_contact") String p_contact,
            @Field("Country") String Country,
            @Field("Cont") String Cont,
            @Field("state") String state,
            @Field("email") String email,
            @Field("city") String city,
            @Part MultipartBody.Part service_photo,
            @Field("p_address") String p_address,
            @Field("model") String model,
            @Field("gpscoordinate") String gpscoordinate,
            @Field("service") String service,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time

            //            @Field("service_photo") String service_photo,
    );*/

    @Multipart
    @POST("service_center/api.php")
    Call<ResponseBody> addServiceCenter(
            @Part("Service_name") String Service_name,
            @Part("p_contact") String p_contact,
            @Part("Country") String Country,
            @Part("Cont") String Cont,
            @Part("state") String state,
            @Part("email") String email,
            @Part("city") String city,
            @Part MultipartBody.Part service_photo,
            @Part("p_address") String p_address,
            @Part("model") String model,
            @Part("gpscoordinate") String gpscoordinate,
            @Part("service") String service,
            @Part("start_time") String start_time,
            @Part("end_time") String end_time

            //            @Field("service_photo") String service_photo,
    );

    @FormUrlEncoded
    @POST("vehicle/api.php")
    Call<AddVehicleResponse> addVehicle(
            @Field("name") String name,
            @Field("model") String model,
            @Field("year") String year,
            @Field("engine") String engine,
            @Field("colour") String colour,
            @Field("mileage") String mileage
    );

    @FormUrlEncoded
    @POST("product/api.php")
    Call<AddResponse> addProduct(
            @Field("product_name") String product_name,
            @Field("man_hours") String man_hours,
            @Field("complexity") String complexity,
            @Field("service_charge") String service_charge,
            @Field("description") String description,
            @Field("service_time") String service_time
    );

    @FormUrlEncoded
    @POST("mechanic/api.php")
    Call<AddResponse> addMechanic(
            @Field("f_name") String f_name,
            @Field("l_name") String l_name,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("physical_ads") String physical_ads,
            @Field("location") String location,
            @Field("residence") String residence,
            @Field("p_contact") String p_contact,
            @Field("alt_contact") String alt_contact,
            @Field("email") String email,
            @Field("id_number") String id_number,
            @Field("id_image") String id_image,
            @Field("image") String image,
            @Field("model") String model,
            @Field("skills") String skills,
            @Field("service_center") String service_center,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time
    );

    @FormUrlEncoded
    @POST("customer/api.php")
    Call<AddResponse> addCustomer(
            @Field("f_name") String f_name,
            @Field("l_name") String l_name,
            @Field("photo") String photo,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("iD_no") String iD_no,
            @Field("iD_card_image") String iD_card_image,
            @Field("address") String address,
            @Field("p_contact") String p_contact,
            @Field("altr_contact") String altr_contact,
            @Field("email") String email,
            @Field("vehicle") String vehicle,
            @Field("gps_location") String gps_location

    );

    @FormUrlEncoded
    @POST("inventory/api.php")
    Call<AddResponse> addInventory(
            @Field("item_name") String item_name,
            @Field("brand") String brand,
            @Field("description") String description,
            @Field("quantity") String quantity,
            @Field("vendor") String vendor,
            @Field("photo") String photo,
            @Field("bar_code") String bar_code
    );

    @FormUrlEncoded
    @POST("booking/api.php")
    Call<AddResponse> addBooking(
            @Field("c_name") String c_name,
            @Field("b_time") String b_time,
            @Field("p_required") String p_required,
            @Field("s_type") String s_type,
            @Field("b_date") String b_date,
            @Field("a_service") String a_service
    );


//Update............................................................................................
    @FormUrlEncoded
    @PUT("service_center/updateapi.php")
    Call<UpdateCenterResponse> updateServiceCenter(
            @Field("Service_id") String Service_id,
            @Field("Service_name") String Service_name,
            @Field("p_contact") String p_contact,
            @Field("Country") String Country,
            @Field("Cont") String Cont,
            @Field("state") String state,
            @Field("email") String email,
            @Field("city") String city,
            @Field("service_photo") String service_photo,
            @Field("p_address") String p_address,
            @Field("model") String model,
            @Field("location") String location,
            @Field("service") String service,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time
    );

    @FormUrlEncoded
    @PUT("vehicle/api.php")
    Call<UpdateVehicleResponse> updateVehicle(
            @Field("id") String id,
            @Field("name") String name,
            @Field("model") String model,
            @Field("year") String year,
            @Field("engine") String engine,
            @Field("colour") String colour,
            @Field("mileage") String mileage
    );

    @FormUrlEncoded
    @PUT("product/api.php")
    Call<UpdateProductResponse> updateProduct(
            @Field("product_id") String product_id,
            @Field("product_name") String product_name,
            @Field("man_hours") String man_hours,
            @Field("complexity") String complexity,
            @Field("service_charge") String service_charge,
            @Field("description") String description,
            @Field("service_time") String service_time

    );

    @FormUrlEncoded
    @PUT("mechanic/api.php")
    Call<AddResponse> updateMechanic(
            @Field("service_id") String service_id,
            @Field("f_name") String f_name,
            @Field("l_name") String l_name,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("physical_ads") String physical_ads,
            @Field("location") String location,
            @Field("residence") String residence,
            @Field("p_contact") String p_contact,
            @Field("alt_contact") String alt_contact,
            @Field("email") String email,
            @Field("id_number") String id_number,
            @Field("id_image") String id_image,
            @Field("image") String image,
            @Field("model") String model,
            @Field("skills") String skills,
            @Field("service_center") String service_center,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time
    );

    @FormUrlEncoded
    @PUT("customer/api.php")
    Call<AddResponse> updateCustomer(
            @Field("customer_id") String customer_id,
            @Field("f_name") String f_name,
            @Field("l_name") String l_name,
            @Field("photo") String photo,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("iD_no") String iD_no,
            @Field("iD_card_image") String iD_card_image,
            @Field("address") String address,
            @Field("p_contact") String p_contact,
            @Field("altr_contact") String altr_contact,
            @Field("email") String email,
            @Field("vehicle") String vehicle,
            @Field("gps_location") String gps_location
    );

    @FormUrlEncoded
    @PUT("inventory/api.php")
    Call<AddResponse> updateInventory(
            @Field("inventory_id") String inventory_id,
            @Field("item_name") String item_name,
            @Field("brand") String brand,
            @Field("description") String description,
            @Field("quantity") String quantity,
            @Field("vendor") String vendor,
            @Field("photo") String photo,
            @Field("bar_code") String bar_code
    );

    @FormUrlEncoded
    @PUT("booking/api.php")
    Call<AddResponse> updateBooking(
            @Field("c_id") String c_id,
            @Field("c_name") String c_name,
            @Field("b_time") String b_time,
            @Field("p_required") String p_required,
            @Field("s_type") String s_type,
            @Field("b_date") String b_date,
            @Field("a_service") String a_service
    );


//Delete............................................................................................
    @DELETE("vehicle/api.php")
    Call<DeleteResponse> deleteVehicle(
            @Query("id") String id
    );

    @DELETE("service_center/api.php")
    Call<DeleteCenterResponse> deleteCenter(
            @Query("Service_id") String Service_id
    );

    @DELETE("product/api.php")
    Call<DeleteResponse> deleteProduct(
            @Query("product_id") String product_id
    );

    @DELETE("mechanic/api.php")
    Call<DeleteResponse> deleteMechanic(
            @Query("service_id") String service_id
    );

    @DELETE("inventory/api.php")
    Call<DeleteResponse> deleteInventory(
            @Query("inventory_id") String inventory_id
    );

    @DELETE("booking/api.php")
    Call<DeleteResponse> deleteBooking(
            @Query("c_id") String c_id
    );

    @DELETE("admin/userapi.php")
    Call<DeleteResponse> deleteUser(
            @Query("id") String id
    );

}
