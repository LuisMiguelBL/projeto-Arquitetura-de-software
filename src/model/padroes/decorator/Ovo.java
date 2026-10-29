package model.padroes.decorator;

import model.entidades.produto.Lanche;
import model.vo.Dinheiro;

public class Ovo extends AdicionalDecorator{

    public Ovo(Lanche lanche) {
        super(lanche, "1 ovo", Dinheiro.de(2.00));
    }
}
