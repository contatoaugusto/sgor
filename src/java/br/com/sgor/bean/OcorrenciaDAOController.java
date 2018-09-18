package br.com.sgor.bean;

import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.AdministradorDAO;
import br.com.sgor.dao.GuardaDAO;
import br.com.sgor.bean.util.JsfUtil;
import br.com.sgor.bean.util.PaginationHelper;
import br.com.sgor.dao.ResidenciaDAO;
import br.com.sgor.facade.OcorrenciaDAOFacade;
import br.com.sgor.facade.ResidenciaDAOFacade;
import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.AdministradorDAOFacade;
import br.com.sgor.facade.GuardaDAOFacade;
import br.com.sgor.facade.MoradorDAOFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * *
 * Os status de ocorrência podem ser Em Aberto Aceito Recusado Finalizado
 *
 * @author prohgy
 */
@Named("ocorrenciaDAOController")
@SessionScoped
public class OcorrenciaDAOController implements Serializable {

    private OcorrenciaDAO current;
    private MoradorDAO currentMorador;
    private AdministradorDAO currentAdministrador;
    private GuardaDAO currentGuarda;

    private DataModel items = null;
    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    @EJB
    private ResidenciaDAOFacade ejbFacadeResidencia;
    private ResidenciaDAO residencia;
    private Integer idResidencia;

    @EJB
    private MoradorDAOFacade ejbFacadeMorador;
    @EJB
    private AdministradorDAOFacade ejbFacadeAdministrador;
    @EJB
    private GuardaDAOFacade ejbFacadeGuarda;

    public OcorrenciaDAOController() {
    }

    @PostConstruct
    public void init() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        UsuarioDAO usuario = (UsuarioDAO) attr.getRequest().getSession().getAttribute("usuario");

        setCurrentMorador(null);
        setCurrentAdministrador(null);
        setCurrentGuarda(null);

        // Procura os perfis e configura a tela de ocorrências e monta a listagem de acordo
        if (usuario.getIdperfil().getNmperfil().equalsIgnoreCase("Morador")) {
            // Carregar os dados do morador logado
            setCurrentMorador(ejbFacadeMorador.findByUsuario(usuario));
            setResidencia(ejbFacadeResidencia.find(getCurrentMorador().getIdresidencia().getIdresidencia()));
        } else if (usuario.getIdperfil().getNmperfil().equalsIgnoreCase("Administrador")) {
            setCurrentAdministrador(ejbFacadeAdministrador.findByUsuario(usuario));
        } else if (usuario.getIdperfil().getNmperfil().equalsIgnoreCase("Guarda")) {
            setCurrentGuarda(ejbFacadeGuarda.findByUsuario(usuario));
        }
    }

    public OcorrenciaDAO getSelected() {
        if (current == null) {
            current = new OcorrenciaDAO();
            selectedItemIndex = -1;
        }
        return current;
    }

    private OcorrenciaDAOFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    if (currentMorador != null) {
                        return new ListDataModel(getFacade().findByMorador(currentMorador));
                    } else if (currentAdministrador != null) {
                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                    } else if (currentGuarda != null) {
                        return new ListDataModel(getFacade().findByMorador(currentMorador));
                    } else {
                        return new ListDataModel(null);
                    }
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (OcorrenciaDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new OcorrenciaDAO();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setIdmorador(currentMorador);
            current.setStatus("Em Aberto");
            current.setData(new Date());

            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (OcorrenciaDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String destroy() {
        current = (OcorrenciaDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OcorrenciaDAODeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public OcorrenciaDAO getOcorrenciaDAO(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = OcorrenciaDAO.class)
    public static class OcorrenciaDAOControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OcorrenciaDAOController controller = (OcorrenciaDAOController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ocorrenciaDAOController");
            return controller.getOcorrenciaDAO(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof OcorrenciaDAO) {
                OcorrenciaDAO o = (OcorrenciaDAO) object;
                return getStringKey(o.getIdocorrencia());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + OcorrenciaDAO.class.getName());
            }
        }

    }

    /**
     * @return the residencia
     */
    public ResidenciaDAO getResidencia() {
        return residencia;
    }

    /**
     * @param residencia the residencia to set
     */
    public void setResidencia(ResidenciaDAO residencia) {
        this.residencia = residencia;
    }

    /**
     * @return the idResidencia
     */
    public Integer getIdResidencia() {
        return idResidencia;
    }

    /**
     * @param idResidencia the idResidencia to set
     */
    public void setIdResidencia(Integer idResidencia) {
        this.idResidencia = idResidencia;
    }

    /**
     * @return the currentMorador
     */
    public MoradorDAO getCurrentMorador() {
        return currentMorador;
    }

    /**
     * @param currentMorador the currentMorador to set
     */
    public void setCurrentMorador(MoradorDAO currentMorador) {
        this.currentMorador = currentMorador;
    }

    /**
     * @return the currentAdministrador
     */
    public AdministradorDAO getCurrentAdministrador() {
        return currentAdministrador;
    }

    /**
     * @param currentAdministrador the currentAdministrador to set
     */
    public void setCurrentAdministrador(AdministradorDAO currentAdministrador) {
        this.currentAdministrador = currentAdministrador;
    }

    /**
     * @return the currentGuarda
     */
    public GuardaDAO getCurrentGuarda() {
        return currentGuarda;
    }

    /**
     * @param currentGuarda the currentGuarda to set
     */
    public void setCurrentGuarda(GuardaDAO currentGuarda) {
        this.currentGuarda = currentGuarda;
    }
}
