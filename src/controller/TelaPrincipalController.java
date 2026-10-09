package controller;

import dao.TransacaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Transacao;
import utils.FormatoUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class TelaPrincipalController {

    @FXML
    private Label lblSaldo;

    @FXML
    private TableView<Transacao> tabelaTransacoes;

    @FXML
    private TableColumn<Transacao, Integer> colId;

    @FXML
    private TableColumn<Transacao, LocalDate> colData;

    @FXML
    private TableColumn<Transacao, String> colDescricao;

    @FXML
    private TableColumn<Transacao, String> colTipo;

    @FXML
    private TableColumn<Transacao, Double> colValor;

    @FXML
    private Button btnNova;

    @FXML
    private Button btnRemover;

    @FXML
    private Button btnRelatorio;

    @FXML
    private Button btnAtualizar;

    private TransacaoDAO dao = new TransacaoDAO();
    private ObservableList<Transacao> transacoesData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        colData.setCellFactory(column -> new TableCell<Transacao, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(FormatoUtils.formatarDataRegistro(item));
                }
            }
        });

        colValor.setCellFactory(column -> new TableCell<Transacao, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(FormatoUtils.formatarValorMoeda(item));
                }
            }
        });

        tabelaTransacoes.setItems(transacoesData);
        carregarTransacoes();
    }

    public void carregarTransacoes() {
        transacoesData.clear();
        transacoesData.addAll(dao.listarTodas());

        double saldo = dao.calcularSaldo();
        lblSaldo.setText(FormatoUtils.formatarValorMoeda(saldo));
    }

    @FXML
    private void handleNovaTransacao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaCadastroTransacao.fxml"));
            Parent root = loader.load();

            TelaCadastroTransacaoController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Cadastrar Transação");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir o formulário: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemover() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione uma transação na tabela para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente remover a transação \"" + selecionada.getDescricao() + "\"?");

        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            if (dao.excluir(selecionada.getId())) {
                carregarTransacoes();
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível remover a transação.");
            }
        }
    }

    @FXML
    private void handleRelatorio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaRelatorio.fxml"));
            Parent root = loader.load();

            TelaRelatorioController controller = loader.getController();
            controller.carregarDados(dao.listarTodas());

            Stage stage = new Stage();
            stage.setTitle("Relatório de Finanças");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir o relatório: " + e.getMessage());
        }
    }

    @FXML
    private void handleAtualizar() {
        carregarTransacoes();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
