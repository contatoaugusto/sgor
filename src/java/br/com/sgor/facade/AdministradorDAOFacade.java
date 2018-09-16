/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.facade;

import br.com.sgor.dao.AdministradorDAO;
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
public class AdministradorDAOFacade extends AbstractFacade<AdministradorDAO> {

    @PersistenceContext(unitName = "sgorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministradorDAOFacade() {
        super(AdministradorDAO.class);
    }
    
    public AdministradorDAO findByUsuario(UsuarioDAO usuario) throws NoResultException{
            //log.debug("Obtendo Usuario com o nome: " + nmLogin);

        getEntityManager();
        AdministradorDAO retorno;
        try {
                retorno = (AdministradorDAO) em.createNamedQuery("AdministradorDAO.findByUsuario")
                                .setParameter("usuario", usuario).getSingleResult();
        }catch (NoResultException e){
                throw new NoResultException("Administrador não encontrado");
        }
        return retorno;
    }
}
