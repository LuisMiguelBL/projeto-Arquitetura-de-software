package controller;

import dao.ClienteDAO;
import dao.ClienteDAOMemoria;
import dao.PedidoDAO;
import dao.PedidoDAOMemoria;
import model.entidades.Cliente;
import model.entidades.ItemPedido;
import model.entidades.Pedido;
import model.entidades.produto.Lanche;
import model.entidades.produto.Produto;
import model.factory.CardapioFactory;
import model.factory.CardapioFitFactory;
import model.factory.CardapioTradicionalFactory;
import model.padroes.decorator.Bacon;
import model.padroes.decorator.Ovo;
import model.padroes.decorator.QueijoExtra;
import model.vo.Dinheiro;

import java.util.*;

public class PedidoController {

    private final PedidoDAO pedidoDAO = new PedidoDAOMemoria();
    private final ClienteDAO clienteDAO = new ClienteDAOMemoria();

    // Fábricas disponíveis (Abstract Factory), indexadas pelo nome
    private final Map<String, CardapioFactory> cardapios = new LinkedHashMap<>();
    private CardapioFactory cardapioAtual;

    // Itens do pedido que ainda está sendo montado
    private final List<ItemPedido> carrinho = new ArrayList<>();

    public PedidoController() {
        registrarCardapio(new CardapioTradicionalFactory());
        registrarCardapio(new CardapioFitFactory());
        cardapioAtual = cardapios.values().iterator().next();
    }

    private void registrarCardapio(CardapioFactory fabrica) {
        cardapios.put(fabrica.getNomeCardapio(), fabrica);
    }

    // ---------- Cardápio (Abstract Factory) ----------

    public List<String> listarCardapios() {
        return new ArrayList<>(cardapios.keySet());
    }

    public void selecionarCardapio(String nome) {
        CardapioFactory fabrica = cardapios.get(nome);
        if (fabrica == null) {
            throw new IllegalArgumentException("Cardápio inválido: " + nome);
        }
        cardapioAtual = fabrica;
    }

    /** Índice 0 = lanche, 1 = bebida, 2 = sobremesa */
    public List<Produto> produtosDoCardapio() {
        return List.of(
                cardapioAtual.criarLanche(),
                cardapioAtual.criarBebida(),
                cardapioAtual.criarSobremesa());
    }

    public boolean aceitaAdicionais(Produto produto) {
        return produto instanceof Lanche;
    }

    // ---------- Montagem do produto (Decorator) ----------

    public Produto montarProduto(int indiceProduto, boolean bacon, boolean queijoExtra, boolean ovo) {
        return switch (indiceProduto) {
            case 0 -> montarLanche(bacon, queijoExtra, ovo);
            case 1 -> cardapioAtual.criarBebida();
            case 2 -> cardapioAtual.criarSobremesa();
            default -> throw new IllegalArgumentException("Selecione um produto.");
        };
    }

    private Lanche montarLanche(boolean bacon, boolean queijoExtra, boolean ovo) {
        Lanche lanche = cardapioAtual.criarLanche();
        if (bacon) {
            lanche = new Bacon(lanche);
        }
        if (queijoExtra) {
            lanche = new QueijoExtra(lanche);
        }
        if (ovo) {
            lanche = new Ovo(lanche);
        }
        return lanche;
    }

    // ---------- Carrinho ----------

    public void adicionarAoCarrinho(Produto produto, int quantidade) {
        carrinho.add(new ItemPedido(produto, quantidade));
    }

    public void removerDoCarrinho(int indice) {
        if (indice < 0 || indice >= carrinho.size()) {
            throw new IllegalArgumentException("Selecione um item do carrinho.");
        }
        carrinho.remove(indice);
    }

    public void limparCarrinho() {
        carrinho.clear();
    }

    public List<ItemPedido> getCarrinho() {
        return Collections.unmodifiableList(carrinho);
    }

    public Dinheiro getSubtotalCarrinho() {
        Dinheiro total = Dinheiro.ZERO;
        for (ItemPedido item : carrinho) {
            total = total.somar(item.getSubtotal());
        }
        return total;
    }

    public Pedido finalizarPedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Selecione um cliente.");
        }
        if (carrinho.isEmpty()) {
            throw new IllegalArgumentException("O carrinho está vazio.");
        }
        Pedido pedido = new Pedido(cliente);
        for (ItemPedido item : carrinho) {
            pedido.adicionarItem(item.getProduto(), item.getQuantidade());
        }
        pedidoDAO.salvar(pedido);
        carrinho.clear();
        return pedido;
    }



    public List<Cliente> listarClientes() {
        return clienteDAO.listar();
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.listar();
    }

    public Optional<Pedido> buscarPedidoPorId(Integer id) {
        return pedidoDAO.buscar(id);
    }
}
