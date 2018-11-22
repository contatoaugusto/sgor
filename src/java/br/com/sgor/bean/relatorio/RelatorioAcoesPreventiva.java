package br.com.sgor.bean.relatorio;

import br.com.facilitamovel.bean.Retorno;
import br.com.facilitamovel.bean.SmsSimples;
import br.com.facilitamovel.service.SendMessage;
import br.com.sgor.dao.OcorrenciaDAO;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String nuTelefoneDestino = "";

    @PostConstruct
    public void init() {
        ocorrencias = ejbFacade.findAll();
        moduloOcorrencia50Percent();
        moduloOcorrencia20Percent();

        openDialog();
    }

    public void openDialog() {

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
        int count = 0;
        int quantidadePercentOcorrenciaModulo = 0;

        // Cálcula percentual
        quantidadePercentOcorrenciaModulo = (50 * ocorrencias.size()) / 100;

        for (Map.Entry<String, Integer> entry : modulos.entrySet()) {

            if (entry.getValue() >= quantidadePercentOcorrenciaModulo) {
                modulo50PercentTexto = modulo50PercentTexto.isEmpty() ? entry.getKey() : modulo50PercentTexto + " e " + entry.getKey();
                count++;
            }
        }
        if (!modulo50PercentTexto.isEmpty()) {
            if (count > 1) {
                modulo50PercentTexto = "Os Módulos " + modulo50PercentTexto + " atingiram 50% das ocorrências. Realize 3 rondas por hora em cada um deles";
            } else {
                modulo50PercentTexto = "O Módulo " + modulo50PercentTexto + " atingiu 50% das ocorrências. Realize 3 rondas por hora no módulo";
            }
        }
    }

    /**
     * Verifica o modulo com mais ocorrencia e emite alerta, envia mensagens,
     * toca sirenes, etc
     */
    public void moduloOcorrencia20Percent() {

        HashMap<String, Integer> modulos = listOcorrenciasByModulo();

        // O sistema deve sugerir rotas passando por módulos com maior quantidade de ocorrências Caso um módulo ultrapasse 20% das ocorrências semanais , exibir uma alerta indicando um sugestão de ronda passando pelo módulo a contar 7 dias da data do relatório
        int count = 0;
        int quantidadePercentOcorrenciaModulo = 0;

        // Cálcula percentual
        quantidadePercentOcorrenciaModulo = (20 * ocorrencias.size()) / 100;

        for (Map.Entry<String, Integer> entry : modulos.entrySet()) {

            if (entry.getValue() >= quantidadePercentOcorrenciaModulo) {
                modulo20PercentTexto = modulo20PercentTexto.isEmpty() ? entry.getKey() : modulo20PercentTexto + " e " + entry.getKey();
                count++;
            }
        }
        if (!modulo20PercentTexto.isEmpty()) {
            if (count > 1) {
                modulo20PercentTexto = "Os Módulos " + modulo20PercentTexto + " atingiram 20% das ocorrências. Intensifique as rondas neles";
            } else {
                modulo20PercentTexto = "O Módulo " + modulo20PercentTexto + " atingiu 20% das ocorrências. Intensifique as rondas nele";
            }
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
            String modulo = item.getIdmorador().getIdresidencia().getModulo().toUpperCase();
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

    public String getNuTelefoneDestino() {
        return nuTelefoneDestino;
    }

    public void setNuTelefoneDestino(String nuTelefone) {
        this.nuTelefoneDestino = nuTelefone;
    }

    public String enviaSMS() {
        // Simple Send
        SmsSimples sms = new SmsSimples();
        sms.setUser("prohgy");
        sms.setPassword("kdsoiw1");
        sms.setDestinatario(nuTelefoneDestino.replace(" ","").replace("(","").replace(")","").replace("-",""));
        sms.setMessage(modulo20PercentTexto.replace("õ","o").replace("ó","o").replace("ê","e").replace("ç","c").replace("í","i"));
        //sms.setAno(2015);
        Retorno retorno = new Retorno();
        try {
            retorno = SendMessage.simpleSend(sms);
        } catch (Exception ex) {
            Logger.getLogger(RelatorioAcoesPreventiva.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Codigo:" + retorno.getCodigo());
        System.out.println("Descricao:" + retorno.getMensagem());

        return null;
    }
}
