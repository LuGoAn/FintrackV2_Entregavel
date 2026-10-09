package repository;

import model.Transacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositorioGenericoTest {

    private RepositorioGenerico<Transacao> repositorio;

    @BeforeEach
    public void setUp() {
        repositorio = new RepositorioGenerico<>();
    }

    @Test
    public void testAdicionarERemoverItem() {
        Transacao t = new Transacao(1, "Conta de Luz", 180.0, "DESPESA", LocalDate.now());

        repositorio.adicionar(t);
        assertEquals(1, repositorio.total());
        assertEquals("Conta de Luz", repositorio.listarTodos().get(0).getDescricao());

        boolean removido = repositorio.remover(t);
        assertTrue(removido);
        assertEquals(0, repositorio.total());
    }

    @Test
    public void testAdicionarTodosComCuringa() {
        List<Transacao> lista = new ArrayList<>();
        lista.add(new Transacao(1, "Item A", 100.0, "RECEITA", LocalDate.now()));
        lista.add(new Transacao(2, "Item B", 200.0, "DESPESA", LocalDate.now()));

        repositorio.adicionarTodos(lista);
        assertEquals(2, repositorio.total());
    }

    @Test
    public void testCopiarParaComCuringa() {
        Transacao t = new Transacao(1, "Salário", 5000.0, "RECEITA", LocalDate.now());
        repositorio.adicionar(t);

        List<Object> destino = new ArrayList<>();
        repositorio.copiarPara(destino);

        assertEquals(1, destino.size());
        assertEquals(t, destino.get(0));
    }

    @Test
    public void testListaImutavelAoTentarModificarDiretamente() {
        Transacao t = new Transacao(1, "Teste", 50.0, "DESPESA", LocalDate.now());
        repositorio.adicionar(t);

        List<Transacao> lista = repositorio.listarTodos();
        assertThrows(UnsupportedOperationException.class, () -> {
            lista.add(new Transacao(2, "Invalido", 10.0, "DESPESA", LocalDate.now()));
        });
    }
}
