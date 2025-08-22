package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Dao.ProdutoDAO;
import Dao.VendaDAO;
import Dao.VoucherDAO;
import Model.Venda;
import Model.VendaItem;
import Model.Produto;
import Model.Voucher;

public class MenuVenda {
    private static final Scanner leitor = new Scanner(System.in);
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final VendaDAO vendaDAO = new VendaDAO();
    private static final VoucherDAO voucherDAO = new VoucherDAO();

    public static void menuVendas() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("--- MENU DE VENDAS ---");
            System.out.println("1. Realizar Venda");
            System.out.println("2. Listar Vendas");
            System.out.println("3. Buscar Venda por ID");
            System.out.println("4. Cancelar Venda");
            System.out.println("5. Devolução de Produto");
            System.out.println("6. Trocar Produto");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = leitor.nextInt();
                leitor.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        realizarVenda();
                        break;
                    case 2:
                        listarVendas();
                        break;
                    case 3:
                        buscarVendaPorId();
                        break;
                    case 4:
                        cancelarVenda();
                        break;
                    case 5:
                        devolucaoProduto();
                        break;
                    case 6:
                        trocaProduto();
                        break;
                    case 0:
                        System.out.println("Retornando ao Menu Principal.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número.");
                leitor.nextLine();
                opcao = -1;
            }
            System.out.println();
        }
    }

    private static void realizarVenda() {
        System.out.print("ID do cliente: ");
        int clienteId = leitor.nextInt();
        leitor.nextLine();

        List<VendaItem> itensDaVenda = new ArrayList<>();
        boolean continuar = true;
        double valorTotal = 0;

        while (continuar) {
            System.out.print("Digite parte do nome do produto para buscar: ");
            String termoBusca = leitor.nextLine();

            List<Produto> encontrados = produtoDAO.buscarPorNome(termoBusca);

            if (encontrados.isEmpty()) {
                System.out.println("Nenhum produto encontrado.");
                continue;
            }

            System.out.println("=== Produtos encontrados ===");
            for (Produto p : encontrados) {
                System.out.printf("ID: %d | Nome: %s | Status: %s | Estoque: %d | Preço: R$ %.2f\n",
                        p.getId(), p.getNome(), p.getStatusAtual(), p.getEstoque(), p.getPreco());
            }

            System.out.print("Digite o ID do produto que deseja: ");
            int produtoId = leitor.nextInt();
            leitor.nextLine();

            Produto produto = produtoDAO.getByIdProduto(produtoId);
            if (produto == null) {
                System.out.println("Produto não encontrado. Tente novamente.");
                continue;
            }

            System.out.print("Quantidade desejada: ");
            int quantidade = leitor.nextInt();
            leitor.nextLine();

            if (quantidade <= 0 || quantidade > produto.getEstoque()) {
                System.out.println("Quantidade inválida ou maior que o estoque.");
                continue;
            }

            VendaItem item = new VendaItem();
            item.setProdutoId(produto.getId());
            item.setProdutoNome(produto.getNome());
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPreco());
            itensDaVenda.add(item);

            valorTotal += produto.getPreco() * quantidade;

            System.out.printf("Produto adicionado à venda. Subtotal: R$ %.2f\n", valorTotal);
            System.out.print("Deseja adicionar outro produto? (s/n): ");
            String resposta = leitor.nextLine();
            if (!resposta.equalsIgnoreCase("s")) {
                continuar = false;
            }
        }

        if (itensDaVenda.isEmpty()) {
            System.out.println("Venda cancelada por não ter produtos.");
            return;
        }

        List<Voucher> vouchers = voucherDAO.listarVouchersAtivosPorCliente(clienteId);
        if (vouchers != null && !vouchers.isEmpty()) {
            System.out.println("Vouchers disponíveis:");
            for (Voucher v : vouchers) {
                System.out.printf("ID: %d | Valor: R$ %.2f | Validade: %s\n", v.getId(), v.getValor(), v.getValidade());
            }
            System.out.print("Usar voucher? (ID ou 0 para não): ");
            long idVoucherEscolhido = leitor.nextLong();
            leitor.nextLine();

            Voucher voucherUsado = vouchers.stream()
                    .filter(v -> v.getId() == idVoucherEscolhido)
                    .findFirst()
                    .orElse(null);
            if (voucherUsado != null) {
                valorTotal = Math.max(0, valorTotal - voucherUsado.getValor());
                voucherDAO.usarVoucher(voucherUsado.getId());
                System.out.println("Voucher aplicado!");
            }
        }

        System.out.print("Informe a forma de pagamento (Dinheiro, Cartão, PIX): ");
        String formaPagamento = leitor.nextLine();

        Venda vendaGeral = new Venda();
        vendaGeral.setClienteId(clienteId);
        vendaGeral.setDataVenda(LocalDate.now());
        vendaGeral.setTotal(valorTotal);
        vendaGeral.setFormaPagamento(formaPagamento);

        try {
            int idVenda = vendaDAO.inserirVenda(vendaGeral);
            if (idVenda != -1) {
                for (VendaItem item : itensDaVenda) {
                    item.setVendaId(idVenda);
                    vendaDAO.inserirItemVenda(item);
                    produtoDAO.atualizarEstoque(item.getProdutoId(), -item.getQuantidade());
                }

                System.out.printf("Valor final: R$ %.2f\n", valorTotal);
                System.out.println("Venda registrada com sucesso com ID: " + idVenda);
            } else {
                System.out.println("Falha ao registrar a venda.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao registrar venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarVendas() {
        List<Venda> vendas = vendaDAO.listarVendas();
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada.");
        } else {
            vendas.forEach(System.out::println);
        }
    }

    private static void buscarVendaPorId() {
        System.out.print("Digite o ID da venda: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Venda venda = vendaDAO.buscarVendaPorId(id);
        if (venda == null) {
            System.out.println("Venda não encontrada.");
        } else {
            System.out.println(venda);
        }
    }

    private static void cancelarVenda() {
        System.out.print("Digite o ID da venda: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Venda venda = vendaDAO.buscarVendaPorId(id);
        if (venda == null) {
            System.out.println("Venda não encontrada.");
            return;
        }


        try {
            for (VendaItem item : venda.getItens()) {
                produtoDAO.atualizarEstoque(item.getProdutoId(), item.getQuantidade());
            }

            vendaDAO.excluirVenda(id);
            System.out.println("Venda cancelada com sucesso! Estoque reposto.");
        } catch (SQLException e) {
            System.out.println("Erro ao cancelar a venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void devolucaoProduto() {
        System.out.print("ID da venda para devolução: ");
        int vendaId = leitor.nextInt();
        leitor.nextLine();

        if (vendaDAO.existeDevolucaoParaVenda(vendaId)) {
            System.out.println("Esta venda já possui devolução registrada.");
            return;
        }

        Venda venda = vendaDAO.buscarVendaPorId(vendaId);
        if (venda == null) {
            System.out.println("Venda não encontrada.");
            return;
        }

        System.out.printf("Valor total da venda: R$ %.2f\n", venda.getTotal());
        System.out.print("Motivo da devolução: ");
        String motivo = leitor.nextLine();

        if (vendaDAO.registrarDevolucao(vendaId, motivo, venda.getTotal())) {

            for (VendaItem item : venda.getItens()) {
                try {
                    produtoDAO.atualizarEstoque(item.getProdutoId(), item.getQuantidade());
                } catch (SQLException e) {
                    System.out.println("Erro ao repor estoque do produto " + item.getProdutoNome());
                }
            }

            boolean voucherCriado = voucherDAO.criarVoucher(venda.getClienteId(), venda.getTotal());
            if (voucherCriado) {
                System.out.printf("Voucher criado no valor de R$ %.2f para o cliente.\n", venda.getTotal());
            } else {
                System.out.println("Erro ao criar voucher para o cliente.");
            }
            System.out.println("Devolução registrada com sucesso!");
        } else {
            System.out.println("Erro ao registrar devolução.");
        }
    }

    private static void trocaProduto() {
        System.out.print("ID da venda: ");
        int idVenda = leitor.nextInt();
        leitor.nextLine();

        Venda venda = vendaDAO.buscarVendaPorId(idVenda);
        if (venda == null) {
            System.out.println("Venda não encontrada.");
            return;
        }

        System.out.println("Itens da venda:");
        for (VendaItem item : venda.getItens()) {
            System.out.printf("ID Produto: %d | Nome: %s | Quantidade: %d\n", item.getProdutoId(), item.getProdutoNome(), item.getQuantidade());
        }

        System.out.print("ID do produto a ser trocado: ");
        int idAntigo = leitor.nextInt();
        leitor.nextLine();

        System.out.print("ID do novo produto: ");
        int idNovo = leitor.nextInt();
        leitor.nextLine();

        System.out.print("Quantidade (a trocar): ");
        int qtd = leitor.nextInt();
        leitor.nextLine();

        Produto produtoAntigo = produtoDAO.getByIdProduto(idAntigo);
        Produto produtoNovo = produtoDAO.getByIdProduto(idNovo);

        if (produtoAntigo == null || produtoNovo == null) {
            System.out.println("Produto antigo ou novo não encontrado.");
            return;
        }

        if (produtoNovo.getEstoque() < qtd) {
            System.out.println("Estoque insuficiente para o novo produto. Operação cancelada.");
            return;
        }

        double valorAntigoTotal = produtoAntigo.getPreco() * qtd;
        double valorNovoTotal = produtoNovo.getPreco() * qtd;
        double diferenca = valorNovoTotal - valorAntigoTotal;


        try {
            vendaDAO.trocarProdutoNaVenda(idVenda, idAntigo, idNovo, qtd, produtoNovo.getPreco());
            produtoDAO.atualizarEstoque(idAntigo, qtd);
            produtoDAO.atualizarEstoque(idNovo, -qtd);
            vendaDAO.adicionarAoTotalVenda(idVenda, diferenca);
            System.out.println("Produto trocado com sucesso!");

            if (diferenca < 0) {
                double valorVoucher = -diferenca;
                boolean voucherCriado = voucherDAO.criarVoucher(venda.getClienteId(), valorVoucher);
                if (voucherCriado) {
                    System.out.printf("Voucher criado no valor de R$ %.2f para o cliente.\n", valorVoucher);
                } else {
                    System.out.println("Erro ao criar voucher para o cliente.");
                }
            } else if (diferenca > 0) {
                System.out.printf("Cliente deve pagar a diferença de R$ %.2f.\n", diferenca);
            } else {
                System.out.println("Não há diferença de valores na troca.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a troca. A operação foi desfeita.");
            e.printStackTrace();
        }
    }
}
