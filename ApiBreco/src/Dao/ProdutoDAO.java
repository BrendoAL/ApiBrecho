package Dao;

import Model.Fornecedor;
import Model.Produto;
import View.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {


    public void atualizarEstoque(int produtoId, int quantidade) throws SQLException {
        String sql = "UPDATE tb_produto SET estoque = estoque + ? WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);
            stmt.executeUpdate();
        }
    }

    // Método para buscar produtos por parte do nome
  /*  public List<Produto> buscarPorNome(String termo) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto WHERE nome LIKE ? AND status_atual = 'Ativo'";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + termo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setEstoque(rs.getInt("estoque"));
                    produto.setStatusAtual(rs.getString("status_atual"));
                    produtos.add(produto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }*/
    public static List<Produto> buscarPorNome(String nomeParcial) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, tamanho, preco, statusAtual, tb_fornecedor_id, estoque FROM tb_produto WHERE nome LIKE ?";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeParcial + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setTamanho(rs.getString("tamanho"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setStatusAtual(rs.getString("statusAtual"));
                produto.setTb_fornecedor_id(rs.getInt("tb_fornecedor_id"));
                produto.setEstoque(rs.getInt("estoque"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }



    public static Produto inserirProduto(Produto produto) {
        String sql = "INSERT INTO tb_produto (nome, tamanho, preco, statusAtual, tb_fornecedor_id, estoque) VALUES ( ?, ?,?,?, ?, ?)";
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
        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, fornecedorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = criarProdutoCompleto(rs, fornecedorDAO);
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
        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, fornecedorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = criarProdutoCompleto(rs, fornecedorDAO);
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }

    // Listar todos produtos, com fornecedor completo
    public static List<Produto> listarProduto() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto";
        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Produto produto = criarProdutoCompleto(rs, fornecedorDAO);
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    // Atualizar status do produto
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
    // Método para alterar um produto existente
   /* public void alterarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE tb_produto SET nome = ?, preco = ?, estoque = ?, status_atual = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setString(4, produto.getStatusAtual());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        }
    }*/
    public static boolean alterarProduto(Produto produto) {
        String sql = "UPDATE tb_produto SET nome = ?, tamanho = ?, preco = ?, statusAtual = ?, tb_fornecedor_id = ?, estoque = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, produto.getNome());
            stm.setString(2, produto.getTamanho());
            stm.setDouble(3, produto.getPreco());
            stm.setString(4, produto.getStatusAtual());
            stm.setInt(5, produto.getTb_fornecedor_id());
            stm.setInt(6, produto.getEstoque());
            stm.setInt(7, produto.getId());

            int linhasAfetadas = stm.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar produto: " + e.getMessage());
            return false;
        }
    }


    // Excluir produto se não tiver vendas vinculadas
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
                        return false;
                    }
                }
            }

            try (PreparedStatement deleteStmt = con.prepareStatement(deleteProdutoSql)) {
                deleteStmt.setInt(1, id);
                int linhasAfetadas = deleteStmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    return true;
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
        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                produto = criarProdutoCompleto(rs, fornecedorDAO);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar produto: " + e.getMessage());
        }
        return produto;
    }
    // Método para buscar um produto pelo ID
   /* public Produto getByIdProduto(int id) {
        String sql = "SELECT * FROM tb_produto WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setEstoque(rs.getInt("estoque"));
                    produto.setStatusAtual(rs.getString("status_atual"));
                    return produto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } */

    private static Produto criarProdutoCompleto(ResultSet rs, FornecedorDAO fornecedorDAO) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setTamanho(rs.getString("tamanho"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setStatusAtual(rs.getString("statusAtual"));
        int fornecedorId = rs.getInt("tb_fornecedor_id");
        produto.setTb_fornecedor_id(fornecedorId);
        produto.setEstoque(rs.getInt("estoque"));


        Fornecedor fornecedor = fornecedorDAO.getById(fornecedorId);
        produto.setFornecedor(fornecedor);

        return produto;
    }
}
