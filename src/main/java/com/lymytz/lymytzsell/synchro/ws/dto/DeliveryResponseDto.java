package com.lymytz.lymytzsell.synchro.ws.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryResponseDto implements Serializable {
    private Long id;
    private String status;
    private String statusLivraison;
}
