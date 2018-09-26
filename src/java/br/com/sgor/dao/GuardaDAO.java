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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "guarda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GuardaDAO.findAll", query = "SELECT g FROM GuardaDAO g")
    , @NamedQuery(name = "GuardaDAO.findByIdguarda", query = "SELECT g FROM GuardaDAO g WHERE g.idguarda = :idguarda")
    , @NamedQuery(name = "GuardaDAO.findByTelefone", query = "SELECT g FROM GuardaDAO g WHERE g.telefone = :telefone")
    , @NamedQuery(name = "GuardaDAO.findByEndereco", query = "SELECT g FROM GuardaDAO g WHERE g.endereco = :endereco")
    , @NamedQuery(name = "GuardaDAO.findByCep", query = "SELECT g FROM GuardaDAO g WHERE g.cep = :cep")
    , @NamedQuery(name = "GuardaDAO.findByDatanasc", query = "SELECT g FROM GuardaDAO g WHERE g.datanasc = :datanasc")
    , @NamedQuery(name = "GuardaDAO.findBySexo", query = "SELECT g FROM GuardaDAO g WHERE g.sexo = :sexo")
    , @NamedQuery(name = "GuardaDAO.findByNome", query = "SELECT g FROM GuardaDAO g WHERE g.nome = :nome")
    , @NamedQuery(name = "GuardaDAO.findByCpf", query = "SELECT g FROM GuardaDAO g WHERE g.cpf = :cpf")
    , @NamedQuery(name = "GuardaDAO.findByUsuario", query = "SELECT g FROM GuardaDAO g WHERE g.idusuario = :usuario")})
public class GuardaDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idguarda;
    @Size(max = 15)
    private String telefone;
    @Size(max = 45)
    private String endereco;
    @Size(max = 10)
    private String cep;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datanasc;
    @Size(max = 10)
    private String sexo;
    @Size(max = 45)
    private String nome;
    @Size(max = 15)
    private String cpf;

    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "idguarda")
    private Collection<OcorrenciaDAO> ocorrenciaDAOCollection;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne
    private UsuarioDAO idusuario;

    public GuardaDAO() {
    }

    public GuardaDAO(Integer idguarda) {
        this.idguarda = idguarda;
    }

    public Integer getIdguarda() {
        return idguarda;
    }

    public void setIdguarda(Integer idguarda) {
        this.idguarda = idguarda;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Date getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(Date datanasc) {
        this.datanasc = datanasc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @XmlTransient
    public Collection<OcorrenciaDAO> getOcorrenciaDAOCollection() {
        return ocorrenciaDAOCollection;
    }

    public void setOcorrenciaDAOCollection(Collection<OcorrenciaDAO> ocorrenciaDAOCollection) {
        this.ocorrenciaDAOCollection = ocorrenciaDAOCollection;
    }

    public UsuarioDAO getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(UsuarioDAO idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idguarda != null ? idguarda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GuardaDAO)) {
            return false;
        }
        GuardaDAO other = (GuardaDAO) object;
        if ((this.idguarda == null && other.idguarda != null) || (this.idguarda != null && !this.idguarda.equals(other.idguarda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.GuardaDAO[ idguarda=" + idguarda + " ]";
    }

 
}
