package br.org.utfpr.dataset.controller;

import br.org.utfpr.dataset.dto.BairroDTO;
import br.org.utfpr.dataset.dto.DistanciaDTO;
import br.org.utfpr.dataset.endpoint.restclient.GeocodingRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DatasetController {

    public static final int EARTH_RADIUS_KM = 6371;

    @ConfigProperty(name = "GOOGLE_KEY")
    String GOOGLE_KEY;

    @Inject
    @RestClient
    GeocodingRestClient geocodingRestClient;

    public void gerarDataset(){

        List<BairroDTO> bairros = new ArrayList<>();
        List<DistanciaDTO> distancias = new ArrayList<>();

        List<String> listBairros = buscarListaBairros();

        for (String bairro : listBairros) {
            bairros.add(buscarCoodernadasBairro(bairro));
        }

        gravarArquivoJson(bairros, "bairros.json");

        for (BairroDTO bairro : bairros) {
            distancias.add(compararDistancias(bairro, bairros));
        }

        gravarArquivoJson(distancias, "distancias.json");
    }

    //--

    private List<String> buscarListaBairros(){

        List<String> listBairros = new ArrayList<>();
        final JSONObject result = geocodingRestClient.consultarComponents(GOOGLE_KEY, "postal_code:85660000");
        final JSONArray list = result.getJSONArray("results").optJSONObject(0).getJSONArray("postcode_localities");

        for (int i = 0; i < list.length(); i++) {
            listBairros.add(list.optString(i));
        }

        return listBairros;
    }

    private BairroDTO buscarCoodernadasBairro(String dsBairro){

        final JSONObject result = geocodingRestClient.consultarAddress(GOOGLE_KEY, "Dois Vizinhos " + dsBairro);
        final JSONObject location = result.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        return new BairroDTO(dsBairro, location.optDouble("lat"), location.optDouble("lng"));
    }

    private DistanciaDTO compararDistancias(BairroDTO bairroDTO, List<BairroDTO> result){

        List<DistanciaDTO> listDistancias = new ArrayList<>();
        for (BairroDTO adjacente : result) {
            if (bairroDTO.getBairro().equals(adjacente.getBairro())){
                continue;
            }

            final double diss = calcularDistancia(bairroDTO, adjacente);
            listDistancias.add(new DistanciaDTO(adjacente.getBairro(), diss));
        }

        return new DistanciaDTO(bairroDTO.getBairro(), listDistancias);
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

    private void gravarArquivoJson(Object result, String filename){

        try {
            final String payload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result);
            final Path path = Paths.get(System.getProperty("user.dir") + "/" + filename);
            Files.writeString(path, payload, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
