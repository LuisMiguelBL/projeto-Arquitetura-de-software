package model.factory;

import model.entidades.produto.Bebida;
import model.entidades.produto.Lanche;
import model.entidades.produto.Sobremesa;

public class CardapioFitFactory implements CardapioFactory{
    @Override
    public String getNomeCardapio() {
        return "Cardapio Fit";
    }

    @Override
    public Lanche criarLanche() {
        return new SanduicheNatural();
    }

    @Override
    public Bebida criarBebida() {
        return new SucoNatural();
    }

    @Override
    public Sobremesa criarSobremesa() {
        return new SaladaDeFrutas();
    }
}
