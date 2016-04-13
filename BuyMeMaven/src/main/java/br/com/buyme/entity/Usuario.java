package br.com.buyme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="usuario")
@Data
public class Usuario {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="login", nullable=false)
	private String login;
	@Column(name="senha", nullable=false)
	private String senha;
	@Column(name="admin", nullable=false)
	private Integer admin;
	
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

}
