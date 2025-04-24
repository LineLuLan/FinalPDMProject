package backend.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    private String dssn;
    private Integer userId;
    private String dname;
    private String specialization;
    private String email;
    private Integer bloodBankId;
}
