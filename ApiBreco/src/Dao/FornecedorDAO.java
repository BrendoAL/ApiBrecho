package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Fornecedor;
import View.ConexaoDB;

public class FornecedorDAO {

    // Inserir novo fornecedor
    public static boolean cadastrarFornecedor(Fornecedor fornecedor) {
        String sql = "INSERT INTO tb_fornecedor (nome, telefone, cpf, endereco) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, fornecedor.getNome());
            stm.setString(2, fornecedor.getTelefone());
            stm.setString(3, fornecedor.getCpf());
            stm.setString(4, fornecedor.getEndereco());

            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar Fornecedor/Cliente: " + e.getMessage());
            return false;
        }
    }

    // Consultar fornecedor pelo ID
    public static Fornecedor getById(int id) {
        String sql = "SELECT * FROM tb_fornecedor WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf")); // ou cnpj
                f.setTelefone(rs.getString("telefone"));
                f.setEndereco(rs.getString("endereco"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar fornecedor: " + e.getMessage());
        }
        return null;
    }

    // Alterar fornecedor pelo ID
    public static boolean alterarFornecedorPorId(Fornecedor fornecedor) {
        String sql = "UPDATE tb_fornecedor SET nome = ?, telefone = ?, cpf = ?, endereco = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, fornecedor.getNome());
            stm.setString(2, fornecedor.getTelefone());
            stm.setString(3, fornecedor.getCpf());
            stm.setString(4, fornecedor.getEndereco());
            stm.setInt(5, fornecedor.getId());

            int atualizado = stm.executeUpdate();
            return atualizado > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar fornecedor/cliente: " + e.getMessage());
            return false;
        }
    }

    // Excluir fornecedor se não tiver produtos vinculados
    public static boolean excluirFornecedorPorId(int id) {
        String verificaProdutosSql = "SELECT COUNT(*) FROM tb_produto WHERE tb_fornecedor_id = ?";
        String deleteFornecedorSql = "DELETE FROM tb_fornecedor WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement verificaStmt = con.prepareStatement(verificaProdutosSql)) {

            verificaStmt.setInt(1, id);
            try (ResultSet rs = verificaStmt.executeQuery()) {
                if (rs.next()) {
                    int quantidadeProdutos = rs.getInt(1);
                    if (quantidadeProdutos > 0) {
                        System.out.println("Erro: Fornecedor não pode ser excluído pois possui produtos vinculados.");
                        return false;
                    }
                }
            }

            try (PreparedStatement deleteStmt = con.prepareStatement(deleteFornecedorSql)) {
                deleteStmt.setInt(1, id);
                int linhasAfetadas = deleteStmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    return true;
                } else {
                    System.out.println("Fornecedor não encontrado.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir fornecedor: " + e.getMessage());
            return false;
        }
    }
    public static List<Fornecedor> listarFornecedores() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT id, nome, telefone, cpf, endereco FROM tb_fornecedor";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setTelefone(rs.getString("telefone"));
                f.setCpf(rs.getString("cpf"));
                f.setEndereco(rs.getString("endereco"));
                fornecedores.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar fornecedores: " + e.getMessage());
        }
        return fornecedores;
    }


    // Listar todos fornecedores ordenados por nome
    public static List<Fornecedor> listarFornecedoresAaZ() {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_fornecedor ORDER BY nome ASC";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setTelefone(rs.getString("telefone"));
                f.setCpf(rs.getString("cpf"));
                f.setEndereco(rs.getString("endereco"));
                lista.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar fornecedores: " + e.getMessage());
        }

        return lista;
    }
}
