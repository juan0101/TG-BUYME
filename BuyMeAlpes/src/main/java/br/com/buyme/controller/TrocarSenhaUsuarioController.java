package br.com.buyme.controller;

import br.com.buyme.dao.UsuarioDAO;
import br.com.buyme.entity.Usuario;
import br.com.buyme.popup.Popup;
import br.com.buyme.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TrocarSenhaUsuarioController {
	
	private Stage dialogStage;
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	private Usuario usuario;
	public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
	private Popup popup = new Popup("Trocar senha usuário");
	@FXML private TextField novaSenha,senhaAntiga;
	private UsuarioDAO usuDao = new UsuarioDAO();
	
	
	public void salvarNovaSenha(){
		try{
			String seAn = senhaAntiga.getText();
			String noSe = novaSenha.getText();
			if(seAn != null && !seAn.isEmpty() && noSe != null && !noSe.isEmpty()){
				if(noSe.length()>4){
					String antigaCripto = Utils.senhaSha256(seAn);
					if(antigaCripto.equals(usuario.getSenha())){
						String novaCripto = Utils.senhaSha256(noSe);
						usuDao.trocarSenha(usuario.getId(), novaCripto);
						
						popup.getInformation("Senha modificada com sucesso!");
				        
				        dialogStage.close();
					}else{
						popup.getError("Senha antiga incorreta!");
					}
				}else{
					popup.getError("A nova senha deve conter mais de 4 caracteres!");
				}
			}else{
				popup.getError("Preencha todos os campos!");
			}
		}catch(Exception e){
			e.printStackTrace();
			popup.getError("Houve um erro, tente novamente!");
		}
		
	}
	
	public void cancelar(){
		dialogStage.close();
	}
	

}
