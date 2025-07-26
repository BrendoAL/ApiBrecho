package View;

import java.util.Scanner;
import Model.Fornecedor;
import Dao.FornecedorDAO;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (ConexaoDB.getConexao() != null) {
            int opcao;

            do {
                System.out.println("\n*** MENU INICIAL ***");
                System.out.println("1 - Cadastrar Produto");
                System.out.println("2 - Cadastrar Fornecedor / Cliente");
                System.out.println("3 - Realizar Venda");
                System.out.println("4 - Realizar Locação");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1 -> System.out.println("Função de cadastro de produto ainda não implementada.");
                    case 2 -> cadastrarFornecedor(sc);
                    case 3 -> System.out.println("Função de venda ainda não implementada.");
                    case 4 -> System.out.println("Função de locação ainda não implementada.");
                    case 0 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }

            } while (opcao != 0);

        } else {
            System.out.println("Erro de conexão com banco de dados");
        }
    }

    private static void cadastrarFornecedor(Scanner leitor) {
        System.out.print("Nome Fornecedor/Cliente: ");
        String nome = leitor.nextLine();

        System.out.print("Telefone: ");
        String telefone = leitor.nextLine();

        Fornecedor cliente = new Fornecedor(nome, telefone);

        if (FornecedorDAO.cadastrarFornecedor(cliente)) {
            System.out.println("Fornecedor/Cliente cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar.");
        }
    }
}