package Model;

public class Produto {

    private int id;
    private String nome;
    private String tamanho; // P, M, G, GG, XGG
    private double preco;
    private String statusAtual;
    private int tb_fornecedor_id;
    private int estoque;

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public Produto() {
    }

    public Produto(int id, String nome, String tamanho, double preco, String statusAtual, int tb_fornecedor_id) {
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.preco = preco;
        this.statusAtual = statusAtual;
        this.tb_fornecedor_id = tb_fornecedor_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public int getTb_fornecedor_id() {
        return tb_fornecedor_id;
    }

    public void setTb_fornecedor_id(int tb_fornecedor_id) {
        this.tb_fornecedor_id = tb_fornecedor_id;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tamanho='" + tamanho + '\'' +
                ", preco=" + preco +
                ", statusAtual='" + statusAtual + '\'' +
                ", tb_fornecedor_id=" + tb_fornecedor_id +
                '}';
    }
}
