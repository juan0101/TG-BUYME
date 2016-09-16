package br.com.buyme.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VendedorController {

	@FXML
	private Parent root;
	
	@FXML
	public void gerarEncomenda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/VendedorGerarEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void atenderEncomenda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/VendedorAtenderEncomendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void venda() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/VendedorVendaView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
}
