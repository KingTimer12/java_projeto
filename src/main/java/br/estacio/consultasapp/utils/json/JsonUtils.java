package br.estacio.consultasapp.utils.json;

import br.estacio.consultasapp.utils.json.interfaces.IJson;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtils {

    public static void saveToJsonFile(String filename, IJson json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filename), json);
    }

    public static <T> T loadFromJsonFile(String filename, Class<T> json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filename), json);
    }

}
