package com.lymytz.lymytzsell.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class FactureDto {
    private Long idEntete;
    private Date dateEntete;
    private ClientDto client;
    private String typeDoc;
    private Date dateLivraisonPrevu;
    private List<ContentFactureDto> contentFacture;

}
