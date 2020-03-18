package aara.tech.rootless_auto_admin.repository.model;

public class DeleteCenterResponse {

    String Status;
    DeleteCenterData data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public DeleteCenterData getData() {
        return data;
    }

    public void setData(DeleteCenterData data) {
        this.data = data;
    }
}
