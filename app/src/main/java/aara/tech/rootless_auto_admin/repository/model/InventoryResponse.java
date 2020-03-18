package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class InventoryResponse {
    private String Status;
    private List<InventoryListData> data;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<InventoryListData> getData() {
        return data;
    }

    public void setData(List<InventoryListData> data) {
        this.data = data;
    }
}
