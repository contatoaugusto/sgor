package br.com.sgor.bean;

import br.com.sgor.dao.AdministradorDAO;
import br.com.sgor.bean.util.JsfUtil;
import br.com.sgor.bean.util.PaginationHelper;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.AdministradorDAOFacade;
import br.com.sgor.facade.PerfilDAOFacade;
import br.com.sgor.facade.UsuarioDAOFacade;

import java.io.Serializable;
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

@Named("administradorDAOController")
@SessionScoped
public class AdministradorDAOController implements Serializable {

    private AdministradorDAO current;
    private DataModel items = null;

    @EJB
    private br.com.sgor.facade.AdministradorDAOFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    @EJB
    private UsuarioDAOFacade usuarioDAOFacade;
    @EJB
    private PerfilDAOFacade ejbFacadePerfil;
    private int idPerfil = 2;
    private String nmUsuario = "";
    private String deSenha = "";

    public AdministradorDAOController() {
    }

    @PostConstruct
    public void init() {
        current = new AdministradorDAO();
    }

    public AdministradorDAO getSelected() {
        if (current == null) {
            current = new AdministradorDAO();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AdministradorDAOFacade getFacade() {
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
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        current = new AdministradorDAO();
        return "manterAdministrador";
    }

    public String prepareView() {
        current = (AdministradorDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "manterAdministrador";
    }

    public String prepareCreate() {
        current = new AdministradorDAO();
        selectedItemIndex = -1;
        return "manterAdministrador";
    }

    public String create() {
        try {
            // Cria o usuario
            UsuarioDAO currentUsuario = new UsuarioDAO();
            currentUsuario = new UsuarioDAO();
            currentUsuario.setNmusuario(getNmUsuario());
            currentUsuario.setDeSenha(getDeSenha());
            // 2 = Administrador
            currentUsuario.setIdperfil(ejbFacadePerfil.find(idPerfil));
            usuarioDAOFacade.create(currentUsuario);
            current.setIdusuario(currentUsuario);

            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (AdministradorDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AdministradorDAOUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (AdministradorDAO) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AdministradorDAODeleted"));
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

    public AdministradorDAO getAdministradorDAO(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = AdministradorDAO.class)
    public static class AdministradorDAOControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AdministradorDAOController controller = (AdministradorDAOController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "administradorDAOController");
            return controller.getAdministradorDAO(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AdministradorDAO) {
                AdministradorDAO o = (AdministradorDAO) object;
                return getStringKey(o.getCpf());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AdministradorDAO.class.getName());
            }
        }

    }

    /**
     * @return the nmUsuario
     */
    public String getNmUsuario() {
        return nmUsuario;
    }

    /**
     * @param nmUsuario the nmUsuario to set
     */
    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    /**
     * @return the deSenha
     */
    public String getDeSenha() {
        return deSenha;
    }

    /**
     * @param deSenha the deSenha to set
     */
    public void setDeSenha(String deSenha) {
        this.deSenha = deSenha;
    }
}
