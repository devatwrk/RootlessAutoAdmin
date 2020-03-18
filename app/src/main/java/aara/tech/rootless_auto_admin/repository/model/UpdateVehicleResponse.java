package aara.tech.rootless_auto_admin.repository.model;

public class UpdateVehicleResponse {
    private String Status;
    private UpdateVehicleData data;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UpdateVehicleData getData() {
        return data;
    }

    public void setData(UpdateVehicleData data) {
        this.data = data;
    }
}
