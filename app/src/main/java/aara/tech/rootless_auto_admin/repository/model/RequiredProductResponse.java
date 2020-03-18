package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class RequiredProductResponse {
    private String Status;
    private List<RequiredProductListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<RequiredProductListData> getData() {
        return data;
    }

    public void setData(List<RequiredProductListData> data) {
        this.data = data;
    }
}
