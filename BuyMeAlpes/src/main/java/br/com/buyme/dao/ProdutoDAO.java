package br.com.buyme.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.Produto;
import br.com.buyme.entity.ProdutoIngrediente;

public class ProdutoDAO {
	
	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public void salvar(Produto prod){
		EntityManager em = getEM();
		try{
			em.getTransaction().begin();
			em.persist(prod);
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void excluirProduto(int id){
		EntityManager em = getEM();
		Produto produto = null;
		try{
			produto = em.find(Produto.class, id);
			em.getTransaction().begin();
			em.remove(produto);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	public void editarProduto(int id, String nome, Double valor, List<ProdutoIngrediente> listProdIng){
		EntityManager em = getEM();
		Produto produto = em.find(Produto.class, id);
		try{
			em.getTransaction().begin();
			produto.setNome(nome);
			produto.setValor(valor);
			produto.setProduto_ingrediente(listProdIng);
			em.merge(produto);
			em.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
	}
	
	public void updateSalvarForma(Produto pro){
		EntityManager em = getEM();
		Produto produto = em.find(Produto.class, pro.getId());
		try{
			em.getTransaction().begin();
			produto.getForma().add(pro.getForma().get(0)); 
			em.getTransaction().commit();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> getAll(){
		EntityManager em = getEM();
		List<Produto> produtos = null;
		try{
			Query query = em.createQuery("Select p FROM Produto p");
			produtos = query.getResultList();
		}catch(Exception e){
			System.out.println(e);
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
		return produtos;
	}
	
	public Produto getOne(String codigo){
		EntityManager em = getEM();
		Produto produto = null;
		try{
			Query query = em.createQuery("Select p FROM Produto p where p.codigo = :cod");
			query.setParameter("cod", codigo);
			produto = (Produto)query.getSingleResult();
		}catch(Exception e){
			System.out.println(e);
		}finally {
			em.close();
		}
		return produto;
	}
	
	public Produto getOneById(int id){
		EntityManager em = getEM();
		Produto produto = null;
		try{
			produto = em.find(Produto.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			em.close();
		}
		return produto;
	}

}
