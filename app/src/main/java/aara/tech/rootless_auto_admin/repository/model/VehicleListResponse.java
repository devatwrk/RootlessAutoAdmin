package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class VehicleListResponse {
    String Status;
    List<VehicleListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<VehicleListData> getData() {
        return data;
    }

    public void setData(List<VehicleListData> data) {
        this.data = data;
    }
}
