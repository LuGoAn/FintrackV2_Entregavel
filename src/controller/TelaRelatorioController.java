package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Transacao;
import utils.FormatoUtils;

import java.util.List;

public class TelaRelatorioController {

    @FXML
    private Label lblTotalReceitas;

    @FXML
    private Label lblTotalDespesas;

    @FXML
    private Label lblSaldoLiquido;

    @FXML
    private Label lblTotalLancamentos;

    @FXML
    private TextArea txtDetalhamento;

    @FXML
    private Button btnFechar;

    public void carregarDados(List<Transacao> transacoes) {
        double totalReceitas = 0.0;
        double totalDespesas = 0.0;
        StringBuilder sb = new StringBuilder();

        for (Transacao t : transacoes) {
            if (t.isReceita()) {
                totalReceitas += t.getValor();
            } else {
                totalDespesas += t.getValor();
            }

            sb.append(String.format("[%s] %s | %s | %s\n",
                    t.getTipo(),
                    FormatoUtils.formatarDataRegistro(t.getData()),
                    t.getDescricao(),
                    FormatoUtils.formatarValorMoeda(t.getValor())));
        }

        double saldo = totalReceitas - totalDespesas;

        lblTotalReceitas.setText(FormatoUtils.formatarValorMoeda(totalReceitas));
        lblTotalDespesas.setText(FormatoUtils.formatarValorMoeda(totalDespesas));
        lblSaldoLiquido.setText(FormatoUtils.formatarValorMoeda(saldo));
        lblTotalLancamentos.setText(String.valueOf(transacoes.size()));

        txtDetalhamento.setText(sb.toString());
    }

    @FXML
    private void handleFechar() {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }
}
