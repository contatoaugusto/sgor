package br.com.sgor.controleacesso.manager;

import br.com.sgor.bean.util.JsfUtil;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.UsuarioDAOFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Essa classe faz a implementação de autenticação, o que permite acessar
 * detalhes do usuario existente no banco de dados e se o usuario e senha não
 * são iguais. Entre outras coisas dispara uma, {@link BadCredentialsException}
 *
 * @author Antonio Augusto
 *
 */
public class CustomAuthenticationManager implements AuthenticationManager {

    private static final Log log = LogFactory
            .getLog(CustomAuthenticationManager.class);

    private UsuarioDAO usuarioDao = new UsuarioDAO();
    //private Fornecedor current;

    @EJB
    private UsuarioDAOFacade ejbFacade;

    private ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        log.debug("Na classe CustomAuthenticationManager. Iniciando o m�todo authenticate()");

        UsuarioDAO usuario = null;

        try {

            ejbFacade = new UsuarioDAOFacade();
            //UsuarioJpaController usuarioJpa = UsuarioJpaController().;
            usuario = ejbFacade.findUsuarioByName(auth.getName());
            //usuario = usuarioJpa.findUsuarioByName(auth.getName());

            if (usuario == null) {
                String mensagem = "Usuário " + auth.getName() + " não encontrado!";
                //JsfUtil.addSuccessMessage(mensagem);
                log.error(mensagem);
                throw new BadCredentialsException(mensagem);
            }
        } catch (NoResultException e) {
            //JsfUtil.addSuccessMessage(e.getMessage());
            log.error(e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            JsfUtil.addSuccessMessage(e.getMessage());
            log.error(e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }

        // Comparar senhas
        // Assegura que decodifica a senha antes de comparar
//        if (passwordEncoder.isPasswordValid(usuario.getDeSenha(),
//                        (String) auth.getCredentials(), null) == false) {
        if (usuario.getDeSenha().equals((String) auth.getCredentials())) {

            // Here's the main logic of this custom authentication manager
            // Username and password must be the same to authenticate
//		if (!auth.getName().equals(auth.getCredentials())) {
//			log.debug("O usu�rio informado ou a senha incorretos!");
//			throw new BadCredentialsException("O usu�rio informado ou a senha incorretos!");
//		} else {

            log.debug("Todos os dados do usuário estão corretos e pronto pra prosseguir");

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            attr.getRequest().getSession(true);
            attr.getRequest().getSession().setAttribute("usuario", usuario);
            
            // Coloca as informações do usuário em sessão do Spring para que seja lido no xml de configuração de segurança 
            // Será o valor de que será validado em na chave do XML intercept-url
            return new UsernamePasswordAuthenticationToken(auth.getName(),
                    auth.getCredentials(), getAuthoritiesByUser(usuario));
//		}
        } else {
            log.error("Senha Iconrreta!");
            throw new BadCredentialsException("Senha Incorreta!");
        }
    }

    /**
     * Retorna tipo ROLE dependendo do n�vel de acesso, o qual � definido por um
     * Integer.
     *
     * @param access an integer value representing the access of the user
     * @return collection of granted authorities
     */
    public Collection<GrantedAuthority> getAuthorities(Integer access) {
        // Create a list of grants for this user
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(1);

        // All users are granted with ROLE_USER access
        // Therefore this user gets a ROLE_USER by default
        // logger.debug("Grant ROLE_USER to this user");
        // Check if this user has admin access
        // We interpret Integer(1) as an admin user
        if (access.compareTo(1) == 0) {
            log.debug("Dado ACESSO_PERMITIDO ao usuario.");
            authList.add(new GrantedAuthorityImpl("ACESSO_PERMITIDO"));
        } else {
            log.debug("Dado ACESSO_NEGADO ao usuario.");
            authList.add(new GrantedAuthorityImpl("ACESSO_NEGADO"));
        }
        // Return list of granted authorities
        return authList;
    }

    /**
     * A partir de um link, ou recurso verifica se está configurado e adiciona a
     * permissão de acesso.
     *
     * @param linkrecurso
     * @return
     */
    public Collection<GrantedAuthority> getAuthorities(String linkrecurso) {
        // Create a list of grants for this user
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(1);

        log.debug("Dado ACESSO_PERMITIDO ao usuario.");
        authList.add(new GrantedAuthorityImpl(linkrecurso.replace('/', ' ')
                .trim()));

        // Return granted authoritie
        return authList;
    }

    /**
     * Preenche lista de autoriza��es conforme recursos a que o usuario tenha
     * acesso. Retorna
     *
     * @param username
     * @return Collection<GrantedAuthority>
     */
//	public Collection<GrantedAuthority> getAuthoritiesByUser(ControleAcessoUsuario usuario) {
//      public Collection<GrantedAuthority> getAuthoritiesByUser() {
    public Collection<GrantedAuthority> getAuthoritiesByUser(UsuarioDAO usuario) {

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new GrantedAuthorityImpl(usuario.getIdperfil().getNmperfil()));
//              authList.add(new GrantedAuthorityImpl("index.xhtml"));
//		for (ControleAcessoPerfil perfil : usuario.getTbperfils()) {
//			for (ControleAcessoRecurso recurso : perfil.getTbrecursos()) {
//				authList.add(new GrantedAuthorityImpl(recurso.getLkLink()
//						.replace('/', ' ').trim()));
//			}
//		}
        log.debug("Dado ACESSO_PERMITIDO ao usuario.");

        // Return granted authoritie
        return authList;
    }

}
