package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Produto;
import Model.Venda;
import View.ConexaoDB;

public class VendaDAO {

    public void salvar(Venda venda) {
        String sql = "INSERT INTO tb_venda (dataVenda, total, tb_produto_id) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, new java.sql.Date(venda.getData().getTime()));
            stmt.setDouble(2, venda.getProduto().getPreco() * venda.getQuantidade());
            stmt.setLong(3, venda.getProduto().getId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                venda.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Venda> listarTodas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM tb_venda";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ProdutoDAO produtoDAO = new ProdutoDAO();

            while (rs.next()) {
                Venda v = new Venda();
                v.setId(rs.getLong("id"));
                v.setData(rs.getDate("dataVenda"));
                Produto p = produtoDAO.getByIdProduto(rs.getInt("tb_produto_id"));
                v.setProduto(p);
                v.setQuantidade(1); // Supondo que seja 1 unidade vendida. Caso tenha campo "quantidade", adicione.
                vendas.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendas;
    }

    public Venda buscarPorId(long id) {
        String sql = "SELECT * FROM tb_venda WHERE id = ?";
        Venda venda = null;

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setData(rs.getDate("dataVenda"));

                ProdutoDAO produtoDAO = new ProdutoDAO();
                Produto p = produtoDAO.buscarPorId(rs.getInt("tb_produto_id"));
                venda.setProduto(p);
                venda.setQuantidade(1); // ajustar se necessário
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venda;
    }
}


private static void realizarVenda(Scanner sc) {
    Venda novaVenda = new Venda();

    System.out.print("Cliente: ");
    String cliente = sc.nextLine();
    sc.nextLine();

    System.out.print("ID do produto: ");
    int produtoId = sc.nextInt();

    System.out.print("Quantidade: ");
    int quantidade = sc.nextInt();
    sc.nextLine();

    ProdutoDAO produtoDAO = new ProdutoDAO();
    Produto produto = produtoDAO.buscarPorId(produtoId);

    if (produto == null) {
        System.out.println("Produto não encontrado.");
        return;
    }

    novaVenda.setProduto(produto);
    novaVenda.setQuantidade(quantidade);
    novaVenda.setData(new java.util.Date()); // data atual

    VendaDAO vendaDAO = new VendaDAO();
    vendaDAO.salvar(novaVenda);
    System.out.println("Venda realizada com sucesso! ID: " + novaVenda.getId());
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

private static void buscarVendaPorId(Scanner sc) {
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
}
}
 
 
