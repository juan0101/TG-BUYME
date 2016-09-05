package br.com.buyme.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.dao.ProdutoDAO;
import br.com.buyme.dao.ProdutoProntoDAO;
import br.com.buyme.dao.ProdutoVendidoDAO;
import br.com.buyme.entity.Cliente;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoPronto;
import br.com.buyme.entity.ProdutoVendido;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class VendaController {
	
	private boolean teste;
	public static VendaController MeuController;
	
	@FXML private ComboBox<String> comboCliente, comboProducao;
	@FXML private Button btnAdicionar,btnRetirar,btnFinalizar;
	@FXML private Label lblValor,totalValor,lblNomeProd;
	@FXML private TextField quantidade,nome,codigoProduto;
	@FXML private CheckBox chkClienteCadastrado;
	@FXML private TableView<ProdutoVendido> tabela_pv;
	@FXML private TableColumn<ProdutoVendido, String> cCliente;
    @FXML private TableColumn<ProdutoVendido, String> cProduto;
    @FXML private TableColumn<ProdutoVendido, Integer> cQuantidade;
    @FXML private TableColumn<ProdutoVendido, Double> cValor;
    private ObservableList<ProdutoVendido> data;
    private ObservableList<ProdutoVendido> dataSave;
    
    //LISTA DE CLIENTES
    private ClienteDAO cliDao = new ClienteDAO();
    private List<Cliente> clientes = null;
    //RELACIONADO AO PRODUTO SELECIONADO
    private Produto p = null;
    private ProdutoDAO pDao = new ProdutoDAO();
    //LISTA DE PRODUTOS PRONTOS E LISTA DE DATAS DO COMBO
    private List<ProdutoPronto> listPp = null;
    private List<ProdutoPronto> listPpDeletarBanco = new ArrayList<ProdutoPronto>();
    private ProdutoProntoDAO ppDao = new ProdutoProntoDAO();
    private List<String> listDataProduto = new ArrayList<String>();
    //USADO PARA SALVAR O PRODUTO VENDIDO
    private ProdutoVendidoDAO pvDao = new ProdutoVendidoDAO();
    
    @FXML
	protected void initialize(){
    	MeuController = this;
    	try{
    		clientes = cliDao.getAll();
    		if(clientes != null){
    			List<String> clientesNomes = new ArrayList<String>();
    			for(Cliente cli: clientes){
    				clientesNomes.add(cli.getNome());
    			}
    			comboCliente.getItems().setAll(clientesNomes);
    		}
    		
    		cCliente.setCellValueFactory(
	            new PropertyValueFactory<ProdutoVendido,String>("cliente")
	        );			
    		cProduto.setCellValueFactory(
	            new PropertyValueFactory<ProdutoVendido,String>("produto")
	        );
    		cQuantidade.setCellValueFactory(
    	            new PropertyValueFactory<ProdutoVendido,Integer>("quantidade")
    	        );
	        cValor.setCellValueFactory(
	            new PropertyValueFactory<ProdutoVendido,Double>("valor")
	        );
	 
	        data = FXCollections.observableArrayList();
	        tabela_pv.setItems(data);
	        
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * MÉTODO QUE VERIFICA SE CHECKBOX PARA CLIENTE NÃO CADASTRADO ESTA SELECIONADO
     * CASO ESTEJA, O BOX PARA SELECIONAR O CLIENTE FICA DESATIVADO
     * CASO NÃO ESTEJA, O CAMPO PARA INSERIR O NOME DO CLIENTE FICA DESATIVADO
     */
    @FXML
    public void clienteNaoCadastrado(){
    	if(chkClienteCadastrado.isSelected()){
    		comboCliente.setDisable(true);
    		nome.setDisable(false);
    	}else{
    		comboCliente.setDisable(false);
    		nome.setDisable(true);
    	}
    }
    
    /**
     * MÉTODO QUE BUSCA O PRODUTO PELO CÓDIGO INSERIDO
     * ALIMENTA O COMBOBOX COM AS DATAS DAS PRODUÇOES DO PRODUTO
     * HABILITA O COMBOBOX, CAMPO QUANTIDADE E BOTOES PARA INSERÇÃO E EXCLUSÃO DE ITEMS DA TABELA
     */
    @FXML
    public void buscarProduto(){
    	String codProd = codigoProduto.getText();
    	if(codProd != null && !codProd.isEmpty()){
    		p = pDao.getOne(codProd);
    		if(p != null){
    			lblValor.setText(String.valueOf(p.getValor()).replace('.', ','));
    			lblNomeProd.setText(p.getNome());
    			
    			listPp = ppDao.getListByCodigo(Integer.parseInt(p.getCodigo()));
    			for(ProdutoPronto pp: listPp){
    				listDataProduto.add(pp.getDataFabricacao()+" - "+pp.getDataValidade());
    			}
    			comboProducao.getItems().setAll(listDataProduto);
    			
    			comboProducao.setDisable(false);
    			quantidade.setDisable(false);
    			btnAdicionar.setDisable(false);
    			btnRetirar.setDisable(false);
    			btnFinalizar.setDisable(false);
    		}else{
    			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
    	        dialogoInfo.setTitle("BuyMe");
    	        dialogoInfo.setHeaderText("Venda");
    	        dialogoInfo.setContentText("Produto não encontrado!");
    	        dialogoInfo.showAndWait();
    		}
    	}else{
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Venda");
	        dialogoInfo.setContentText("Insira o código para buscar o produto!");
	        dialogoInfo.showAndWait();
    	}
    }
    
    /**
     * MÉTODO PARA LIMPAR OS CAMPOS DA TELA
     */
    public void limparCampos(){
    	data.clear();
    	dataSave.clear();
    	comboCliente.getSelectionModel().clearSelection();
    	nome.setText("");
    	lblValor.setText("0,00");
    	lblNomeProd.setText("");
    	codigoProduto.setText("");
    	comboCliente.setDisable(false);
    	nome.setDisable(true);
    	comboProducao.setDisable(true);
    	comboProducao.getSelectionModel().clearSelection();
    	quantidade.setText("");
		quantidade.setDisable(true);
		chkClienteCadastrado.setSelected(false);
		btnAdicionar.setDisable(true);
		btnRetirar.setDisable(true);
		btnFinalizar.setDisable(true);
    }
    
    
    /**
     * MÉTODO QUE FINALIZA UMA COMPRA
     * SALVA OS PRODUTOS VENDIDOS, E REMOVE DO ESTOQUE
     */
    @FXML
    public void finalizar(){
    	dataSave = tabela_pv.getItems();
    	if(dataSave != null && !dataSave.isEmpty()){
    		try{
    			for(ProdutoVendido pv: dataSave){
    				pvDao.salvar(pv.getProduto(), pv.getQuantidade(), pv.getCliente(), new Date(), pv.getValor()); //salvando a venda
    				ppDao.removerQuantidadeById(pv.getProdutoPronto(), pv.getQuantidade());
    			}
    			
    			Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
    	        dialogoInfo.setTitle("BuyMe");
    	        dialogoInfo.setHeaderText("Venda");
    	        dialogoInfo.setContentText("Vendas efetuadas com sucesso!");
    	        dialogoInfo.showAndWait();
    	        
    	        limparCampos();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}else{
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Venda");
	        dialogoInfo.setContentText("Não há nenhuma venda!");
	        dialogoInfo.showAndWait();
    	}
    }
    
    
    /**
     * MÉTODO QUE RETIRA UM PRODUTO VENDIDO DA TABELA
     */
    @FXML
    public void retirar(){
    	ProdutoVendido pv = null;
		pv = tabela_pv.getSelectionModel().getSelectedItem();
		if(pv != null){
			for(ProdutoPronto pp: listPp){
				if(pp.getId() == pv.getProdutoPronto()){
					if(pp.getQuantidade() == 0){
						comboProducao.getItems().add(pp.getDataFabricacao()+" - "+pp.getDataValidade());
					}
					pp.setQuantidade(pp.getQuantidade() + pv.getQuantidade());
					break;
				}
			}
			data.remove(pv);
			valorTotal(pv.getValor(), true);
		}else{
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Venda");
	        dialogoInfo.setContentText("Selecione uma venda para remover!");
	        dialogoInfo.showAndWait();
		}
    }
    
    
    /**
     * MÉTODO QUE MONTA UM OBJETO PRODUTO VENDIDO E ADICIONA NA TABELA
     */
    @FXML
	public void adicionar(){
    	try{
    		ProdutoVendido pv = new ProdutoVendido();
    		String nomeCliente = nomeCliente();
    		if(nomeCliente != null && !nomeCliente.isEmpty()){
    			pv.setCliente(nomeCliente);
    			if(p != null){
    				String dataCombo = comboProducao.getSelectionModel().selectedItemProperty().getValue();
    				if(dataCombo != null && !dataCombo.isEmpty()){
    					String strQuantidade = quantidade.getText();
    					if(strQuantidade != null && !strQuantidade.isEmpty() && Utils.isNumber(strQuantidade)){
    						int intQuantidade = Integer.parseInt(strQuantidade);
    						pv.setProduto(p.getNome());
    						for(ProdutoPronto pp: listPp){
    							if((pp.getDataFabricacao()+" - "+pp.getDataValidade()).equals(dataCombo)){
    								if(pp.getQuantidade() >= intQuantidade){
    									pv.setProdutoPronto(pp.getId());
        								pv.setValor(intQuantidade * p.getValor());
        								pv.setQuantidade(intQuantidade);
        								pp.setQuantidade(pp.getQuantidade()-intQuantidade);
        								if(pp.getQuantidade() == 0){
        									comboProducao.getItems().remove(dataCombo);
        								}
        								data.add(pv);
        								break;
    								}else{
    									Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
    		            		        dialogoInfo.setTitle("BuyMe");
    		            		        dialogoInfo.setHeaderText("Venda");
    		            		        dialogoInfo.setContentText("A quantidade de venda é maior do que a quantidade em estoque: "+ pp.getQuantidade());
    		            		        dialogoInfo.showAndWait();
    								}
    							}
    						}
    						valorTotal(pv.getValor(), false);
    					}else{
    						Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
            		        dialogoInfo.setTitle("BuyMe");
            		        dialogoInfo.setHeaderText("Venda");
            		        dialogoInfo.setContentText("O campo quantidade não pode ser vazio, e deve ser um número inteiro!");
            		        dialogoInfo.showAndWait();
    					}
    				}else{
    					Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
        		        dialogoInfo.setTitle("BuyMe");
        		        dialogoInfo.setHeaderText("Venda");
        		        dialogoInfo.setContentText("Selecione a produção!");
        		        dialogoInfo.showAndWait();
    				}
    			}else{
    				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
    		        dialogoInfo.setTitle("BuyMe");
    		        dialogoInfo.setHeaderText("Venda");
    		        dialogoInfo.setContentText("O produto não foi encontrado!");
    		        dialogoInfo.showAndWait();
    			}
    		}else{
    			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
    	        dialogoInfo.setTitle("BuyMe");
    	        dialogoInfo.setHeaderText("Venda");
    	        dialogoInfo.setContentText("Selecione um cliente, ou insira o nome do cliente não cadastrado!");
    	        dialogoInfo.showAndWait();
    		}
    			
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Venda");
	        dialogoInfo.setContentText("Houve um erro, tente novamente!");
	        dialogoInfo.showAndWait();
    	}
	}
    
    /**
     * MÉTODO QUE CAPTURA O NOME DO CLIENTE QUE ESTA FAZENDO A COMPRA
     * @return nomeDoCliente
     */
    public String nomeCliente(){
    	String nomeCliente = "";
    	if(chkClienteCadastrado.isSelected()){
			if(nome.getText() != null && !nome.getText().isEmpty()){
				nomeCliente = nome.getText();
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Venda");
		        dialogoInfo.setContentText("Se o cliente não é cadastrado, é necessário o nome para a venda!");
		        dialogoInfo.showAndWait();
			}
		}else{
			if(comboCliente.getSelectionModel().selectedItemProperty().getValue() != null && 
				!comboCliente.getSelectionModel().selectedItemProperty().getValue().isEmpty()){
				nomeCliente = comboCliente.getSelectionModel().selectedItemProperty().getValue();
			}else{
				Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
		        dialogoInfo.setTitle("BuyMe");
		        dialogoInfo.setHeaderText("Venda");
		        dialogoInfo.setContentText("É necessário selecionar um cliente para a venda!");
		        dialogoInfo.showAndWait();
			}
		}
    	return nomeCliente;
    }
    
    /**
     * MÉTODO QUE MANIPULA O VALOR TOTAL DA COMPRA
     * @param valor
     * @param retirar
     */
    public void valorTotal(double valor, boolean retirar){
    	String v = totalValor.getText().replace(',', '.');
    	double valorTotal = Double.parseDouble(v);
    	if(retirar){
    		valorTotal = valorTotal - valor;
    		totalValor.setText(String.valueOf(valorTotal).replace('.', ','));
    	}else{
    		valorTotal = valorTotal + valor;
    		totalValor.setText(String.valueOf(valorTotal).replace('.', ','));
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
