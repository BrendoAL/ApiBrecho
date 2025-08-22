package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;



import View.ConexaoDB;
import Model.Fornecedor;
import Model.Locacao;
import Model.Produto;


import java.util.List;

public class LocacaoDAO {
    public static List<Locacao> buscarTodasLocacoes() {
        List<Locacao> locacoes = new ArrayList<>();
        String sql = "SELECT id, data_locacao, data_devolucao, fornecedor_id, produto_id, statusAtual FROM tb_locacao";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Locacao locacao = new Locacao();
                locacao.setId(rs.getInt("id"));
                locacao.setDataLocacao(rs.getDate("data_locacao").toLocalDate());
                locacao.setDataDevolucao(rs.getDate("data_devolucao").toLocalDate());
                locacao.setStatusAtual(rs.getString("statusAtual"));

                int fornecedorId = rs.getInt("fornecedor_id");
                int produtoId = rs.getInt("produto_id");

                locacao.setFornecedorId(fornecedorId);
                locacao.setProdutoId(produtoId);

                locacoes.add(locacao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar locações: " + e.getMessage());
        }

        return locacoes;
    }


    public static Locacao buscarLocacaoPorId(int id) {
        String sql = "SELECT * FROM tb_locacao WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Locacao locacao = new Locacao();

                locacao.setId(rs.getInt("id"));
                locacao.setDataLocacao(rs.getDate("data_locacao").toLocalDate());
                locacao.setDataDevolucao(rs.getDate("data_devolucao").toLocalDate());

                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(rs.getInt("fornecedor_id"));
                locacao.setFornecedor(fornecedor);

                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                locacao.setProduto(produto);

                return locacao;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar locação por ID: " + e.getMessage());
        }
        return null;
    }

    public static void atualizarDataDevolucao(int id, LocalDate novaData) {
        String sql = "UPDATE tb_locacao SET dataDevolucao = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(novaData));
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar data de devolução: " + e.getMessage());
        }
    }
    public static void excluirLocacao(int id) {
        String sql = "DELETE FROM tb_locacao WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Locação excluída com sucesso.");
            } else {
                System.out.println("Nenhuma locação foi encontrada com o ID informado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir locação: " + e.getMessage());
        }
    }

    public static void atualizarStatusProduto(int idProduto, String novoStatus) {
        String sql = "UPDATE tb_produto SET statusAtual = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status do produto: " + e.getMessage());
        }
    }


    public static void salvarLocacao(Locacao locacao) {
        String sqlInsert = "INSERT INTO tb_locacao (fornecedor_id, produto_id, data_locacao, data_devolucao, statusAtual) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateProduto = "UPDATE tb_produto SET statusAtual = ? WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement stmtUpdateProduto = conn.prepareStatement(sqlUpdateProduto)) {


            String novoStatus = definirStatus(locacao.getDataDevolucao());//aqui define o status baseado na data


            stmtInsert.setInt(1, locacao.getFornecedor().getId());
            stmtInsert.setInt(2, locacao.getProduto().getId());
            stmtInsert.setDate(3, java.sql.Date.valueOf(locacao.getDataLocacao()));
            stmtInsert.setDate(4, java.sql.Date.valueOf(locacao.getDataDevolucao()));
            stmtInsert.setString(5, novoStatus);
            stmtInsert.executeUpdate();

            // Aqui atualiza o status do produto
            stmtUpdateProduto.setString(1, novoStatus);
            stmtUpdateProduto.setInt(2, locacao.getProduto().getId());
            stmtUpdateProduto.executeUpdate();

            System.out.println("Locação registrada e status do produto atualizado com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar locação: " + e.getMessage());
        }
    }

    private static String definirStatus(LocalDate dataDevolucao) {
        if (LocalDate.now().isAfter(dataDevolucao)) {
            return "Indisponível";
        } else {
            return "Locado";
        }
    }
}
