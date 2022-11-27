package br.org.utfpr.dataset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BairroDTO {

    private String bairro;

    private double latitude;

    private double longitude;

}
