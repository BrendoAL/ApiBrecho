package service;

import Dao.FornecedorDAO;
import Dao.ProdutoDAO;
import Model.Fornecedor;
import Model.Produto;
import java.util.List;

public class ProdutoService {

    public static void cadastrarProduto(Produto produto) {
        // Verifica se o fornecedor existe para poder prosseguir com o cadastro
        Fornecedor fornecedor = FornecedorDAO.consultarFornecedorPorId(produto.getTb_fornecedor_id());
        if (fornecedor == null) {
            throw new RuntimeException("Fornecedor não encontrado.");
        }

        // Verifica se o estoque não está negativo
        if (produto.getEstoque() < 0) {
            throw new RuntimeException("O estoque inicial não pode ser negativo.");
        }

        // Verifica se não tem um outro produto com o mesmo nome
        Produto existente = ProdutoDAO.buscarPorNome(produto.getNome());
        if (existente != null) {
            throw new RuntimeException("Já existe um produto com esse nome");
        }

        // se tudo passar, inserir normalmente
        ProdutoDAO.inserirProduto(produto);
    }

    public static List<Produto> listarProdutos() {
        return ProdutoDAO.getAll();
    }

    public static  boolean alterarProduto(Produto produto) {
        Produto existente = ProdutoDAO.getByIdProduto(produto.getId());
        if (existente == null) {
            return false;
        }
        ProdutoDAO.alterarProduto(produto);
        return true;
    }

    public static boolean excluirProduto(int id) {
        Produto produto = ProdutoDAO.getByIdProduto(id);
        if (produto != null) {
            ProdutoDAO.excluirProduto(id);
            return true;
        }
        return false;
    }
    public static Produto getByID(int id) {
        return ProdutoDAO.getByIdProduto(id);
    }
}
