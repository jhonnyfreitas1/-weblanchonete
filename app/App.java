package app;

import java.util.HashMap;
import java.util.ArrayList;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.JsonUtils;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.lang.String;
// import static utils.JsonUtils.jsonMake;
// import static utils.JsonUtils.jsonField;



class App {

    public static Connection conn;

    public static void main(String[] args) throws Exception {
        initDatabase();
        startServer();
    }

    public static void initDatabase() {
        String url = "jdbc:mysql://localhost/poo20182_lanchonete";
        String user = "poo20182";
        String passw = "poo20182";

        conn = null; // Note que o objeto foi inicializado com "null"

        try {
            Class.forName("com.mysql.jdbc.Driver"); // pode lançar ClassNotFoundException
            conn = DriverManager.getConnection(url, user, passw); // pode lançar SQLException
            // JOptionPane.showMessageDialog(null,"Conexão realizada com sucesso");
            System.out.println("Conexão ok");
            // System.out.println("Conexão realizada com sucesso");
        } catch (ClassNotFoundException ex) { // Esta exceção acontece quando o driver não é encontrado no classpath
            System.out.println("Driver não encontrado");
        } catch (SQLException ex) { // Esta exceção acontece quando há algum problema na string de conexão com o banco
            System.out.println("Erro ao conectar ao banco");
            System.out.println(ex);
        }
    }

    public static void startServer() throws Exception {
        // String port = args[0]; // descomente esta linha para passar a porta como parâmetro no console
        String port = "9999"; // porta padrão para a aplicação
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 0);
        System.out.println("listening on port " + port);
        server.createContext("/produtos", new ProdutosController());
        server.createContext("/pedidos", new PedidosController());
        server.createContext("/item", new ItensController());
        //server.createContext("/users", new UserController());
        //server.createContext("/messages", new MessagesController());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class ProdutosController extends POOController{
        public HashMap<String, String> routes() {
            return new HashMap<String, String>() {{
                put("adicionar_tipo", "tipo_add");
                put("gettipo", "tipo_get");         //  /users/add -> add()
                put("add", "add");
                put("get", "getProd"); 
                put("listar","listar" );
                put("pegar","pegar_prod" );
            }};
        } 



        public void tipo_add() {


            String tipo = getParameter("tipo");
            String tipo2 = getParameter("select2");
            String tipo3 = getParameter("get_prod_tipo");
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT *  FROM tipos WHERE tipo = ?");
                stmt.setString(1, tipo);
                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    ok("tipo_ja_existe", true);
                } else {
                    // mudando o nome da variável porque a variável "stmt" já estava sendo usada (declarada umas linhas acima)
                    PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO tipos (tipo) VALUES (?)");
                    stmt1.setString(1, tipo);
                    stmt1.execute();

                    ok("inserted", true);
                }

            } catch (SQLException ex) {
                System.out.println(ex);
                ok("inserted", false);
            }
        }
        public void tipo_get() {
            ArrayList<JSON> tipos = new ArrayList<JSON>(); // este é um ArrayList de JSON, mas Message é uma subclasse de JSON, então um objecto Message pode ser colocado neste ArrayList

            try {
                Statement stmt = conn.createStatement(); // Este método pode lançar SQLException

                ResultSet result = stmt.executeQuery("SELECT tipo,id FROM tipos"); // Este método pode lançar SQLException

                while ( result.next() ) {
                    Message resultado = new Message();
                    resultado.tipo = result.getString("tipo");
                    resultado.id = result.getInt("id");
                    
                    tipos.add(resultado);  
                    // System.out.println(resultado.tipo);
                    
                }
            } catch (SQLException ex) {
                System.out.println("Erro na consulta ao banco");
                System.out.println(ex);
            }
            ok(
                JsonUtils.jsonArray(tipos)
                );

        }   
        public void listar(){

           String tipo_id = getParameter("id");
           System.out.println(tipo_id);
            ArrayList<JSON> produtos1 = new ArrayList<JSON>(); // este é um ArrayList de JSON, mas Message é uma subclasse de JSON, então um objecto Message pode ser colocado neste ArrayList

            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT nome,preco,id FROM produtos WHERE tipo_id = ?");
                stmt.setString(1, tipo_id);
                ResultSet result = stmt.executeQuery();
                while ( result.next() ) {
                    Listar resultado = new Listar();
                    resultado.nome = result.getString("nome");
                    resultado.id = result.getInt("id");
                    resultado.preco = result.getInt("preco");
                    
                    produtos1.add(resultado);  
                   //  System.out.println(resultado.nome);

                }
            } catch (SQLException ex) {
                System.out.println("Erro na consulta ao banco");
                System.out.println(ex);
            }
            ok(
                JsonUtils.jsonArray(produtos1)
                
                );
        }


        public void  add(){

            String nome_prod = getParameter("nome_prod");
            String preco = getParameter("preco");
            String id_tipo = getParameter("select");
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT *  FROM produtos WHERE nome = ?");
                stmt.setString(1, nome_prod);
                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    ok("produto_ja_existe", true);
                } else {
                    // mudando o nome da variável porque a variável "stmt" já estava sendo usada (declarada umas linhas acima)
                    PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO produtos (nome,preco,tipo_id) VALUES (?,?,?)");
                    stmt1.setString(1, nome_prod);
                    stmt1.setString(2, preco );
                    stmt1.setString(3, id_tipo);
                    stmt1.execute();
                    ok("inserted", true);

                }

            } catch (SQLException ex) {
                ok("inserted", false);
            }      
        }
        
        public void pegar_prod(){

           String prod_id = getParameter("id_prod");

            ArrayList<JSON> produtos1 = new ArrayList<JSON>(); // este é um ArrayList de JSON, mas Message é uma subclasse de JSON, então um objecto Message pode ser colocado neste ArrayList

            try {

                PreparedStatement stmt = conn.prepareStatement("SELECT produtos.nome,produtos.preco,produtos.id,tipos.tipo FROM produtos join tipos WHERE produtos.id=? and tipo_id = tipos.id;");
                stmt.setString(1, prod_id);
                ResultSet result = stmt.executeQuery();
                while ( result.next() ) {
                    Listar2 resultado = new Listar2();
                    resultado.nome = result.getString("nome");
                    resultado.id = result.getInt("id");
                    resultado.preco = result.getInt("preco");
                    resultado.tipo = result.getString("tipo");
                    
                    produtos1.add(resultado);  
                   //  System.out.println(resultado.nome);

                }
            } catch (SQLException ex) {
                System.out.println("Erro na consulta ao banco");
                System.out.println(ex);
            }
            ok(
                JsonUtils.jsonArray(produtos1)
                
                );
        }    
        
    }



    static class PedidosController extends POOController{
        public HashMap<String, String> routes() {
            return new HashMap<String, String>() {{
               // put("tipoadd", "tipo_add");
               // put("ver_valor", "ver_valor");         //  /users/add -> add()
                put("criar", "ped_add");
                put("adicionar_item", "add_item"); 
                put("remover_item","remover" );
                put("pagar","pedido_pagar");
            }};
        }          public void ped_add() {
            //java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
          // ArrayList<JSON> id = new ArrayList<JSON>();
            DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
            Date today = Calendar.getInstance().getTime(); 
            
            String reportDate = df.format(today);
            
            String pronto = "0";
            String pago =  "0";
            
            try {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO pedidos (pago,pronto,pedido_em) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);
                    stmt2.setString(1, pago);
                    stmt2.setString(2, pronto);
                    stmt2.setDate(3,new java.sql.Date(today.getTime()));
                    stmt2.executeUpdate();
                    //ResultSet resultSet = preparedStatement.executeQuery("SELECT LAST_INSERT_ID()");
                    
                     ResultSet resultSet = stmt2.executeQuery("SELECT LAST_INSERT_ID()");
                    if (resultSet.next()) {
                
                        ok("id", resultSet.getInt("LAST_INSERT_ID()"))  ;
                      }
                                                // mudando o nome da variável porque a variável "stmt" já estava sendo usada (declarada umas linhas acima)
                    ok("inserted", true);
            } catch (SQLException ex) {
                System.out.println(ex);
                ok("inserted", false);
            }
        
        } 

            public void  add_item(){

            String id_produto = getParameter("produto_id");
            String id_pedido = getParameter("pedido_id");
            String quantidade= getParameter("quantidade");
            String pronto  = "0";
            try {
                    PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO itens_pedido (produto_id,pedido_id,quantidade,pronto) VALUES (?,?,?,?)"); 
                    stmt3.setString(1,id_produto);
                    stmt3.setString(2,id_pedido);
                    stmt3.setString(3,quantidade);
                    stmt3.setString(4,pronto);
                    stmt3.execute();
                    ok("inserted", true);

            } catch (SQLException ex) {
                ok("inserted", false);
            }      
        }

        public void  remover(){
            String id_item = getParameter("id_item");
            try {
                    PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM itens_pedido WHERE id=?"); 
                    stmt3.setString(1,id_item);
                    stmt3.execute();
                    ok("removido", true);
            } catch (SQLException ex) {
                ok("removido", false);
            }      
        }

         public void pedido_pagar() {
            String valor = getParameter("valor");
            int real = Integer.parseInt(valor);
            String id_ped = getParameter("id_pedido");
            Listar2 resultado = new Listar2();
                
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT (produtos.preco*itens_pedido.quantidade) as valor from itens_pedido join produtos  on itens_pedido.produto_id = produtos.id and itens_pedido.pedido_id=?");
                stmt.setString(1, id_ped);
                ResultSet result = stmt.executeQuery();
                
                if (result.next()) {
                    
                int real2 = resultado.valor;
                    resultado.valor = Integer.parseInt(result.getString("valor"));

                        if (real > resultado.valor || real == resultado.valor ) {
                            
                            try {
                                   
                                     PreparedStatement stmt5 = conn.prepareStatement("UPDATE pedidos SET pago=1 WHERE id=?");
                                         stmt5.setString(1, id_ped);
                                          stmt5.executeUpdate();

                                     //if( result4.next() ) {
                                       
                                       // System.out.println("pago:"+id_ped);
                                     
                                    //}
                                         } catch (SQLException ex) {
                                          
                                        System.out.println(ex);
                                    }
                                    ok("troco", real - resultado.valor ,"pago",true);
                       
                        }else if(real < resultado.valor){
                            ok("pago",false);
                        }
                }

            } catch (SQLException ex) {
                System.out.println(ex);
                ok("inserted", false);
            }
        }

    }
       
 static class ItensController extends POOController{
        public HashMap<String, String> routes() {
            return new HashMap<String, String>() {{      
                put("pronto", "pronto");
               
            }};
        }
        public void  pronto(){
            String id_item = getParameter("valor_item");
            try {
                   PreparedStatement stmt6 = conn.prepareStatement("UPDATE itens_pedido SET pronto=1 WHERE pedido_id=?");
                     stmt6.setString(1, id_item);
                    stmt6.executeUpdate();
                    ok("pronto", true);
            } catch (SQLException ex) {
                ok("pronto", false);
            }      
        }
}}