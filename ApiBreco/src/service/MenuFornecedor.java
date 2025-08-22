package service;

import java.util.List;
import java.util.Scanner;

import Dao.FornecedorDAO;
import Model.Fornecedor;

public class MenuFornecedor {


    public static void menuFornecedor() {
        Scanner leitor = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- Menu de Clientes/Fornecedores ---");
            System.out.println("1. Cadastrar Fornecedor/Cliente");
            System.out.println("2. Consultar Fornecedor/Cliente por ID");
            System.out.println("3. Alterar Fornecedor/Cliente");
            System.out.println("4. Excluir Fornecedor/Cliente");
            System.out.println("5. Listar Fornecedores em ordem alfabética");
            System.out.println("6. Listar Fornecedores ");

            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarFornecedor(leitor);
                    break;
                case 2:
                    consultarFornecedor(); // FAZER A CONSULTA POR CPF  OU CNPJ
                    break;
                case 3:
                    alterarFornecedor(); // FAZER A ALTERAÇÃO POR CPF OU CNPJ
                    break;
                case 4:
                    excluirFornecedor(); // FAZER A ALTERAÇÃO POR CPF OU CNPJ
                    break;
                case 5:
                    listarFornecedoresAaZ(); //ESSA LISTA MOSTRA DE A á Z
                    break;
                case 6:
                    listarFornecedores();

                    break;

                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);


    }

    public static void listarFornecedores() {
        List<Fornecedor> fornecedores = FornecedorDAO.listarFornecedores();

        System.out.println("=== LISTA DE FORNECEDORES ===");
        for (Fornecedor f : fornecedores) {
            System.out.println("ID: " + f.getId());
            System.out.println("Nome: " + f.getNome());
            System.out.println("Telefone: " + f.getTelefone());
            System.out.println("CPF: " + f.getCpf());
            System.out.println("Endereço: " + f.getEndereco());
            System.out.println("------------------------------------------");
        }
    }


    public static void listarFornecedoresAaZ() {
        List<Fornecedor> lista = FornecedorDAO.listarFornecedoresAaZ();

        if (lista.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado.");
        } else {
            System.out.println("\n--- Lista de Fornecedores (ordem alfabética) ---");
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
        System.out.print("Nome do Fornecedor/Cliente: ");
        String nome = leitor.nextLine();

        System.out.print("Telefone: ");
        String telefone = leitor.nextLine();

        System.out.print("CPF/CNPJ: ");
        String cpf = leitor.nextLine();

        System.out.print("Endereço: ");
        String endereco = leitor.nextLine();

        Fornecedor cliente = new Fornecedor(nome, telefone, cpf, endereco);

        if (FornecedorDAO.cadastrarFornecedor(cliente)) {
            System.out.println("Fornecedor/Cliente cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar.");
        }

    }

    public static void consultarFornecedor() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o ID do fornecedor/cliente: ");
        int id = leitor.nextInt();

        Fornecedor fornecedor = FornecedorDAO.getById(id);

        if (fornecedor != null) {
            System.out.println("Fornecedor/cliente encontrado:");
            System.out.println("ID: " + fornecedor.getId());
            System.out.println("Nome: " + fornecedor.getNome());
            System.out.println("CPF/CNPJ: " + fornecedor.getCpf());
            System.out.println("Telefone: " + fornecedor.getTelefone());
            System.out.println("Endereço: " + fornecedor.getEndereco());
        } else {
            System.out.println("Fornecedor/cliente não encontrado.");
        }

    }

    public static void alterarFornecedor() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o ID do fornecedor que deseja alterar: ");
        int id = leitor.nextInt();
        leitor.nextLine(); // Consumir quebra de linha

        Fornecedor fornecedorExistente = FornecedorDAO.getById(id);

        if (fornecedorExistente == null) {
            System.out.println("Fornecedor não encontrado com o ID informado.");
            return;
        }

        System.out.println("\nFornecedor atual:");
        System.out.println("Nome: " + fornecedorExistente.getNome());
        System.out.println("Telefone: " + fornecedorExistente.getTelefone());
        System.out.println("CPF: " + fornecedorExistente.getCpf());
        System.out.println("Endereço: " + fornecedorExistente.getEndereco());

        System.out.println("\nDigite os novos dados (pressione Enter para manter o valor atual):");

        System.out.print("Novo nome (" + fornecedorExistente.getNome() + "): ");
        String novoNome = leitor.nextLine();
        if (!novoNome.isEmpty()) {
            fornecedorExistente.setNome(novoNome);
        }

        System.out.print("Novo telefone (" + fornecedorExistente.getTelefone() + "): ");
        String novoTelefone = leitor.nextLine();
        if (!novoTelefone.isEmpty()) {
            fornecedorExistente.setTelefone(novoTelefone);
        }

        System.out.print("Novo CPF (" + fornecedorExistente.getCpf() + "): ");
        String novoCpf = leitor.nextLine();
        if (!novoCpf.isEmpty()) {
            fornecedorExistente.setCpf(novoCpf);
        }

        System.out.print("Novo endereço (" + fornecedorExistente.getEndereco() + "): ");
        String novoEndereco = leitor.nextLine();
        if (!novoEndereco.isEmpty()) {
            fornecedorExistente.setEndereco(novoEndereco);
        }

        boolean sucesso = FornecedorDAO.alterarFornecedorPorId(fornecedorExistente);

        if (sucesso) {
            System.out.println("Fornecedor alterado com sucesso!");
        } else {
            System.out.println("Erro ao alterar fornecedor.");
        }
    }


    public static void excluirFornecedor() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Digite o ID do fornecedor/cliente que deseja excluir: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        boolean sucesso = FornecedorDAO.excluirFornecedorPorId(id);

        if (sucesso) {
            System.out.println("Fornecedor/cliente excluído com sucesso.");
        }
        // Caso falso, mensagem de erro já foi exibida no DAO
    }
}
