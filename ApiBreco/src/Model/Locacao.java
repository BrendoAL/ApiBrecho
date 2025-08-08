package Model;

import java.time.LocalDate;

public class Locacao {
    private int id;
    private Fornecedor fornecedor;
    private Produto produto;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;
    private String statusAtual; // "Locado" ou "Indisponível"

    public Locacao() {}

    public Locacao(int id, Fornecedor fornecedor, Produto produto, LocalDate dataLocacao, LocalDate dataDevolucao) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.produto = produto;
        this.dataLocacao = dataLocacao;
        this.dataDevolucao = dataDevolucao;
        this.statusAtual = calcularStatus();
    }

    private String calcularStatus() {
        if (LocalDate.now().isAfter(dataDevolucao)) {
            return "Indisponível";
        } else {
            return "Locado";
        }
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) { this.dataLocacao = dataLocacao; }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public String getStatusAtual() {
        return calcularStatus();
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }
}

