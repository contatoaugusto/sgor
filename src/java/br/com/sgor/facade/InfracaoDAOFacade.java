/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.InfracaoDAO;
import br.com.sgor.dao.OcorrenciaDAO;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author prohgy
 */
@Stateless
public class InfracaoDAOFacade extends AbstractFacade<InfracaoDAO> {

    @PersistenceContext(unitName = "sgorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InfracaoDAOFacade() {
        super(InfracaoDAO.class);
    }

    public InfracaoDAO findByOcorrencia(OcorrenciaDAO ocorrencia) {

        getEntityManager();
        InfracaoDAO infracao;
        try {
            infracao = (InfracaoDAO) em.createNamedQuery("InfracaoDAO.findByOcorrencia")
                    .setParameter("ocorrencia", ocorrencia).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return infracao;
    }

}
