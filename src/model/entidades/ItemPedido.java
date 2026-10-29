package model.entidades;

import model.entidades.produto.Produto;
import model.vo.Dinheiro;

public class ItemPedido {

    private final Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto é obrigatório.");
        }
        validarQuantidade(quantidade);
        this.produto = produto;
        this.quantidade = quantidade;
    }

    private static void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }

    public Dinheiro getSubtotal() {
        return produto.getPreco().multiplicar(quantidade);
    }

    @Override
    public String toString() {
        return quantidade + "x " + produto.getNome() + " (" + produto.getPreco() + ") = " + getSubtotal();
    }
}
