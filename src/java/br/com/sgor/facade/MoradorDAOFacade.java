/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.dao.UsuarioDAO;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author prohgy
 */
@Stateless
public class MoradorDAOFacade extends AbstractFacade<MoradorDAO> {

    @PersistenceContext(unitName = "sgorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MoradorDAOFacade() {
        super(MoradorDAO.class);
    }
 
    public MoradorDAO findByUsuario(UsuarioDAO usuario) throws NoResultException{
            //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        MoradorDAO retorno;
        try {
                retorno = (MoradorDAO) em.createNamedQuery("MoradorDAO.findByUsuario")
                                .setParameter("idusuario", usuario).getSingleResult();
        }catch (NoResultException e){
                throw new NoResultException("Morador não encontrado");
        }
        return retorno;
    }
}
