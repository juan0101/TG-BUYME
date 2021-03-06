package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.ProdutoAnalisarDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.entity.PermissoesEnum;
import br.com.buyme.entity.ProdMot;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AnalisarProducaoController {
	
	@FXML private Parent root;
	@FXML private Menu btnUsuario,menuHome,menuCadastrar,menuEditar,menuProducao,menuEncomenda,menuVenda,menuRelatorio;
	//CONTROLA O USUARIO QUE ESTA LOGADO
	private Usuario usuario = Usuario.getInstance();
	//CONTROLA O MENUBAR
	private MenuController menu = MenuController.getInstance();
	
	private Popup popup = new Popup("An�lise de Produ��o");
	@FXML private TableView<ProdutoAnalisar> tabela_analise;
	@FXML private TableColumn<ProdutoAnalisar, String> cDescricao;
    @FXML private TableColumn<ProdutoAnalisar, Integer> cQuantidade;
    @FXML private TableColumn<ProdutoAnalisar, Double> cValorUni;
    @FXML private TableColumn<ProdutoAnalisar, Double> cValorTotal;
    @FXML private TableColumn<ProdutoAnalisar, String> cDataFabricacao;
    @FXML private TableColumn<ProdutoAnalisar, String> cDataValidade;
    private ObservableList<ProdutoAnalisar> data;
    private List<ProdutoAnalisar> pas = null;
    
    @FXML private Label lblCodProduto, lblNomeProduto, lblDFabricacao, lblDValidade, lblQuantidade;
    @FXML private Label lblProduto,lblProd,lblQuantEsperada,lblQuantValor,lblQuantProduzida;
    @FXML private TextField quantidadeProduzida;
    @FXML private Button btnFinalizar;
    
    private ProdutoAnalisarDAO paDao = new ProdutoAnalisarDAO();
    private ProdutoAnalisar pa = null;
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    private List<ProdMot> pms = new ArrayList<ProdMot>();
    
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
    		popup.getError("Selecione uma produ��o para analisar!");
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
    		popup.getError("Selecione uma produ��o para excluir!");
    	}
    }
    
    public void limparCampos(){
    	quantidadeProduzida.setText("");
    	lblProd.setText("");
		lblProd.setVisible(false);
		lblQuantValor.setText("");
		lblQuantValor.setVisible(false);
		btnFinalizar.setDisable(true);
		pms.clear();
		pa=null;
    }
    
    
    @FXML
    public void finalizar(){
    	String qtdProd = quantidadeProduzida.getText();
    	if(qtdProd != null && !qtdProd.isEmpty()){
    		if(Utils.isNumber(qtdProd)){
    			try{
    				int qtdProdReal = Integer.parseInt(qtdProd);
    				if(pa.getQuantidade() >= qtdProdReal){
    					if((pa.getQuantidade() - qtdProdReal) > 0){
        					pms = verificarMotivoPerda(pa.getQuantidade() - qtdProdReal);
        				}
        				paDao.finalizarAnalise(pa.getId(), qtdProdReal,pms);
        				
        				ppDao.salvar(pa.getCodigoProduto(), pa.getDescricao(), pa.getDataFabricacao(), pa.getDataValidade(), qtdProdReal, pa.getLote());
        				
        				lblCodProduto.setText(pa.getCodigoProduto()+"");
        				lblNomeProduto.setText(pa.getDescricao());
        				lblDFabricacao.setText(pa.getDataFabricacaoStr());
        				lblDValidade.setText(pa.getDataValidadeStr());
        				lblQuantidade.setText(qtdProdReal+"");
        				
        				popup.getInformation("Produ��o gerada com sucesso!");
            	        
        				atualizarListProducao();
        				
        				limparCampos();
        				
    				}else{
    					popup.getError("A quantidade de produ��o n�o deve ser maior que a esperada.");
    				}
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
    	btnUsuario.setText(usuario.getLogin());
    	menuPermissoes();
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
		            new PropertyValueFactory<ProdutoAnalisar,String>("dataFabricacaoStr")
		        );
    			cDataValidade.setCellValueFactory(
		            new PropertyValueFactory<ProdutoAnalisar,String>("dataValidadeStr")
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
    		loader.setLocation(getClass().getResource("SelecionaMotivoPerdaView.fxml"));
    		AnchorPane page = (AnchorPane) loader.load();
    		
    		Stage dialogStage = new Stage();
            dialogStage.setTitle("Selecionar Motivo Perda");
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
	
    //M�TODOS MENU BAR
  	@FXML public void home(ActionEvent event) throws IOException{menu.home(event,root);}
  	
  	@FXML public void cadastrarUsuario(ActionEvent event) throws IOException{menu.cadastrarUsuario(event,root);}
  	
  	@FXML public void cadastrarIngrediente(ActionEvent event) throws IOException{menu.cadastrarIngrediente(event,root);}
  	
  	@FXML public void editarUsuario(ActionEvent event) throws IOException{menu.editarUsuario(event,root);}
  	
  	@FXML public void editarIngrediente(ActionEvent event) throws IOException{menu.editarIngrediente(event,root);}
  	
  	@FXML public void cadastrarProduto(ActionEvent event) throws IOException{menu.cadastrarProduto(event, root);}
  	
  	@FXML public void editarProduto(ActionEvent event) throws IOException{menu.editarProduto(event, root);}
  	
  	@FXML public void cadastrarForma(ActionEvent event) throws IOException{menu.cadastrarForma(event, root);}
  	
  	@FXML public void editarForma(ActionEvent event) throws IOException{menu.editarForma(event, root);}
  	
  	@FXML public void produzirForma(ActionEvent event) throws IOException{menu.produzirForma(event, root);}
  	
  	@FXML public void analisarProducao(ActionEvent event) throws IOException{menu.analisarProducao(event, root);}
  	
  	@FXML public void cadastrarCliente(ActionEvent event) throws IOException{menu.cadastrarCliente(event, root);}
  	
  	@FXML public void gerarEncomenda(ActionEvent event) throws IOException{menu.gerarEncomenda(event, root);}
  	
  	@FXML public void atenderEncomenda(ActionEvent event) throws IOException{menu.atenderEncomenda(event, root);}
  	
  	@FXML public void venda(ActionEvent event) throws IOException{menu.venda(event, root);}
  	
  	@FXML public void editarCliente(ActionEvent event) throws IOException{menu.editarCliente(event, root);}
  	
  	@FXML public void relatorioPerdaProduto(ActionEvent event) throws IOException{menu.relatorioPerdaProduto(event, root);}
  	
  	@FXML public void cadastrarMotivoPerda(ActionEvent event) throws IOException{menu.cadastrarMotivoPerda(event, root);}
  	
  	@FXML public void editarMotivoPerda(ActionEvent event) throws IOException{menu.editarMotivoPerda(event, root);}
  	
  	@FXML public void verificarEstoque(ActionEvent event) throws IOException{menu.verificarEstoque(event, root);}
  	
  	@FXML public void logout(ActionEvent event) throws IOException{menu.logout(event, root, usuario);}

  	@FXML public void motivoPerdaLote(ActionEvent event) throws IOException{menu.motivoPerdaLote(event, root);}
  	
  	public void menuPermissoes(){
		if(!usuario.isAdmin()){
			if(!usuario.getPermissoes().contains(PermissoesEnum.CADASTRO)){
				menuCadastrar.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.EDICAO)){
				menuEditar.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.PRODUCAO)){
				menuProducao.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.ENCOMENDA)){
				menuEncomenda.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.VENDA)){
				menuVenda.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.RELATORIO)){
				menuRelatorio.setVisible(false);
			}
			if(!usuario.getPermissoes().contains(PermissoesEnum.HOME)){
				menuHome.setVisible(false);
			}
		}
	}
  	
}
