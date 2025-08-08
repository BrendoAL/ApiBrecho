package Dao;

import Model.Produto;
import Model.Venda;
import View.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            Dao.ProdutoDAO produtoDAO = new Dao.ProdutoDAO();

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

                Dao.ProdutoDAO produtoDAO = new Dao.ProdutoDAO();
                Produto p = produtoDAO.getByIdProduto(rs.getInt("tb_produto_id"));
                venda.setProduto(p);
                venda.setQuantidade(1); // ajustar se necessário
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venda;
    }
}
