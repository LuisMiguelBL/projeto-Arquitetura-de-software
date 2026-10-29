package model.factory;

import model.entidades.produto.Bebida;
import model.entidades.produto.ProdutoBase;
import model.vo.Dinheiro;

public class Refrigerante extends ProdutoBase implements Bebida {
    public Refrigerante() {
        super("Refrigerante",  Dinheiro.de(6.00),"Lata 350ml");
    }
}
