package service;

import Dao.FornecedorDAO;
import Model.Fornecedor;

import java.util.List;

public class FornecedorService {
    public boolean cadastrar (Fornecedor fornecedor) {
        return FornecedorDAO.cadastrarFornecedor(fornecedor);
    }

    public Fornecedor getById(int id) {
        return FornecedorDAO.consultarFornecedorPorId(id);
    }

    public boolean alterar (Fornecedor fornecedor) {
        return FornecedorDAO.alterarFornecedorPorId(fornecedor);
    }

    public boolean excluir (int id) {
        return FornecedorDAO.excluirFornecedorPorId(id);
    }

    public List<Fornecedor> listar(){
        return FornecedorDAO.listarFornecedores();
    }
}
