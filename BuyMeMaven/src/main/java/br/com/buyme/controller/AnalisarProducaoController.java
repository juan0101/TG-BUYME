package br.com.buyme.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.buyme.dao.ProdutoAnalisarDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.entity.ProdMot;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AnalisarProducaoController {
	
	@FXML
	private Parent root;
	
	private Popup popup = new Popup("Análise de Produção");
	@FXML private TableView<ProdutoAnalisar> tabela_analise;
	@FXML private TableColumn<ProdutoAnalisar, String> cDescricao;
    @FXML private TableColumn<ProdutoAnalisar, Integer> cQuantidade;
    @FXML private TableColumn<ProdutoAnalisar, Double> cValorUni;
    @FXML private TableColumn<ProdutoAnalisar, Double> cValorTotal;
    @FXML private TableColumn<ProdutoAnalisar, Date> cDataFabricacao;
    @FXML private TableColumn<ProdutoAnalisar, Date> cDataValidade;
    private ObservableList<ProdutoAnalisar> data;
    private List<ProdutoAnalisar> pas = null;
    
    @FXML private Label lblCodProduto, lblNomeProduto, lblDFabricacao, lblDValidade, lblQuantidade;
    @FXML private Label lblProduto,lblProd,lblQuantEsperada,lblQuantValor,lblQuantProduzida;
    @FXML private TextField quantidadeProduzida;
    @FXML private Button btnFinalizar;
    
    private ProdutoAnalisarDAO paDao = new ProdutoAnalisarDAO();
    private ProdutoAnalisar pa = null;
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    private List<ProdMot> pms = null;
    
    @FXML
    public void analisar(){
    	pa = tabela_analise.getSelectionModel().getSelectedItem();
    	if(pa != null){
    		lblProd.setText(pa.getDescricao());
    		lblProd.setVisible(true);
    		lblQuantValor.setText(pa.getQuantidade()+"");
    		lblQuantValor.setVisible(true);
    		btnFinalizar.setDisable(false);
    		
    		lblCodProduto.setText("");
    		lblNomeProduto.setText("");
    		lblDFabricacao.setText("");
    		lblDValidade.setText("");
    		lblQuantidade.setText("");
    		
    	}else{
    		popup.getError("Selecione uma produção para analisar!");
    	}
    }
    
    @FXML
    public void excluir(){
    	pa = tabela_analise.getSelectionModel().getSelectedItem();
    	if(pa != null){
    		try{
    			data.remove(pa);
    			paDao.excluir(pa.getId());
    			atualizarListProducao();
    		}catch(Exception e){
    			e.printStackTrace();
    			popup.getError("Houve um erro, tente novamente!");
    		}
    	}else{
    		popup.getError("Selecione uma produção para excluir!");
    	}
    }
    
    public void limparCampos(){
    	quantidadeProduzida.setText("");
    	lblProd.setText("");
		lblProd.setVisible(false);
		lblQuantValor.setText("");
		lblQuantValor.setVisible(false);
		btnFinalizar.setDisable(true);
    }
    
    
    @FXML
    public void finalizar(){
    	String qtdProd = quantidadeProduzida.getText();
    	if(qtdProd != null && !qtdProd.isEmpty()){
    		if(Utils.isNumber(qtdProd)){
    			try{
    				int qtdProdReal = Integer.parseInt(qtdProd);
    				if(qtdProdReal > 0){
    					pms = verificarMotivoPerda(pa.getQuantidade() - qtdProdReal);
        				for(ProdMot pm: pms){
        					System.out.println(pm.getMotDesc());
        				}
    				}
    				paDao.finalizarAnalise(pa.getId(), qtdProdReal,pms);
    				
    				ppDao.salvar(pa.getCodigoProduto(), pa.getDescricao(), pa.getDataFabricacao(), pa.getDataValidade(), qtdProdReal);
    				
    				lblCodProduto.setText(pa.getCodigoProduto()+"");
    				lblNomeProduto.setText(pa.getDescricao());
    				lblDFabricacao.setText(pa.getDataFabricacao()+"");
    				lblDValidade.setText(pa.getDataValidade()+"");
    				lblQuantidade.setText(qtdProdReal+"");
    				
    				popup.getInformation("Produção gerada com sucesso!");
        	        
    				atualizarListProducao();
    				
    				limparCampos();
    			}catch (Exception e){
    				e.printStackTrace();
    				popup.getError("Houve um erro, tente novamente!");
    			}
    		}else{
    			popup.getError("Quantidade deve ser um numeral inteiro!");
    		}
    	}else{
    		popup.getError("Informe a quantidade que foi produzida do produto!");
    	}
    	
    }
    
    public void atualizarListProducao(){
    	pas.clear();
    	data.clear();
    	pas = paDao.getAllAnalisado(false);
    	if(pas != null){
    		data.setAll(pas);
    	}
    }
    
    @FXML
	protected void initialize(){
    	try{
    		pas = paDao.getAllAnalisado(false);
    		if(pas != null){
    			cDescricao.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,String>("descricao")
		        );
    			cQuantidade.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Integer>("quantidade")
		        );
    			cValorUni.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Double>("valor")
		        );
    			cValorTotal.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Double>("valorTotal")
		        );
    			cDataFabricacao.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Date>("dataFabricacao")
		        );
    			cDataValidade.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,Date>("dataValidade")
		        );
		 
		        data = FXCollections.observableArrayList();
		        data.addAll(pas);
		        tabela_analise.setItems(data);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public List<ProdMot> verificarMotivoPerda(int quantidadeProduzida){
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("../view/SelecionaMotivoPerdaView.fxml"));
    		AnchorPane page = (AnchorPane) loader.load();
    		
    		Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
    		
    		SelecionarMotivoPerdaController selecionaroMPController = loader.getController();
    		selecionaroMPController.setValorQuantTotal(quantidadeProduzida);
    		selecionaroMPController.setDialogStage(dialogStage);
    		
    		dialogStage.showAndWait();
    		return selecionaroMPController.fechar();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
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
