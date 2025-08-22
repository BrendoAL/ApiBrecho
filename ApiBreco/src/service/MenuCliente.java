package service;

import java.util.List;
import java.util.Scanner;
import Dao.ClienteDAO;
import Model.Cliente;

public class MenuCliente {

    public static void menuCliente() {
        Scanner sc = new Scanner(System.in);
        ClienteDAO clienteDAO = new ClienteDAO();
        int opcao = -1;

        while (opcao != 5) {
            System.out.println("\n=== Menu de Clientes ===");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Alterar Cliente");
            System.out.println("4 - Excluir Cliente");
            System.out.println("5 - Voltar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    Cliente novoCliente = new Cliente();
                    System.out.print("Nome: ");
                    novoCliente.setNome(sc.nextLine());
                    System.out.print("Telefone: ");
                    novoCliente.setTelefone(sc.nextLine());
                    System.out.print("CPF: ");
                    novoCliente.setCpf(sc.nextLine());
                    System.out.print("Endereço: ");
                    novoCliente.setEndereco(sc.nextLine());
                    clienteDAO.cadastrarCliente(novoCliente);
                    break;

                case 2:
                    List<Cliente> clientes = clienteDAO.listarClientes();
                    System.out.println("\n=== Lista de Clientes ===");
                    for (Cliente c : clientes) {
                        System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() +
                                " | Telefone: " + c.getTelefone() + " | CPF: " + c.getCpf() +
                                " | Endereço: " + c.getEndereco());
                    }
                    break;

                case 3:
                    System.out.print("ID do cliente a alterar: ");
                    int idAlt = Integer.parseInt(sc.nextLine());
                    Cliente clienteAlt = new Cliente();
                    clienteAlt.setId(idAlt);
                    System.out.print("Novo Nome: ");
                    clienteAlt.setNome(sc.nextLine());
                    System.out.print("Novo Telefone: ");
                    clienteAlt.setTelefone(sc.nextLine());
                    System.out.print("Novo CPF: ");
                    clienteAlt.setCpf(sc.nextLine());
                    System.out.print("Novo Endereço: ");
                    clienteAlt.setEndereco(sc.nextLine());
                    clienteDAO.alterarCliente(clienteAlt);
                    break;

                case 4:
                    System.out.print("ID do cliente a excluir: ");
                    int idExc = Integer.parseInt(sc.nextLine());
                    clienteDAO.excluirCliente(idExc);
                    break;

                case 5:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}


