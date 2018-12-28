package app;

import static utils.JsonUtils.jsonMake; // importa o método estático, para ser utilizado diretamente
import static utils.JsonUtils.jsonField; // importa o método estático, para ser utilizado diretamente

public class Usuario extends JSON {
    public int id;
    public String username;

    public Usuario(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public String toJson() {
        return jsonMake(                    // cria um json a partir de ...
            jsonField("login", true),
            jsonField("id", this.id),       // um campo "id" com o valor sendo o atributo id deste objeto e ...
            jsonField("username", this.username)    // um campo "nome" com o valor sendo o atributo nome deste objeto
        );
    }
}