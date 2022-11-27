package br.org.utfpr.dataset.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class DistanciaDTO {

    private String bairro;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double distancia;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DistanciaDTO> bairros;

    public DistanciaDTO(String bairro, double distancia) {
        this.bairro = bairro;
        this.distancia = distancia;
    }

    public DistanciaDTO(String bairro, List<DistanciaDTO> bairros) {
        this.bairro = bairro;
        this.bairros = bairros;
    }
}
