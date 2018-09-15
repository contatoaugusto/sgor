/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.MoradorDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author prohgy
 */
@Stateless
public class OcorrenciaDAOFacade extends AbstractFacade<OcorrenciaDAO> {

    @PersistenceContext(unitName = "sgorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OcorrenciaDAOFacade() {
        super(OcorrenciaDAO.class);
    }

    public List<OcorrenciaDAO> findByMorador(MoradorDAO morador) throws NoResultException{
            //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        List<OcorrenciaDAO> ocorrencia;
        try {
                ocorrencia = (List<OcorrenciaDAO>) em.createNamedQuery("OcorrenciaDAO.findByMorador")
                                .setParameter("morador", morador).getResultList();
        }catch (NoResultException e){
                throw new NoResultException("Morador não encontrado");
        }
        return ocorrencia;
    }
}
