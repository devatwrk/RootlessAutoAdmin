package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CustomerNameResponse {
    private String Status;
    private List<CustomerNameListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CustomerNameListData> getData() {
        return data;
    }

    public void setData(List<CustomerNameListData> data) {
        this.data = data;
    }
}
