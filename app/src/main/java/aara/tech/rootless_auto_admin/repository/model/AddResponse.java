package aara.tech.rootless_auto_admin.repository.model;

public class AddResponse {
    private String Status;
    private AddData data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public AddData getData() {
        return data;
    }

    public void setData(AddData data) {
        this.data = data;
    }
}
