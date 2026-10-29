package model.padroes.decorator;

import model.entidades.produto.Lanche;
import model.vo.Dinheiro;

public class QueijoExtra extends AdicionalDecorator{

    public QueijoExtra(Lanche lanche){
        super(lanche, "Queijo extra", Dinheiro.de(3.00));
    }
}
