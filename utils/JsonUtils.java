package utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import app.JSON;

public class JsonUtils {

    public static String jsonField(String key, Object data) {
        String _data = data.toString().replace("\"", "\\\"");
        if (!(data instanceof Boolean) && !(data instanceof Number)) {
            _data = String.format("\"%s\"", _data);
        }
        return String.format("\"%s\":%s", key, _data);
    }

    // Recebe um array de objetos da classe JSON (ou de alguma subclasse sua) e transforma numa String JSON
    public static String jsonArray(JSON ... objects) {
        return jsonArray(Arrays.asList(objects));
    }

    public static String jsonArray(List<JSON> objects) {
        return String.format("[%s]", objects.stream().map(JSON::toJson).collect(Collectors.joining(", "))); // Não tente entender esta linha agora
    }

    public static String jsonMake(String ... parts) {
        return String.format("{%s}", String.join(",", parts));
    }
}