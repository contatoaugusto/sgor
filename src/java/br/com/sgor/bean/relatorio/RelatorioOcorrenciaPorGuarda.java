package br.com.sgor.bean.relatorio;

import br.com.sgor.dao.GuardaDAO;
import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.OcorrenciaDAOFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author prohgy
 */
@Named("relatorioOcorrenciaController")
@RequestScoped
public class RelatorioOcorrenciaPorGuarda implements Serializable {

    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacade;
    @EJB
    private br.com.sgor.facade.GuardaDAOFacade ejbFacadeGuarda;
    @EJB
    private br.com.sgor.facade.MoradorDAOFacade ejbFacadeMorador;

    private BarChartModel relOcorrenciaPorGuarda;
    private BarChartModel relOcorrenciaPorGuardaMensal;

    UsuarioDAO usuario;
    String nomePerfil;

    public BarChartModel getRelOcorrenciaPorGuarda() {
        return relOcorrenciaPorGuarda;
    }

    public BarChartModel getRelOcorrenciaPorGuardaMensal() {
        return relOcorrenciaPorGuardaMensal;
    }

    @PostConstruct
    public void init() {

        relOcorrenciaPorGuarda = new BarChartModel();
        relOcorrenciaPorGuardaMensal = new BarChartModel();
        
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        usuario = (UsuarioDAO) attr.getRequest().getSession().getAttribute("usuario");

        if (usuario != null) {
            nomePerfil = usuario.getIdperfil().getNmperfil();

            if (nomePerfil.equalsIgnoreCase("Administrador")) {
                emitirOcorrenciaPorGuarda();
            } else if (nomePerfil.equalsIgnoreCase("Guarda")) {
                emitirOcorrenciaPorGuardaMensal();
            } else if (nomePerfil.equalsIgnoreCase("Morador")) {

            }
        }
    }

    /**
     * Monta o relatório por guarda a ser exibido no perfil de administrador
     */
    public void emitirOcorrenciaPorGuarda() {

        List<OcorrenciaDAO> ocorrencias = ocorrenciasList();

        // Graficos por Guarda
        HashMap<String, Integer> guardaOcorrencia = new HashMap<String, Integer>();

        for (OcorrenciaDAO item : ocorrencias) {
            if (item.getIdguarda() != null) {
                String nomeGuarda = item.getIdguarda().getNome();
                if (guardaOcorrencia.containsKey(nomeGuarda)) {
                    guardaOcorrencia.put(nomeGuarda, guardaOcorrencia.get(nomeGuarda) + 1);
                } else {
                    guardaOcorrencia.put(nomeGuarda, 1);
                }
            }
        }

        relOcorrenciaPorGuarda = new BarChartModel();

        ChartSeries ocorrenciasSeries = new ChartSeries();
        ocorrenciasSeries.setLabel("Guarda");
        for (Map.Entry pair : guardaOcorrencia.entrySet()) {
            ocorrenciasSeries.set(pair.getKey().toString(), (Number) pair.getValue());
        }
        relOcorrenciaPorGuarda.addSeries(ocorrenciasSeries);

        relOcorrenciaPorGuarda.setTitle("Ocorrência por guarda");
        relOcorrenciaPorGuarda.setLegendPosition("ne");

        Axis xAxis = relOcorrenciaPorGuarda.getAxis(AxisType.X);
        xAxis.setLabel("");

        Axis yAxis = relOcorrenciaPorGuarda.getAxis(AxisType.Y);
        yAxis.setLabel("Ocorrências");
        yAxis.setMin(0);
        yAxis.setMax(20);
    }

    /**
     * Monta o relatório por guarda a ser exibido no perfil do guarda
     */
    public void emitirOcorrenciaPorGuardaMensal() {

        List<OcorrenciaDAO> ocorrencias = ocorrenciasList();

        HashMap<Integer, String> meses = new HashMap<Integer, String>();
        meses.put(1, "Janeiro");
        meses.put(2, "Fevereiro");
        meses.put(3, "Marco");
        meses.put(4, "Abril");
        meses.put(5, "Maio");
        meses.put(6, "Junho");
        meses.put(7, "Julho");
        meses.put(8, "Agosto");
        meses.put(9, "Setembro");
        meses.put(10, "Outubro");
        meses.put(11, "Novembro");
        meses.put(12, "Dezembro");

        // Graficos por Guarda
        HashMap<String, Integer> mesOcorrencia = new HashMap<String, Integer>();
        String nmGuarda = "";
        for (OcorrenciaDAO item : ocorrencias) {
            if (item.getIdguarda() != null) {
                nmGuarda = item.getIdguarda().getNome();
                int mes = new DateTime(item.getData().getTime()).getMonthOfYear();
                String mesExtenso = meses.get(mes);
                if (mesOcorrencia.containsKey(mesExtenso)) {
                    mesOcorrencia.put(mesExtenso, mesOcorrencia.get(mesExtenso) + 1);
                } else {
                    mesOcorrencia.put(mesExtenso, 1);
                }
            }
        }

        relOcorrenciaPorGuardaMensal = new BarChartModel(); 

        ChartSeries ocorrenciasSeries = new ChartSeries();  
        ocorrenciasSeries.setLabel(nmGuarda);
        for (Map.Entry pair : mesOcorrencia.entrySet()) {
            ocorrenciasSeries.set(pair.getKey().toString(), (Number) pair.getValue());
        }
        relOcorrenciaPorGuardaMensal.addSeries(ocorrenciasSeries);


        relOcorrenciaPorGuardaMensal.setTitle("Suas Ocorrências");
        relOcorrenciaPorGuardaMensal.setLegendPosition("ne");

        Axis xAxis = relOcorrenciaPorGuardaMensal.getAxis(AxisType.X);
        xAxis.setLabel("Mês");

        Axis yAxis = relOcorrenciaPorGuardaMensal.getAxis(AxisType.Y);
        yAxis.setLabel("Ocorrência");
        yAxis.setMin(0);
        yAxis.setMax(mesOcorrencia.size() + 2);
    }

    /**
     * Seleciona a lista das ocorrencias existentes por perfil logado
     *
     * @return List<OcorrenciaDAO>
     */
    private List<OcorrenciaDAO> ocorrenciasList() {

        List<OcorrenciaDAO> ocorrencias = null;

        if (nomePerfil.equalsIgnoreCase("Administrador")) {
            ocorrencias = ejbFacade.findAll();
        } else if (nomePerfil.equalsIgnoreCase("Guarda")) {
            GuardaDAO guarda = ejbFacadeGuarda.findByUsuario(usuario);
            ocorrencias = ejbFacade.findByGuarda(guarda);
        } else if (nomePerfil.equalsIgnoreCase("Morador")) {
            MoradorDAO morador = ejbFacadeMorador.findByUsuario(usuario);
            ocorrencias = ejbFacade.findByMorador(morador);
        }

        return ocorrencias;
    }
}
