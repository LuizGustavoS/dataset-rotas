package br.org.utfpr.dataset.controller;

import br.org.utfpr.dataset.dto.BairroDTO;
import br.org.utfpr.dataset.endpoint.restclient.GeocodingRestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BairroController extends UtilController {

    @ConfigProperty(name = "GOOGLE_KEY")
    String GOOGLE_KEY;

    @Inject
    @RestClient
    GeocodingRestClient geocodingRestClient;

    public List<String> buscarListaBairros() {

        List<String> listBairros = new ArrayList<>();
        final JSONObject result = geocodingRestClient.consultarComponents(GOOGLE_KEY, "postal_code:85660000");
        final JSONArray list = result.getJSONArray("results").optJSONObject(0).getJSONArray("postcode_localities");

        for (int i = 0; i < list.length(); i++) {
            listBairros.add(list.optString(i));
        }

        return listBairros;
    }

    public void gerarNamesJson(List<String> listBairros){

        JSONObject data = new JSONObject();

        for (String bairro : listBairros) {
            JSONArray details = new JSONArray();
            details.put(bairro);
            details.put("Dois Vizinhos");

            data.put(bairro, details);
        }

        gravarArquivoJson(data, "names.json");
    }

    public List<BairroDTO> gerarCordinatesJson(List<String> listBairros){

        JSONObject data = new JSONObject();
        List<BairroDTO> list = new ArrayList<>();

        for (String bairro : listBairros) {

            final JSONObject result = geocodingRestClient.consultarAddress(GOOGLE_KEY, "Dois Vizinhos " + bairro);
            final JSONObject location = result.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

            final double lat = location.optDouble("lat");
            final double lng = location.optDouble("lng");

            list.add(new BairroDTO(bairro, lat, lng));

            JSONArray coordenadas = new JSONArray();
            coordenadas.put(lat);
            coordenadas.put(lng);

            data.put(bairro, coordenadas);
        }

        gravarArquivoJson(data, "coordinates.json");
        return list;
    }

}
