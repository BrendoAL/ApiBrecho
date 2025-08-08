package Model;

import java.util.Date;

public class Venda {
    private long id;
    private Produto produto;
    private int quantidade;
    private double valorTotal;
    private Date data; // <-- Atributo necessário para usar setData()
    private Fornecedor fornecedor;

    public Venda() {
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    public Venda(int id, Produto produto, int quantidade, double valorTotal, Date data) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.data = data;
    }



    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", valorTotal=" + valorTotal +
                ", data=" + data +
                '}';
    }
}
