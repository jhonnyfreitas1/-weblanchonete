package app;

import static utils.JsonUtils.jsonMake; // importa o método estático, para ser utilizado diretamente
import static utils.JsonUtils.jsonField; // importa o método estático, para ser utilizado diretamente

public class Listar2 extends JSON {
    public int id;
    public String nome;
    public int preco;
    public String tipo;
    public int valor;

    public Listar2() {}

    public String toJson() {
        return jsonMake(                    // cria um json a partir de ...
            jsonField("id", this.id),       // um campo "id" com o valor sendo o atributo id deste objeto e ...
            jsonField("nome", this.nome),
            jsonField("preco", this.preco),
            jsonField("tipo", this.tipo),
            jsonField("valor", this.valor)        // um campo "id" com o valor sendo o atributo id deste objeto e ...
              
        );
    }
}