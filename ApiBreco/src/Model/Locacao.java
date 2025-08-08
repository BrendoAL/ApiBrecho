package Model;

import java.time.LocalDate;

public class Locacao {
        private int id;
        private Fornecedor fornecedor;
        private Produto produto;
        private int fornecedorId;
        private int produtoId;
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

                // Se objetos não forem nulos, extrair IDs
                if (fornecedor != null) {
                        this.fornecedorId = fornecedor.getId();
                }
                if (produto != null) {
                        this.produtoId = produto.getId();
                }
        }

        // Calcula status automaticamente com base na data
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
        public void setFornecedor(Fornecedor fornecedor) {
                this.fornecedor = fornecedor;
                if (fornecedor != null) {
                        this.fornecedorId = fornecedor.getId();
                }
        }

        public Produto getProduto() { return produto; }
        public void setProduto(Produto produto) {
                this.produto = produto;
                if (produto != null) {
                        this.produtoId = produto.getId();
                }
        }

        public int getFornecedorId() { return fornecedorId; }
        public void setFornecedorId(int fornecedorId) { this.fornecedorId = fornecedorId; }

        public int getProdutoId() { return produtoId; }
        public void setProdutoId(int produtoId) { this.produtoId = produtoId; }

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


