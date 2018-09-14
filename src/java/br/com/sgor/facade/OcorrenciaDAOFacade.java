/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.OcorrenciaDAO;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

//    public OcorrenciaDAO findByUsuario(Integer idUsuario) throws NoResultException {
//        //log.debug("Obtendo Usuario com o nome: " + nmLogin);
//
//        getEntityManager();
//        OcorrenciaDAO morador;
//        try {
//            morador = (OcorrenciaDAO) em.createNamedQuery("OcorrenciaDAO.findByUsuario")
//                    .setParameter("idusuario", idUsuario).getSingleResult();
//        } catch (NoResultException e) {
//            throw new NoResultException("Morador não encontrado");
//        }
//        return morador;
//    }
}
