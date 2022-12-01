package br.org.utfpr.dataset.controller;

import br.org.utfpr.dataset.dto.BairroDTO;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DistanciaController extends UtilController {

    public static final int EARTH_RADIUS_KM = 6371;

    public void gerarNamesJson(List<BairroDTO> listBairros){

        JSONObject result = new JSONObject();

        for (BairroDTO bairro : listBairros) {
            JSONObject data = compararBairros(bairro, listBairros);
            result.put(bairro.getBairro(), data);
        }

        gravarArquivoJson(result, "distances.json");
    }

    //--

    private JSONObject compararBairros(BairroDTO bairroDTO, List<BairroDTO> listBairros){

        JSONObject data = new JSONObject();

        for (BairroDTO adjacente : listBairros) {

            if (bairroDTO.getBairro().equals(adjacente.getBairro())){
                continue;
            }

            final double distancia = calcularDistancia(bairroDTO, adjacente);
            data.put(adjacente.getBairro(), distancia);
        }

        return data;
    }

    private double calcularDistancia(BairroDTO bairroDTO1, BairroDTO bairroDTO2) {

        //https://thiagovespa.com.br/blog/2010/09/10/distancia-utilizando-coordenadas-geograficas-em-java/

        double firstLatToRad = Math.toRadians(bairroDTO1.getLatitude());
        double secondLatToRad = Math.toRadians(bairroDTO2.getLatitude());

        double deltaLongitudeInRad = Math.toRadians(bairroDTO2.getLongitude() - bairroDTO1.getLongitude());

        return Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
                * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
                * Math.sin(secondLatToRad))
                * EARTH_RADIUS_KM;
    }

}
