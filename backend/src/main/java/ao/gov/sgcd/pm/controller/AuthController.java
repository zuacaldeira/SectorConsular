package ao.gov.sgcd.pm.controller;

import ao.gov.sgcd.pm.config.JwtTokenProvider;
import ao.gov.sgcd.pm.dto.AuthRequestDTO;
import ao.gov.sgcd.pm.dto.AuthResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    @Value("${sgcd-pm.stakeholder.token}")
    private String stakeholderToken;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        // Simple hardcoded auth for this management tool
        String role;
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            role = "DEVELOPER";
        } else if ("stakeholder".equals(request.getUsername()) && "stakeholder2026".equals(request.getPassword())) {
            role = "STAKEHOLDER";
        } else {
            return ResponseEntity.status(401).build();
        }

        String token = tokenProvider.generateToken(request.getUsername(), role);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .role(role)
                .expiresIn(tokenProvider.getExpiration())
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String role = authentication.getAuthorities().iterator().next().getAuthority()
                .replace("ROLE_", "");
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "role", role
        ));
    }
}
