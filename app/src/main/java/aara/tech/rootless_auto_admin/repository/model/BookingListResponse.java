package aara.tech.rootless_auto_admin.repository.model;

import java.util.List;

public class BookingListResponse {
    private String Status;
    private List<BookingListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<BookingListData> getData() {
        return data;
    }

    public void setData(List<BookingListData> data) {
        this.data = data;
    }
}
