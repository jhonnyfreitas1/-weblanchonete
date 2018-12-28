$('#add_tipo').on('submit', function(e) {
    var that = this;
    e.preventDefault();
    $.ajax({
        type: 'POST',
        url: "http://localhost:9999/produtos/adicionar_tipo",
        data: $(this).serialize(),
        success: function(response) {
            if (response.inserted) {
                alert("tipo inserido");
                $(that).trigger("reset");
            } else if (response.tipo_ja_existe) {
                alert("tipo já adicionado");
            } else {
                alert("deu ruim...");
            }
            console.log(response);
        }
    });
});

$('#add_prod').on('submit', function(e) {
   var that = this;
   e.preventDefault();
   $.ajax({
        type: 'POST',
      url: "http://localhost:9999/produtos/add?id_tipo="+document.getElementById('#ponto_id')  ,
        data: $(this).serialize(),
          success: function(response) {
            if (response.inserted) {
              alert("produto adicionado")
           } else {
               alert("deu ruim...");
          }
        console.log(response);
           }
           });
         });

 

{$('#select').on('mouseover', function(e) {
              
               select=document.getElementById('select');
                $.ajax({
                url: "http://localhost:9999/produtos/gettipo",
                success: function(data) {
                    
                    var tipos = [];
                    try {
                        tipos = JSON.parse(data);
                    } catch (err) {
                        tipos = data;
                    }
                    $('#select').html('');
                    for (i = 0; i < tipos.length; i++) {
                         
                                    var select = `
                            <option  value=${tipos[i].id} name="id"  >${tipos[i].username}</option>
                        `;
                        $('#select').append(select);
                         
                    }
                    
                }
            });
        });

     $('#get_tipos').on('click', function() {
    $.ajax({
        url: "http://localhost:9999/produtos/gettipo",
        success: function(data) {
            console.log(data); 
            var messages = [];
            try {
                messages = JSON.parse(data);
            } catch (err) {
                messages = data;
            }
            $('#messages').html('');
            for (i = 0; i < messages.length; i++) {
                var li = `
                    <li style='background-color:;'>Nome: ${messages[i].username} | Id: ${messages[i].id}</li>
                `;
                $('#messages').append(li);
            }

            
        }
    });
});
   
$('#select2').on('mouseover', function(e) {
      
       select=document.getElementById('select2');
        $.ajax({
        url: "http://localhost:9999/produtos/gettipo",
        success: function(data) {
            var tipos = [];
            try {
                tipos = JSON.parse(data);
            } catch (err) {
                tipos = data;
            }
            $('#select2').html('');
            for (i = 0; i < tipos.length; i++) {
                 
                            var select = `
                    <option  value=${tipos[i].id} name="id_tipo"  >${tipos[i].username}</option>
                `;
                $('#select2').append(select);
                
            }
            
        }
    });
});
    $('#get_prod_tipo').on('submit', function(e) {
        e.preventDefault();
    $.ajax({
             type: 'POST',
            url: "http://localhost:9999/produtos/listar",
            data: $(this).serialize(),
            success: function(data){
            var produtos = [];
            try {
                produtos = JSON.parse(data);
            } catch (err) {
                produtos = data;
            }
            $('#resultado').html('');
            for (i = 0; i < produtos.length; i++) {
                var li = ` 
                    <li style='background-color:;'>NOME: ${produtos[i].nome} | ID: ${produtos[i].id} 
                   PRECO: ${produtos[i].preco}  </li>
                `;
                $('#resultado').append(li);
            }
            console.log(data);
            
        }
    });
});
    $('#get_prod').on('submit', function(e) {
        e.preventDefault();
    $.ajax({
             type: 'POST',
            url: "http://localhost:9999/produtos/pegar",
            data: $(this).serialize(),
            success: function(data){
            var produtos = [];
            try {
                produtos = JSON.parse(data);
            } catch (err) {
                produtos = data;
            }
            $('#resultado_prod').html('');
            for (i = 0; i < produtos.length; i++) {
                var li = ` 
                    <li style='background-color:;'>NOME: ${produtos[i].nome} | ID: ${produtos[i].id} |
                   PRECO: ${produtos[i].preco} |Tipo: ${produtos[i].tipo}  </li>
                `;
                $('#resultado_prod').append(li);
            }
            console.log(data);
            
        }
    });
});

 $('#fazer_pedido').on('click', function() {
    $.ajax({
        url: "http://localhost:9999/pedidos/criar",
        success: function(data) {
            var pedido = [];
            try {
                pedido = JSON.parse(data);
            } catch (err) {
                pedido = data;
            }
            $('#pedidos').html('');
            
                var li = `
                    <li> Id do pedido: ${data.id}</li>
                `;
                $('#pedidos').append(li);
            console.log(data);
         //   $("#id").css("display", "none");
            $("#add_item").css("display", "block");
            document.querySelector("[id='pedido']").value = data.id;
            
        }
    });
});

}


 $('#add_item').on('submit', function(e) {
   var that = this;
   e.preventDefault();
   $.ajax({
        type: 'POST',
        url: "http://localhost:9999/pedidos/adicionar_item",
        data: $(this).serialize(),
          success: function(response) {
            if (response.inserted) {
              alert("item adicionado")
           } else {
               alert("deu ruim...");
          }
        console.log(response);
           }
           });
         });

 $('#remover_item').on('submit', function(e) {
   var that = this;
   e.preventDefault();
   $.ajax({
        type: 'POST',
        url: "http://localhost:9999/pedidos/remover_item",
        data: $(this).serialize(),
          success: function(response) {
            if (response.removido) {
              alert("item removido")
           } else {
               alert("deu ruim...");
          }
        console.log(response);
           }
           });
         });

 $('#pagar_pedido').on('submit', function(e) {
   var that = this;
   e.preventDefault();
   $.ajax({
        type: 'POST',
        url: "http://localhost:9999/pedidos/pagar",
        data: $(this).serialize(),
          success: function(response) {
            if (response.pago ){
              alert("item pago")
           } else {
               alert("deu ruim no pagamento...");
          }if(response.troco){
            console.log(response.troco);
            alert("seu troco eh"+response.troco);
          }
        console.log(response);
           }
           });
         });

 $('#pedido_pronto').on('submit', function(e) {
   var that = this;
   e.preventDefault();
   $.ajax({
        type: 'POST',
        url: "http://localhost:9999/item/pronto",
        data: $(this).serialize(),
          success: function(response) {
            if (response.pronto){
              alert("item Pronto!!!!!!!!")
          }
        console.log(response);
           }
           });
         }); 
 
