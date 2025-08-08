package service;

import Dao.ProdutoDAO;
import Dao.VendaDAO;
import Model.Produto;
import Model.Venda;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuVenda {

    public static void menuVendas() {
        Scanner leitor = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- MENU DE VENDAS ---");
            System.out.println("1. Realizar venda");
            System.out.println("2. Listar Vendas");
            System.out.println("3. Buscar Vendas por ID");
            System.out.println("4. Cancelar venda");
            System.out.println("5. Devolução");
            System.out.println("6. Troca de produto");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = leitor.nextInt();
                leitor.nextLine();

                switch (opcao) {
                    case 1 -> realizarVenda();
                    case 2 -> listarVendas();
                    case 3 -> buscarVendaPorId();
                    case 4 -> cancelarVenda();
                    case 5 -> devolucaoProduto();
                    case 6 -> trocaProduto();
                    case 0 -> System.out.println("Voltando ao menu principal...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número.");
                leitor.nextLine();
                opcao = -1;
            }

        } while (opcao != 0);
    }

    private static void realizarVenda() {
        Scanner leitor = new Scanner(System.in);
        List<Venda> listaVendas = new ArrayList<>();
        ProdutoDAO produtoDAO = new ProdutoDAO();
        VendaDAO vendaDAO = new VendaDAO();

        System.out.print("Cliente: ");
        String cliente = leitor.nextLine();

        boolean continuar = true;
        double valorTotal = 0;

        while (continuar) {
            System.out.print("ID do produto: ");
            int produtoId = leitor.nextInt();
            leitor.nextLine();

            Produto produto = produtoDAO.getByIdProduto(produtoId);

            if (produto == null) {
                System.out.println("Produto não encontrado. Tente novamente.");
                continue;
            }

            System.out.println("Produto selecionado: " + produto.getNome());
            System.out.println("Estoque disponível: " + produto.getEstoque());
            System.out.println("Preço unitário: R$ " + produto.getPreco());

            System.out.print("Quantidade desejada: ");
            int quantidade = leitor.nextInt();
            leitor.nextLine();

            if (quantidade <= 0) {
                System.out.println("Quantidade inválida.");
                continue;
            }

            if (quantidade > produto.getEstoque()) {
                System.out.println("Erro: quantidade maior que o estoque disponível.");
                continue;
            }

            // Atualiza o estoque
            produto.setEstoque(produto.getEstoque() - quantidade);
            produtoDAO.alterarProduto(produto);

            // Criar e salvar venda
            Venda venda = new Venda();
            venda.setProduto(produto);
            venda.setQuantidade(quantidade);
            venda.setData(new java.util.Date());
            vendaDAO.registrarVenda(venda);
            listaVendas.add(venda);

            double valorParcial = produto.getPreco() * quantidade;
            valorTotal += valorParcial;

            System.out.println("Produto adicionado à venda. Subtotal: R$ " + valorParcial);
            System.out.print("Deseja adicionar outro produto? (s/n): ");
            String resposta = leitor.nextLine();
            if (!resposta.equalsIgnoreCase("s")) {
                continuar = false;
            }
        }

        if (valorTotal > 0) {
            System.out.println("\nVenda finalizada!");
            System.out.printf("Valor total: R$ %.2f\n", valorTotal);

            System.out.println("Forma de pagamento:");
            System.out.println("1 - Dinheiro");
            System.out.println("2 - Cartão");
            System.out.println("3 - PIX");
            System.out.print("Escolha a opção: ");
            int opcaoPagamento = leitor.nextInt();
            leitor.nextLine();

            String formaPagamento = switch (opcaoPagamento) {
                case 1 -> "Dinheiro";
                case 2 -> "Cartão";
                case 3 -> "PIX";
                default -> "Forma não identificada";
            };

            System.out.println("Pagamento via: " + formaPagamento);
            System.out.println("Venda concluída com sucesso!");
        } else {
            System.out.println("Nenhum produto foi vendido.");
        }
    }

    private static void listarVendas() {
        VendaDAO vendaDAO = new VendaDAO();
        List<Venda> vendas = vendaDAO.vendasRegistradas();

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada.");
        } else {
            System.out.println("--- Lista de Vendas ---");
            for (Venda v : vendas) {
                System.out.println(v);
            }
        }
    }

    private static void buscarVendaPorId() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("Digite o ID da venda: ");
        long id = leitor.nextLong();
        leitor.nextLine();

        VendaDAO vendaDAO = new VendaDAO();
        Venda venda = vendaDAO.buscarPorId(id);

        if (venda == null) {
            System.out.println("Venda não encontrada.");
        } else {
            System.out.println("Venda encontrada:");
            System.out.println(venda);
        }
    }

    private static void cancelarVenda() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("Digite o ID da venda: ");
        long id = leitor.nextLong();
        leitor.nextLine();

        VendaDAO vendaDAO = new VendaDAO();
        boolean sucesso = vendaDAO.removerVendaPorId(id);

        if (sucesso) {
            System.out.println("Venda cancelada com sucesso!");
        } else {
            System.out.println("Venda não encontrada ou erro ao cancelar.");
        }
    }

    private static void devolucaoProduto() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("ID da venda para devolução: ");
        long id = leitor.nextLong();
        leitor.nextLine();

        VendaDAO vendaDAO = new VendaDAO();
        if (vendaDAO.registrarDevolucao(id)) {
            System.out.println("Devolução registrada com sucesso.");
        } else {
            System.out.println("Erro ao registrar devolução.");
        }
    }

    private static void trocaProduto() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("ID da venda: ");
        long idVenda = leitor.nextLong();

        System.out.print("ID do produto antigo: ");
        long idAntigo = leitor.nextLong();

        System.out.print("ID do novo produto: ");
        long idNovo = leitor.nextLong();

        System.out.print("Quantidade: ");
        int qtd = leitor.nextInt();
        leitor.nextLine();

        VendaDAO vendaDAO = new VendaDAO();
        boolean sucesso = vendaDAO.trocarProdutoNaVenda(idVenda, idAntigo, idNovo, qtd);

        if (sucesso) {
            System.out.println("Produto trocado com sucesso!");
        } else {
            System.out.println("Erro ao trocar produto. Verifique o estoque ou IDs.");
        }
    }
}

