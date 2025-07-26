package Dao;

import Model.Produto;
import View.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public static List<Produto> getAll() {
    List<Produto> produtos = new ArrayList<>();
    String sql = "SELECT * FROM tb_produto";

    try {
        Connection con = ConexaoDB.getConexao();
        PreparedStatement stm = con.prepareStatement(sql);
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            Produto p = new Produto();
            p.setId(rs.getInt("id"));
            p.setNome(rs.getString("nome"));
            p.setTamanho(rs.getString("tamanho"));
            p.setPreco(rs.getDouble("preco"));
            p.setStatusAtual(rs.getString("statusAtual"));
            p.setTb_fornecedor_id(rs.getInt("tb_fornecedor_id"));
            produtos.add(p);
        }

    } catch (SQLException e) {
         System.out.println("Erro ao listar produtos: " + e.getMessage());
    }
    return produtos;
    }

    public static Produto inserirProduto(Produto produto) {
        String sql = "INSERT INTO tb_produto (nome, tamanho, preco, statusAtual, tb_fornecedor_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getTamanho());
            stm.setDouble(3, produto.getPreco());
            stm.setString(4, produto.getStatusAtual());
            stm.setInt(5, produto.getTb_fornecedor_id());

            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
        return produto;
    }


    public static Produto alterarProduto(Produto produto) {
        String sql ="UPDATE produto SET nome = ?, tamanho = ?, preco = ?, statusAtual = ?, tb_fornecedor_id = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getTamanho());
            stm.setDouble(3, produto.getPreco());
            stm.setString(4, produto.getStatusAtual());
            stm.setInt(5, produto.getTb_fornecedor_id());

            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao alterar produto: " + e.getMessage());
        }
        return produto;
    }

    public static void excluirProduto(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, id);
            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public static Produto getByIdProduto(int id) {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, id);
            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
        return produto;
    }
}
