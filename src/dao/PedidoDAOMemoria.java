package dao;

import model.entidades.Pedido;
import model.entidades.StatusPedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAOMemoria implements PedidoDAO {

    private final BancoDeDados banco = BancoDeDados.getInstancia();

    @Override
    public List<Pedido> listarPorCliente(Integer idCliente) {
        return banco.getPedidos().values().stream()
                .filter(p -> p.getCliente().getId() != null && p.getCliente().getId().equals(idCliente))
                .toList();
    }

    @Override
    public List<Pedido> listarPorStatus(StatusPedido status) {
        return banco.getPedidos().values().stream()
                .filter(p -> p.getStatus() == status)
                .toList();
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo.");
        }
        if (pedido.getId() == null) {
            pedido.setId(banco.proximoIdPedido());
        } else if (!banco.getPedidos().containsKey(pedido.getId())) {
            throw new IllegalArgumentException("Pedido #" + pedido.getId() + " não existe para ser atualizado.");
        }
        banco.getPedidos().put(pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> buscar(Integer id) {
        return Optional.ofNullable(banco.getPedidos().get(id));
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(banco.getPedidos().values());
    }

    @Override
    public boolean excluir(Integer id) {
        return banco.getPedidos().remove(id) != null;
    }
}
