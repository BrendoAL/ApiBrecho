package service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Dao.LocacaoDAO;
import Dao.ProdutoDAO;
import Model.Fornecedor;
import Model.Locacao;
import Model.Produto;

public class MenuLocacao {


    public static void menuLocacao(Scanner sc) {
        int opcao;

        do {
            System.out.println("\n--- MENU DE LOCAÇÃO ---");
            System.out.println("1. Registrar nova locação");
            System.out.println("2. Listar locações");
            System.out.println("3. Realizar devolução de locação");
            System.out.println("4. Excluir locação");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    registrarLocacao(sc);
                    break;
                case 2:
                    listarLocacoes();
                    break;
                case 3:
                    devolucaoLocacao(sc);
                    break;
                case 4:
                    System.out.print("Digite o ID da locação para excluir: ");
                    int idLocacao = sc.nextInt();
                    sc.nextLine();
                    excluirLocacao(idLocacao);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void listarLocacoes() {
        List<Locacao> locacoes = LocacaoDAO.buscarTodasLocacoes();

        if (locacoes.isEmpty()) {
            System.out.println("Nenhuma locação encontrada.");
            return;
        }

        System.out.println("\n--- LISTA DE LOCAÇÕES ---");
        for (Locacao l : locacoes) {
            System.out.println("ID: " + l.getId());
            System.out.println("Fornecedor: " + l.getFornecedor().getNome());
            System.out.println("Produto: " + l.getProduto().getNome());
            System.out.println("Data de Locação: " + l.getDataLocacao());
            System.out.println("Data de Devolução: " + l.getDataDevolucao());
            System.out.println("-----------------------------");
        }
    }
    public static void devolucaoLocacao(Scanner sc) {
        System.out.print("Digite o ID da locação para devolução: ");
        int idLocacao = sc.nextInt();
        sc.nextLine();

        Locacao locacao = LocacaoDAO.buscarLocacaoPorId(idLocacao);

        if (locacao == null) {
            System.out.println("Locação não encontrada.");
            return;
        }

        LocalDate hoje = LocalDate.now();
        long diasRestantes = ChronoUnit.DAYS.between(hoje, locacao.getDataDevolucao());

        if (diasRestantes <= 5 && diasRestantes > 0) {
            System.out.println(" Atenção: Faltam apenas " + diasRestantes + " dias para a devolução.");
            System.out.print("Deseja postergar a data de devolução? (s/n): ");
            String resposta = sc.nextLine();
            if (resposta.equalsIgnoreCase("s")) {
                System.out.print("Nova data de devolução (AAAA-MM-DD): ");
                String novaDataStr = sc.nextLine();
                LocalDate novaData = LocalDate.parse(novaDataStr);
                LocacaoDAO.atualizarDataDevolucao(idLocacao, novaData);
                System.out.println("Data de devolução atualizada.");
                return;
            }
        }

        // Devolver e atualizar status do produto
        LocacaoDAO.excluirLocacao(idLocacao);
        ProdutoDAO.atualizarStatusProduto(locacao.getProduto().getId(), "Disponível");
        System.out.println("Produto devolvido com sucesso.");
    }
    public static void excluirLocacao(int idLocacao) {
        Locacao locacao = LocacaoDAO.buscarLocacaoPorId(idLocacao);

        if (locacao == null) {
            System.out.println("Locação não encontrada.");
            return;
        }

        LocacaoDAO.excluirLocacao(idLocacao);
        ProdutoDAO.atualizarStatusProduto(locacao.getProduto().getId(), "Disponível");

        System.out.println("Locação excluída com sucesso.");
    }

    public static void registrarLocacao(Scanner sc) {
        try {
            System.out.print("ID do Fornecedor: ");
            int idFornecedor = sc.nextInt();
            sc.nextLine();

            // Buscar somente produtos DISPONÍVEIS do fornecedor
            List<Produto> produtosDisponiveis = ProdutoDAO.listarProdutosDisponiveisPorFornecedor(idFornecedor);

            if (produtosDisponiveis.isEmpty()) {
                System.out.println("Nenhum produto disponível para locação deste fornecedor.");
                return;
            }

            System.out.println("\n--- Produtos Disponíveis do Fornecedor ---");
            for (Produto p : produtosDisponiveis) {
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() + " | Status: " + p.getStatusAtual());
            }

            System.out.println("\nComo deseja selecionar os produtos para locação?");
            System.out.println("1 - Importar TODOS os produtos disponíveis");
            System.out.println("2 - Selecionar produtos manualmente pelo ID");
            System.out.print("Escolha: ");
            int escolha = sc.nextInt();
            sc.nextLine();

            List<Integer> produtosSelecionados = new ArrayList<>();

            if (escolha == 1) {
                for (Produto p : produtosDisponiveis) {
                    produtosSelecionados.add(p.getId());
                }
            } else if (escolha == 2) {
                int idProduto;
                do {
                    System.out.print("Digite o ID do produto (0 para encerrar): ");
                    idProduto = sc.nextInt();
                    sc.nextLine();
                    if (idProduto != 0) {
                        // Verifica se está disponível
                        boolean encontrado = false;
                        for (Produto p : produtosDisponiveis) {
                            if (p.getId() == idProduto) {
                                produtosSelecionados.add(idProduto);
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            System.out.println("Produto não disponível ou não pertence ao fornecedor.");
                        }
                    }
                } while (idProduto != 0);
            } else {
                System.out.println("Opção inválida.");
                return;
            }

            if (produtosSelecionados.isEmpty()) {
                System.out.println("Nenhum produto selecionado.");
                return;
            }


            System.out.print("Data da locação (AAAA-MM-DD): ");
            String dataLocacaoStr = sc.nextLine();
            LocalDate dataLocacao = LocalDate.parse(dataLocacaoStr);

            System.out.print("Data da devolução (AAAA-MM-DD): ");
            String dataDevolucaoStr = sc.nextLine();
            LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoStr);

            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(idFornecedor);

            for (Integer produtoId : produtosSelecionados) {
                Produto produto = new Produto();
                produto.setId(produtoId);

                Locacao locacao = new Locacao();
                locacao.setFornecedor(fornecedor);
                locacao.setProduto(produto);
                locacao.setDataLocacao(dataLocacao);
                locacao.setDataDevolucao(dataDevolucao);

                LocacaoDAO.salvarLocacao(locacao);
                ProdutoDAO.atualizarStatusProduto(produtoId, "Locado");
            }

            System.out.println("Locação registrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao registrar locação: " + e.getMessage());
        }
    }
}

