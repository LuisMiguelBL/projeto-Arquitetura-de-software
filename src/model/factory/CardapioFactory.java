package model.factory;
import model.entidades.produto.Bebida;
import model.entidades.produto.Lanche;
import model.entidades.produto.Sobremesa;

/**
 * PADRÃO ABSTRACT FACTORY - Fábrica abstrata
 * Declara a criação de uma FAMÍLIA de produtos relacionados.
 */
public interface CardapioFactory {
    String getNomeCardapio();
    Lanche criarLanche();
    Bebida criarBebida();
    Sobremesa criarSobremesa();
}