package controller;

import dao.ClienteDAO;
import dao.ClienteDAOMemoria;
import model.entidades.Cliente;
import model.vo.Endereco;

import java.util.List;
import java.util.Optional;

public class ClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAOMemoria();

    public Cliente salvar(Integer id, String nome, String telefone,
                          String rua, String numero, String bairro, String cidade) {


        Endereco endereco = new Endereco(rua, numero, bairro, cidade);

        if (id == null) {
            // Novo cliente (o construtor de Cliente valida o nome)
            return clienteDAO.salvar(new Cliente(nome, telefone, endereco));
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        Cliente cliente = clienteDAO.buscar(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente #" + id + " não encontrado."));
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEndereco(endereco);
        return clienteDAO.salvar(cliente);
    }

    public List<Cliente> listar() {
        return clienteDAO.listar();
    }

    public List<Cliente> buscarPorNome(String trecho) {
        return clienteDAO.listarPorNome(trecho);
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteDAO.buscar(id);
    }

    public void excluir(Integer id) {
        if (!clienteDAO.excluir(id)) {
            throw new IllegalArgumentException("Cliente #" + id + " não encontrado.");
        }
    }
}
