package dao;

import model.entidades.Pedido;
import model.entidades.StatusPedido;

import java.util.List;

public interface PedidoDAO extends DAO<Pedido, Integer> {
    List<Pedido> listarPorCliente(Integer idCliente);
    List<Pedido> listarPorStatus(StatusPedido statusPedido);
}
