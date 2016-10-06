package br.com.buyme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="prod_mot")
public class ProdMot {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@JoinColumn(name="id_motivo_perda")
	@ManyToOne
	private MotivoPerda motPerda;
	@JoinColumn(name="id_produto_analisar")
	@ManyToOne
	private ProdutoAnalisar prodAnalisar;
	@Column(name="quantidade")
	private int quantidade;
	@Column(name="mot_desc")
	private String motDesc;
	

}
