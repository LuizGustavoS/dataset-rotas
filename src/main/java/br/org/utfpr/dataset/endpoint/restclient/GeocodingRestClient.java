package br.org.utfpr.dataset.endpoint.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@RegisterProvider(DataCubeExceptionHandle.class)
@RegisterRestClient(baseUri = "https://maps.googleapis.com/maps/api/geocode/json")
public interface GeocodingRestClient {

    @GET
    JSONObject consultarGeocoding(@QueryParam("key") String key, @QueryParam("components") String components);

}
