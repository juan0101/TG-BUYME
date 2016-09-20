package br.com.buyme.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.buyme.entity.PermissoesEnum;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class CadastroParametroController {
	
	private Stage dialogStage;
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML private CheckBox cadastro,editarCadastro,producao,encomenda,venda,relatorio,home;
	private List<PermissoesEnum> permissoes = new ArrayList<PermissoesEnum>();
	private List<PermissoesEnum> permissoesEdicao = null;
	
	public List<PermissoesEnum> buscarPermissoes(){
		if(cadastro.isSelected()){
			permissoes.add(PermissoesEnum.CADASTRO);
		}
		if(editarCadastro.isSelected()){
			permissoes.add(PermissoesEnum.EDICAO);
		}
		if(producao.isSelected()){
			permissoes.add(PermissoesEnum.PRODUCAO);
		}
		if(encomenda.isSelected()){
			permissoes.add(PermissoesEnum.ENCOMENDA);
		}
		if(venda.isSelected()){
			permissoes.add(PermissoesEnum.VENDA);
		}
		if(relatorio.isSelected()){
			permissoes.add(PermissoesEnum.RELATORIO);
		}
		if(home.isSelected()){
			permissoes.add(PermissoesEnum.HOME);
		}
		return permissoes;
		
	}
	
	@FXML
	public void finalizar(){
		dialogStage.close();
	}
	
	public void preencherEdicao(){
		if(permissoesEdicao != null){
			for(PermissoesEnum pe: permissoesEdicao){
				if(pe.equals(PermissoesEnum.CADASTRO)){
					cadastro.setSelected(true);
				}
				if(pe.equals(PermissoesEnum.EDICAO)){
					editarCadastro.setSelected(true);
				}
				if(pe.equals(PermissoesEnum.PRODUCAO)){
					producao.setSelected(true);
				}
				if(pe.equals(PermissoesEnum.ENCOMENDA)){
					encomenda.setSelected(true);
				}
				if(pe.equals(PermissoesEnum.VENDA)){
					venda.setSelected(true);
				}
				if(pe.equals(PermissoesEnum.RELATORIO)){
					relatorio.setSelected(true);
				}
				if(pe.equals(PermissoesEnum.HOME)){
					home.setSelected(true);
				}
			}
		}
	}

	public void setPermissoesEdicao(List<PermissoesEnum> permissoesEdicao) {
		this.permissoesEdicao = permissoesEdicao;
	}

}
