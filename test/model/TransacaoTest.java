package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransacaoTest {

    @Test
    public void testCriacaoTransacao() {
        LocalDate data = LocalDate.of(2026, 10, 8);
        Transacao transacao = new Transacao(1, "Salário", 3500.0, "RECEITA", data);

        assertEquals(1, transacao.getId());
        assertEquals("Salário", transacao.getDescricao());
        assertEquals(3500.0, transacao.getValor(), 0.001);
        assertEquals("RECEITA", transacao.getTipo());
        assertEquals(data, transacao.getData());
    }

    @Test
    public void testIdentificacaoReceitaEDespesa() {
        Transacao receita = new Transacao("Freela", 1200.0, "RECEITA", LocalDate.now());
        Transacao despesa = new Transacao("Mercado", 450.0, "DESPESA", LocalDate.now());

        assertTrue(receita.isReceita());
        assertFalse(despesa.isReceita());
    }

    @Test
    public void testAlteracaoValores() {
        Transacao t = new Transacao();
        t.setId(10);
        t.setDescricao("Internet");
        t.setValor(120.0);
        t.setTipo("DESPESA");
        LocalDate data = LocalDate.of(2026, 10, 1);
        t.setData(data);

        assertEquals(10, t.getId());
        assertEquals("Internet", t.getDescricao());
        assertEquals(120.0, t.getValor(), 0.001);
        assertEquals("DESPESA", t.getTipo());
        assertEquals(data, t.getData());
    }
}
