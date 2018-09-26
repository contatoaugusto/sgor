/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "infracao_nivel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InfracaoNivelDAO.findAll", query = "SELECT i FROM InfracaoNivelDAO i")
    , @NamedQuery(name = "InfracaoNivelDAO.findByIdinfracaoNivel", query = "SELECT i FROM InfracaoNivelDAO i WHERE i.idinfracaoNivel = :idinfracaoNivel")
    , @NamedQuery(name = "InfracaoNivelDAO.findByNminfracaoNivel", query = "SELECT i FROM InfracaoNivelDAO i WHERE i.nminfracaoNivel = :nminfracaoNivel")
    , @NamedQuery(name = "InfracaoNivelDAO.findByDeinfracaoNivel", query = "SELECT i FROM InfracaoNivelDAO i WHERE i.deinfracaoNivel = :deinfracaoNivel")
    , @NamedQuery(name = "InfracaoNivelDAO.findByVrmulta", query = "SELECT i FROM InfracaoNivelDAO i WHERE i.vrmulta = :vrmulta")})
public class InfracaoNivelDAO implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idinfracaoNivel")
    private Collection<InfracaoDAO> infracaoDAOCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idinfracao_nivel")
    private Integer idinfracaoNivel;
    @Size(max = 60)
    @Column(name = "nminfracao_nivel")
    private String nminfracaoNivel;
    @Size(max = 200)
    @Column(name = "deinfracao_nivel")
    private String deinfracaoNivel;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vrmulta")
    private BigDecimal vrmulta;

    public InfracaoNivelDAO() {
    }

    public InfracaoNivelDAO(Integer idinfracaoNivel) {
        this.idinfracaoNivel = idinfracaoNivel;
    }

    public Integer getIdinfracaoNivel() {
        return idinfracaoNivel;
    }

    public void setIdinfracaoNivel(Integer idinfracaoNivel) {
        this.idinfracaoNivel = idinfracaoNivel;
    }

    public String getNminfracaoNivel() {
        return nminfracaoNivel;
    }

    public void setNminfracaoNivel(String nminfracaoNivel) {
        this.nminfracaoNivel = nminfracaoNivel;
    }

    public String getDeinfracaoNivel() {
        return deinfracaoNivel;
    }

    public void setDeinfracaoNivel(String deinfracaoNivel) {
        this.deinfracaoNivel = deinfracaoNivel;
    }

    public BigDecimal getVrmulta() {
        return vrmulta;
    }

    public void setVrmulta(BigDecimal vrmulta) {
        this.vrmulta = vrmulta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idinfracaoNivel != null ? idinfracaoNivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InfracaoNivelDAO)) {
            return false;
        }
        InfracaoNivelDAO other = (InfracaoNivelDAO) object;
        if ((this.idinfracaoNivel == null && other.idinfracaoNivel != null) || (this.idinfracaoNivel != null && !this.idinfracaoNivel.equals(other.idinfracaoNivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.InfracaoNivelDAO[ idinfracaoNivel=" + idinfracaoNivel + " ]";
    }

    @XmlTransient
    public Collection<InfracaoDAO> getInfracaoDAOCollection() {
        return infracaoDAOCollection;
    }

    public void setInfracaoDAOCollection(Collection<InfracaoDAO> infracaoDAOCollection) {
        this.infracaoDAOCollection = infracaoDAOCollection;
    }
    
}
