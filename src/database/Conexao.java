package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private static final String URL_PADRAO = "jdbc:sqlite:fintrack.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite não encontrado: " + e.getMessage());
        }
    }

    public static Connection getConexao() throws SQLException {
        return getConexao(URL_PADRAO);
    }

    public static Connection getConexao(String url) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        criarTabelaSeNaoExistir(conn);
        return conn;
    }

    private static void criarTabelaSeNaoExistir(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS transacoes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "descricao VARCHAR(100) NOT NULL, "
                + "valor REAL NOT NULL, "
                + "tipo VARCHAR(10) NOT NULL, "
                + "data TEXT NOT NULL"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar tabela: " + e.getMessage());
        }
    }
}
