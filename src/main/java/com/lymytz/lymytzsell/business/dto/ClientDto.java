package com.lymytz.lymytzsell.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientDto {
    private Long id;
    private String codeClient;
    private String nomUsage;
    private String telephone;
}
