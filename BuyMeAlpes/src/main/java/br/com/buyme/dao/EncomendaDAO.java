package br.com.buyme.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.buyme.entity.Cliente;
import br.com.buyme.entity.Encomenda;
import br.com.buyme.entity.Produto;

public class EncomendaDAO {

	public EntityManager getEM(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("buyme");
		return factory.createEntityManager();
	}
	
	public void salvar(String codigo, Cliente cliente, Produto produto, int quantidade, double valor, String descProduto, String descCliente){
		EntityManager em = getEM();
		Encomenda e = new Encomenda();
		try{
			em.getTransaction().begin();
			e.setCodigo(codigo);
			e.setCliente(cliente);
			e.setProduto(produto);
			e.setQuantidade(quantidade);
			e.setValor(valor);
			e.setDescCliente(descProduto);
			e.setDescProduto(descCliente);
			e.setDesativado(false);
			em.persist(e);
			em.getTransaction().commit();
		}catch(Exception ex){
			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally {
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Encomenda> getAll(){
		EntityManager em = getEM();
		List<Encomenda> encomendas = null;
		try{
			Query query = em.createQuery("Select e FROM Encomenda e where e.desativado = 0");
			encomendas = query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			em.close();
		}
		return encomendas;
	}
	
	public Encomenda getOne(String codigo){
		EntityManager em = getEM();
		Encomenda e = null;
		try{
			Query query = em.createQuery("Select e FROM Encomenda e where e.codigo = :cod");
			query.setParameter("cod", codigo);
			e = (Encomenda)query.getSingleResult();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			em.close();
		}
		return e;
	}
	
	public void removerById(int id){
		try{
			EntityManager em = getEM();
			Encomenda e = em.find(Encomenda.class, id);
			try{
				em.getTransaction().begin();
				em.remove(e);
				em.getTransaction().commit();
			}catch(Exception ex){
				ex.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void desativarById(int id){
		try{
			EntityManager em = getEM();
			Encomenda e = em.find(Encomenda.class, id);
			try{
				em.getTransaction().begin();
				e.setDesativado(true);
				em.getTransaction().commit();
			}catch(Exception ex){
				ex.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void diminuirQuantidadeById(int id,int quantidade){
		try{
			EntityManager em = getEM();
			Encomenda e = em.find(Encomenda.class, id);
			try{
				em.getTransaction().begin();
				e.setQuantidade(quantidade);
				em.getTransaction().commit();
			}catch(Exception ex){
				ex.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
