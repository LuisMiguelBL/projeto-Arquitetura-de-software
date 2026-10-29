package view;

import controller.ClienteController;
import model.entidades.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class TelaClientes extends JPanel {

    private final ClienteController controller;

    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtTelefone = new JTextField(12);
    private final JTextField txtRua = new JTextField(20);
    private final JTextField txtNumero = new JTextField(6);
    private final JTextField txtBairro = new JTextField(15);
    private final JTextField txtCidade = new JTextField(15);
    private final JTextField txtBusca = new JTextField(15);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Telefone", "Endereço"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);


    private Integer idSelecionado = null;

    public TelaClientes(ClienteController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(criarPainelTabela(), BorderLayout.CENTER);

        atualizarTabela(controller.listar());
    }



    private JPanel criarFormulario() {
        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBorder(BorderFactory.createTitledBorder("Dados do cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampo(campos, gbc, 0, 0, "Nome:", txtNome);
        adicionarCampo(campos, gbc, 0, 2, "Telefone:", txtTelefone);
        adicionarCampo(campos, gbc, 1, 0, "Rua:", txtRua);
        adicionarCampo(campos, gbc, 1, 2, "Número:", txtNumero);
        adicionarCampo(campos, gbc, 2, 0, "Bairro:", txtBairro);
        adicionarCampo(campos, gbc, 2, 2, "Cidade:", txtCidade);

        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");

        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnNovo);
        botoes.add(btnSalvar);
        botoes.add(btnExcluir);

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(campos, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc,
                                int linha, int coluna, String rotulo, JComponent campo) {
        gbc.gridy = linha;

        gbc.gridx = coluna;
        gbc.weightx = 0;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = coluna + 1;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }

    private JPanel criarPainelTabela() {
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComSelecionado();
            }
        });

        JButton btnBuscar = new JButton("Buscar");
        JButton btnTodos = new JButton("Mostrar todos");

        btnBuscar.addActionListener(e -> atualizarTabela(controller.buscarPorNome(txtBusca.getText().trim())));
        btnTodos.addActionListener(e -> {
            txtBusca.setText("");
            atualizarTabela(controller.listar());
        });

        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busca.add(new JLabel("Buscar por nome:"));
        busca.add(txtBusca);
        busca.add(btnBuscar);
        busca.add(btnTodos);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Clientes cadastrados"));
        painel.add(busca, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }


    private void salvar() {
        try {
            Cliente salvo = controller.salvar(
                    idSelecionado,
                    txtNome.getText().trim(),
                    txtTelefone.getText().trim(),
                    txtRua.getText().trim(),
                    txtNumero.getText().trim(),
                    txtBairro.getText().trim(),
                    txtCidade.getText().trim());

            JOptionPane.showMessageDialog(this, "Cliente #" + salvo.getId() + " salvo com sucesso!");
            limparFormulario();
            atualizarTabela(controller.listar());

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o cliente #" + idSelecionado + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                limparFormulario();
                atualizarTabela(controller.listar());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void preencherFormularioComSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Integer id = (Integer) modeloTabela.getValueAt(linha, 0);

        controller.buscarPorId(id).ifPresent(c -> {
            idSelecionado = c.getId();
            txtNome.setText(c.getNome());
            txtTelefone.setText(c.getTelefone());
            txtRua.setText(c.getEndereco().rua());
            txtNumero.setText(c.getEndereco().numero());
            txtBairro.setText(c.getEndereco().bairro());
            txtCidade.setText(c.getEndereco().cidade());
        });
    }

    private void limparFormulario() {
        idSelecionado = null;
        txtNome.setText("");
        txtTelefone.setText("");
        txtRua.setText("");
        txtNumero.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        tabela.clearSelection();
        txtNome.requestFocus();
    }

    private void atualizarTabela(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{
                    c.getId(), c.getNome(), c.getTelefone(), c.getEndereco().toString()
            });
        }
    }
}