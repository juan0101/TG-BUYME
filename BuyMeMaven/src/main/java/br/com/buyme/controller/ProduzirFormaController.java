package br.com.buyme.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.com.buyme.dao.FormaDAO;
import br.com.buyme.dao.ProdutoAnalisarDAO;
import br.com.buyme.entity.Forma;
import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoAnalisar;
import br.com.buyme.entity.ProdutoIngrediente;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProduzirFormaController {

	private Popup popup = new Popup("Produção de forma");
	@FXML private Button btnProduzir;
	@FXML private TextField quantidade,lote;
	@FXML private ComboBox<String> comboForma;
	@FXML private TableView<ProdutoIngrediente> tabela_ingrediente;
	@FXML private TableColumn<ProdutoIngrediente, String> coluna_ingrediente;
    @FXML private TableColumn<ProdutoIngrediente, Double> coluna_quantidade;
    @FXML private TableColumn<ProdutoIngrediente, String> coluna_medida;
    @FXML private DatePicker dataValidade;
    @FXML private Label lblAviso,lblAvisoQuantidade,lblValor;
    private ObservableList<ProdutoIngrediente> data;
    private Integer quantidadeForma = null;
    
    private List<Forma> formas = null;
    private FormaDAO formaDao = new FormaDAO();
    private Forma forma = null;
    private Produto produto = null;
    private ProdutoAnalisarDAO paDao = new ProdutoAnalisarDAO();
    
    @FXML
    public void gerarLote(){
    	Random randomGenerator = new Random();
    	ProdutoAnalisar pa = null;
    	try{
			while(true){
				int codLote = randomGenerator.nextInt(10000);
				pa = paDao.getByLote("L"+codLote);
				if(pa != null){
					continue;
				}else{
					lote.setText("L"+codLote);
					break;
				}
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Erro ao gerar Lote! Tente novamente.");
    	}
    }
    
	@FXML
	public void gerarDados(){
		lblAviso.setVisible(false);
        lblAvisoQuantidade.setVisible(false);
        lblValor.setVisible(false);
        
		String cForma = comboForma.getSelectionModel().selectedItemProperty().getValue();
		String quantForma = quantidade.getText();
		if(cForma != null && !cForma.isEmpty()){
			if(quantForma != null && !quantForma.isEmpty()){
				if(Utils.isNumber(quantForma)){
					quantidadeForma = Integer.parseInt(quantForma);
					for(Forma f: formas){
						if(f.getDescricao().equals(cForma)){
							forma = f;
							break;
						}
					}
					if(forma != null){
						produto = forma.getProduto();
						if(produto != null){
							List<ProdutoIngrediente> listaPi = produto.getProduto_ingrediente();
							List<ProdutoIngrediente> listaPiTotal = new ArrayList<ProdutoIngrediente>();
							for(ProdutoIngrediente pi: listaPi){
								ProdutoIngrediente auxPi = new ProdutoIngrediente(pi);
								auxPi.setQuantidade((pi.getQuantidade() * forma.getQuantidade()) * quantidadeForma);
								if(auxPi.getQuantidade() >= 1000){
									auxPi.setQuantidade(auxPi.getQuantidade()/1000);
									auxPi.setGrama_quilo("Quilo");
								}
								listaPiTotal.add(auxPi);
							}
							data.setAll(listaPiTotal);
						}else{
							popup.getError("Não há produto para essa forma!");
						}
					}else{
						popup.getError("Forma não encontrada!");
					}
				}else{
					popup.getError("A quantidade deve ser um numeral inteiro!");
				}
			}else{
				popup.getError("O campo quantidade não pode ficar em branco!");
			}
		}else{
			popup.getError("Seleciona uma forma para gerar os dados!");
		}
	}
	
	public void limparCampos(){
		comboForma.getSelectionModel().clearSelection();
		quantidade.setText("");
		data.clear();
	}
	
	/**
	 * Método que faz a produção dos produtos para análise
	 */
	
	@FXML
	public void produzir(){
		LocalDate stringData = dataValidade.getValue();
		String codLote = lote.getText();
		if(stringData != null){
			if(codLote != null && !codLote.isEmpty()){
				try{
					Date data_validade = Utils.getLocalDateToDate(stringData);
					Date data_fabricacao = new Date();
					int numeroDeProdutos = forma.getQuantidade() * quantidadeForma;
					
					paDao.salvar(numeroDeProdutos, forma.getProduto().getNome(), data_fabricacao, data_validade, forma.getProduto().getValor(), false, Integer.parseInt(produto.getCodigo()),codLote);
					
					popup.getInformation("A produção foi enviada para análise!");
			        
			        lblAviso.setVisible(true);
			        lblAvisoQuantidade.setVisible(true);
			        lblValor.setText(numeroDeProdutos+"");
			        lblValor.setVisible(true);
			        
			        limparCampos();
			        
				}catch(Exception e){
					e.printStackTrace();
					popup.getError("Houve um erro, tente novamente!");
				}
			}else{
				popup.getError("Gere um código de lote para esta produção!");
			}
		}else{
			popup.getError("Selecione a data de validade da produção!");
		}
	}
	
	/**
	 * Método inicial, que roda ao iniciar a tela
	 */
	@FXML
	protected void initialize(){
		try{
			formas = formaDao.getAll();
			if(formas != null){
				List<String> formasNomes = new ArrayList<String>();
				for(Forma f: formas){
					formasNomes.add(f.getDescricao());
				}
				comboForma.getItems().setAll(formasNomes);
			}
			
			// Set up the table data
	        coluna_ingrediente.setCellValueFactory(
	            new PropertyValueFactory<ProdutoIngrediente,String>("descricao_ingrediente")
	        );
	        coluna_quantidade.setCellValueFactory(
	            new PropertyValueFactory<ProdutoIngrediente,Double>("quantidade")
	        );
	        coluna_medida.setCellValueFactory(
	            new PropertyValueFactory<ProdutoIngrediente,String>("grama_quilo")
	        );
	 
	        data = FXCollections.observableArrayList();
	        tabela_ingrediente.setItems(data);
		}catch(Exception e){
			e.printStackTrace();
			Alert dialogoInfo = new Alert(Alert.AlertType.ERROR);
	        dialogoInfo.setTitle("BuyMe");
	        dialogoInfo.setHeaderText("Produção de forma");
	        dialogoInfo.setContentText("Ocorreu um erro durante o processo, tente novamente!");
	        dialogoInfo.showAndWait();
		}
	}
	
	/**
     * Método para voltar para o menu principal administrador
     * @param event
     * @throws IOException
     */
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
