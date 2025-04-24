package backend.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequest {
    private Integer requestId;
    private String dssn;
    private String pssn;
    private String bloodType;
    private Integer quantity;
    private String status;
    private java.time.LocalDate requestDate;
    private java.time.LocalDate responseDate;
}
