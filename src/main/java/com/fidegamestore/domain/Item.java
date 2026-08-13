package com.fidegamestore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//Esta clase se usara para gestionar las llaves compradas dentro del carrito
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    // Referencia al anuncio publicado
    private Anuncio anuncio;

    //Se mantiene la cantidad para la facilidad del codigo. 
    //No se puede comprar mas de una misma llave.
    private int cantidad;
    
    //Precio pagado al momento de la compra
    private BigDecimal precioHistorico;

    // Método para calcular el subtotal
    public BigDecimal getSubTotal() {
        return anuncio.getPrecio().multiply(new BigDecimal(cantidad));
    }
}
