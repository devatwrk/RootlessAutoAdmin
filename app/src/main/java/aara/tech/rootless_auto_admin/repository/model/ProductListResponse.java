package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class ProductListResponse {

    private String Status;
    private List<ProductListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<ProductListData> getData() {
        return data;
    }

    public void setData(List<ProductListData> data) {
        this.data = data;
    }
}
