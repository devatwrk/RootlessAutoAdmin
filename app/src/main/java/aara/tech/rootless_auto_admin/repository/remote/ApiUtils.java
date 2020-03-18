package aara.tech.rootless_auto_admin.repository.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://rootless.aaratechnologies.in/user/";

    public static final String IMAGE_BASE_URL_SERVICE_CENTER = "https://rootless.aaratechnologies.in/user/assets/uploads/service_center/";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}
