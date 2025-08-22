package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private int id;
    private LocalDate dataVenda;
    private double total;
    private int clienteId;
    private String formaPagamento;
    private List<VendaItem> itens = new ArrayList<>(); // Lista de itens da venda




    public int getId() { return id; }
    public Venda() {
        super();

    }
    public void setId(int id) { this.id = id; }

    public LocalDate getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public List<VendaItem> getItens() { return itens; }
    public void setItens(List<VendaItem> itens) { this.itens = itens; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venda #").append(id).append(" | Data: ").append(dataVenda)
                .append(" | Cliente ID: ").append(clienteId)
                .append(" | Total: R$ ").append(String.format("%.2f", total))
                .append(" | Pagamento: ").append(formaPagamento).append("\n");
        sb.append("--- Itens da Venda ---\n");
        for (VendaItem item : itens) {
            sb.append("  - ").append(item.getProdutoNome())
                    .append(" | Qtd: ").append(item.getQuantidade())
                    .append(" | Preço Unitário: R$ ").append(String.format("%.2f", item.getPrecoUnitario())).append("\n");
        }
        return sb.toString();
    }
}