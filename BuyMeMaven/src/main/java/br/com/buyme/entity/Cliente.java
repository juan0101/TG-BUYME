package br.com.buyme.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="cliente")
@Data
public class Cliente {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
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
	@Column(name="encomenda")
	@OneToMany(mappedBy="cliente",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Encomenda> encomenda;
	
}
