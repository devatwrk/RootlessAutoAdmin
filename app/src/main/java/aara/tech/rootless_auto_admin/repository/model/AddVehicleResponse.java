package aara.tech.rootless_auto_admin.repository.model;

public class AddVehicleResponse {

    String Status;
    AddVehicleData data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public AddVehicleData getData() {
        return data;
    }

    public void setData(AddVehicleData data) {
        this.data = data;
    }
}
