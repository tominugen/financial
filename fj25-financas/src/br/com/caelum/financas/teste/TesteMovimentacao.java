package br.com.caelum.financas.teste;

import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.util.JPAUtil;

public class TesteMovimentacao {
	public static void main(String[] args) {
		
		EntityManager manager = new JPAUtil().getEntityManager();
		manager.getTransaction().begin();
		
		Conta conta = new Conta();
		manager.persist(conta);
		
		Movimentacao mov = new Movimentacao();
		mov.setConta(conta);
		manager.persist(mov);
		manager.getTransaction().commit();
		manager.close();
	}

}
