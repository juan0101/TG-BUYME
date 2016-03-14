package entity;

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
	private Integer id;
	@Column(name="login", nullable=false)
	private String login;
	@Column(name="senha", nullable=false)
	private String senha;
	@Column(name="admin", nullable=false)
	private Integer admin;

}
