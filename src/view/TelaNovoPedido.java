package view;

import controller.PedidoController;
import model.entidades.Cliente;
import model.entidades.ItemPedido;
import model.entidades.Pedido;
import model.entidades.produto.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * PADRÃO MVC - View de montagem de pedido.
 */
public class TelaNovoPedido extends JPanel {

    private final PedidoController controller;

    private final JComboBox<Cliente> cbCliente = new JComboBox<>();
    private final JComboBox<String> cbCardapio = new JComboBox<>();
    private final JComboBox<Produto> cbProduto = new JComboBox<>();

    private final JCheckBox chkBacon = new JCheckBox("Bacon (+R$ 4,00)");
    private final JCheckBox chkQueijo = new JCheckBox("Queijo extra (+R$ 3,00)");
    private final JCheckBox chkOvo = new JCheckBox("Ovo (+R$ 2,00)");

    private final JSpinner spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    private final JLabel lblPrecoUnitario = new JLabel(" ");
    private final JLabel lblSubtotal = new JLabel(" ");

    private final DefaultTableModel modeloCarrinho = new DefaultTableModel(
            new Object[]{"Qtd", "Produto", "Preço unit.", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JTable tabelaCarrinho = new JTable(modeloCarrinho);

    // Evita reagir a eventos enquanto o combo de produtos está sendo recarregado
    private boolean carregando = false;

    public TelaNovoPedido(PedidoController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        configurarRenderizadores();

        add(criarPainelMontagem(), BorderLayout.NORTH);
        add(criarPainelCarrinho(), BorderLayout.CENTER);
        add(criarPainelRodape(), BorderLayout.SOUTH);

        // Carga inicial dos dados (antes de registrar os eventos dos combos)
        for (String nome : controller.listarCardapios()) {
            cbCardapio.addItem(nome);
        }
        atualizarClientes();
        recarregarProdutos();
        atualizarCarrinho();

        registrarEventos();
    }

    // ---------- Montagem da tela ----------

    private void configurarRenderizadores() {
        // Mostra "#1 - Maria" no combo em vez do toString completo do Cliente
        cbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente c) {
                    setText("#" + c.getId() + " - " + c.getNome());
                }
                return this;
            }
        });

        // Mostra "X-Burguer - R$ 18,00" no combo de produtos
        cbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto p) {
                    setText(p.getNome() + " - " + p.getPreco());
                }
                return this;
            }
        });
    }

    private JPanel criarPainelMontagem() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Montar pedido"));

        JPanel adicionais = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        adicionais.add(chkBacon);
        adicionais.add(chkQueijo);
        adicionais.add(chkOvo);

        JButton btnAdicionar = new JButton("Adicionar ao carrinho");
        btnAdicionar.addActionListener(e -> adicionarAoCarrinho());

        posicionar(painel, new JLabel("Cliente:"), 0, 0, 1, 0);
        posicionar(painel, cbCliente, 1, 0, 1, 1);
        posicionar(painel, new JLabel("Cardápio:"), 2, 0, 1, 0);
        posicionar(painel, cbCardapio, 3, 0, 1, 1);

        posicionar(painel, new JLabel("Produto:"), 0, 1, 1, 0);
        posicionar(painel, cbProduto, 1, 1, 1, 1);
        posicionar(painel, new JLabel("Quantidade:"), 2, 1, 1, 0);
        posicionar(painel, spQuantidade, 3, 1, 1, 1);

        posicionar(painel, new JLabel("Adicionais:"), 0, 2, 1, 0);
        posicionar(painel, adicionais, 1, 2, 3, 1);

        posicionar(painel, lblPrecoUnitario, 1, 3, 2, 1);
        posicionar(painel, btnAdicionar, 3, 3, 1, 1);

        return painel;
    }

    private void posicionar(JPanel painel, JComponent componente, int x, int y, int largura, double peso) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = largura;
        gbc.weightx = peso;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(componente, gbc);
    }

    private JPanel criarPainelCarrinho() {
        tabelaCarrinho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Carrinho"));
        painel.add(new JScrollPane(tabelaCarrinho), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelRodape() {
        lblSubtotal.setFont(lblSubtotal.getFont().deriveFont(Font.BOLD, 14f));

        JButton btnRemover = new JButton("Remover item");
        JButton btnLimpar = new JButton("Limpar carrinho");
        JButton btnFinalizar = new JButton("Finalizar pedido");

        btnRemover.addActionListener(e -> removerItem());
        btnLimpar.addActionListener(e -> {
            controller.limparCarrinho();
            atualizarCarrinho();
        });
        btnFinalizar.addActionListener(e -> finalizarPedido());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnRemover);
        botoes.add(btnLimpar);
        botoes.add(btnFinalizar);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(lblSubtotal, BorderLayout.WEST);
        painel.add(botoes, BorderLayout.EAST);
        return painel;
    }

    private void registrarEventos() {
        cbCardapio.addActionListener(e -> {
            controller.selecionarCardapio((String) cbCardapio.getSelectedItem());
            recarregarProdutos();
        });
        cbProduto.addActionListener(e -> {
            if (!carregando) {
                atualizarOpcoesDoProduto();
            }
        });
        chkBacon.addActionListener(e -> atualizarPrecoUnitario());
        chkQueijo.addActionListener(e -> atualizarPrecoUnitario());
        chkOvo.addActionListener(e -> atualizarPrecoUnitario());
    }

    // ---------- Atualizações da tela ----------

    /** Chamado pela janela principal sempre que a aba é aberta (pega clientes novos). */
    public void atualizarClientes() {
        Object selecionado = cbCliente.getSelectedItem();
        cbCliente.removeAllItems();
        for (Cliente c : controller.listarClientes()) {
            cbCliente.addItem(c);
        }
        if (selecionado != null) {
            cbCliente.setSelectedItem(selecionado);
        }
    }

    private void recarregarProdutos() {
        carregando = true;
        cbProduto.removeAllItems();
        for (Produto p : controller.produtosDoCardapio()) {
            cbProduto.addItem(p);
        }
        carregando = false;
        atualizarOpcoesDoProduto();
    }

    private void atualizarOpcoesDoProduto() {
        Produto produto = (Produto) cbProduto.getSelectedItem();
        boolean aceita = produto != null && controller.aceitaAdicionais(produto);

        for (JCheckBox chk : new JCheckBox[]{chkBacon, chkQueijo, chkOvo}) {
            chk.setEnabled(aceita);
            if (!aceita) {
                chk.setSelected(false);
            }
        }
        atualizarPrecoUnitario();
    }

    private void atualizarPrecoUnitario() {
        int indice = cbProduto.getSelectedIndex();
        if (indice < 0) {
            lblPrecoUnitario.setText(" ");
            return;
        }
        Produto montado = controller.montarProduto(indice,
                chkBacon.isSelected(), chkQueijo.isSelected(), chkOvo.isSelected());
        lblPrecoUnitario.setText("Preço unitário: " + montado.getPreco() + "  |  " + montado.getNome());
    }

    private void atualizarCarrinho() {
        modeloCarrinho.setRowCount(0);
        for (ItemPedido item : controller.getCarrinho()) {
            modeloCarrinho.addRow(new Object[]{
                    item.getQuantidade(),
                    item.getProduto().getNome(),
                    item.getProduto().getPreco(),
                    item.getSubtotal()
            });
        }
        lblSubtotal.setText("Subtotal: " + controller.getSubtotalCarrinho());
    }

    // ---------- Ações ----------

    private void adicionarAoCarrinho() {
        try {
            Produto produto = controller.montarProduto(cbProduto.getSelectedIndex(),
                    chkBacon.isSelected(), chkQueijo.isSelected(), chkOvo.isSelected());
            int quantidade = (Integer) spQuantidade.getValue();

            controller.adicionarAoCarrinho(produto, quantidade);

            // Reseta as opções para o próximo item
            chkBacon.setSelected(false);
            chkQueijo.setSelected(false);
            chkOvo.setSelected(false);
            spQuantidade.setValue(1);
            atualizarPrecoUnitario();
            atualizarCarrinho();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerItem() {
        try {
            controller.removerDoCarrinho(tabelaCarrinho.getSelectedRow());
            atualizarCarrinho();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void finalizarPedido() {
        try {
            Pedido pedido = controller.finalizarPedido((Cliente) cbCliente.getSelectedItem());
            JOptionPane.showMessageDialog(this,
                    "Pedido #" + pedido.getId() + " finalizado!\nTotal: " + pedido.getSubtotal());
            atualizarCarrinho();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}