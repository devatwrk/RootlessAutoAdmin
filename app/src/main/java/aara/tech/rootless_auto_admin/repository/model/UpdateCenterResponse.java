package aara.tech.rootless_auto_admin.repository.model;

public class UpdateCenterResponse {

    private String Status;
    private UpdateCenterData data;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UpdateCenterData getData() {
        return data;
    }

    public void setData(UpdateCenterData data) {
        this.data = data;
    }
}

