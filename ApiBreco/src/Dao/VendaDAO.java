package Dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.Venda;
import Model.VendaItem;
import View.ConexaoDB;

public class VendaDAO {


    public int inserirVenda(Venda venda) {
        String sql = "INSERT INTO tb_venda (dataVenda, total, cliente_id, forma_pagamento) VALUES (?, ?, ?, ?)";
        int idVenda = -1;

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(venda.getDataVenda()));
            stmt.setDouble(2, venda.getTotal());
            stmt.setInt(3, venda.getClienteId());
            stmt.setString(4, venda.getFormaPagamento());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idVenda = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idVenda;
    }


    public void inserirItemVenda(VendaItem item) {
        String sql = "INSERT INTO tb_venda_itens (tb_venda_id, tb_produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getVendaId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Venda> listarVendas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = """
            SELECT v.id, v.dataVenda, v.total, v.cliente_id, v.forma_pagamento,
                   i.tb_produto_id, p.nome AS produto_nome, i.quantidade, i.preco_unitario
            FROM tb_venda v
            LEFT JOIN tb_venda_itens i ON v.id = i.tb_venda_id
            LEFT JOIN tb_produto p ON i.tb_produto_id = p.id
            ORDER BY v.id
        """;

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Venda venda = null;
            int idVendaAtual = -1;

            while (rs.next()) {
                int idVenda = rs.getInt("id");

                if (idVenda != idVendaAtual) {
                    venda = new Venda();
                    venda.setId(idVenda);
                    venda.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                    venda.setTotal(rs.getDouble("total"));
                    venda.setClienteId(rs.getInt("cliente_id"));
                    venda.setFormaPagamento(rs.getString("forma_pagamento"));
                    vendas.add(venda);
                    idVendaAtual = idVenda;
                }

                int produtoId = rs.getInt("tb_produto_id");
                if (produtoId > 0) {
                    VendaItem item = new VendaItem();
                    item.setVendaId(idVenda);
                    item.setProdutoId(produtoId);
                    item.setProdutoNome(rs.getString("produto_nome"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                    venda.getItens().add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendas;
    }


    public Venda buscarVendaPorId(int id) {
        Venda venda = null;
        String sql = """
            SELECT v.id, v.dataVenda, v.total, v.cliente_id, v.forma_pagamento,
                   i.tb_produto_id, p.nome AS produto_nome, i.quantidade, i.preco_unitario
            FROM tb_venda v
            LEFT JOIN tb_venda_itens i ON v.id = i.tb_venda_id
            LEFT JOIN tb_produto p ON i.tb_produto_id = p.id
            WHERE v.id = ?
        """;

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (venda == null) {
                        venda = new Venda();
                        venda.setId(rs.getInt("id"));
                        venda.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                        venda.setTotal(rs.getDouble("total"));
                        venda.setClienteId(rs.getInt("cliente_id"));
                        venda.setFormaPagamento(rs.getString("forma_pagamento"));
                        venda.setItens(new ArrayList<>());
                    }

                    int produtoId = rs.getInt("tb_produto_id");
                    if (produtoId > 0) {
                        VendaItem item = new VendaItem();
                        item.setVendaId(venda.getId());
                        item.setProdutoId(produtoId);
                        item.setProdutoNome(rs.getString("produto_nome"));
                        item.setQuantidade(rs.getInt("quantidade"));
                        item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                        venda.getItens().add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venda;
    }


    public void excluirVenda(int id) {
        String sqlItens = "DELETE FROM tb_venda_itens WHERE tb_venda_id = ?";
        String sqlVenda = "DELETE FROM tb_venda WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                stmtItens.setInt(1, id);
                stmtItens.executeUpdate();
            }

            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda)) {
                stmtVenda.setInt(1, id);
                stmtVenda.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean existeDevolucaoParaVenda(int vendaId) {
        String sql = "SELECT COUNT(*) FROM tb_devolucao WHERE venda_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean registrarDevolucao(int vendaId, String motivo, double valorDevolvido) {
        String sqlDevolucao = "INSERT INTO tb_devolucao (venda_id, motivo, valor_devolvido, data_devolucao) VALUES (?, ?, ?, ?)";
        String sqlUpdateVenda = "UPDATE tb_venda SET status = 'Devolvida' WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtDevolucao = conn.prepareStatement(sqlDevolucao)) {
                stmtDevolucao.setInt(1, vendaId);
                stmtDevolucao.setString(2, motivo);
                stmtDevolucao.setDouble(3, valorDevolvido);
                stmtDevolucao.setDate(4, Date.valueOf(LocalDate.now()));
                stmtDevolucao.executeUpdate();
            }

            try (PreparedStatement stmtUpdateVenda = conn.prepareStatement(sqlUpdateVenda)) {
                stmtUpdateVenda.setInt(1, vendaId);
                stmtUpdateVenda.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean adicionarAoTotalVenda(int idVenda, double diferenca) {
        String sql = "UPDATE tb_venda SET total = total + ? WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, diferenca);
            stmt.setInt(2, idVenda);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean trocarProdutoNaVenda(int idVenda, int idProdutoAntigo, int idProdutoNovo, int quantidade, double novoPrecoUnitario) {
        String sql = "UPDATE tb_venda_itens SET tb_produto_id = ?, quantidade = ?, preco_unitario = ? WHERE tb_venda_id = ? AND tb_produto_id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProdutoNovo);
            stmt.setInt(2, quantidade);
            stmt.setDouble(3, novoPrecoUnitario);
            stmt.setInt(4, idVenda);
            stmt.setInt(5, idProdutoAntigo);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}