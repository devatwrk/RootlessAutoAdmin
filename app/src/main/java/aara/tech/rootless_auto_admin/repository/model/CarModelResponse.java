package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CarModelResponse {
    private String Status;
    private List<CarModelListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CarModelListData> getData() {
        return data;
    }

    public void setData(List<CarModelListData> data) {
        this.data = data;
    }
}
