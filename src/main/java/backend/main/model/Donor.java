package backend.main.model;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donor {
    @com.fasterxml.jackson.annotation.JsonProperty("donor_ssn")
    private String donorSsn;
    private String name;
    private String phone;
    private Integer age;
    @com.fasterxml.jackson.annotation.JsonProperty("blood_type")
    private String bloodType;
    private Double weight;
    private java.time.LocalDate lastDonationDate;
    @com.fasterxml.jackson.annotation.JsonProperty("health_status")
    private String healthStatus;
    private Boolean isEligible = true;
    private java.time.LocalDateTime registrationDate;
    private String email;
}