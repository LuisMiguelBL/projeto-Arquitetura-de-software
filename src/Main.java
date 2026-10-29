import controller.ClienteController;
import controller.PedidoController;
import dao.ClienteDAO;
import dao.ClienteDAOMemoria;
import model.entidades.Cliente;
import model.vo.*;
import view.JanelaPrincipal;

import javax.swing.*;

void main() {
    carregarDadosIniciais();

    SwingUtilities.invokeLater(() -> {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorado) {
        }
        new JanelaPrincipal(new ClienteController(), new PedidoController()).setVisible(true);
    });
}

        private static void carregarDadosIniciais() {
            ClienteDAO dao = new ClienteDAOMemoria();
            dao.salvar(new Cliente("Maria", "(87) 99999-0000",
                    new Endereco("Rua A", "10", "Centro", "Quixaba")));
            dao.salvar(new Cliente("João", "(87) 98888-1111",
                    new Endereco("Rua B", "25", "Alto", "Serra Talhada")));
        }


