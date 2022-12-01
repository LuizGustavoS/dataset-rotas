package br.org.utfpr.dataset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class UtilController {

    public void gravarArquivoJson(Object result, String filename){

        try {
            final String payload = new ObjectMapper()
                    .registerModule(new JsonOrgModule())
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(result);

            final Path path = Paths.get(System.getProperty("user.dir") + "/" + filename);
            Files.writeString(path, payload, StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
