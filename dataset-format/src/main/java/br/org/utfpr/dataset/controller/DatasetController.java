package br.org.utfpr.dataset.controller;

import br.org.utfpr.dataset.dto.BairroDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class DatasetController {

    @Inject
    BairroController bairroController;

    @Inject
    DistanciaController distanciaController;

    public void gerarDataset(){

        List<String> listBairros = bairroController.buscarListaBairros();
        bairroController.gerarNamesJson(listBairros);

        List<BairroDTO> bairroDTOS = bairroController.gerarCordinatesJson(listBairros);
        distanciaController.gerarNamesJson(bairroDTOS);
    }

    //--







}
