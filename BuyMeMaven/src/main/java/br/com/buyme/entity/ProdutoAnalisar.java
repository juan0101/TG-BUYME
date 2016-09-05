package br.com.buyme.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="produto_analisar")
@Data
public class ProdutoAnalisar {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="quantidade", nullable=false)
	private int quantidade;
	@Column(name="quantidade_produzida", nullable=false)
	private int quantidadeProduzida;
	@Column(name="descricao", nullable=false)
	private String descricao;
	@Temporal(TemporalType.DATE)
	@Column(name="data_fabricacao", nullable=false)
	private Date dataFabricacao;
	@Temporal(TemporalType.DATE)
	@Column(name="data_validade", nullable=false)
	private Date dataValidade;
	@Column(name="valor", nullable=false)
	private double valor;
	@Column(name="valor_total", nullable=false)
	private double valorTotal;
	@Column(name="analisado", nullable=false)
	private boolean analisado;
	@Column(name="codigo_produto", nullable=false)
	private int codigoProduto;
	@Column(name="quantidadePerda")
	private Integer quantidadePerda;
	@Column(name="lote", nullable=false)
	private String lote;
	@Column(name="prod_mot")
	@OneToMany(mappedBy="prodAnalisar",fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<ProdMot> prod_mot;
	
}
