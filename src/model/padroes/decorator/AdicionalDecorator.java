package model.padroes.decorator;

import model.entidades.produto.Lanche;
import model.vo.Dinheiro;


public abstract class AdicionalDecorator implements Lanche {

    protected final Lanche lanche;
    private final String nomeAdicional;
    private final Dinheiro valorAdicional;

    protected AdicionalDecorator(Lanche lanche, String nome, Dinheiro valor) {
        if(lanche == null) {
            throw new IllegalArgumentException("o decorator precisa de uma lanche");
        }
        this.lanche = lanche;
        this.nomeAdicional = nome;
        this.valorAdicional = valor;
    }

    @Override
    public String getNome(){
        return lanche.getNome() + " + " + nomeAdicional;
    }

    @Override
    public String getDescricao(){
        return  lanche.getDescricao() + " + " + nomeAdicional;
    }

    @Override
    public Dinheiro getPreco(){
        return lanche.getPreco().somar(valorAdicional);
    }

    @Override
    public String toString() {
        return getNome() + " (" + getDescricao() + ") - " + getPreco();
    }


}
