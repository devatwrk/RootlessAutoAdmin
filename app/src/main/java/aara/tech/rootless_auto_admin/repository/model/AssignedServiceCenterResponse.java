package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class AssignedServiceCenterResponse {
    private String Status;
    private List<AssignedServiceCenterListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<AssignedServiceCenterListData> getData() {
        return data;
    }

    public void setData(List<AssignedServiceCenterListData> data) {
        this.data = data;
    }
}
