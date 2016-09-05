package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.IngredienteDAO;
import br.com.buyme.entity.Ingrediente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class EditarIngredienteController {

	@FXML private Parent root;
	@FXML private TableView<Ingrediente> tabela_ingrediente;
    @FXML private TableColumn<Ingrediente, String> cCodigo;
    @FXML private TableColumn<Ingrediente, String> cNome;
    private ObservableList<Ingrediente> data;
    private List<Ingrediente> ingredientes = null;
	@FXML private TextField txtCodigo,txtDescricao;
	@FXML private Button btnVoltar,btnAtualizar;
	
	private IngredienteDAO ingDao = new IngredienteDAO();
	private Ingrediente ingrediente = null;
	
	@FXML
	protected void initialize(){
		try{
			ingredientes = ingDao.getAllIngredientes();
			if(ingredientes != null){
				cCodigo.setCellValueFactory(
						new PropertyValueFactory<Ingrediente,String>("codigo")
						);
				cNome.setCellValueFactory(
						new PropertyValueFactory<Ingrediente,String>("descricao")
						);
				data = FXCollections.observableArrayList();
				data.addAll(ingredientes);
				tabela_ingrediente.setItems(data);
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Ingrediente");
	        dialogoInfo.setContentText("Houve um erro ao carregar os ingredientes, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	@FXML
	public void editar(){
		try{
			ingrediente = tabela_ingrediente.getSelectionModel().getSelectedItem();
			if(ingrediente != null){
				txtCodigo.setText(ingrediente.getCodigo());
				txtDescricao.setText(ingrediente.getDescricao());
				btnAtualizar.setDisable(false);
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Ingrediente");
		        dialogoInfo.setContentText("Selecione um ingrediente para editar!");
		        dialogoInfo.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Ingrediente");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	@FXML
	public void excluir(){
		try{
			ingrediente = tabela_ingrediente.getSelectionModel().getSelectedItem();
			if(ingrediente != null){
				try{
					ingDao.excluir(ingrediente.getId());
					data.remove(ingrediente);
					
					Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Ingrediente");
			        dialogoInfo.setContentText("Ingrediente excluido com sucesso!");
			        dialogoInfo.showAndWait();
			        
					atualizarListIng();
					
				}catch (Exception e){
					e.printStackTrace();
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Ingrediente");
			        dialogoInfo.setContentText("Houve um erro ao excluir, tente novamente!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Ingrediente");
		        dialogoInfo.setContentText("Selecione um ingrediente para editar!");
		        dialogoInfo.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Ingrediente");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	public void atualizarListIng(){
		ingredientes.clear();
		ingredientes = ingDao.getAllIngredientes();
		data.clear();
		if(ingredientes != null){
			data.setAll(ingredientes);
		}
	}
	
 	@FXML
	public void atualizarIngrediente(){
		try{
			String codIng = txtCodigo.getText();
			String descIng = txtDescricao.getText();
			if(codIng != null && !codIng.isEmpty() && descIng != null && !descIng.isEmpty()){
				try{
					ingDao.editarIngrediente(ingrediente.getId(), descIng);
					
					Alert dialogoInfo2 = new Alert(Alert.AlertType.INFORMATION);
			        dialogoInfo2.setTitle("BuyMe");
			        dialogoInfo2.setHeaderText("Edição de ingrediente");
			        dialogoInfo2.setContentText("Ingrediente editado com sucesso.");
			        dialogoInfo2.showAndWait();
			        
			        atualizarListIng();
			        
			        txtCodigo.setText("");
			        txtDescricao.setText("");
			        btnAtualizar.setDisable(true);
				}catch(Exception e){
					e.printStackTrace();
					Alert dialogoInfo2 = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo2.setTitle("BuyMe");
			        dialogoInfo2.setHeaderText("Edição de ingrediente");
			        dialogoInfo2.setContentText("Houve um erro ao salvar! Tente novamente.");
			        dialogoInfo2.showAndWait();
				}
			}else{
				Alert dialogoInfo2 = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo2.setTitle("BuyMe");
		        dialogoInfo2.setHeaderText("Edição de ingrediente");
		        dialogoInfo2.setContentText("Existem campos obrigatórios em branco.");
		        dialogoInfo2.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			Alert dialogoInfo2 = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo2.setTitle("BuyMe");
	        dialogoInfo2.setHeaderText("Edição de ingrediente");
	        dialogoInfo2.setContentText("Houve um erro durante o processo! Tente novamente.");
	        dialogoInfo2.showAndWait();
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
	
	/*
	 * MÉTODOS DO MENUBAR
	 */
	
	@FXML
	public void cadastrarUsuario(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarIngrediente(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarUsuario() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarUsuarioView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void cadastrarProduto(ActionEvent event) throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/CadastroProdutoView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
	@FXML
	public void editarProduto() throws IOException{
		Parent newPageParent = FXMLLoader.load(getClass().getResource("../view/EditarIngredienteView.fxml"));
		Scene newPageScene = new Scene(newPageParent);
		Stage newStage = (Stage) root.getScene().getWindow();
		newStage.hide();
		newStage.setScene(newPageScene);
		newStage.show();
	}
	
}
