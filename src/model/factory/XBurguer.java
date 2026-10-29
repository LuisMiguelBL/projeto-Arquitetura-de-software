package model.factory;

import model.entidades.produto.Lanche;
import model.entidades.produto.ProdutoBase;
import model.vo.Dinheiro;

public class XBurguer  extends ProdutoBase implements Lanche {
    public XBurguer() {
        super("x-burguer", Dinheiro.de(18.00), "Pão, hambúrguer, queijo e salada");
    }
}
