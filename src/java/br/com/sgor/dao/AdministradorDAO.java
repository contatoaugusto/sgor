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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author prohgy
 */
@Entity
@Table(name = "administrador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdministradorDAO.findAll", query = "SELECT a FROM AdministradorDAO a")
    , @NamedQuery(name = "AdministradorDAO.findByCpf", query = "SELECT a FROM AdministradorDAO a WHERE a.cpf = :cpf")
    , @NamedQuery(name = "AdministradorDAO.findByNome", query = "SELECT a FROM AdministradorDAO a WHERE a.nome = :nome")
    , @NamedQuery(name = "AdministradorDAO.findByDatanasc", query = "SELECT a FROM AdministradorDAO a WHERE a.datanasc = :datanasc")
    , @NamedQuery(name = "AdministradorDAO.findByCep", query = "SELECT a FROM AdministradorDAO a WHERE a.cep = :cep")
    , @NamedQuery(name = "AdministradorDAO.findByEndereco", query = "SELECT a FROM AdministradorDAO a WHERE a.endereco = :endereco")
    , @NamedQuery(name = "AdministradorDAO.findBySexo", query = "SELECT a FROM AdministradorDAO a WHERE a.sexo = :sexo")
    , @NamedQuery(name = "AdministradorDAO.findByUsuario", query = "SELECT a FROM AdministradorDAO a WHERE a.idusuario = :usuario")})
public class AdministradorDAO implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    private String cpf;
    @Size(max = 45)
    private String nome;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datanasc;
    @Size(max = 10)
    private String cep;
    @Size(max = 45)
    private String endereco;
    @Size(max = 10)
    private String sexo;

    private static final long serialVersionUID = 1L;
  
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne
    private UsuarioDAO idusuario;
    @OneToMany(mappedBy = "cpf")
    private Collection<InfracaoDAO> infracaoDAOCollection;

    public AdministradorDAO() {
    }

    public AdministradorDAO(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(Date datanasc) {
        this.datanasc = datanasc;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public UsuarioDAO getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(UsuarioDAO idusuario) {
        this.idusuario = idusuario;
    }

    @XmlTransient
    public Collection<InfracaoDAO> getInfracaoDAOCollection() {
        return infracaoDAOCollection;
    }

    public void setInfracaoDAOCollection(Collection<InfracaoDAO> infracaoDAOCollection) {
        this.infracaoDAOCollection = infracaoDAOCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpf != null ? cpf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdministradorDAO)) {
            return false;
        }
        AdministradorDAO other = (AdministradorDAO) object;
        if ((this.cpf == null && other.cpf != null) || (this.cpf != null && !this.cpf.equals(other.cpf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.AdministradorDAO[ cpf=" + cpf + " ]";
    }

}
