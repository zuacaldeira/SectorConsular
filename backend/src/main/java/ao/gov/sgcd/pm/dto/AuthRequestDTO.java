package ao.gov.sgcd.pm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class AuthRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
