package dao;

import model.entidades.Cliente;

import java.util.List;

public interface ClienteDAO extends DAO<Cliente, Integer>{

    List<Cliente> listarPorNome(String nome);
}
