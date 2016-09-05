package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.FormaDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.entity.Forma;
import br.com.buyme.entity.Produto;
import br.com.buyme.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroFormaController {
	
	@FXML private TextField txtCodigo,txtDescricao,txtQuantidade;
	@FXML private ComboBox<String> comboProd;
	private List<Produto> produtos;
	private boolean gerouCod = false;
	
	private ProdutoDAO prodDao = new ProdutoDAO();
	private FormaDAO formaDao = new FormaDAO();
	
	/**
	 * Método que roda quando inicializa a tela
	 * 
	 */
	@FXML
	protected void initialize(){
		try{
			produtos = prodDao.getAll();
			List<String> produtoNome = new ArrayList<String>();
			for(Produto prod: produtos){
				produtoNome.add(prod.getNome());
			}
			comboProd.getItems().setAll(produtoNome);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
     * Método que gera o código de um produto
     */
    @FXML
	public void gerarCodigo(){
		Random randomGenerator = new Random();
		try{
			Forma f = null;
			while(true){
				int codProduto = randomGenerator.nextInt(10000);
				f = formaDao.getOneByCodigo(codProduto+"");
				if(f != null){
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
	 * Método para cadastro de uma nova forma
	 * 
	 */
	@FXML
	public void cadastrarForma(){
		try{
			if(gerouCod){
				String codForma = txtCodigo.getText();
				String descForma = txtDescricao.getText();
				String quantProd = txtQuantidade.getText();
				String nomeProd = comboProd.getSelectionModel().selectedItemProperty().getValue();
				if(codForma != null && !codForma.isEmpty() && descForma != null && !descForma.isEmpty() &&
						quantProd != null && !quantProd.isEmpty() && nomeProd != null && !nomeProd.isEmpty()){
					if(Utils.isNumber(quantProd)){
						Forma forma = new Forma();
						List<Forma> listaForma = new ArrayList<Forma>();
						forma.setCodigo(codForma);
						forma.setDescricao(descForma);
						forma.setQuantidade(Integer.parseInt(quantProd));
						for(Produto pro: produtos){
							if(pro.getNome().equals(nomeProd)){
								forma.setProduto(pro);
								forma.setNomeProduto(pro.getNome());
								listaForma.add(forma);
								pro.setForma(listaForma); //Setando a lista de forma no produto
								prodDao.updateSalvarForma(pro); //Update no produto com a lista de forma
								
								Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
						        dialogoInfo.setTitle("BuyMe");
						        dialogoInfo.setHeaderText("Salvar Forma");
						        dialogoInfo.setContentText("Forma salva com sucesso!");
						        dialogoInfo.showAndWait();
								
								limparCampos();
							}
						}
					}else{
						Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
				        dialogoInfo.setTitle("BuyMe");
				        dialogoInfo.setHeaderText("Salvar Forma");
				        dialogoInfo.setContentText("O campo quantidade deve ser um número válido!");
				        dialogoInfo.showAndWait();
					}
				}else{
					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
			        dialogoInfo.setTitle("BuyMe");
			        dialogoInfo.setHeaderText("Salvar Forma");
			        dialogoInfo.setContentText("Existem campos obrigatórios nulos!");
			        dialogoInfo.showAndWait();
				}
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Salvar Forma");
		        dialogoInfo.setContentText("O código da forma deve ser gerado!");
		        dialogoInfo.showAndWait();
			}
		}catch (Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Salvar Forma");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	public void limparCampos(){
		/*Deixando os campos do form em branco*/
		txtCodigo.setText("");
		txtDescricao.setText("");
		txtQuantidade.setText("");
		comboProd.getSelectionModel().clearSelection();
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
