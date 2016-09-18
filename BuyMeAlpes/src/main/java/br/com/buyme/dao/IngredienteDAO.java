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
	
	public Ingrediente getOne(String cod){
		EntityManager em = getEM();
		Ingrediente ingrediente = null;
		try{
			Query query = em.createQuery("Select i FROM Ingrediente i WHERE i.codigo = :codigo");
			query.setParameter("codigo", cod);
			ingrediente = (Ingrediente)query.getSingleResult();	
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(e);
		}finally{
			em.close();
		}
		return ingrediente;
	}
	
	public void editarIngrediente(int id,String desc){
		EntityManager em = getEM();
		Ingrediente ingrediente = em.find(Ingrediente.class, id);
		try{
			em.getTransaction().begin();
			ingrediente.setDescricao(desc); 
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
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
	
	public void excluir(int id){
		EntityManager em = getEM();
		Ingrediente ingrediente = null;
		try{
			ingrediente = em.find(Ingrediente.class, id);
			em.getTransaction().begin();
			em.remove(ingrediente);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
}
