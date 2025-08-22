package Model;

public class VendaItem {
    private int vendaId;
    private int produtoId;
    private String produtoNome;
    private int quantidade;
    private double precoUnitario;

    public VendaItem() {}

    public VendaItem(int vendaId, int produtoId, String produtoNome, int quantidade, double precoUnitario) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }



    @Override
    public String toString() {
        return "VendaItem{" +
                "vendaId=" + vendaId +
                ", produtoId=" + produtoId +
                ", produtoNome='" + produtoNome + '\'' +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                '}';
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
