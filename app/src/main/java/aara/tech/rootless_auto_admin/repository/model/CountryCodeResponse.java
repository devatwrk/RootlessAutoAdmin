package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class CountryCodeResponse {

    private String Status;
    private List<CountryCodeData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CountryCodeData> getData() {
        return data;
    }

    public void setData(List<CountryCodeData> data) {
        this.data = data;
    }
}

