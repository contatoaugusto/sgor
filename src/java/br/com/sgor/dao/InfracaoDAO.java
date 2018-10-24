/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.dao;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author prohgy
 */
@Entity
@Table(name = "infracao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InfracaoDAO.findAll", query = "SELECT i FROM InfracaoDAO i")
    , @NamedQuery(name = "InfracaoDAO.findByIdinfracao", query = "SELECT i FROM InfracaoDAO i WHERE i.idinfracao = :idinfracao")
    , @NamedQuery(name = "InfracaoDAO.findByDescricao", query = "SELECT i FROM InfracaoDAO i WHERE i.descricao = :descricao")
    , @NamedQuery(name = "InfracaoDAO.findByOcorrencia", query = "SELECT i FROM InfracaoDAO i WHERE i.idocorrencia = :ocorrencia")})
public class InfracaoDAO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idinfracao;
    @Size(max = 500)
    private String descricao;
    @JoinColumn(name = "cpf", referencedColumnName = "cpf")
    @ManyToOne(optional = false)
    private AdministradorDAO cpf;
    @JoinColumn(name = "idinfracao_nivel", referencedColumnName = "idinfracao_nivel")
    @ManyToOne(optional = false)
    private InfracaoNivelDAO idinfracaoNivel;
    @JoinColumn(name = "idocorrencia", referencedColumnName = "idocorrencia")
    @ManyToOne(optional = false)
    private OcorrenciaDAO idocorrencia;

    public InfracaoDAO() {
    }

    public InfracaoDAO(Integer idinfracao) {
        this.idinfracao = idinfracao;
    }

    public Integer getIdinfracao() {
        return idinfracao;
    }

    public void setIdinfracao(Integer idinfracao) {
        this.idinfracao = idinfracao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public AdministradorDAO getCpf() {
        return cpf;
    }

    public void setCpf(AdministradorDAO cpf) {
        this.cpf = cpf;
    }

    public InfracaoNivelDAO getIdinfracaoNivel() {
        return idinfracaoNivel;
    }

    public void setIdinfracaoNivel(InfracaoNivelDAO idinfracaoNivel) {
        this.idinfracaoNivel = idinfracaoNivel;
    }

    public OcorrenciaDAO getIdocorrencia() {
        return idocorrencia;
    }

    public void setIdocorrencia(OcorrenciaDAO idocorrencia) {
        this.idocorrencia = idocorrencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idinfracao != null ? idinfracao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InfracaoDAO)) {
            return false;
        }
        InfracaoDAO other = (InfracaoDAO) object;
        if ((this.idinfracao == null && other.idinfracao != null) || (this.idinfracao != null && !this.idinfracao.equals(other.idinfracao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.InfracaoDAO[ idinfracao=" + idinfracao + " ]";
    }
    
}
