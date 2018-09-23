package br.com.sgor.bean;

import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.bean.util.JsfUtil;
import br.com.sgor.bean.util.PaginationHelper;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.MoradorDAOFacade;
import br.com.sgor.facade.PerfilDAOFacade;
import br.com.sgor.facade.ResidenciaDAOFacade;
import br.com.sgor.facade.UsuarioDAOFacade;
import br.com.sgor.dao.ResidenciaDAO;

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
import javax.annotation.PostConstruct;

@Named("moradorDAOController")
@SessionScoped
public class MoradorDAOController implements Serializable {

    private MoradorDAO current;
    private DataModel items = null;
    @EJB
    private br.com.sgor.facade.MoradorDAOFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    private int idPerfil = 1;
    private String nmUsuario = "";
    private String deSenha = "";

    @EJB
    private UsuarioDAOFacade usuarioDAOFacade;
    @EJB
    private PerfilDAOFacade ejbFacadePerfil;

    @EJB
    private ResidenciaDAOFacade ejbFacadeResidencia;

    private List<ResidenciaDAO> residencias;
    private Integer idResidencia;

    public MoradorDAOController() {
    }

    @PostConstruct
    public void init() {
        current = new MoradorDAO();
        setNmUsuario("");
        residencias = ejbFacadeResidencia.findAll();
    }

    public MoradorDAO getSelected() {
        if (current == null) {
            current = new MoradorDAO();
            selectedItemIndex = -1;

            residencias = ejbFacadeResidencia.findAll();
        }
        return current;
    }

    private MoradorDAOFacade getFacade() {
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
        current = new MoradorDAO();
        return "manterMorador";
    }

    public String prepareView() {
        current = (MoradorDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "manterMorador";
    }

    public String prepareCreate() {
        current = new MoradorDAO();
        selectedItemIndex = -1;
        return "manterMorador";
    }

    private void usuarioHandle() {
        // Cria o usuario
        UsuarioDAO currentUsuario = new UsuarioDAO();
        currentUsuario = new UsuarioDAO();
        currentUsuario.setNmusuario(getNmUsuario());
        currentUsuario.setDeSenha(getDeSenha());
        // 1 = Perfil Morador
        currentUsuario.setIdperfil(ejbFacadePerfil.find(idPerfil));
        usuarioDAOFacade.create(currentUsuario);
        current.setIdusuario(currentUsuario);
        current.setIdresidencia(ejbFacadeResidencia.find(idResidencia));
    }

    public String createUpdate() {

        usuarioHandle();

        if (current.getIdmorador() == null) {
            return create();
        } else {
            return update();
        }
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));

            return prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (MoradorDAO) getItems().getRowData();
        setNmUsuario(current.getIdusuario().getNmusuario());
        setDeSenha(current.getIdusuario().getDeSenha());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //return "manterMorador?faces-redirect=true";
        return "manterMorador";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OperacaoSucesso"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OperacaoErro"));
            return null;
        }
    }

    public String destroy() {
        current = (MoradorDAO) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "manterMorador";
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
        return "manterMorador";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "manterMorador";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public MoradorDAO getMoradorDAO(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = MoradorDAO.class)
    public static class MoradorDAOControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MoradorDAOController controller = (MoradorDAOController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "moradorDAOController");
            return controller.getMoradorDAO(getKey(value));
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
            if (object instanceof MoradorDAO) {
                MoradorDAO o = (MoradorDAO) object;
                return getStringKey(o.getCpf());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + MoradorDAO.class.getName());
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
     * @return the enederecos
     */
    public List<ResidenciaDAO> getResidencias() {
        return residencias;
    }

    /**
     * @param enederecos the enederecos to set
     */
    public void setResidencias(List<ResidenciaDAO> residencias) {
        this.residencias = residencias;
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

}
