package view;

import controller.PedidoController;
import model.entidades.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * PADRÃO MVC - View de consulta dos pedidos salvos.
 */
public class TelaPedidos extends JPanel {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final PedidoController controller;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Data", "Cliente", "Itens", "Subtotal", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JTextArea txtDetalhes = new JTextArea(8, 40);

    public TelaPedidos(PedidoController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizar());
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(btnAtualizar);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalhes();
            }
        });

        txtDetalhes.setEditable(false);
        txtDetalhes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JScrollPane rolagemTabela = new JScrollPane(tabela);
        rolagemTabela.setBorder(BorderFactory.createTitledBorder("Pedidos"));
        JScrollPane rolagemDetalhes = new JScrollPane(txtDetalhes);
        rolagemDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes do pedido selecionado"));

        JSplitPane divisao = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rolagemTabela, rolagemDetalhes);
        divisao.setResizeWeight(0.6);

        add(topo, BorderLayout.NORTH);
        add(divisao, BorderLayout.CENTER);

        atualizar();
    }

    /** Chamado pela janela principal sempre que a aba é aberta. */
    public void atualizar() {
        modelo.setRowCount(0);
        for (Pedido p : controller.listarPedidos()) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getDataHora().format(FORMATO_DATA),
                    p.getCliente().getNome(),
                    p.getItens().size(),
                    p.getSubtotal(),
                    p.getStatus().getDescricao()
            });
        }
        txtDetalhes.setText("");
    }

    private void mostrarDetalhes() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Integer id = (Integer) modelo.getValueAt(linha, 0);
        controller.buscarPedidoPorId(id).ifPresent(p -> txtDetalhes.setText(p.resumo()));
    }
}