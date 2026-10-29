package model.factory;

import model.entidades.produto.ProdutoBase;
import model.entidades.produto.Sobremesa;
import model.vo.Dinheiro;

public class Pudim extends ProdutoBase implements Sobremesa {
    public Pudim() {
        super("Pudim", Dinheiro.de(8.00) ,"Pudim de leite condensado");
    }
}
