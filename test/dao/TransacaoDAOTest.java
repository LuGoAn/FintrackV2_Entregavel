package dao;

import database.Conexao;
import model.Transacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransacaoDAOTest {

    private Connection conexaoMemoria;
    private TransacaoDAO dao;

    @BeforeEach
    public void setUp() throws SQLException {
        conexaoMemoria = Conexao.getConexao("jdbc:sqlite::memory:");
        dao = new TransacaoDAO(conexaoMemoria);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (conexaoMemoria != null && !conexaoMemoria.isClosed()) {
            conexaoMemoria.close();
        }
    }

    @Test
    public void testInserirEListarTransacoes() {
        Transacao t = new Transacao("Salário Mensal", 3500.0, "RECEITA", LocalDate.of(2026, 10, 5));
        boolean inserido = dao.inserir(t);

        assertTrue(inserido);
        assertTrue(t.getId() > 0);

        List<Transacao> lista = dao.listarTodas();
        assertEquals(1, lista.size());
        assertEquals("Salário Mensal", lista.get(0).getDescricao());
        assertEquals(3500.0, lista.get(0).getValor(), 0.001);
    }

    @Test
    public void testBuscarPorId() {
        Transacao t = new Transacao("Internet", 150.0, "DESPESA", LocalDate.now());
        dao.inserir(t);

        Transacao encontrada = dao.buscarPorId(t.getId());
        assertNotNull(encontrada);
        assertEquals("Internet", encontrada.getDescricao());
    }

    @Test
    public void testAtualizarTransacao() {
        Transacao t = new Transacao("Aluguel", 1200.0, "DESPESA", LocalDate.now());
        dao.inserir(t);

        t.setValor(1300.0);
        t.setDescricao("Aluguel Reajustado");
        boolean atualizado = dao.atualizar(t);

        assertTrue(atualizado);

        Transacao atualizada = dao.buscarPorId(t.getId());
        assertEquals(1300.0, atualizada.getValor(), 0.001);
        assertEquals("Aluguel Reajustado", atualizada.getDescricao());
    }

    @Test
    public void testExcluirTransacao() {
        Transacao t = new Transacao("Assinatura TV", 80.0, "DESPESA", LocalDate.now());
        dao.inserir(t);

        boolean excluido = dao.excluir(t.getId());
        assertTrue(excluido);

        Transacao busca = dao.buscarPorId(t.getId());
        assertNull(busca);
    }

    @Test
    public void testCalcularSaldo() {
        dao.inserir(new Transacao("Salário", 4000.0, "RECEITA", LocalDate.now()));
        dao.inserir(new Transacao("Mercado", 1000.0, "DESPESA", LocalDate.now()));
        dao.inserir(new Transacao("Combustível", 300.0, "DESPESA", LocalDate.now()));

        double saldo = dao.calcularSaldo();
        assertEquals(2700.0, saldo, 0.001);
    }
}
