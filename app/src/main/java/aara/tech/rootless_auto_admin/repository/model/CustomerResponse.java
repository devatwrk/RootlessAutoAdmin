package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CustomerResponse {

    private String Status;
    private List<CustomerListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CustomerListData> getData() {
        return data;
    }

    public void setData(List<CustomerListData> data) {
        this.data = data;
    }
}
