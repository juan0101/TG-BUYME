package br.com.buyme.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.Forma;
import br.com.buyme.entity.Produto;

public class FormaDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public Forma salvar(Forma forma){
		EntityManager em = getEM();
		try{
			em.getTransaction().begin();
			em.persist(forma);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return forma;
	}
	
	public Forma getOneByCodigo(String codigo){
		EntityManager em = getEM();
		Forma forma = null;
		try{
			Query query = em.createQuery("Select f FROM Forma f where f.codigo = :cod");
			query.setParameter("cod", codigo);
			forma = (Forma)query.getSingleResult();
		}catch(Exception e){
			System.out.println(e);
		}finally {
			em.close();
		}
		return forma;
	}
	
	public Forma salvarEdicao(String codigo, String descricao, int quantidade,int idProduto, int id){
		EntityManager em = getEM();
		Forma forma = em.find(Forma.class, id);
		try{
			em.getTransaction().begin();
			forma.setCodigo(codigo);
			forma.setDescricao(descricao);
			forma.setQuantidade(quantidade);
			//Encontrando o produto para setar
			Produto prod = em.find(Produto.class, idProduto);
			forma.setProduto(prod);
			forma.setNomeProduto(prod.getNome());
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return forma;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Forma> getAll(){
		EntityManager em = getEM();
		List<Forma> forma = null;
		try{
			Query query = em.createQuery("Select f FROM Forma f");
			forma = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return forma;
	}
	
	public void excluir(int id){
		EntityManager em = getEM();
		Forma forma = null;
		try{
			forma = em.find(Forma.class, id);
			em.getTransaction().begin();
			em.remove(forma);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
}
