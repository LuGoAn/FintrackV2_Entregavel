package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioGenerico<T> {

    private List<T> itens = new ArrayList<>();

    public void adicionar(T item) {
        if (item != null) {
            itens.add(item);
        }
    }

    public void adicionarTodos(List<? extends T> novosItens) {
        if (novosItens != null) {
            itens.addAll(novosItens);
        }
    }

    public boolean remover(T item) {
        return itens.remove(item);
    }

    public List<T> listarTodos() {
        return Collections.unmodifiableList(itens);
    }

    public int total() {
        return itens.size();
    }

    public void copiarPara(List<? super T> destino) {
        if (destino != null) {
            destino.addAll(itens);
        }
    }

    public void limpar() {
        itens.clear();
    }
}
