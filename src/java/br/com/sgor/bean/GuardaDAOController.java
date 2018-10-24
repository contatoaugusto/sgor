package br.com.sgor.bean;

import br.com.sgor.dao.GuardaDAO;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.bean.util.JsfUtil;
import br.com.sgor.bean.util.PaginationHelper;
import br.com.sgor.dao.PerfilDAO;
import br.com.sgor.facade.GuardaDAOFacade;
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
import java.util.List;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Named("guardaDAOController")
@SessionScoped
public class GuardaDAOController implements Serializable {

    @EJB
    private UsuarioDAOFacade usuarioDAOFacade;
    @EJB
    private PerfilDAOFacade ejbFacadePerfil;
    @EJB
    private br.com.sgor.facade.GuardaDAOFacade ejbFacade;
    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacadeOcorrencia;

    private GuardaDAO current;
    // Para que funcione selectOneMenu juntamente com o converter. Melhorar
    private GuardaDAO guarda;
    private DataModel items = null;

    private PaginationHelper pagination;
    private int selectedItemIndex;

    private int idPerfil = 3;
    private String nmUsuario = "";
    private String deSenha = "";

    private List<GuardaDAO> guardaList;
    private Integer idGuarda;

    public GuardaDAOController() {
    }

    @PostConstruct
    public void init() {
        current = new GuardaDAO();
    }

    public GuardaDAO getSelected() {
        guardaList = ejbFacade.findAll();
        if (current == null) {
            current = new GuardaDAO();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GuardaDAOFacade getFacade() {
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
        current = new GuardaDAO();
        return "manterGuarda";
    }

    public String prepareView() {
        current = (GuardaDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "manterGuarda";
    }

    public String prepareCreate() {
        current = new GuardaDAO();
        selectedItemIndex = -1;
        return "manterGuarda";
    }

    public String create() {
        try {
            // Cria o usuario
            UsuarioDAO currentUsuario = new UsuarioDAO();
            currentUsuario = new UsuarioDAO();
            currentUsuario.setNmusuario(getNmUsuario());
            currentUsuario.setDeSenha(getDeSenha());
            // 3 = Perfil Guarda
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

    public String adicionaGuardaOcorrencia() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            // Recuperar a ocorrência
            OcorrenciaDAO currentOcorrencia = new OcorrenciaDAO();
            currentOcorrencia = (OcorrenciaDAO) attr.getRequest().getSession().getAttribute("Ocorrencia");

            //GuardaDAO guarda = ejbFacade.find(current.getIdguarda());
            currentOcorrencia.setIdguarda(guarda);

            ejbFacadeOcorrencia.edit(currentOcorrencia);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (GuardaDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "manterGuarda";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return "manterGuarda";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String destroy() {
        current = (GuardaDAO) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
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

    public GuardaDAO getGuardaDAO(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = GuardaDAO.class, value = "guardaConverter")
    public static class GuardaDAOControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GuardaDAOController controller = (GuardaDAOController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "guardaDAOController");
            return controller.getGuardaDAO(getKey(value));
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
            if (object instanceof GuardaDAO) {
                GuardaDAO o = (GuardaDAO) object;
                return getStringKey(o.getIdguarda());
            } else if (object instanceof Integer) {
                return getStringKey((Integer)object);
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + GuardaDAO.class.getName());
            }
        }

    }

    /**
     * @return the idPerfil
     */
    public int getIdPerfil() {
        return idPerfil;
    }

    /**
     * @param idPerfil the idPerfil to set
     */
    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
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

    /**
     * @return the guardaList
     */
    public List<GuardaDAO> getGuardaList() {
        return guardaList;
    }

    /**
     * @param guardaList the guardaList to set
     */
    public void setGuardaList(List<GuardaDAO> guardaList) {
        this.guardaList = guardaList;
    }

    /**
     * @return the idGuarda
     */
    public Integer getIdGuarda() {
        return idGuarda;
    }

    /**
     * @return the guarda
     */
    public GuardaDAO getGuarda() {
        return guarda;
    }

    /**
     * @param guarda the guarda to set
     */
    public void setGuarda(GuardaDAO guarda) {
        this.guarda = guarda;
    }
}
