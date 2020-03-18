package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CountryResponse {
    private String Status;
    private List<CountryData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CountryData> getData() {
        return data;
    }

    public void setData(List<CountryData> data) {
        this.data = data;
    }
}
