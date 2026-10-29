package dao;

import model.entidades.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOMemoria implements ClienteDAO {
    private final BancoDeDados banco = BancoDeDados.getInstancia();

    @Override
    public Cliente salvar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        if (cliente.getId() == null) {
            cliente.setId(banco.proximoIdCliente());          // INSERT
        } else if (!banco.getClientes().containsKey(cliente.getId())) {
            throw new IllegalArgumentException("Cliente #" + cliente.getId() + " não existe para ser atualizado.");
        }
        banco.getClientes().put(cliente.getId(), cliente);     // INSERT ou UPDATE
        return cliente;
    }

    @Override
    public Optional<Cliente> buscar(Integer id) {
        return Optional.ofNullable(banco.getClientes().get(id));
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(banco.getClientes().values());
    }

    @Override
    public boolean excluir(Integer id) {
        return banco.getClientes().remove(id) != null;
    }



    @Override
    public List<Cliente> listarPorNome(String nome) {
        String busca = nome == null ? "" : nome.toLowerCase();
        return banco.getClientes().values().stream()
                .filter(c -> c.getNome().toLowerCase().contains(busca))
                .toList();
    }
}
