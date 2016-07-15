package br.com.buyme.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.conexao.ConexaoBD;
import br.com.buyme.entity.Usuario;

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
	
	public Usuario getOneWithLogin(String login){
		EntityManager em = getEM();
		Usuario usuario = null;
		try{
			Query query = em.createQuery("Select u FROM Usuario u WHERE u.login = :login");
			query.setParameter("login", login);
			usuario = (Usuario)query.getSingleResult();	
		}catch (Exception e){
			System.out.println(e);
		}finally{
			em.close();
		}
		return usuario;
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
		            retorno.setAdmin(rs.getBoolean("ADMIN"));
		        }
				return retorno;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public void editarUsuario(int id, String login, String nome, String telefone, String endereco, int numero, String cidade, String email, boolean admin){
		EntityManager em = getEM();
		Usuario usuarioEditado = em.find(Usuario.class, id);
		try{
			em.getTransaction().begin();
			usuarioEditado.setLogin(login);
			usuarioEditado.setNome(nome);
			usuarioEditado.setTelefone(telefone);
			usuarioEditado.setEndereco(endereco);
			usuarioEditado.setNumero(numero);
			usuarioEditado.setCidade(cidade);
			usuarioEditado.setEmail(email);
			usuarioEditado.setAdmin(admin);
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
	}
	
	public void trocarSenha(int id, String novaSenha){
		EntityManager em = getEM();
		Usuario usuario = em.find(Usuario.class, id);
		try{
			em.getTransaction().begin();
			usuario.setSenha(novaSenha);
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getAll(){
		EntityManager em = getEM();
		List<Usuario> usuarios = null;
		try{
			Query query = em.createQuery("Select u FROM Usuario u");
			usuarios = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return usuarios;
	}
	
	public void removerById(int id){
		try{
			EntityManager em = getEM();
			Usuario usuario = em.find(Usuario.class, id);
			try{
				em.getTransaction().begin();
				em.remove(usuario);
				em.getTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}