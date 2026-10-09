package dao;

import database.Conexao;
import model.Transacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    private Connection conexaoExterna;

    public TransacaoDAO() {
    }

    public TransacaoDAO(Connection conexao) {
        this.conexaoExterna = conexao;
    }

    private Connection obterConexao() throws SQLException {
        if (conexaoExterna != null && !conexaoExterna.isClosed()) {
            return conexaoExterna;
        }
        return Conexao.getConexao();
    }

    public boolean inserir(Transacao transacao) {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data) VALUES (?, ?, ?, ?)";
        boolean fecharAoTerminar = (conexaoExterna == null);

        try {
            Connection conn = obterConexao();
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, transacao.getDescricao());
                stmt.setDouble(2, transacao.getValor());
                stmt.setString(3, transacao.getTipo());
                stmt.setString(4, transacao.getData().toString());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        transacao.setId(rs.getInt(1));
                    }
                }
                return true;
            } finally {
                if (fecharAoTerminar) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir transacao: " + e.getMessage());
            return false;
        }
    }

    public List<Transacao> listarTodas() {
        List<Transacao> lista = new ArrayList<>();
        String sql = "SELECT id, descricao, valor, tipo, data FROM transacoes ORDER BY data DESC, id DESC";
        boolean fecharAoTerminar = (conexaoExterna == null);

        try {
            Connection conn = obterConexao();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String desc = rs.getString("descricao");
                    double valor = rs.getDouble("valor");
                    String tipo = rs.getString("tipo");
                    LocalDate data = LocalDate.parse(rs.getString("data"));

                    lista.add(new Transacao(id, desc, valor, tipo, data));
                }
            } finally {
                if (fecharAoTerminar) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar transacoes: " + e.getMessage());
        }
        return lista;
    }

    public Transacao buscarPorId(int id) {
        String sql = "SELECT id, descricao, valor, tipo, data FROM transacoes WHERE id = ?";
        boolean fecharAoTerminar = (conexaoExterna == null);

        try {
            Connection conn = obterConexao();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String desc = rs.getString("descricao");
                        double valor = rs.getDouble("valor");
                        String tipo = rs.getString("tipo");
                        LocalDate data = LocalDate.parse(rs.getString("data"));
                        return new Transacao(id, desc, valor, tipo, data);
                    }
                }
            } finally {
                if (fecharAoTerminar) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar transacao: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(Transacao transacao) {
        String sql = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, data = ? WHERE id = ?";
        boolean fecharAoTerminar = (conexaoExterna == null);

        try {
            Connection conn = obterConexao();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, transacao.getDescricao());
                stmt.setDouble(2, transacao.getValor());
                stmt.setString(3, transacao.getTipo());
                stmt.setString(4, transacao.getData().toString());
                stmt.setInt(5, transacao.getId());
                return stmt.executeUpdate() > 0;
            } finally {
                if (fecharAoTerminar) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar transacao: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM transacoes WHERE id = ?";
        boolean fecharAoTerminar = (conexaoExterna == null);

        try {
            Connection conn = obterConexao();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            } finally {
                if (fecharAoTerminar) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir transacao: " + e.getMessage());
            return false;
        }
    }

    public double calcularSaldo() {
        double saldo = 0.0;
        List<Transacao> todas = listarTodas();
        for (Transacao t : todas) {
            if (t.isReceita()) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }
}
