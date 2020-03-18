package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class StateResponse {
    private String Status;
    private List<StateData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<StateData> getData() {
        return data;
    }

    public void setData(List<StateData> data) {
        this.data = data;
    }
}
