package View;

import Model.Produto;
import service.ProdutoService;

import java.util.List;
import java.util.Scanner;

public class MenuProduto {
    private static Scanner leitor;
    private static Scanner sc;

    public static void menuProduto() {
        sc = new Scanner(System.in);
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
                    inserirProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    alterarProduto();
                    break;
                case 4:
                    excluirProduto();
                    break;
                case 5:
                    getByIdProduto();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal..;");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void inserirProduto() {
        leitor = new Scanner(System.in);

        System.out.print("Digite o nome do produto: ");
        String nome = leitor.nextLine();
        System.out.print("Digite o tamanho do produto: ");
        String tamanho = leitor.nextLine();
        System.out.print("Digite o preço do produto: ");
        double preco = leitor.nextDouble();
        leitor.nextLine(); // Limpa o buffer
        System.out.print("Digite o status do produto: ");
        String status = leitor.nextLine();
        System.err.println("Digite o estoque do produto: ");
        int estoque = leitor.nextInt();
        leitor.nextLine();
        System.out.print("Digite o ID do fornecedor do produto: ");
        int fornecedorId = leitor.nextInt();
        leitor.nextLine();

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setTamanho(tamanho);
        produto.setPreco(preco);
        produto.setStatusAtual(status);
        produto.setEstoque(estoque);
        produto.setTb_fornecedor_id(fornecedorId);

        try {
            ProdutoService.cadastrarProduto(produto);
            System.out.println("Produto cadastrado com sucesso.");
        } catch (RuntimeException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    private static void listarProdutos() {
        List<Produto> produtos = ProdutoService.listarProdutos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Lista de produtos:");
            for (Produto p : produtos) {
                System.out.println(p);
            }
        }
    }

    private static void alterarProduto() {
        leitor = new Scanner(System.in);

        System.out.print("Digite o ID do produto para alterar: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Produto produto = ProdutoService.getByID(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Digite o novo nome do produto: ");
        produto.setNome(leitor.nextLine());
        System.out.print("Digite o novo tamanho do produto: ");
        produto.setTamanho(leitor.nextLine());
        System.out.print("Digite o novo preço do produto: ");
        produto.setPreco(leitor.nextDouble());
        leitor.nextLine();
        System.out.print("Digite o novo status do produto: ");
        produto.setStatusAtual(leitor.nextLine());
        System.out.print("Digite o novo ID do fornecedor do produto: ");
        produto.setTb_fornecedor_id(leitor.nextInt());
        leitor.nextLine();

        if(ProdutoService.alterarProduto(produto)) {
            System.out.println("Produto alterado com sucesso.");
        } else {
            System.out.println("Prouto não encontrado.");
        }
    }

    private static void excluirProduto() {
        System.out.print("ID do produto: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        if (ProdutoService.excluirProduto(id)) {
            System.out.println("Produto excluído com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void getByIdProduto() {
        System.out.print("ID do produto: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Produto produto = ProdutoService.listarProdutos().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
        } else {
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Tamanho: " + produto.getTamanho());
            System.out.println("Preço: " + produto.getPreco());
            System.out.println("Status: " + produto.getStatusAtual());
            System.out.println("Estoque: " + produto.getEstoque());
            System.out.println("Fornecedor ID: " + produto.getTb_fornecedor_id());
        }
    }
}
