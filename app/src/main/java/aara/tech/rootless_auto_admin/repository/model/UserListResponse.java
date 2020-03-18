package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class UserListResponse {
    private String Status;
    private List<UserListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<UserListData> getData() {
        return data;
    }

    public void setData(List<UserListData> data) {
        this.data = data;
    }
}
