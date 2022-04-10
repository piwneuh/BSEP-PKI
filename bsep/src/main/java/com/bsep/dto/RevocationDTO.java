package com.bsep.dto;

import lombok.Data;

@Data
public class RevocationDTO {
    private String serialNumber;
    private String revocationReason;
}
