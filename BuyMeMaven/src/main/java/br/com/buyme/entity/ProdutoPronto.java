package br.com.buyme.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="produto_pronto")
@Data
public class ProdutoPronto {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="codigo", nullable=false)
	private String codigo;
	@Column(name="descricao", nullable=false)
	private String descricao;
	@Temporal(TemporalType.DATE)
	@Column(name="data_fabricacao", nullable=false)
	private Date data_fabricacao;
	@Temporal(TemporalType.DATE)
	@Column(name="data_validade", nullable=false)
	private Date data_validade;
	@Column(name="valor", nullable=false)
	private double valor;
	
}
