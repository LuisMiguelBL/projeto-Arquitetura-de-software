package model.factory;

import model.entidades.produto.Bebida;
import model.entidades.produto.ProdutoBase;
import model.vo.Dinheiro;

public class SucoNatural extends ProdutoBase implements Bebida {

    public SucoNatural() {
        super("Suco de laranja", Dinheiro.de(7.00),"Laranja");
    }
}
