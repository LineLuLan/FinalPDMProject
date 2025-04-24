package backend.main.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodBank {
    private Integer bid;
    private String name;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private LocalDate lastInventoryDate;
}
