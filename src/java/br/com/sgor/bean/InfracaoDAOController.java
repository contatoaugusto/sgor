package br.com.sgor.bean;

import br.com.sgor.dao.InfracaoDAO;
import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.InfracaoNivelDAO;
import br.com.sgor.dao.AdministradorDAO;
import br.com.sgor.bean.util.JsfUtil;
import br.com.sgor.bean.util.PaginationHelper;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.InfracaoDAOFacade;
import br.com.sgor.facade.InfracaoNivelDAOFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
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
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedProperty;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Named("infracaoDAOController")
@SessionScoped
public class InfracaoDAOController implements Serializable {

    private InfracaoDAO current;
    private DataModel items = null;
    @EJB
    private br.com.sgor.facade.InfracaoDAOFacade ejbFacade;
    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacadeOcorrencia;
    @EJB
    private br.com.sgor.facade.AdministradorDAOFacade ejbFacadeAdministrador;

    // Nivel Infração
    @EJB
    private InfracaoNivelDAOFacade ejbFacadeInfracaoNivel;
//    @EJB
//    private InfracaoDAOFacade ejbFacadeInfracao;
    private List<InfracaoNivelDAO> infracaoNivelList;
    private Integer idNivelInfracao;
    private String deInfracao;

    private PaginationHelper pagination;
    private int selectedItemIndex;

    @ManagedProperty(value = "#{ocorrenciaDAOController}")
    OcorrenciaDAOController currentOcorrencia;

    public InfracaoDAOController() {
    }

    public InfracaoDAO getSelected() {
        infracaoNivelList = ejbFacadeInfracaoNivel.findAll();
        if (current == null) {
            current = new InfracaoDAO();
            //current.setIdinfracaoNivel(new InfracaoNivelDAO());
            selectedItemIndex = -1;
        }
        return current;
    }

    private InfracaoDAOFacade getFacade() {
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
        return "List";
    }

    public String prepareView() {
        current = (InfracaoDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new InfracaoDAO();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InfracaoDAOCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String atribuirInfracao() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            OcorrenciaDAO ocorrencia = new OcorrenciaDAO();
            ocorrencia = (OcorrenciaDAO) attr.getRequest().getSession().getAttribute("Ocorrencia");
                    
            // Exclui a infracao anterior, se existir
            InfracaoDAO infracao = ejbFacade.findByOcorrencia(ocorrencia);
            if (infracao != null)
                ejbFacade.remove(infracao);
            
            // Recuperar a ocorrência
            current.setIdocorrencia(ocorrencia);

            // Recuperar o administrador logado no sistema
            UsuarioDAO usuario = (UsuarioDAO) attr.getRequest().getSession().getAttribute("usuario");
            current.setCpf(ejbFacadeAdministrador.findByUsuario(usuario));

            //ejbFacadeInfracao.create(current);
            ejbFacade.create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public void setInfracaoCurrentByOcorrencia(OcorrenciaDAO ocorrencia) {
        current =  ejbFacade.findByOcorrencia(ocorrencia);
    }

    public String prepareEdit() {
        current = (InfracaoDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InfracaoDAOUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (InfracaoDAO) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InfracaoDAODeleted"));
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

    public InfracaoDAO getInfracaoDAO(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = InfracaoDAO.class)
    public static class InfracaoDAOControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InfracaoDAOController controller = (InfracaoDAOController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "infracaoDAOController");
            return controller.getInfracaoDAO(getKey(value));
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
            if (object instanceof InfracaoDAO) {
                InfracaoDAO o = (InfracaoDAO) object;
                return getStringKey(o.getIdinfracao());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + InfracaoDAO.class.getName());
            }
        }

    }

    /**
     * @return the infracaoNivel
     */
    public List<InfracaoNivelDAO> getInfracaoNivelList() {
        return infracaoNivelList;
    }

    /**
     * @param infracaoNivelList the infracaoNivel to set
     */
    public void setInfracaoNivel(List<InfracaoNivelDAO> infracaoNivelList) {
        this.infracaoNivelList = infracaoNivelList;
    }

    /**
     * @return the idNivelInfracao
     */
    public Integer getIdNivelInfracao() {
        return idNivelInfracao;
    }

    /**
     * @param idNivelInfracao the idNivelInfracao to set
     */
    public void setIdNivelInfracao(Integer idNivelInfracao) {
        this.idNivelInfracao = idNivelInfracao;
    }

    /**
     * @return the idNivelInfracao
     */
    public String getDeInfracao() {
        return deInfracao;
    }

    public void setDeInfracao(String deInfracao) {
        this.deInfracao = deInfracao;
    }
}
