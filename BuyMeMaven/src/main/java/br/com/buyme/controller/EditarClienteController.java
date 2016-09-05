package br.com.buyme.controller;

import java.io.IOException;
import java.util.List;

import br.com.buyme.dao.ClienteDAO;
import br.com.buyme.entity.Cliente;
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
import javafx.stage.Stage;

public class EditarClienteController {
	
	
	//POPUP RESPONSAVEL PELAS MENSAGENS DE ERROS,INFORMATION E WARNING
	private Popup popup = new Popup("Editar Cliente");
	//ALIMENTA A TABELA PRINCIPAL DE CLIENTES
	@FXML private TableView<Cliente> tabela_cliente;
    @FXML private TableColumn<Cliente, String> cNome;
	@FXML private TableColumn<Cliente, String> cTelefone;
    @FXML private TableColumn<Cliente, String> cCidade;
    @FXML private TableColumn<Cliente, String> cEndereco;
    @FXML private TableColumn<Cliente, Integer> cNumero;
    @FXML private TableColumn<Cliente, String> cEmail;
    private ObservableList<Cliente> data;
    private List<Cliente> clientes = null;
    
    @FXML private TextField nome,telefone,cidade,endereco,numero,email,login,novaSenha,senhaAntiga;
    @FXML private Button btnFinalizar,btnTrocarSenha,btnSalvarSenha;
    @FXML private Label lblSenhaAntiga,lblNovaSenha,lblLogin;
    
    //RESPONSAVEL PELO MANUSEIO DO BANCO DE DADOS DE CLIENTES
    private ClienteDAO cliDao = new ClienteDAO();
    //CLIENTE SENDO EDITADO
    private Cliente cliente = null;
    
    @FXML
	protected void initialize(){
    	try{
    		clientes = cliDao.getAll();
    		if(clientes != null && !clientes.isEmpty()){
    			//ALIMENTANDO TABELA DE CLIENTES
    			cNome.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("nome")
		        );
    			cTelefone.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("telefone")
		        );
    			cCidade.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("cidade")
		        );
    			cEndereco.setCellValueFactory(
		            new PropertyValueFactory<Cliente,String>("endereco")
		        );
    			cNumero.setCellValueFactory(
		            new PropertyValueFactory<Cliente,Integer>("numero")
		        );
    			cEmail.setCellValueFactory(
    		            new PropertyValueFactory<Cliente,String>("email")
    		    );
		 
    			data = FXCollections.observableArrayList();
    			data.addAll(clientes);
    			tabela_cliente.setItems(data);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void editar(){
    	cliente = tabela_cliente.getSelectionModel().getSelectedItem();
    	if(cliente != null){
    		alimentarCamposCliente();
    	}else{
    		popup.getError("Selecione um cliente para editar");
    	}
    }
    
    @FXML
    public void excluir(){
    	cliente = tabela_cliente.getSelectionModel().getSelectedItem();
    	if(cliente != null){
    		data.remove(cliente);
    		cliDao.removerById(cliente.getId());
    		popup.getInformation("Cliente excluído com sucesso!");
    	}else{
    		popup.getError("Selecione um cliente para excluir!");
    	}
    }
    
    @FXML
    public void salvarEdicao(){
    	String cliNome = nome.getText();
    	String cliTel = telefone.getText();
    	String cliCidade = cidade.getText();
    	String cliEndereco = endereco.getText();
    	String cliNumero = numero.getText();
    	String cliEmail = email.getText();
    	if(cliNome != null && !cliNome.isEmpty() && cliTel != null && !cliTel.isEmpty() && cliCidade != null && !cliCidade.isEmpty() &&
    			cliEndereco != null && !cliEndereco.isEmpty() && cliNumero != null && !cliNumero.isEmpty() && 
    			cliEmail != null && !cliEmail.isEmpty()){
    		if(Utils.isNumber(cliNumero)){
    			try{
					cliDao.salvarEdicao(cliente.getId(), cliNome, cliTel, cliEndereco, Integer.parseInt(cliNumero), cliCidade, cliEmail);
					popup.getInformation("Edição feita com sucesso!");
					atualizarListEncomenda();
					limparCampos();
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		}else{
    			popup.getError("O campo número deve ser um numeral!");
    		}
    	}else{
    		popup.getError("Existe campos obrigatórios em branco!");
    	}
    	
    }
    
    @FXML
    public void salvarNovaSenha(){
    	try{
    		String cliLogin = login.getText();
    		String seAn = senhaAntiga.getText();
			String noSe = novaSenha.getText();
			if(cliLogin == null || cliLogin.isEmpty()){
				cliDao.salvarLoginSenha(cliente.getId(), null, null);
			}else{
				if(cliLogin.equals(cliente.getLogin()) || verificarLogin(cliLogin)){
					if(noSe != null && !noSe.isEmpty()){
						String antigaCripto = Utils.senhaSha256(seAn);
						if(cliente.getSenha() == null || antigaCripto.equals(cliente.getSenha())){
							 String noSenha = Utils.senhaSha256(noSe);
							 try{
								 cliDao.salvarLoginSenha(cliente.getId(), cliLogin, noSenha);
								 popup.getInformation("Login/Senha alterado com sucesso");
								 atualizarListEncomenda();
								 limparCampos();
							 }catch(Exception e){
								 e.printStackTrace();
								 popup.getError("Houve um erro, tente novamente!");
							 }
						}else{
							popup.getError("Senha antiga incorreta!");
						}
					}else{
						popup.getError("A nova senha não pode ser em branco!");
					}
				}else{
					popup.getError("Usuário já existente!");
				}
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void trocarSenha(){
    	lblLogin.setVisible(true);
    	lblNovaSenha.setVisible(true);
    	lblSenhaAntiga.setVisible(true);
    	btnSalvarSenha.setVisible(true);
    	novaSenha.setVisible(true);
    	senhaAntiga.setVisible(true);
    	login.setText(cliente.getLogin());
    	login.setVisible(true);
    }
    
    public void atualizarListEncomenda(){
    	clientes.clear();
    	data.clear();
    	try{
    		clientes = cliDao.getAll();
    		if(clientes != null && !clientes.isEmpty()){
    			data.setAll(clientes);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void alimentarCamposCliente(){
    	nome.setText(cliente.getNome());
    	telefone.setText(cliente.getTelefone());
    	cidade.setText(cliente.getCidade());
    	endereco.setText(cliente.getEndereco());
    	numero.setText(cliente.getNumero()+"");
    	email.setText(cliente.getEmail());
    	login.setText(cliente.getLogin());
    	btnFinalizar.setDisable(false);
    	btnTrocarSenha.setDisable(false);
    }
    
    public void limparCampos(){
    	nome.setText("");
    	telefone.setText("");
    	cidade.setText("");
    	endereco.setText("");
    	numero.setText("");
    	email.setText("");
    	login.setText("");
    	novaSenha.setText("");
    	senhaAntiga.setText("");
    	btnFinalizar.setDisable(true);
    	btnTrocarSenha.setDisable(true);
    	
    	lblLogin.setVisible(false);
    	lblNovaSenha.setVisible(false);
    	lblSenhaAntiga.setVisible(false);
    	btnSalvarSenha.setVisible(false);
    	novaSenha.setVisible(false);
    	senhaAntiga.setVisible(false);
    	login.setVisible(false);
    }
    
    public boolean verificarLogin(String login){
		try{
			Cliente cli = null;
			cli = cliDao.getOneWithLogin(login);
			if(cli == null){
				return true;
			}else{
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
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
