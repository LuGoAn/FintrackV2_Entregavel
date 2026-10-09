package model;

import java.time.LocalDate;

public class Transacao {

    private int id;
    private String descricao;
    private double valor;
    private String tipo;
    private LocalDate data;

    public Transacao() {
    }

    public Transacao(String descricao, double valor, String tipo, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }

    public Transacao(int id, String descricao, double valor, String tipo, LocalDate data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isReceita() {
        return "RECEITA".equalsIgnoreCase(tipo) || "ENTRADA".equalsIgnoreCase(tipo);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | R$ %.2f | %s", tipo, descricao, valor, data);
    }
}
