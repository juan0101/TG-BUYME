package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import conexao.ConexaoBD;
import entity.Usuario;

public class UsuarioDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public Usuario salvar(Usuario usu){
		EntityManager em = getEM();
		try{
			em.getTransaction().begin();
			em.persist(usu);
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return usu;
	}
	
	public Usuario getOne(Usuario usu){
		String query = "SELECT * FROM USUARIO WHERE LOGIN = '"+usu.getLogin()+"' AND SENHA = '"+usu.getSenha()+"'";
		ConexaoBD conexao = ConexaoBD.getDbCon();
		try {
			ResultSet rs = conexao.query(query);
			if(!rs.isBeforeFirst()){
				return null;
			}else{
				Usuario retorno = new Usuario();
				while (rs.next()) {
					retorno.setId(rs.getInt("ID"));
		            retorno.setLogin(rs.getString("LOGIN"));
		            retorno.setSenha(rs.getString("SENHA"));
		            retorno.setAdmin(rs.getInt("ADMIN"));
		        }
				return retorno;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
}
