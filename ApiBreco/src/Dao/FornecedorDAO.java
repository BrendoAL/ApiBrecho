package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Fornecedor;
import View.ConexaoDB;

public class FornecedorDAO {

    public static boolean cadastrarFornecedor(Fornecedor cliente) {
        String sql = "INSERT INTO tb_fornecedor (nome, telefone) VALUES (?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, cliente.getNome());
            stm.setString(2, cliente.getTelefone());
            stm.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar Fornecedor/Cliente: " + e.getMessage());
            return false;
        }
    }
}