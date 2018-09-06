/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sgor.controleacesso.manager;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Antonio Augusto
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    private static final long serialVersionUID = 1029928088340565343L;

    private String rolename;

    public GrantedAuthorityImpl(String rolename) {
        this.rolename = rolename;
    }

    public String getAuthority() {
        return this.rolename;
    }

}
