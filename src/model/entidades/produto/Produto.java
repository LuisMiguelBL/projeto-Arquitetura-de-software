package model.entidades.produto;

import model.vo.Dinheiro;

public interface Produto {

    String getNome();
    String getDescricao();
    Dinheiro getPreco();

}
