package dao;

import model.entidades.Cliente;
import model.entidades.Pedido;

import java.util.LinkedHashMap;
import java.util.Map;


public class BancoDeDados {


    private static BancoDeDados instancia;


    private final Map<Integer, Cliente> clientes = new LinkedHashMap<>();
    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();


    private int sequenciaCliente = 0;
    private int sequenciaPedido = 0;


    private BancoDeDados() {
        System.out.println("[BancoDeDados] Instância criada.");
    }


    public static synchronized BancoDeDados getInstancia() {
        if (instancia == null) {
            instancia = new BancoDeDados();   // <-- aqui é NEW, nunca getInstancia()
        }
        return instancia;
    }


    Map<Integer, Cliente> getClientes() {
        return clientes;
    }

    Map<Integer, Pedido> getPedidos() {
        return pedidos;
    }

    int proximoIdCliente() {
        return ++sequenciaCliente;
    }

    int proximoIdPedido() {
        return ++sequenciaPedido;
    }
}