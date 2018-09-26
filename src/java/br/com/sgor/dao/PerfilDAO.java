/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.dao;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author prohgy
 */
@Entity
@Table(name = "perfil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PerfilDAO.findAll", query = "SELECT p FROM PerfilDAO p")
    , @NamedQuery(name = "PerfilDAO.findByIdperfil", query = "SELECT p FROM PerfilDAO p WHERE p.idperfil = :idperfil")
    , @NamedQuery(name = "PerfilDAO.findByNmperfil", query = "SELECT p FROM PerfilDAO p WHERE p.nmperfil = :nmperfil")})
public class PerfilDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idperfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String nmperfil;

    private static final long serialVersionUID = 1L;
 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idperfil")
    private Collection<UsuarioDAO> usuarioDAOCollection;

    public PerfilDAO() {
    }

    public PerfilDAO(Integer idperfil) {
        this.idperfil = idperfil;
    }

    public PerfilDAO(Integer idperfil, String nmperfil) {
        this.idperfil = idperfil;
        this.nmperfil = nmperfil;
    }

    public Integer getIdperfil() {
        return idperfil;
    }

    public void setIdperfil(Integer idperfil) {
        this.idperfil = idperfil;
    }

    public String getNmperfil() {
        return nmperfil;
    }

    public void setNmperfil(String nmperfil) {
        this.nmperfil = nmperfil;
    }

    @XmlTransient
    public Collection<UsuarioDAO> getUsuarioDAOCollection() {
        return usuarioDAOCollection;
    }

    public void setUsuarioDAOCollection(Collection<UsuarioDAO> usuarioDAOCollection) {
        this.usuarioDAOCollection = usuarioDAOCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idperfil != null ? idperfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilDAO)) {
            return false;
        }
        PerfilDAO other = (PerfilDAO) object;
        if ((this.idperfil == null && other.idperfil != null) || (this.idperfil != null && !this.idperfil.equals(other.idperfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.PerfilDAO[ idperfil=" + idperfil + " ]";
    }

  
}
