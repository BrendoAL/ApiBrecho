package View;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import Dao.LocacaoDAO;
import Dao.ProdutoDAO;
import Dao.VendaDAO;
import Model.Fornecedor;
import Dao.FornecedorDAO;
import Model.Locacao;
import Model.Produto;
import Model.Venda;

public class App {
    public static void main(String[] args) {
        menuPrincipal();
    }

    public static void menuPrincipal() {
        Scanner leitor = new Scanner(System.in);

        if (ConexaoDB.getConexao() != null) {
            int opcao;

            do {
                System.out.println("\n*** MENU INICIAL ***");
                System.out.println("1 - Cadastrar Produto");
                System.out.println("2 - Cadastrar Cliente");
                System.out.println("3 - Realizar Venda");
                System.out.println("4 - Realizar Locação");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = leitor.nextInt();
                leitor.nextLine();

                switch (opcao) {
                    case 1 -> menuProduto();
                    case 2 -> menuFornecedor();
                    case 3 -> menuVendas();
                    case 4 -> menuLocacao(leitor);
                    case 0 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }

            } while (opcao != 0);

        } else {
            System.out.println("Erro de conexão com banco de dados");
        }
        leitor.close();
    }

    public static void menuLocacao(Scanner sc) {
        int opcao;
        do {
            System.out.println("\n--- MENU DE LOCAÇÃO ---");
            System.out.println("1. Registrar nova locação");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    registrarLocacao(sc);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    menuPrincipal();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

    }
    public static void registrarLocacao(Scanner sc) {
        try {
            System.out.print("ID do Fornecedor: ");
            int idFornecedor = sc.nextInt();
            sc.nextLine();

            System.out.print("ID do Produto: ");
            int idProduto = sc.nextInt();
            sc.nextLine();

            System.out.print("Data da locação (AAAA-MM-DD): ");
            String dataLocacaoStr = sc.nextLine();
            LocalDate dataLocacao = LocalDate.parse(dataLocacaoStr);

            System.out.print("Data da devolução (AAAA-MM-DD): ");
            String dataDevolucaoStr = sc.nextLine();
            LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoStr);

            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(idFornecedor);

            Produto produto = new Produto();
            produto.setId(idProduto);

            Locacao locacao = new Locacao();
            locacao.setFornecedor(fornecedor);
            locacao.setProduto(produto);
            locacao.setDataLocacao(dataLocacao);
            locacao.setDataDevolucao(dataDevolucao);

            LocacaoDAO.salvarLocacao(locacao);

        } catch (Exception e) {
            System.out.println("Erro ao registrar locação: " + e.getMessage());
        }
    }

    public static void menuFornecedor() {
        Scanner leitor = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- MENU DE CLIENTES ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Consultar Cliente por ID");
            System.out.println("3. Alterar Cliente");
            System.out.println("4. Excluir Cliente");
            System.out.println("5. Listar Fornecedores em ordem alfabética");

            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarFornecedor(leitor);
                    break;
                case 2:
                    consultarFornecedor();
                    break;
                case 3:
                    alterarFornecedor();
                    break;
                case 4:
                    excluirFornecedor();
                    break;
                case 5:
                    listarFornecedores();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    menuPrincipal();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        leitor.close();
    }

    public static void listarFornecedores() {
        List<Fornecedor> lista = FornecedorDAO.listarFornecedores();

        if (lista.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado.");
        } else {
            System.out.println("\n--- Lista de Cliente (ordem alfabética) ---");
            for (Fornecedor f : lista) {
                System.out.println("ID: " + f.getId());
                System.out.println("Nome: " + f.getNome());
                System.out.println("Telefone: " + f.getTelefone());
                System.out.println("CPF: " + f.getCpf());
                System.out.println("Endereço: " + f.getEndereco());
                System.out.println("------------------------------------------");
            }
        }
    }


    private static void cadastrarFornecedor(Scanner leitor) {
        System.out.print("Cliente: ");
        String nome = leitor.nextLine();

        System.out.print("Telefone: ");
        String telefone = leitor.nextLine();

        System.out.print("CPF/CNPJ: ");
        String cpf = leitor.nextLine();

        System.out.print("Endereço: ");
        String endereco = leitor.nextLine();

        Fornecedor cliente = new Fornecedor(nome, telefone, cpf, endereco);

        if (FornecedorDAO.cadastrarFornecedor(cliente)) {
            System.out.println("Cliente cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar.");
        }
        leitor.close();
    }

    public static void consultarFornecedor() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o ID do cliente: ");
        int id = leitor.nextInt();

        Fornecedor fornecedor = FornecedorDAO.consultarFornecedorPorId(id);

        if (fornecedor != null) {
            System.out.println("Cliente encontrado:");
            System.out.println("ID: " + fornecedor.getId());
            System.out.println("Nome: " + fornecedor.getNome());
            System.out.println("CPF/CNPJ: " + fornecedor.getCpf());
            System.out.println("Telefone: " + fornecedor.getTelefone());
            System.out.println("Endereço: " + fornecedor.getEndereco());
        } else {
            System.out.println("Cliente não encontrado.");
        }
        leitor.close();
    }

    public static void alterarFornecedor() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o ID do cliente que deseja alterar: ");
        int id = leitor.nextInt();
        leitor.nextLine(); //

        Fornecedor fornecedorExistente = FornecedorDAO.consultarFornecedorPorId(id);

        if (fornecedorExistente == null) {
            System.out.println("Cliente não encontrado com o ID informado.");
            return;
        }

        System.out.println("\nCliente atual:");
        System.out.println("Nome: " + fornecedorExistente.getNome());
        System.out.println("Telefone: " + fornecedorExistente.getTelefone());
        System.out.println("CPF: " + fornecedorExistente.getCpf());
        System.out.println("Endereço: " + fornecedorExistente.getEndereco());

        System.out.println("\nDigite os novos dados:");

        System.out.print("Novo nome: ");
        String novoNome = leitor.nextLine();

        System.out.print("Novo telefone: ");
        String novoTelefone = leitor.nextLine();

        System.out.print("Novo CPF: ");
        String novoCpf = leitor.nextLine();

        System.out.print("Novo endereço: ");
        String novoEndereco = leitor.nextLine();

        fornecedorExistente.setNome(novoNome);
        fornecedorExistente.setTelefone(novoTelefone);
        fornecedorExistente.setCpf(novoCpf);
        fornecedorExistente.setEndereco(novoEndereco);

        boolean sucesso = FornecedorDAO.alterarFornecedorPorId(fornecedorExistente);

        if (sucesso) {
            System.out.println("Cliente alterado com sucesso!");
        } else {
            System.out.println("Erro ao alterar cliente.");
        }
        leitor.close();
    }

    public static void excluirFornecedor() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o ID do cliente que deseja excluir: ");
        int id = leitor.nextInt();

        boolean sucesso = FornecedorDAO.excluirFornecedorPorId(id);

        if (sucesso) {
            System.out.println("Cliente excluído com sucesso.");
        } else {
            System.out.println("Erro ao excluir cliente.");
        }
        leitor.close();
    }

    //Vendas

    private static void menuVendas() {
        Scanner sc = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n--- MENU DE VENDAS ---");
            System.out.println("1 - Realizar Venda");
            System.out.println("2 - Listar Vendas");
            System.out.println("3 - Buscar Venda Por ID");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

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
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    menuPrincipal();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        sc.close();
    }

    private static void realizarVenda() {
        Scanner leitor = new Scanner(System.in);
        Venda novaVenda = new Venda();

        System.out.print("Cliente: ");
        String cliente = leitor.nextLine();
        leitor.nextLine();

        System.out.print("ID do produto: ");
        int produtoId = leitor.nextInt();

        System.out.print("Quantidade: ");
        int quantidade = leitor.nextInt();
        leitor.nextLine();

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.getByIdProduto(produtoId);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        novaVenda.setProduto(produto);
        novaVenda.setQuantidade(quantidade);
        novaVenda.setData(new java.util.Date());

        VendaDAO vendaDAO = new VendaDAO();
        vendaDAO.salvar(novaVenda);
        System.out.println("Venda realizada com sucesso! ID: " + novaVenda.getId());

        leitor.close();
    }

    private static void listarVendas() {
        VendaDAO vendaDAO = new VendaDAO();
        List<Venda> vendas = vendaDAO.listarTodas();

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
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o ID da venda: ");
        long id = sc.nextLong();
        sc.nextLine();

        VendaDAO vendaDAO = new VendaDAO();
        Venda venda = vendaDAO.buscarPorId(id);

        if (venda == null) {
            System.out.println("Venda não encontrada.");
        } else {
            System.out.println("Venda encontrada:");
            System.out.println(venda);
        }
        sc.close();
    }

    //Menu produtos

    private static void menuProduto() {
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
                    menuPrincipal();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        sc.close();
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

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setTamanho(tamanho);
        produto.setPreco(preco);
        produto.setStatusAtual(status);
        produto.setTb_fornecedor_id(fornecedorId);

        ProdutoDAO.inserirProduto(produto);
        System.out.println("Produto cadastrado com sucesso.");
    }

    private static void listarProdutos() {
        List<Produto> produtos = ProdutoDAO.listarProduto();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("Lista de produtos:");
            for (Produto p : produtos) {
                System.out.println("ID: " + p.getId() +
                        ", Nome: " + p.getNome() +
                        ", Tamanho: " + p.getTamanho() +
                        ", Preço: " + p.getPreco() +
                        ", Status: " + p.getStatusAtual() +
                        ", Fornecedor ID: " + p.getTb_fornecedor_id());
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

        ProdutoDAO.alterarProduto(produto);
        System.out.println("Produto alterado com sucesso.");
    }

    private static void excluirProduto(Scanner sc) {
        System.out.print("Digite o ID do produto para deletar: ");
        int id = sc.nextInt();
        sc.nextLine();

        ProdutoDAO.excluirProduto(id);
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
            System.out.println("Produto encontrado:");
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Tamanho: " + produto.getTamanho());
            System.out.println("Preço: " + produto.getPreco());
            System.out.println("Status: " + produto.getStatusAtual());
            System.out.println("Fornecedor ID: " + produto.getTb_fornecedor_id());
        }
    }
 }
