package model.factory;

import model.entidades.produto.Lanche;
import model.entidades.produto.ProdutoBase;
import model.vo.Dinheiro;

public class SanduicheNatural extends ProdutoBase implements Lanche {
    public SanduicheNatural() {
        super("Sanduiche Natural",Dinheiro.de(7.00), "Laranja sem açúcar 300ml" );
    }
}
