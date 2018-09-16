/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.GuardaDAO;
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
public class GuardaDAOFacade extends AbstractFacade<GuardaDAO> {

    @PersistenceContext(unitName = "sgorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GuardaDAOFacade() {
        super(GuardaDAO.class);
    }
   
    public GuardaDAO findByUsuario(UsuarioDAO usuario) throws NoResultException{
            //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        GuardaDAO retorno;
        try {
                retorno = (GuardaDAO) em.createNamedQuery("GuardaDAO.findByUsuario")
                                .setParameter("usuario", usuario).getSingleResult();
        }catch (NoResultException e){
                throw new NoResultException("Guarda não encontrado");
        }
        return retorno;
    }
}
