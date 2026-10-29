package view;

import controller.ClienteController;
import controller.PedidoController;

import javax.swing.*;
import java.awt.Component;

/**
 * Janela principal: cada funcionalidade do sistema é uma aba.
 */
public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal(ClienteController clienteController, PedidoController pedidoController) {
        super("Lanchonete - Sistema de Pedidos");

        TelaClientes telaClientes = new TelaClientes(clienteController);
        TelaNovoPedido telaNovoPedido = new TelaNovoPedido(pedidoController);
        TelaPedidos telaPedidos = new TelaPedidos(pedidoController);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Clientes", telaClientes);
        abas.addTab("Novo Pedido", telaNovoPedido);
        abas.addTab("Pedidos", telaPedidos);

        // Ao trocar de aba, recarrega os dados (ex.: cliente recém-cadastrado aparece no combo)
        abas.addChangeListener(e -> {
            Component abaAtual = abas.getSelectedComponent();
            if (abaAtual == telaNovoPedido) {
                telaNovoPedido.atualizarClientes();
            } else if (abaAtual == telaPedidos) {
                telaPedidos.atualizar();
            }
        });

        setContentPane(abas);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}