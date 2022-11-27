package br.org.utfpr.dataset.controller;

import br.org.utfpr.dataset.dto.DatasetDTO;
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

    @ConfigProperty(name = "GOOGLE_KEY")
    String GOOGLE_KEY;

    @ConfigProperty(name = "JSON_PATH")
    String JSON_PATH;

    @Inject
    @RestClient
    GeocodingRestClient geocodingRestClient;

    public void gerarDataset(){

        List<DatasetDTO> result = new ArrayList<>();
        List<String> listBairros = buscarListaBairros();

        for (String bairro : listBairros) {
            result.add(buscarCoodernadasBairro(bairro));
        }

        gravarArquivoJson(result);
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

    private DatasetDTO buscarCoodernadasBairro(String dsBairro){

        final JSONObject result = geocodingRestClient.consultarAddress(GOOGLE_KEY, "Dois Vizinhos " + dsBairro);
        final JSONObject location = result.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
        return new DatasetDTO(dsBairro, location.optString("lat"), location.optString("lng"));
    }

    private void gravarArquivoJson(List<DatasetDTO> result){

        try {
            final String payload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result);
            final Path path = Paths.get(JSON_PATH + System.currentTimeMillis() + ".json");
            Files.writeString(path, payload, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
