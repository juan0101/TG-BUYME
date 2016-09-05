package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.MotivoPerdaDAO;
import br.com.buyme.entity.MotivoPerda;
import br.com.buyme.popup.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EditarMotivoPerdaController {
	
	private Popup popup = new Popup("Editar Motivo de Perda");
	@FXML private TableView<MotivoPerda> tabela_motivo_perda;
    @FXML private TableColumn<MotivoPerda, String> cCodigo;
    @FXML private TableColumn<MotivoPerda, String> cNome;
    private ObservableList<MotivoPerda> data;
    private List<MotivoPerda> mps = null;
	@FXML private TextField codigo,nome;
	private MotivoPerdaDAO mpDao = new MotivoPerdaDAO();
	@FXML private Button btnAtualizar;
	
	//OBJETO SELECIONADO
	private MotivoPerda mp = null;
	
	@FXML
	protected void initialize(){
		try{
			mps = mpDao.getAllMotivoPerda();
			if(mps != null){
				cCodigo.setCellValueFactory(
						new PropertyValueFactory<MotivoPerda,String>("codigo")
						);
				cNome.setCellValueFactory(
						new PropertyValueFactory<MotivoPerda,String>("descricao")
						);
				data = FXCollections.observableArrayList();
				data.addAll(mps);
				tabela_motivo_perda.setItems(data);
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	@FXML
	public void atualizarMotivoPerda(){
		String codMotivo = codigo.getText();
		String descMotivo = nome.getText();
		if(codMotivo != null && !codMotivo.isEmpty() && descMotivo != null && !descMotivo.isEmpty()){
			mpDao.editarMotivoPerda(mp.getId(), descMotivo);
			popup.getInformation("Edição feita com sucesso!");
			
			codigo.setText("");
			nome.setText("");
			atualizarLista();
		}else{
			popup.getError("Campos obrigatórios em branco!");
		}
	}
	
	@FXML
	public void editar(){
		mp = tabela_motivo_perda.getSelectionModel().getSelectedItem();
		if(mp != null){
			codigo.setText(mp.getCodigo());
			nome.setText(mp.getDescricao());
			btnAtualizar.setDisable(false);
		}else{
			popup.getError("Selecione um motivo de perda para editar!");
		}
	}
	
	@FXML
	public void excluir(){
		mp = tabela_motivo_perda.getSelectionModel().getSelectedItem();
		if(mp != null){
			mpDao.excluir(mp.getId());
			atualizarLista();
		}else{
			popup.getError("Selecione um motivo para excluir!");
		}
	}
	
	public void atualizarLista(){
		mps.clear();
		data.clear();
		mps = mpDao.getAllMotivoPerda();
		data.addAll(mps);
	}
	
	@FXML
	public void voltarMenu(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/AdminView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}

}
