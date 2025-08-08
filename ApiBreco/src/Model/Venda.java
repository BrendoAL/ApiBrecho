package Model;

import java.util.Date;

public class Venda {
    private long id;
    private Model.Produto produto;
    private Date data;
    private int quantidade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Model.Produto getProduto() {
        return produto;
    }

    public void setProduto(Model.Produto produto) {
        this.produto = produto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ID venda:" + id + ", produto: " + produto +
                ", data: " + data + ", quantidade: " + quantidade;
    }

    public Venda(long id, Model.Produto produto, Date data,
                 int quantidade) {
        this.id = id;
        this.produto = produto;
        this.data = data;
        this.quantidade = quantidade;
    }

    public Venda() {
        super();
    }
}
