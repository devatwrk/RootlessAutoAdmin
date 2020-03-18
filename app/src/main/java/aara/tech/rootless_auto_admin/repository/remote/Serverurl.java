package aara.tech.rootless_auto_admin.repository.remote;

public class Serverurl {

    private static String Server_Address = "http://softgentech.com/trainingapp/APIs/";
    public static String Server_Image_Path = "http://softgentech.com/trainingapp/media/report/";

    //Admin
    public static final String BASE_URL = "https://rootless.aaratechnologies.in/user/";
    public static final String IMAGE_BASE_URL_SERVICE_CENTER = "https://rootless.aaratechnologies.in/user/assets/uploads/service_center/";


    public static String ADD_SERVICE_API = BASE_URL + "service_center/api.php";
    public static String UPDATE_SERVICE_API = BASE_URL + "service_center/updateapi.php?Service_id=";
    public static String ADD_MECHANIC_API = BASE_URL + "mechanic/api.php";
    public static String Forget = Server_Address + "Forgot_Password/";
    public static String Reset_Password = Server_Address + "Reset_Password/";
    public static String Get_Profile = Server_Address + "GetProfile/";
    public static String Change_pass = Server_Address + "Change_Password/";
    public static String GetState = Server_Address + "GetState/";
    public static String GetCity = Server_Address + "GetCity/";
    public static String GetTrade = Server_Address + "GetTrade/";
    public static String GetProject = Server_Address + "GetProject/";
    public static String Add_DailyReport = Server_Address + "AddDailyReport/";
    public static String Add_Report_Image = Server_Address + "AddDailyReport_Image/";
    public static String Get_DailyReport = Server_Address + "GetDailyReport/";

}
