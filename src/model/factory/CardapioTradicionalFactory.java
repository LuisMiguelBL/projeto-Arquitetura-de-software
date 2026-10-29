package model.factory;

import model.entidades.produto.Bebida;
import model.entidades.produto.Lanche;
import model.entidades.produto.Sobremesa;

public class CardapioTradicionalFactory implements CardapioFactory{
    @Override
    public String getNomeCardapio() {
        return "Cardápio Tradicional";
    }

    @Override
    public Lanche criarLanche() {
        return new XBurguer();
    }

    @Override
    public Bebida criarBebida() {
        return new Refrigerante();
    }

    @Override
    public Sobremesa criarSobremesa() {
        return new Pudim();
    }
}
