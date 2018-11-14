/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.dao.GuardaDAO;
import java.util.Date;
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

    public List<OcorrenciaDAO> findByMorador(MoradorDAO morador) throws NoResultException {
        //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        List<OcorrenciaDAO> ocorrencia;
        try {
            ocorrencia = (List<OcorrenciaDAO>) em.createNamedQuery("OcorrenciaDAO.findByMorador")
                    .setParameter("morador", morador).getResultList();
        } catch (NoResultException e) {
            throw new NoResultException("Ocorrencia não encontrado");
        }
        return ocorrencia;
    }

    public List<OcorrenciaDAO> findByGuarda(GuardaDAO guarda) throws NoResultException {
        //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        List<OcorrenciaDAO> ocorrencia;
        try {
            ocorrencia = (List<OcorrenciaDAO>) em.createNamedQuery("OcorrenciaDAO.findByGuarda")
                    .setParameter("guarda", guarda).getResultList();
        } catch (NoResultException e) {
            throw new NoResultException("Ocorrencia não encontrado");
        }
        return ocorrencia;
    }

    public List<OcorrenciaDAO> findByDataInicoFim(Date dataInicial, Date dataFinal) throws NoResultException {
        //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        List<OcorrenciaDAO> ocorrencia;
        try {
            ocorrencia = (List<OcorrenciaDAO>) em.createNamedQuery("OcorrenciaDAO.findByDataInicoFim")
                    .setParameter("dataInicio", dataInicial)
                    .setParameter("dataFim", dataFinal)
                    .getResultList();
        } catch (NoResultException e) {
            throw new NoResultException("Ocorrencia não encontrado");
        }
        return ocorrencia;
    }
}
