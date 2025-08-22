package service;

import java.util.List;
import java.util.Scanner;

import Dao.ProdutoDAO;
import Model.Produto;

public class MenuProduto {


    public static void menuProduto() {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- MENU DE PRODUTOS ---");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Alterar Produto");
            System.out.println("4 - Excluir Produto");
            System.out.println("5 - Buscar Produto Por ID");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    inserirProduto(sc);
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    buscarEAlterarProduto();
                    break;
                case 4:
                    excluirProduto(sc);
                    break;
                case 5:
                    getByIdProduto(sc);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }
    public static void buscarEAlterarProduto() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite parte do nome do produto para buscar: ");
        String termo = sc.nextLine();

        List<Produto> produtos = ProdutoDAO.buscarPorNome(termo);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado com esse nome.");
            return;
        }

        System.out.println("Produtos encontrados:");
        for (Produto p : produtos) {
            System.out.printf("ID: %d | Nome: %s | Status: %s | Estoque: %d\n",
                    p.getId(), p.getNome(), p.getStatusAtual(), p.getEstoque());
        }

        System.out.print("Digite o ID do produto que deseja alterar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto produto = ProdutoDAO.getByIdProduto(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Deixe o campo vazio para manter o valor atual.");

        System.out.printf("Nome atual (%s): ", produto.getNome());
        String nome = sc.nextLine();
        if (!nome.isEmpty()) {
            produto.setNome(nome);
        }

        System.out.printf("Tamanho atual (%s): ", produto.getTamanho());
        String tamanho = sc.nextLine();
        if (!tamanho.isEmpty()) {
            produto.setTamanho(tamanho);
        }

        System.out.printf("Preço atual (%.2f): ", produto.getPreco());
        String precoStr = sc.nextLine();
        if (!precoStr.isEmpty()) {
            try {
                produto.setPreco(Double.parseDouble(precoStr));
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido, mantendo valor atual.");
            }
        }


        produto.setStatusAtual("Disponível");

        System.out.printf("ID do fornecedor atual (%d): ", produto.getTb_fornecedor_id());
        String fornecedorStr = sc.nextLine();
        if (!fornecedorStr.isEmpty()) {
            try {
                produto.setTb_fornecedor_id(Integer.parseInt(fornecedorStr));
            } catch (NumberFormatException e) {
                System.out.println("ID de fornecedor inválido, mantendo valor atual.");
            }
        }

        System.out.printf("Estoque atual (%d): ", produto.getEstoque());
        String estoqueStr = sc.nextLine();
        if (!estoqueStr.isEmpty()) {
            try {
                produto.setEstoque(Integer.parseInt(estoqueStr));
            } catch (NumberFormatException e) {
                System.out.println("Estoque inválido, mantendo valor atual.");
            }
        }

        boolean sucesso = ProdutoDAO.alterarProduto(produto);
        if (sucesso) {
            System.out.println("Produto alterado com sucesso. Status definido como 'Disponível'.");
        } else {
            System.out.println("Erro ao alterar produto.");
        }
    }


    private static void inserirProduto(Scanner sc) {
        System.out.print("Digite o nome do produto: ");
        String nome = sc.nextLine();

        System.out.print("Digite o tamanho do produto: ");
        String tamanho = sc.nextLine();

        System.out.print("Digite o preço do produto: ");
        double preco = sc.nextDouble();
        sc.nextLine();


        String status = "Disponível";

        System.out.print("Digite o ID do fornecedor do produto: ");
        int fornecedorId = sc.nextInt();
        sc.nextLine();

        System.out.print("Digite a quantidade em estoque: ");
        int estoque = sc.nextInt();
        sc.nextLine();

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setTamanho(tamanho);
        produto.setPreco(preco);
        produto.setStatusAtual(status);
        produto.setTb_fornecedor_id(fornecedorId);
        produto.setEstoque(estoque);

        ProdutoDAO.inserirProduto(produto);
        System.out.println("Produto cadastrado com sucesso. Status definido como 'Disponível'.");
    }


    private static void listarProdutos() {
        List<Produto> produtos = ProdutoDAO.listarProduto();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("\n--- Lista de Produtos ---");
            for (Produto p : produtos) {
                System.out.println("ID: " + p.getId()
                        + " | Nome: " + p.getNome()
                        + " | Tamanho: " + p.getTamanho()
                        + " | Preço: R$" + p.getPreco()
                        + " | Status: " + p.getStatusAtual()
                        + " | Estoque: " + p.getEstoque()
                        + " | Fornecedor ID: " + p.getTb_fornecedor_id());
            }
        }
    }

    private static void alterarProduto(Scanner sc) {
        System.out.print("Digite o ID do produto para alterar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto produto = ProdutoDAO.getByIdProduto(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Deixe o campo vazio para manter o valor atual.");

        System.out.print("Digite o novo nome do produto (" + produto.getNome() + "): ");
        String nome = sc.nextLine();
        if (!nome.isEmpty()) {
            produto.setNome(nome);
        }

        System.out.print("Digite o novo tamanho do produto (" + produto.getTamanho() + "): ");
        String tamanho = sc.nextLine();
        if (!tamanho.isEmpty()) {
            produto.setTamanho(tamanho);
        }

        System.out.print("Digite o novo preço do produto (" + produto.getPreco() + "): ");
        String precoStr = sc.nextLine();
        if (!precoStr.isEmpty()) {
            try {
                produto.setPreco(Double.parseDouble(precoStr));
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido. Mantendo valor atual.");
            }
        }


        produto.setStatusAtual("Disponível");

        System.out.print("Digite o novo ID do fornecedor do produto (" + produto.getTb_fornecedor_id() + "): ");
        String fornecedorStr = sc.nextLine();
        if (!fornecedorStr.isEmpty()) {
            try {
                produto.setTb_fornecedor_id(Integer.parseInt(fornecedorStr));
            } catch (NumberFormatException e) {
                System.out.println("ID de fornecedor inválido. Mantendo valor atual.");
            }
        }

        System.out.print("Digite a nova quantidade em estoque (" + produto.getEstoque() + "): ");
        String estoqueStr = sc.nextLine();
        if (!estoqueStr.isEmpty()) {
            try {
                produto.setEstoque(Integer.parseInt(estoqueStr));
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida. Mantendo valor atual.");
            }
        }

        ProdutoDAO.alterarProduto(produto);
        System.out.println("Produto alterado com sucesso. Status definido como 'Disponível'.");
    }


    private static void excluirProduto(Scanner sc) {
        System.out.print("Digite o ID do produto para deletar: ");
        int id = sc.nextInt();
        sc.nextLine();

        if( ProdutoDAO.excluirProduto(id));
        System.out.println("Produto excluído com sucesso.");
    }

    private static void getByIdProduto(Scanner sc) {
        System.out.print("Digite o ID do produto para buscar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto produto = ProdutoDAO.getByIdProduto(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
        } else {
            System.out.println("\n--- Produto Encontrado ---");
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Tamanho: " + produto.getTamanho());
            System.out.println("Preço: R$" + produto.getPreco());
            System.out.println("Status: " + produto.getStatusAtual());
            System.out.println("Estoque: " + produto.getEstoque());
            System.out.println("Fornecedor ID: " + produto.getTb_fornecedor_id());
        }
    }
}

