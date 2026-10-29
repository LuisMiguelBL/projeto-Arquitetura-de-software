package model.factory;

import model.entidades.produto.ProdutoBase;
import model.entidades.produto.Sobremesa;
import model.vo.Dinheiro;

public class SaladaDeFrutas extends ProdutoBase implements Sobremesa {
    public SaladaDeFrutas() {
        super("Salada de Frutas", Dinheiro.de(10.00), "Banana, melão, maçã e leite condesado");
    }
}
