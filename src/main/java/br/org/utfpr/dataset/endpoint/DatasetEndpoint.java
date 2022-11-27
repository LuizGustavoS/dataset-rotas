package br.org.utfpr.dataset.endpoint;

import br.org.utfpr.dataset.controller.DatasetController;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/dataset")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DatasetEndpoint {

    @Inject
    DatasetController datasetController;

    @POST
    public Response gerarDataset(){
        datasetController.gerarDataset();
        return Response.ok().build();
    }

}
