package aara.tech.rootless_auto_admin.repository.model;

public class AddCenterResponse {

    private String Status;
    private AddCenterData data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public AddCenterData getData() {
        return data;
    }

    public void setData(AddCenterData data) {
        this.data = data;
    }
}
