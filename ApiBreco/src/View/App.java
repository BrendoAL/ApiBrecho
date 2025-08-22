package View;

import service.MenuFornecedor;
import service.MenuLocacao;
import service.MenuProduto;
import service.MenuVenda;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        if (ConexaoDB.getConexao() != null) {
            int opcao;

            do {
                System.out.println("\n*** MENU INICIAL ***");
                System.out.println("1 - Produto");
                System.out.println("2 - Fornecedor / Cliente");
                System.out.println("3 - Venda");
                System.out.println("4 - Locação");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = leitor.nextInt();
                leitor.nextLine();

                switch (opcao) {
                    case 1 -> MenuProduto.menuProduto();
                    case 2 -> MenuFornecedor.menuFornecedor();
                    case 3 -> MenuVenda.menuVendas();
                    case 4 -> MenuLocacao.menuLocacao(leitor);
                    case 0 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }

            } while (opcao != 0);

        } else {
            System.out.println("Erro de conexão com banco de dados");
        }

        leitor.close();
    }
}
