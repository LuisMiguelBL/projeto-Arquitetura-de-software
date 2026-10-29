package model.entidades;

import model.entidades.produto.Produto;
import model.vo.Dinheiro;
import model.vo.Endereco;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ENTIDADE: possui identidade (id).
 */
public class Pedido {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Integer id;
    private final Cliente cliente;
    private Endereco enderecoEntrega;
    private final List<ItemPedido> itens = new ArrayList<>();
    private StatusPedido status = StatusPedido.EM_ABERTO;
    private final LocalDateTime dataHora = LocalDateTime.now();

    public Pedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O pedido precisa de um cliente.");
        }
        this.cliente = cliente;
        this.enderecoEntrega = cliente.getEndereco();
    }

    public void adicionarItem(Produto produto, int quantidade) {
        itens.add(new ItemPedido(produto, quantidade));
    }

    public void removerItem(int indice) {
        if (indice < 0 || indice >= itens.size()) {
            throw new IllegalArgumentException("Item inválido.");
        }
        itens.remove(indice);
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public Dinheiro getSubtotal() {
        Dinheiro total = Dinheiro.ZERO;
        for (ItemPedido item : itens) {
            total = total.somar(item.getSubtotal());
        }
        return total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String resumo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id)
                .append(" | ").append(dataHora.format(FORMATO_DATA))
                .append(" | Status: ").append(status.getDescricao()).append("\n");
        sb.append("Cliente: ").append(cliente.getNome()).append("\n");
        sb.append("Entrega: ").append(enderecoEntrega).append("\n");
        sb.append("Itens:\n");
        for (int i = 0; i < itens.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(itens.get(i)).append("\n");
        }
        sb.append("Subtotal: ").append(getSubtotal());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido outro)) return false;
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "dataHora=" + dataHora +
                ", status=" + status +
                ", itens=" + itens +
                ", enderecoEntrega=" + enderecoEntrega +
                ", cliente=" + cliente +
                ", id=" + id +
                '}';
    }
}
