package br.com.buyme.controller;

import java.io.IOException;
import java.util.Random;

import br.com.buyme.dao.MotivoPerdaDAO;
import br.com.buyme.entity.MotivoPerda;
import br.com.buyme.popup.Popup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroMotivoPerdaController {
	
	@FXML private TextField codigo,nome;
	private Popup popup = new Popup("Cadastro de Motivo de Perda");
	private MotivoPerdaDAO mpDao = new MotivoPerdaDAO();
	
	@FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			MotivoPerda mp = null;
			while(true){
				int codMotivo = randomGenerator.nextInt(10000);
				mp = mpDao.getOneByCod(codMotivo+"");
				if(mp != null){
					continue;
				}else{
					codigo.setText(codMotivo+"");
					break;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
	}
	
	@FXML
	public void cadastrarMotivoPerda(){
		String codMotivo = codigo.getText();
		String descMotivo = nome.getText();
		if(codMotivo != null && !codMotivo.isEmpty() && descMotivo != null && !descMotivo.isEmpty()){
			mpDao.salvar(codMotivo, descMotivo);
			popup.getInformation("Motivo salvo com sucesso!");
			
			codigo.setText("");
			nome.setText("");
		}else{
			popup.getError("Campos obrigatórios em branco!");
		}
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
