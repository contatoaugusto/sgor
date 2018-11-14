/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author prohgy
 */
@Entity
@Table(name = "ocorrencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OcorrenciaDAO.findAll", query = "SELECT o FROM OcorrenciaDAO o ORDER BY o.data ASC")
    , @NamedQuery(name = "OcorrenciaDAO.findByIdocorrencia", query = "SELECT o FROM OcorrenciaDAO o WHERE o.idocorrencia = :idocorrencia")
    , @NamedQuery(name = "OcorrenciaDAO.findByData", query = "SELECT o FROM OcorrenciaDAO o WHERE o.data = :data")
    , @NamedQuery(name = "OcorrenciaDAO.findByDescricao", query = "SELECT o FROM OcorrenciaDAO o WHERE o.descricao = :descricao")
    , @NamedQuery(name = "OcorrenciaDAO.findByMorador", query = "SELECT o FROM OcorrenciaDAO o WHERE o.idmorador = :morador")
    , @NamedQuery(name = "OcorrenciaDAO.findByGuarda", query = "SELECT o FROM OcorrenciaDAO o WHERE o.idguarda = :guarda")
    , @NamedQuery(name = "OcorrenciaDAO.findByDataInicoFim", query = "SELECT o FROM OcorrenciaDAO o WHERE o.data BETWEEN :dataInicio AND :dataFim")})
public class OcorrenciaDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idocorrencia;
    @Size(max = 20)
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Size(max = 500)
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idocorrencia")
    private Collection<InfracaoDAO> infracaoDAOCollection;

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "idguarda", referencedColumnName = "idguarda")
    @ManyToOne(fetch = FetchType.EAGER)
    private GuardaDAO idguarda;
    @JoinColumn(name = "idmorador", referencedColumnName = "idmorador")
    @ManyToOne(fetch = FetchType.EAGER)
    private MoradorDAO idmorador;

    public OcorrenciaDAO() {
    }

    public OcorrenciaDAO(Integer idocorrencia) {
        this.idocorrencia = idocorrencia;
    }

    public Integer getIdocorrencia() {
        return idocorrencia;
    }

    public void setIdocorrencia(Integer idocorrencia) {
        this.idocorrencia = idocorrencia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public GuardaDAO getIdguarda() {
        return idguarda;
    }

    public void setIdguarda(GuardaDAO idguarda) {
        this.idguarda = idguarda;
    }

    public MoradorDAO getIdmorador() {
        return idmorador;
    }

    public void setIdmorador(MoradorDAO idmorador) {
        this.idmorador = idmorador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idocorrencia != null ? idocorrencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OcorrenciaDAO)) {
            return false;
        }
        OcorrenciaDAO other = (OcorrenciaDAO) object;
        if ((this.idocorrencia == null && other.idocorrencia != null) || (this.idocorrencia != null && !this.idocorrencia.equals(other.idocorrencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.OcorrenciaDAO[ idocorrencia=" + idocorrencia + " ]";
    }

    @XmlTransient
    public Collection<InfracaoDAO> getInfracaoDAOCollection() {
        return infracaoDAOCollection;
    }

    public void setInfracaoDAOCollection(Collection<InfracaoDAO> infracaoDAOCollection) {
        this.infracaoDAOCollection = infracaoDAOCollection;
    }
}
