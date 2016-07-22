package br.com.caelum.financas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.financas.modelo.Conta;

public class ContaDao {

	private EntityManager manager;

	public ContaDao(EntityManager manager) {
		this.manager = manager;
	}

	public Conta busca(Integer id) {
		return this.manager.find(Conta.class, id);
	}

	public List<Conta> lista() {
		return this.manager.createQuery("select c from Conta c", Conta.class)
				.getResultList();
	}
	
	public void salva (Conta conta)	{
		manager.persist(conta);
	}
	
	public void remove (Conta conta) {
		manager.remove(conta);
	}
	
	public int trocaNomeDoBancoEmLote (String antigoNomeBanco, String novoNomeBanco) {
	
		String jpql = "UPDATE conta c SET c.banco = :novoNome"
					+ "WHERE c.banco = :antigoNome";
	
		Query query = manager .createQuery(jpql);
		query.setParameter("antigoNome", antigoNomeBanco);
		query.setParameter("novoNome", novoNomeBanco);
		
		return query.executeUpdate();
	}

}
