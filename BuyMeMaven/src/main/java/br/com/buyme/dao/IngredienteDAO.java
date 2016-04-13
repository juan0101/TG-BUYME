package br.com.buyme.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.Ingrediente;

public class IngredienteDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public Ingrediente salvar(Ingrediente ing){
		EntityManager em = getEM();
		try{
			em.getTransaction().begin();
			em.persist(ing);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return ing;
	}
	
	@SuppressWarnings("unchecked")
	public List<Ingrediente> getAllIngredientes(){
		EntityManager em = getEM();
		List<Ingrediente> ingrediente = null;
		try{
			Query query = em.createQuery("Select i FROM Ingrediente i");
			ingrediente = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return ingrediente;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getNomes(){
		EntityManager em = getEM();
		List<String> nomes = null;
		try{
			Query query = em.createQuery("Select i.descricao FROM Ingrediente i");
			nomes = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return nomes;
	}
	
}
