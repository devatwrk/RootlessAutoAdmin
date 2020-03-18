package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CityResponse {
    private String Status;
    private List<CityData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CityData> getData() {
        return data;
    }

    public void setData(List<CityData> data) {
        this.data = data;
    }
}
