package br.com.buyme.entity;

import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="usuario")
@Data
public class Usuario {
	
	private static Usuario usuario;
	
	public Usuario(){}
	
	public static synchronized Usuario getInstance(){
		if(usuario == null){
			usuario = new Usuario();
		}
		return usuario;
	}
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="login", nullable=false)
	private String login;
	@Column(name="senha", nullable=false)
	private String senha;
	@Column(name="admin", columnDefinition = "BIT",nullable=false)
	private boolean admin;
	
	public String getAdminStr(){
		if(admin){
			return "Sim";
		}else{
			return "Não";
		}
	}
	
	//DADOS DE PESSOA
	@Column(name="nome", nullable=false)
	private String nome;
	@Column(name="telefone", nullable=false)
	private String telefone;
	@Column(name="endereco", nullable=false)
	private String endereco;
	@Column(name="numero", nullable=false)
	private int numero;
	@Column(name="cidade", nullable=false)
	private String cidade;
	@Column(name="email", nullable=false)
	private String email;
	@ElementCollection(targetClass=PermissoesEnum.class, fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING) // Possibly optional (I'm not sure) but defaults to ORDINAL.
    @CollectionTable(name="usuario_permissao")
    @Column(name="permissoes") // Column name in person_interest
    Collection<PermissoesEnum> permissoes;

}
