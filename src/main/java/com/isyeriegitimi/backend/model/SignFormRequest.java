
package com.isyeriegitimi.backend.model;

import com.isyeriegitimi.backend.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignFormRequest {
    private UUID formId;
    private UUID userId;
    private Role userRole;
}

