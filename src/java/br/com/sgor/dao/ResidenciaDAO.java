/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.dao;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author prohgy
 */
@Entity
@Table(name = "residencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResidenciaDAO.findAll", query = "SELECT r FROM ResidenciaDAO r")
    , @NamedQuery(name = "ResidenciaDAO.findByIdresidencia", query = "SELECT r FROM ResidenciaDAO r WHERE r.idresidencia = :idresidencia")
    , @NamedQuery(name = "ResidenciaDAO.findByModulo", query = "SELECT r FROM ResidenciaDAO r WHERE r.modulo = :modulo")
    , @NamedQuery(name = "ResidenciaDAO.findByNumero", query = "SELECT r FROM ResidenciaDAO r WHERE r.numero = :numero")
    , @NamedQuery(name = "ResidenciaDAO.findByDescricao", query = "SELECT r FROM ResidenciaDAO r WHERE r.descricao = :descricao")})
public class ResidenciaDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idresidencia;
    @Size(max = 45)
    private String modulo;
    private Integer numero;
    @Size(max = 100)
    private String descricao;

    private static final long serialVersionUID = 1L;
 
    @OneToMany(mappedBy = "idresidencia")
    private Collection<MoradorDAO> moradorDAOCollection;

    public ResidenciaDAO() {
    }

    public ResidenciaDAO(Integer idresidencia) {
        this.idresidencia = idresidencia;
    }

    public Integer getIdresidencia() {
        return idresidencia;
    }

    public void setIdresidencia(Integer idresidencia) {
        this.idresidencia = idresidencia;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<MoradorDAO> getMoradorDAOCollection() {
        return moradorDAOCollection;
    }

    public void setMoradorDAOCollection(Collection<MoradorDAO> moradorDAOCollection) {
        this.moradorDAOCollection = moradorDAOCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idresidencia != null ? idresidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResidenciaDAO)) {
            return false;
        }
        ResidenciaDAO other = (ResidenciaDAO) object;
        if ((this.idresidencia == null && other.idresidencia != null) || (this.idresidencia != null && !this.idresidencia.equals(other.idresidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.ResidenciaDAO[ idresidencia=" + idresidencia + " ]";
    }
}
