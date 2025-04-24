package backend.main.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodStock {
    private Integer stockId;
    private String bloodType;
    private Integer quantity;
    private String status;
    private java.time.LocalDate expirationDate;
    private Integer bid;
    private String storageLocation;
}
