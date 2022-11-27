package br.org.utfpr.dataset.controller;

import br.org.utfpr.dataset.endpoint.restclient.GeocodingRestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DatasetController {

    @ConfigProperty(name = "GOOGLE_KEY")
    String GOOGLE_KEY;

    @Inject
    @RestClient
    GeocodingRestClient geocodingRestClient;

    public void gerarDataset(){

        final JSONObject resultBairros = geocodingRestClient.consultarGeocoding(GOOGLE_KEY, "postal_code:85660000");
        final JSONArray listBairros = resultBairros.getJSONArray("result").optJSONObject(0).getJSONArray("postcode_localities");

        for (int i = 0; i < listBairros.length(); i++) {
            System.out.println(listBairros.optString(i));
        }
    }

    //--

/*    private void gravarArquivoJson(List<BrasilAbertoBairroDTO> result){

        try {
            final String payload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result);
            final Path path = Paths.get(PATH + System.currentTimeMillis() + ".json");
            Files.writeString(path, payload, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
