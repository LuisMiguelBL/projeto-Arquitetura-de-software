package model.entidades.produto;

import model.vo.Dinheiro;

public abstract class ProdutoBase implements Produto {

    private final String nome;
    private final Dinheiro preco;
    private final String descricao;


    protected ProdutoBase(String nome, Dinheiro preco, String descricao ) {

        if(nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório");
        }

        if(preco == null) {
            throw new IllegalArgumentException("O nome do produto é obrigatório");
        }

        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public Dinheiro getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return nome + " (" + descricao + ") - " + preco;
    }
}
