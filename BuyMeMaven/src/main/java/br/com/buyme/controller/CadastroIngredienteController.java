package br.com.buyme.controller;

import java.io.IOException;
import java.util.Random;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.entity.Ingrediente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroIngredienteController {
	
	@FXML private TextField txtCodigo,txtDescricao;
	
	private IngredienteDAO ingDao = new IngredienteDAO();
	private boolean gerouCod = false;
	
	 /**
     * Método que gera o código de um produto
     */
    @FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			Ingrediente ing = null;
			while(true){
				int codProduto = randomGenerator.nextInt(10000);
				ing = ingDao.getOne(codProduto+"");
				if(ing != null){
					continue;
				}else{
					txtCodigo.setText(codProduto+"");
					gerouCod = true;
					break;
				}
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
    /**
     * Método que faz o cadastro do ingrediente
     */
	@FXML
	public void cadastrarIngrediente(){
		if(gerouCod){
			try{
				String codIng = txtCodigo.getText();
				String descIng = txtDescricao.getText();
				if(codIng != null && !codIng.isEmpty() && descIng != null && !descIng.isEmpty()){
					Ingrediente ing = new Ingrediente();
					ing.setCodigo(codIng);
					ing.setDescricao(descIng);
					ingDao.salvar(ing);
					
					Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Salvar Ingrediente");
			        dialogoInfo.setContentText("Ingrediente salvo com sucesso!");
			        dialogoInfo.showAndWait();
			        
					limparCampos();
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Salvar Ingrediente");
			        dialogoInfo.setContentText("Existem campos obrigatórios nulos!");
			        dialogoInfo.showAndWait();
				}
			}catch (Exception e){
				e.printStackTrace();
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Salvar Ingrediente");
		        dialogoInfo.setContentText("Houve um erro ao salvar o ingrediente, tente novamente!");
		        dialogoInfo.showAndWait();
			}
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Ingrediente");
	        dialogoInfo.setContentText("O código do ingrediente deve ser gerado!");
	        dialogoInfo.showAndWait();
		}
	}
	
	/**
	 * Método para limpar os campos
	 */
	public void limparCampos(){
		txtCodigo.setText("");
		txtDescricao.setText("");
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
