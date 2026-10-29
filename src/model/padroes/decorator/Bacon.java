package model.padroes.decorator;

import model.entidades.produto.Lanche;
import model.vo.Dinheiro;

public class Bacon extends AdicionalDecorator{

    public Bacon(Lanche lanche) {
        super(lanche, "Banco", Dinheiro.de(4));
    }

}
