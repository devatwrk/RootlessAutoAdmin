package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CenterListResponse {

    private String Status;
    private List<CenterListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CenterListData> getData() {
        return data;
    }

    public void setData(List<CenterListData> data) {
        this.data = data;
    }
}
