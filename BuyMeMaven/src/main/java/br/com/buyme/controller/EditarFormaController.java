package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.FormaDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.entity.Forma;
import br.com.buyme.entity.Produto;
import br.com.buyme.util.Utils;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EditarFormaController {

	@FXML private TableView<Forma> tabela_forma;
    @FXML private TableColumn<Forma, String> cCodigo;
    @FXML private TableColumn<Forma, String> cDescricao;
    @FXML private TableColumn<Forma, String> cProduto;
    @FXML private TableColumn<Forma, Integer> cQuant;
    private ObservableList<Forma> data;
	@FXML private TextField codigo,descricao,quantidade,prod;
	@FXML private ComboBox<String> comboProd;
	@FXML private Button btnFinalizar;
	private List<Forma> formas = null;
	private List<Produto> produtos = null;
	private List<String> produtoNome = new ArrayList<String>();

	private ProdutoDAO prodDao = new ProdutoDAO();
	private FormaDAO formaDao = new FormaDAO();
	private Forma forma = null;
	
	@FXML
	protected void initialize(){
		try{
			formas = formaDao.getAll();
			if(formas != null){
				cCodigo.setCellValueFactory(
						new PropertyValueFactory<Forma,String>("codigo")
						);
				cDescricao.setCellValueFactory(
						new PropertyValueFactory<Forma,String>("descricao")
						);
				cProduto.setCellValueFactory(
						new PropertyValueFactory<Forma,String>("nomeProduto")
						);
				cQuant.setCellValueFactory(
						new PropertyValueFactory<Forma,Integer>("quantidade")
						);
				data = FXCollections.observableArrayList();
				data.addAll(formas);
				tabela_forma.setItems(data);
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Forma");
	        dialogoInfo.setContentText("Houve um erro ao carregar as formas, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	public void atualizarCombo(){
		if(produtoNome != null){
			produtoNome.clear();
		}
		produtos = prodDao.getAll();
		for(Produto prod: produtos){
			produtoNome.add(prod.getNome());
		}
		comboProd.getItems().setAll(produtoNome);
		comboProd.getSelectionModel().select(forma.getNomeProduto());
	}
	
	public void atualizarListForma(){
		formas.clear();
		formas = formaDao.getAll();
		data.clear();
		if(formas != null){
			data.addAll(formas);
		}
	}
	
	@FXML
	public void editar(){
		forma = tabela_forma.getSelectionModel().getSelectedItem();
		if(forma != null){
			codigo.setText(forma.getCodigo());
			descricao.setText(forma.getDescricao());
			quantidade.setText(forma.getQuantidade()+"");
			atualizarCombo();
			btnFinalizar.setDisable(false);
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Forma");
	        dialogoInfo.setContentText("Selecione uma forma para editar!");
	        dialogoInfo.showAndWait();
		}
	}
	
	@FXML
	public void excluir(){
		forma = tabela_forma.getSelectionModel().getSelectedItem();
		if(forma != null){
			try{
				data.remove(forma);
				formaDao.excluir(forma.getId());
				Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Forma");
		        dialogoInfo.setContentText("Forma excluida com sucesso!");
		        dialogoInfo.showAndWait();
			}catch(Exception e){
				e.printStackTrace();
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Forma");
		        dialogoInfo.setContentText("Houve um erro, tente novamente!");
		        dialogoInfo.showAndWait();
			}
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Forma");
	        dialogoInfo.setContentText("Selecione uma forma para editar!");
	        dialogoInfo.showAndWait();
		}
	}
	
	public void limparCampos(){
		codigo.setText("");
		descricao.setText("");
		quantidade.setText("");
		btnFinalizar.setDisable(true);
		comboProd.getSelectionModel().clearSelection();
	}
	
	@FXML
	public void salvarEdicao(){
		try{
			String codForma = codigo.getText();
			String descForma = descricao.getText();
			String prodForma = comboProd.getSelectionModel().selectedItemProperty().getValue();
			String quantForma = quantidade.getText();
			if(codForma != null && !codForma.isEmpty() && descForma != null && !descForma.isEmpty() && prodForma != null && !prodForma.isEmpty() &&
					quantForma != null && !quantForma.isEmpty()){
				if(Utils.isNumber(quantForma)){
					for(Produto pro: produtos){
						if(pro.getNome().equals(prodForma)){
							formaDao.salvarEdicao(codForma, descForma, Integer.parseInt(quantForma), pro.getId(), forma.getId());
							
							Alert dialogoInfo = new Alert(Alert.AlertType.CONFIRMATION);
					        dialogoInfo.setTitle("BuyMe");
					        dialogoInfo.setHeaderText("Editar Forma");
					        dialogoInfo.setContentText("Forma editada com sucesso!");
					        dialogoInfo.showAndWait();
					        
					        atualizarListForma();
					        limparCampos();
						}
					}
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Editar Forma");
			        dialogoInfo.setContentText("A quantidade deve ser um numeral inteiro!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Editar Forma");
		        dialogoInfo.setContentText("Existem campos obrigatórios não preenchidos!");
		        dialogoInfo.showAndWait();
			}
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Editar Forma");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
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
