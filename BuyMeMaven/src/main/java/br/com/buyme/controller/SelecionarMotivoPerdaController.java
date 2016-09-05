package br.com.buyme.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.buyme.dao.MotivoPerdaDAO;
import br.com.buyme.entity.MotivoPerda;
import br.com.buyme.entity.ProdMot;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class SelecionarMotivoPerdaController {
	
	private Stage dialogStage;
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	@FXML private Label lblQuantTotal;
	public void setValorQuantTotal(int quantidadeTotal){
		lblQuantTotal.setText(quantidadeTotal+"");
		quantTotalPerda = quantidadeTotal;
	}
	
	
	private Popup popup = new Popup("Selecionar motivo");
	@FXML private TableView<ProdMot> tabela_motivo_perda;
    @FXML private TableColumn<ProdMot, String> cMotivo;
    @FXML private TableColumn<ProdMot, String> cQuantidade;
    private ObservableList<ProdMot> data;
	@FXML private ComboBox<String> comboMotivoPerda;
	@FXML private TextField quantidade;
	
	//LISTA DE MOTIVOS PERDA
	private int quantTotalPerda;
	private int quantidadePerda = 0;
	private List<MotivoPerda> mps = null;
	private List<ProdMot> listaPMRetorno = new ArrayList<ProdMot>();
	private List<String> nomesMotivoPerda = new ArrayList<String>();
	//CONTROLA O FLUXO DE BANCO DE DADOS DE MOTIVOS PERDA
	private MotivoPerdaDAO mpDao = new MotivoPerdaDAO();

	@FXML
	protected void initialize(){
    	try{
    		mps = mpDao.getAllMotivoPerda();
    		if(mps != null){
    			for(MotivoPerda mp: mps){
    				nomesMotivoPerda.add(mp.getDescricao());
    			}
    			comboMotivoPerda.getItems().setAll(nomesMotivoPerda);
    		}
    		
    		cMotivo.setCellValueFactory(
	            new PropertyValueFactory<ProdMot,String>("motDesc")
	        );
			cQuantidade.setCellValueFactory(
	            new PropertyValueFactory<ProdMot,String>("quantidade")
	        );
			
			data = FXCollections.observableArrayList();
			tabela_motivo_perda.setItems(data);
    	}catch(Exception e){
    		e.printStackTrace();
    		popup.getError("Houve um erro, tente novamente!");
    	}
	 }
	
	
	@FXML
	public void adicionar(){
		String nomeCombo = comboMotivoPerda.getSelectionModel().selectedItemProperty().getValue();
		String quant = quantidade.getText();
		if(nomeCombo != null && !nomeCombo.isEmpty()){
			if(quant != null && !quant.isEmpty()){
				if(Utils.isNumber(quant)){
					int intQuant = Integer.parseInt(quant);
					if((quantidadePerda + intQuant) <= quantTotalPerda ){
						quantidadePerda = quantidadePerda + intQuant;
						for(MotivoPerda mp: mps){
							if(mp.getDescricao().equals(nomeCombo)){
								ProdMot pm = new ProdMot();
								pm.setMotPerda(mp);
								pm.setMotDesc(mp.getDescricao());
								pm.setQuantidade(intQuant);
								data.add(pm);
								break;
							}
						}
					}else{
						popup.getError("A quantidade indicada é maior que a quantidade especificada de perda!");
					}
				}else{
					popup.getError("A quantidade deve ser um numeral!");
				}
			}else{
				popup.getError("Adiciona a quantidade de perda!");
			}
		}else{
			popup.getError("Selecione um motivo!");
		}
	}
	
	@FXML
	public void excluir(){
		ProdMot pm = null;
		pm = tabela_motivo_perda.getSelectionModel().getSelectedItem();
		if(pm != null){
			quantidadePerda = quantidadePerda - pm.getQuantidade();
			data.remove(pm);
		}else{
			popup.getError("Selecione um motivo para retirar da lista!");
		}
	}
	
	@FXML
	public List<ProdMot> fechar(){
		if(data != null && !data.isEmpty()){
			for(ProdMot pm: data){
				listaPMRetorno.add(pm);
			}
			dialogStage.close();
			return listaPMRetorno;
		}else{
			return null;
		}
	}
	
}
