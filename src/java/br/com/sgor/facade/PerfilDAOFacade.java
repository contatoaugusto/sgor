/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.PerfilDAO;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author prohgy
 */
@Stateless
public class PerfilDAOFacade extends AbstractFacade<PerfilDAO> {

    @PersistenceContext(unitName = "sgorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PerfilDAOFacade() {
        super(PerfilDAO.class);
    }
    
//    public PerfilDAO findByName(String nmUsuario) throws NoResultException{
//            //log.debug("Obtendo Usuario com o nome: " + nmLogin);
//
//        getEntityManager();
//        UsuarioDAO usuario;
//        try {
//                usuario = (UsuarioDAO) em.createNamedQuery("UsuarioDAO.findByNmusuario")
//                                .setParameter("nmusuario", nmUsuario).getSingleResult();
//        }catch (NoResultException e){
//                throw new NoResultException("Usuário "+ nmUsuario + " não encontrado");
//        }
//        return usuario;
//    }
}
