package app;

import static utils.JsonUtils.jsonMake; // importa o método estático, para ser utilizado diretamente
import static utils.JsonUtils.jsonField; // importa o método estático, para ser utilizado diretamente

public class Message extends JSON {
    public int id;
    public String tipo;
    
    
    public Message() {}

    public String toJson() {
        return jsonMake(                    // cria um json a partir de ...
            jsonField("id", this.id),       // um campo "id" com o valor sendo o atributo id deste objeto e ...
            jsonField("username", this.tipo)   
        );
    }
}