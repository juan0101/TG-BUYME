package br.com.buyme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	@ManyToOne
	private MotivoPerda motPerda;
	@ManyToOne
	private ProdutoAnalisar prodAnalisar;
	@Column(name="quantidade")
	private int quantidade;
	@Column(name="mot_desc")
	private String motDesc;
	

}
