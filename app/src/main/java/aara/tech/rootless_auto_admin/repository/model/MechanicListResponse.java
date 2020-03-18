package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class MechanicListResponse {
    private String Status;
    private List<MechanicListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<MechanicListData> getData() {
        return data;
    }

    public void setData(List<MechanicListData> data) {
        this.data = data;
    }
}
