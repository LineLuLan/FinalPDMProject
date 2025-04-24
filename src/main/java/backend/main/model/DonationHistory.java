package backend.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationHistory {
    private Integer donationId;
    private String donorSsn;
    private Integer bid;
    private java.time.LocalDateTime date;
    private Integer quantity;
}
