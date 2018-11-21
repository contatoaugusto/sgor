package br.com.sgor.bean.relatorio;

import br.com.sgor.dao.OcorrenciaDAO;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author prohgy
 */
@Named("relatorioAcaoPreventiva")
@RequestScoped
public class RelatorioAcoesPreventiva implements Serializable {

    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacade;

    private List<OcorrenciaDAO> ocorrencias;

    private String modulo50PercentTexto = "";
    private String modulo20PercentTexto = "";

    @PostConstruct
    public void init() {
        ocorrencias = ejbFacade.findAll();
        moduloOcorrencia50Percent();
        moduloOcorrencia20Percent();

        openDialog();
    }

    public void openDialog() {

        RequestContext requestContext = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap<>();
        options.put("modal", false);
        options.put("height", 100);

        if (!modulo50PercentTexto.isEmpty()) {
//            //requestContext.openDialog("/dlgAlertasPreventiva");
//            PrimeFaces current = PrimeFaces.current();
//            current.executeScript("PF('dlgAlertasPreventivaVar').show();");
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.execute("PF('dlgAlertasPreventivaVar').show()");

            FacesContext.getCurrentInstance().addMessage("msgAcoesPreventivas", new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção!", modulo50PercentTexto));

//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");
//            PrimeFaces.current().dialog().showMessageDynamic(message);
        }
        if (!modulo20PercentTexto.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("msgAcoesPreventivas2", new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção!", modulo20PercentTexto));
        }
    }

    /**
     * Verifica o modulo com mais ocorrencia e emite alerta, envia mensagens,
     * toca sirenes, etc
     */
    public void moduloOcorrencia50Percent() {

        HashMap<String, Integer> modulos = listOcorrenciasByModulo();

        //O sistema exibe a mensagem “Atenção os módulos X e Y obtiveram acima de 50% das ocorrências do mês recomenda-se 3 rondas a cada 1 hora durante Z dias
        int totalOcorrencias = ocorrencias.size();
        int count = 0;
        int quantidadePercentOcorrenciaModulo = 0;
        for (Map.Entry<String, Integer> entry : modulos.entrySet()) {
            // Cálcula percentual
            quantidadePercentOcorrenciaModulo = (50 * totalOcorrencias) / 100;
            if (entry.getValue() >= quantidadePercentOcorrenciaModulo) {
                modulo50PercentTexto = modulo50PercentTexto.isEmpty() ? entry.getKey() : " e " + entry.getKey();
                count++;
            }
        }
        if (!modulo50PercentTexto.isEmpty()) {
            modulo50PercentTexto = "Atingiu 50% das ocorrências. Recomenda-se 3 rondas a cada 1 hora. Módulo" + (count > 1 ? "s" : "") + ": " + modulo50PercentTexto;
        }
    }

    /**
     * Verifica o modulo com mais ocorrencia e emite alerta, envia mensagens,
     * toca sirenes, etc
     */
    public void moduloOcorrencia20Percent() {

        HashMap<String, Integer> modulos = listOcorrenciasByModulo();

        // O sistema deve sugerir rotas passando por módulos com maior quantidade de ocorrências Caso um módulo ultrapasse 20% das ocorrências semanais , exibir uma alerta indicando um sugestão de ronda passando pelo módulo a contar 7 dias da data do relatório
        int totalOcorrencias = ocorrencias.size();
        int count = 0;
        int quantidadePercentOcorrenciaModulo = 0;
        for (Map.Entry<String, Integer> entry : modulos.entrySet()) {
            // Cálcula percentual
            quantidadePercentOcorrenciaModulo = (20 * totalOcorrencias) / 100;
            if (entry.getValue() >= quantidadePercentOcorrenciaModulo) {
                modulo20PercentTexto = modulo20PercentTexto.isEmpty() ? entry.getKey() : " e " + entry.getKey();
                count++;
            }
        }
        if (!modulo20PercentTexto.isEmpty()) {
            modulo20PercentTexto = "Rotas sugeridas, atingiu 20% das ocorrências. Módulo" + (count > 1 ? "s" : "") + ": " + modulo20PercentTexto;
        }

        // O sistema exibe se houve uma diminuição das ocorrências naqueles módulos após as rondas preventivas.
    }

    private HashMap<String, Integer> listOcorrenciasByModulo() {
        HashMap<String, Integer> modulos = new HashMap<String, Integer>();
        modulos.put("A", 0);
        modulos.put("B", 0);
        modulos.put("C", 0);
        modulos.put("D", 0);
        modulos.put("E", 0);
        modulos.put("F", 0);
        modulos.put("G", 0);
        modulos.put("H", 0);
        modulos.put("I", 0);
        modulos.put("J", 0);
        modulos.put("L", 0);
        modulos.put("M", 0);

        // Ocorrencias por módulo
        for (OcorrenciaDAO item : ocorrencias) {
            String modulo = item.getIdmorador().getIdresidencia().getModulo();
            if (modulos.containsKey(modulo)) {
                modulos.put(modulo, modulos.get(modulo) + 1);
            } else {
                modulos.put(modulo, 1);
            }
        }

        return modulos;
    }

    public String getModulo50PercentTexto() {
        return modulo50PercentTexto;
    }

    public String getModulo20PercentTexto() {
        return modulo20PercentTexto;
    }
}
