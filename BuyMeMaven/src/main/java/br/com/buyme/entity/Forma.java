package br.com.buyme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="forma")
@Data
public class Forma {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private String codigo;
	@Column(name="descricao", nullable=false)
	private String descricao;
	@Column(name="nome_produto", nullable=false)
	private String nomeProduto;
	@ManyToOne
	private Produto produto;
	@Column(name="quantidade", nullable=false)
	private int quantidade;
	
	

}
