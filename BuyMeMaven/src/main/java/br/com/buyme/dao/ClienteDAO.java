package br.com.buyme.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.Cliente;
import br.com.buyme.util.Utils;


public class ClienteDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public void salvar(String nome, String telefone, String endereco, int numero, String cidade, String email, String login, String senha){
		EntityManager em = getEM();
		Cliente cliente = new Cliente();
		try{
			em.getTransaction().begin();
			cliente.setNome(nome);
			cliente.setTelefone(telefone);
			cliente.setEndereco(endereco);
			cliente.setNumero(numero);
			cliente.setCidade(cidade);
			cliente.setEmail(email);
			if(login != null){
				if(senha != null){
					cliente.setLogin(login);
					String senhaCliente = Utils.senhaSha256(senha);
					cliente.setSenha(senhaCliente);
				}
			}
			em.persist(cliente);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void salvarEdicao(int id, String nome, String telefone, String endereco, int numero, String cidade, String email){
		EntityManager em = getEM();
		Cliente cliente = em.find(Cliente.class, id);
		try{
			em.getTransaction().begin();
			cliente.setNome(nome);
			cliente.setTelefone(telefone);
			cliente.setEndereco(endereco);
			cliente.setNumero(numero);
			cliente.setCidade(cidade);
			cliente.setEmail(email);
			em.persist(cliente);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void salvarLoginSenha(int id, String login, String senha){
		EntityManager em = getEM();
		Cliente cliente = em.find(Cliente.class, id);
		try{
			em.getTransaction().begin();
			cliente.setLogin(login);
			cliente.setSenha(senha);
			em.persist(cliente);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> getAll(){
		EntityManager em = getEM();
		List<Cliente> clientes = null;
		try{
			Query query = em.createQuery("Select c FROM Cliente c");
			clientes = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return clientes;
	}
	
	public void removerById(int id){
		try{
			EntityManager em = getEM();
			Cliente cliente = em.find(Cliente.class, id);
			try{
				em.getTransaction().begin();
				em.remove(cliente);
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
	
	public Cliente getOneWithLogin(String login){
		EntityManager em = getEM();
		Cliente cliente = null;
		try{
			Query query = em.createQuery("Select c FROM Cliente c WHERE c.login = :login");
			query.setParameter("login", login);
			cliente = (Cliente)query.getSingleResult();	
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return cliente;
	}

}
