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

    public static Produto inserirProduto(Produto produto) {
        String sql = "INSERT INTO tb_produto (nome, tamanho, preco, statusAtual, tb_fornecedor_id, quantidadeEstoque) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getTamanho());
            stm.setDouble(3, produto.getPreco());
            stm.setString(4, produto.getStatusAtual());
            stm.setInt(5, produto.getTb_fornecedor_id());
            stm.setInt(6, produto.getEstoque());

            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
        return produto;
    }

    public static List<Produto> listarProdutosDisponiveisPorFornecedor(int fornecedorId) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto WHERE tb_fornecedor_id = ? AND statusAtual = 'Disponível'";
        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, fornecedorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setStatusAtual(rs.getString("statusAtual"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos disponíveis: " + e.getMessage());
        }
        return produtos;
    }

    public static List<Produto> listarProdutosPorFornecedor(int fornecedorId) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto WHERE tb_fornecedor_id = ?";
        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, fornecedorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setStatusAtual(rs.getString("statusAtual"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public static void atualizarStatusProduto(int idProduto, String novoStatus) {
        String sql = "UPDATE tb_produto SET statusAtual = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status do produto: " + e.getMessage());
        }
    }

    public static List<Produto> listarProduto() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto";

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setTamanho(rs.getString("tamanho"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setStatusAtual(rs.getString("statusAtual"));
                produto.setTb_fornecedor_id(rs.getInt("tb_fornecedor_id"));
                produto.setEstoque(rs.getInt("quantidadeEstoque"));

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public static Produto alterarProduto(Produto produto) {
        String sql = "UPDATE tb_produto SET nome = ?, tamanho = ?, preco = ?, statusAtual = ?, tb_fornecedor_id = ?, quantidadeEstoque = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getTamanho());
            stm.setDouble(3, produto.getPreco());
            stm.setString(4, produto.getStatusAtual());
            stm.setInt(5, produto.getTb_fornecedor_id());
            stm.setInt(6, produto.getEstoque());
            stm.setInt(7, produto.getId());

            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao alterar produto: " + e.getMessage());
        }
        return produto;
    }

    public static boolean excluirProduto(int id) {
        String verificaVendaSql = "SELECT COUNT(*) FROM tb_venda WHERE tb_produto_id = ?";
        String deleteProdutoSql = "DELETE FROM tb_produto WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement verificaStmt = con.prepareStatement(verificaVendaSql)) {

            verificaStmt.setInt(1, id);
            try (ResultSet rs = verificaStmt.executeQuery()) {
                if (rs.next()) {
                    int quantidadeVendas = rs.getInt(1);
                    if (quantidadeVendas > 0) {
                        System.out.println("Erro: Produto não pode ser excluído pois está vinculado a uma ou mais vendas.");
                        return false; // NÃO excluiu
                    }
                }
            }

            // Se chegou aqui, não tem venda vinculada, pode excluir
            try (PreparedStatement deleteStmt = con.prepareStatement(deleteProdutoSql)) {
                deleteStmt.setInt(1, id);
                int linhasAfetadas = deleteStmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    return true;  // Excluiu com sucesso
                } else {
                    System.out.println("Produto não encontrado.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }


    public static Produto getByIdProduto(int id) {
        Produto produto = null;
        String sql = "SELECT * FROM tb_produto WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setTamanho(rs.getString("tamanho"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setStatusAtual(rs.getString("statusAtual"));
                produto.setTb_fornecedor_id(rs.getInt("tb_fornecedor_id"));
                produto.setEstoque(rs.getInt("quantidadeEstoque"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar produto: " + e.getMessage());
        }
        return produto;
    }
}
