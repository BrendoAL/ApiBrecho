package Dao;

import Model.Produto;
import Model.Venda;
import View.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public void registrarVenda (Venda venda) {
        String sql = "INSERT INTO tb_venda (dataVenda, total, tb_produto_id, quantidade, fornecedor_id) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // RETURN_GENERATED_KEYS retorna os valores gerados automaticamente
            stmt.setDate(1, new java.sql.Date(venda.getData().getTime()));
            stmt.setDouble(2, venda.getProduto().getPreco() * venda.getQuantidade());
            stmt.setLong(3, venda.getProduto().getId());
            stmt.setInt(4, venda.getQuantidade());
            stmt.setInt(5, venda.getFornecedor().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                venda.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Venda> vendasRegistradas() {
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
                v.setQuantidade(rs.getInt("quantidade"));
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

        try (Connection conn = ConexaoDB.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setData(rs.getDate("dataVenda"));

                ProdutoDAO produtoDAO = new ProdutoDAO();
                Produto p = produtoDAO.getByIdProduto(rs.getInt("tb_produto_id"));
                venda.setProduto(p);
                venda.setQuantidade(rs.getInt("quantidade"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venda;
    }

    public boolean removerVendaPorId(long id) {
        String sql = "DELETE FROM tb_venda WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cancelar venda: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarDevolucao(long idVenda) {
        try (Connection conn = ConexaoDB.getConexao()) {

            // 1. Buscar produto e quantidade da venda
            String sqlBusca = "SELECT tb_produto_id, quantidade FROM tb_venda WHERE id = ?";
            PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca);
            stmtBusca.setLong(1, idVenda);
            ResultSet rs = stmtBusca.executeQuery();

            if (!rs.next()) {
                System.out.println("Venda não encontrada.");
                return false;
            }

            long produtoId = rs.getLong("tb_produto_id");
            int quantidade = rs.getInt("quantidade");

            // 2. Atualizar o estoque do produto
            String sqlEstoque = "UPDATE tb_produto SET estoque = estoque + ? WHERE id = ?";
            PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque);
            stmtEstoque.setInt(1, quantidade);
            stmtEstoque.setLong(2, produtoId);
            stmtEstoque.executeUpdate();

            // 3. Deletar a venda
            String sqlDeleta = "DELETE FROM tb_venda WHERE id = ?";
            PreparedStatement stmtDeleta = conn.prepareStatement(sqlDeleta);
            stmtDeleta.setLong(1, idVenda);
            int linhasAfetadas = stmtDeleta.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
            return false;
        }
    }

    public boolean trocarProdutoNaVenda(long idVenda, long idAntigo, long idNovo, int quantidade) {
        try (Connection conn = ConexaoDB.getConexao()) {

            // 1. Devolve estoque do produto antigo
            PreparedStatement stmtDevolve = conn
                    .prepareStatement("UPDATE tb_produto SET estoque = estoque + ? WHERE id = ?");
            stmtDevolve.setInt(1, quantidade);
            stmtDevolve.setLong(2, idAntigo);
            stmtDevolve.executeUpdate();

            // 2. Verifica estoque do novo produto
            PreparedStatement stmtEstoque = conn.prepareStatement("SELECT estoque FROM tb_produto WHERE id = ?");
            stmtEstoque.setLong(1, idNovo);
            ResultSet rs = stmtEstoque.executeQuery();
            if (!rs.next() || rs.getInt("estoque") < quantidade) {
                return false;
            }

            // 3. Baixa estoque do novo produto
            PreparedStatement stmtBaixar = conn
                    .prepareStatement("UPDATE tb_produto SET estoque = estoque - ? WHERE id = ?");
            stmtBaixar.setInt(1, quantidade);
            stmtBaixar.setLong(2, idNovo);
            stmtBaixar.executeUpdate();

            // 4. Atualiza venda com novo produto
            PreparedStatement stmtAtualiza = conn
                    .prepareStatement("UPDATE tb_venda SET tb_produto_id = ? WHERE id = ?");
            stmtAtualiza.setLong(1, idNovo);
            stmtAtualiza.setLong(2, idVenda);
            stmtAtualiza.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao trocar produto: " + e.getMessage());
            return false;
        }
    }
}