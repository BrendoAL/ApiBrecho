package service;

import Dao.ProdutoDAO;
import Dao.VendaDAO;
import Model.Produto;
import Model.Venda;

import java.util.List;

public class VendaService {

    private VendaDAO VendaDAO = new VendaDAO();

    public boolean realizarVenda(Venda venda) {
        Produto produto = ProdutoDAO.getByIdProduto(venda.getProduto().getId());
        if (produto == null) {
            return false;
        }

        venda.setProduto(produto);
        VendaDAO.registrarVenda(venda);
        return true;
    }

    public List<Venda> listarVendas() {
        return VendaDAO.vendasRegistradas();
    }

    public Venda buscarPorId(long id) {
        return VendaDAO.buscarPorId(id);
    }

    public boolean cancelarVenda(long id) {
        return VendaDAO.removerVendaPorId(id);
    }

    public boolean registrarDevolucao(long idVenda) {
        return VendaDAO.registrarDevolucao(idVenda);
    }

    public boolean trocarProduto(long idVenda, long idAntigo, long idNovo, int qtd) {
        return VendaDAO.trocarProdutoNaVenda(idVenda, idAntigo, idNovo, qtd);
    }
}
