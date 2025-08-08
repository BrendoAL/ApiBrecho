package View;

import Dao.ProdutoDAO;
import Model.Produto;
import java.util.List;
import java.util.Scanner;

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
                    alterarProduto(sc);
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

    private static void inserirProduto(Scanner sc) {
        System.out.print("Digite o nome do produto: ");
        String nome = sc.nextLine();

        System.out.print("Digite o tamanho do produto: ");
        String tamanho = sc.nextLine();

        System.out.print("Digite o preço do produto: ");
        double preco = sc.nextDouble();
        sc.nextLine();

        System.out.print("Digite o status do produto: ");
        String status = sc.nextLine();

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
        System.out.println("Produto cadastrado com sucesso.");
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

    private static void alterarProduto(Scanner sc) { //preciso colocar a condição de alterar somente os digitados o restante que pular deixar como esta exceto o status que quero zerado ate ser locado o produto
        System.out.print("Digite o ID do produto para alterar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto produto = ProdutoDAO.getByIdProduto(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Digite o novo nome do produto: ");
        produto.setNome(sc.nextLine());

        System.out.print("Digite o novo tamanho do produto: ");
        produto.setTamanho(sc.nextLine());

        System.out.print("Digite o novo preço do produto: ");
        produto.setPreco(sc.nextDouble());
        sc.nextLine();

        System.out.print("Digite o novo status do produto: ");
        produto.setStatusAtual(sc.nextLine());

        System.out.print("Digite o novo ID do fornecedor do produto: ");
        produto.setTb_fornecedor_id(sc.nextInt());
        sc.nextLine();

        System.out.print("Digite a nova quantidade em estoque: ");
        produto.setEstoque(sc.nextInt());
        sc.nextLine();

        ProdutoDAO.alterarProduto(produto);
        System.out.println("Produto alterado com sucesso.");
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

