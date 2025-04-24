package backend.main.model;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donor {
    private String donorSsn;
    private String name;
    private String phone;
    private Integer age;
    private String bloodType;
    private Double weight;
    private java.time.LocalDate lastDonationDate;
    private String healthStatus;
    private Boolean isEligible = true;
    private java.time.LocalDateTime registrationDate;
    private String email;
}