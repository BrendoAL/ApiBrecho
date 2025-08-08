package Dao;

import Model.Locacao;
import View.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class LocacaoDAO {

    public static void salvarLocacao(Locacao locacao) {
        String sqlInsert = "INSERT INTO tb_locacao (fornecedor_id, produto_id, data_locacao, data_devolucao, statusAtual) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateProduto = "UPDATE tb_produto SET statusAtual = ? WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement stmtUpdateProduto = conn.prepareStatement(sqlUpdateProduto)) {

            // Define status com base na data
            String novoStatus = definirStatus(locacao.getDataDevolucao());

            // Inserir locação
            stmtInsert.setInt(1, locacao.getFornecedor().getId());
            stmtInsert.setInt(2, locacao.getProduto().getId());
            stmtInsert.setDate(3, java.sql.Date.valueOf(locacao.getDataLocacao()));
            stmtInsert.setDate(4, java.sql.Date.valueOf(locacao.getDataDevolucao()));
            stmtInsert.setString(5, novoStatus);
            stmtInsert.executeUpdate();

            // Atualizar status do produto
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

