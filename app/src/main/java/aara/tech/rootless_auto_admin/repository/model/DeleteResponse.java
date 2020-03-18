package aara.tech.rootless_auto_admin.repository.model;

public class DeleteResponse {

    private String Status;
    private DeleteData data;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public DeleteData getData() {
        return data;
    }

    public void setData(DeleteData data) {
        this.data = data;
    }
}
