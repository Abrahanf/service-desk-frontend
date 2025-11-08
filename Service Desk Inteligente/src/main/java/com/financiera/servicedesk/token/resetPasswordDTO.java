package com.financiera.servicedesk.token;

import lombok.Data;

@Data
public class resetPasswordDTO {
    private String token;
    private String nuevaClave;
    private String nuevaClaveConfirmacion;
}