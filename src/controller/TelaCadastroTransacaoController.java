package controller;

import dao.TransacaoDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Transacao;

import java.time.LocalDate;

public class TelaCadastroTransacaoController {

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<String> cbTipo;

    @FXML
    private DatePicker dpData;

    @FXML
    private CheckBox chkRecorrente;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private TelaPrincipalController parentController;
    private TransacaoDAO dao = new TransacaoDAO();

    public void setParentController(TelaPrincipalController parent) {
        this.parentController = parent;
    }

    @FXML
    public void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList("RECEITA", "DESPESA"));
        cbTipo.setValue("RECEITA");
        dpData.setValue(LocalDate.now());
    }

    @FXML
    private void handleSalvar() {
        String descricao = txtDescricao.getText() != null ? txtDescricao.getText().trim() : "";
        if (descricao.isEmpty()) {
            exibirAlerta("A descrição não pode ser vazia.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
            if (valor <= 0) {
                exibirAlerta("O valor deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            exibirAlerta("Informe um valor numérico válido.");
            return;
        }

        String tipo = cbTipo.getValue();
        if (tipo == null || tipo.isEmpty()) {
            exibirAlerta("Selecione o tipo da movimentação.");
            return;
        }

        LocalDate data = dpData.getValue();
        if (data == null) {
            data = LocalDate.now();
        }

        if (chkRecorrente.isSelected()) {
            descricao += " (Recorrente)";
        }

        Transacao nova = new Transacao(descricao, valor, tipo, data);
        if (dao.inserir(nova)) {
            if (parentController != null) {
                parentController.carregarTransacoes();
            }
            fecharJanela();
        } else {
            exibirAlerta("Erro ao salvar a transação no banco de dados.");
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
