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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "morador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MoradorDAO.findAll", query = "SELECT m FROM MoradorDAO m")
    , @NamedQuery(name = "MoradorDAO.findByIdmorador", query = "SELECT m FROM MoradorDAO m WHERE m.idmorador = :idmorador")
    , @NamedQuery(name = "MoradorDAO.findByCpf", query = "SELECT m FROM MoradorDAO m WHERE m.cpf = :cpf")
    , @NamedQuery(name = "MoradorDAO.findByFatura", query = "SELECT m FROM MoradorDAO m WHERE m.fatura = :fatura")
    , @NamedQuery(name = "MoradorDAO.findByEmail", query = "SELECT m FROM MoradorDAO m WHERE m.email = :email")
    , @NamedQuery(name = "MoradorDAO.findByTelefone", query = "SELECT m FROM MoradorDAO m WHERE m.telefone = :telefone")
    , @NamedQuery(name = "MoradorDAO.findByCep", query = "SELECT m FROM MoradorDAO m WHERE m.cep = :cep")
    , @NamedQuery(name = "MoradorDAO.findByResidencia", query = "SELECT m FROM MoradorDAO m WHERE m.residencia = :residencia")
    , @NamedQuery(name = "MoradorDAO.findByDatanasc", query = "SELECT m FROM MoradorDAO m WHERE m.datanasc = :datanasc")
    , @NamedQuery(name = "MoradorDAO.findBySexo", query = "SELECT m FROM MoradorDAO m WHERE m.sexo = :sexo")
    , @NamedQuery(name = "MoradorDAO.findByNome", query = "SELECT m FROM MoradorDAO m WHERE m.nome = :nome")
    , @NamedQuery(name = "MoradorDAO.findByUsuario", query = "SELECT m FROM MoradorDAO m WHERE m.idusuario = :idusuario")})
public class MoradorDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idmorador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    private String cpf;
    @Size(max = 45)
    private String fatura;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
    private String email;
    @Size(max = 15)
    private String telefone;
    @Size(max = 10)
    private String cep;
    @Size(max = 45)
    private String residencia;
    @Size(max = 10)
    private String datanasc;
    @Size(max = 10)
    private String sexo;
    @Size(max = 45)
    private String nome;

    private static final long serialVersionUID = 1L;
 
    @JoinColumn(name = "idresidencia", referencedColumnName = "idresidencia")
    @ManyToOne
    private ResidenciaDAO idresidencia;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne
    private UsuarioDAO idusuario;
    @OneToMany(mappedBy = "idmorador")
    private Collection<OcorrenciaDAO> ocorrenciaDAOCollection;

    public MoradorDAO() {
    }

    public MoradorDAO(Integer idmorador) {
        this.idmorador = idmorador;
    }

    public MoradorDAO(Integer idmorador, String cpf) {
        this.idmorador = idmorador;
        this.cpf = cpf;
    }

    public Integer getIdmorador() {
        return idmorador;
    }

    public void setIdmorador(Integer idmorador) {
        this.idmorador = idmorador;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFatura() {
        return fatura;
    }

    public void setFatura(String fatura) {
        this.fatura = fatura;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public String getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(String datanasc) {
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

    public ResidenciaDAO getIdresidencia() {
        return idresidencia;
    }

    public void setIdresidencia(ResidenciaDAO idresidencia) {
        this.idresidencia = idresidencia;
    }

    public UsuarioDAO getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(UsuarioDAO idusuario) {
        this.idusuario = idusuario;
    }

    @XmlTransient
    public Collection<OcorrenciaDAO> getOcorrenciaDAOCollection() {
        return ocorrenciaDAOCollection;
    }

    public void setOcorrenciaDAOCollection(Collection<OcorrenciaDAO> ocorrenciaDAOCollection) {
        this.ocorrenciaDAOCollection = ocorrenciaDAOCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmorador != null ? idmorador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MoradorDAO)) {
            return false;
        }
        MoradorDAO other = (MoradorDAO) object;
        if ((this.idmorador == null && other.idmorador != null) || (this.idmorador != null && !this.idmorador.equals(other.idmorador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.sgor.dao.MoradorDAO[ idmorador=" + idmorador + " ]";
    }

}
