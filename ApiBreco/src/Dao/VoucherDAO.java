package Dao;

import java.util.Calendar;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Voucher;
import View.ConexaoDB;

public class VoucherDAO {




    public boolean criarVoucher(int clienteId, double valor)
    { System.out.println("Criando voucher para cliente ID: " + clienteId + " com valor: " + valor);

        if (valor <= 0) {
            System.out.println("Valor inválido para criar voucher.");
            return false;
        }

        // Gera validade padrão de 30 dias a partir de hoje
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        java.util.Date validade = cal.getTime();

        Voucher voucher = new Voucher();
        voucher.setClienteId(clienteId);
        voucher.setValor(valor);
        voucher.setValidade(validade);
        voucher.setUsado(false);

        return inserirVoucher(voucher);
    }


    public boolean inserirVoucher(Voucher voucher) {
        String sql = "INSERT INTO tb_voucher (cliente_id, valor, validade, usado) VALUES (?, ?, ?, ?)";
        if (voucher == null || voucher.getClienteId() <= 0 || voucher.getValor() <= 0 || voucher.getValidade() == null) {
            System.out.println("Dados inválidos para inserir voucher.");
            return false;
        }

        try (Connection con = ConexaoDB.getConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, voucher.getClienteId());
            stmt.setDouble(2, voucher.getValor());
            stmt.setDate(3, new java.sql.Date(voucher.getValidade().getTime()));
            stmt.setBoolean(4, voucher.isUsado());

            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir voucher: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Voucher> listarVouchersAtivosPorCliente(int clienteId) {
        System.out.println("Buscando vouchers ativos para cliente ID: " + clienteId);
        {
            List<Voucher> lista = new ArrayList<>();
            String sql = "SELECT * FROM tb_voucher WHERE cliente_id = ? AND usado = FALSE AND validade >= CURDATE()";

            try (Connection con = ConexaoDB.getConexao();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setInt(1, clienteId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Voucher v = new Voucher();
                    v.setId(rs.getLong("id"));
                    v.setClienteId(rs.getInt("cliente_id"));
                    v.setValor(rs.getDouble("valor"));
                    v.setValidade(rs.getDate("validade"));
                    v.setUsado(rs.getBoolean("usado"));
                    lista.add(v);
                }
            } catch (SQLException e) {
                System.out.println("Erro ao listar vouchers ativos: " + e.getMessage());
                e.printStackTrace();
            }
            return lista;
        }
    }

    public Voucher consultarVoucherPorId(long idVoucher) {
        String sql = "SELECT * FROM tb_voucher WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, idVoucher);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Voucher v = new Voucher();
                v.setId(rs.getLong("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setValor(rs.getDouble("valor"));
                v.setValidade(rs.getDate("validade"));
                v.setUsado(rs.getBoolean("usado"));
                return v;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar voucher: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Marca o voucher como usado se ainda for válido e não usado.
     * @param idVoucher ID do voucher
     * @return true se marcou como usado, false caso contrário
     */
    public boolean usarVoucher(long idVoucher) {
        String sqlVerifica = "SELECT usado, validade FROM tb_voucher WHERE id = ?";
        String sqlUsar = "UPDATE tb_voucher SET usado = TRUE WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement psVerifica = con.prepareStatement(sqlVerifica);
             PreparedStatement psUsar = con.prepareStatement(sqlUsar)) {

            psVerifica.setLong(1, idVoucher);
            ResultSet rs = psVerifica.executeQuery();

            if (rs.next()) {
                boolean usado = rs.getBoolean("usado");
                Date validade = rs.getDate("validade");
                Date hoje = new Date();

                if (usado) {
                    System.out.println("Este voucher já foi usado.");
                    return false;
                }
                if (validade.before(hoje)) {
                    System.out.println("Este voucher está expirado.");
                    return false;
                }

                psUsar.setLong(1, idVoucher);
                int linhas = psUsar.executeUpdate();
                return linhas > 0;
            } else {
                System.out.println("Voucher não encontrado.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao usar voucher: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca voucher válido e não usado para um cliente, com valor mínimo desejado (por exemplo, para pagar diferença).
     * Retorna um voucher ou null.
     */
    public Voucher buscarVoucherDisponivel(int clienteId, double valorMinimo) {
        String sql = "SELECT * FROM tb_voucher WHERE cliente_id = ? AND usado = FALSE AND validade >= CURDATE() AND valor >= ? ORDER BY valor ASC LIMIT 1";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);
            stmt.setDouble(2, valorMinimo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Voucher v = new Voucher();
                v.setId(rs.getLong("id"));
                v.setClienteId(rs.getInt("cliente_id"));
                v.setValor(rs.getDouble("valor"));
                v.setValidade(rs.getDate("validade"));
                v.setUsado(rs.getBoolean("usado"));
                return v;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar voucher disponível: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

