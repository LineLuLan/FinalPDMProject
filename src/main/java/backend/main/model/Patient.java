package backend.main.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String pssn;
    private Integer userId;
    private String name;
    private String bloodType;
    private Integer age;
    private String gender;
    private String phone;
    private String email;
    private String assignedDoctorId;
}
