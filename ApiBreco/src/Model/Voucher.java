package Model;

import java.util.Date;

public class Voucher {
    private long id;
    private int clienteId;  // id do cliente (Fornecedor usado como cliente)
    private double valor;
    private Date validade;
    private boolean usado;



    public Voucher() {}

    public Voucher(int clienteId, double valor, Date validade) {
        this.clienteId = clienteId;
        this.valor = valor;
        this.validade = validade;
        this.usado = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }


}

