package aara.tech.rootless_auto_admin.repository.model;

public class UpdateProductResponse {
     String Status;
     UpdateProductData data;

     public String getStatus() {
          return Status;
     }

     public void setStatus(String status) {
          Status = status;
     }

     public UpdateProductData getData() {
          return data;
     }

     public void setData(UpdateProductData data) {
          this.data = data;
     }
}
