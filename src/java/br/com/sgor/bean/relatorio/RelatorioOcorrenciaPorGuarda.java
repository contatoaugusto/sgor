package br.com.sgor.bean.relatorio;

import br.com.sgor.dao.GuardaDAO;
import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.OcorrenciaDAOFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.ElementCollection;
import javax.persistence.OrderBy;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
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
    private BarChartModel relProjecaoProximaSemana;

    UsuarioDAO usuario;
    String nomePerfil;

    public BarChartModel getRelOcorrenciaPorGuarda() {
        return relOcorrenciaPorGuarda;
    }

    public BarChartModel getRelOcorrenciaPorGuardaMensal() {
        return relOcorrenciaPorGuardaMensal;
    }

    public BarChartModel getRelProjecaoProximaSemana() {
        return relProjecaoProximaSemana;
    }

    @ElementCollection
    @OrderBy("OcorrenciaDAO.Idguarda.nome")
    private List<OcorrenciaDAO> ocorrencias;

    private String titleGraficoPorGuardaSemanal = "Suas Ocorrências";

    @PostConstruct
    public void init() {

        relOcorrenciaPorGuarda = new BarChartModel();
        relOcorrenciaPorGuardaMensal = new BarChartModel();
        relProjecaoProximaSemana = new BarChartModel();

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        usuario = (UsuarioDAO) attr.getRequest().getSession().getAttribute("usuario");

        if (usuario != null) {
            nomePerfil = usuario.getIdperfil().getNmperfil();

            if (nomePerfil.equalsIgnoreCase("Administrador")) {
                titleGraficoPorGuardaSemanal = "Ocorrências Mensal por Guarda";
                emitirOcorrenciaPorGuarda();
                emitirOcorrenciaPorGuardaMensal();
                emitirProjecaoProximaSemana();
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

        ocorrencias = ocorrenciasList();

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
        List<String> nomesGuardas = new ArrayList<String>();
        String nmGuarda = "";
        for (OcorrenciaDAO item : ocorrencias) {
            if (item.getIdguarda() != null) {
                nmGuarda = item.getIdguarda().getNome();

                if (!nomesGuardas.contains(nmGuarda)) {
                    nomesGuardas.add(nmGuarda);
                }

                int mes = new DateTime(item.getData().getTime()).getMonthOfYear();
                String mesExtensoGuarda = meses.get(mes) + ":" + nmGuarda;
                if (mesOcorrencia.containsKey(mesExtensoGuarda)) {
                    mesOcorrencia.put(mesExtensoGuarda, mesOcorrencia.get(mesExtensoGuarda) + 1);
                } else {
                    mesOcorrencia.put(mesExtensoGuarda, 1);
                }
            }
        }

        // Ajuste para que sempre todos os guardas encontrados tenha ocorrência em todos os meses de ocorrências.
        // Por exemplo, se algum guarda tiver ocorrência em Abril e um guarda X não, deve criar uma entrada 0 para este guarda ()
        HashMap<String, Integer> mesOcorrenciaTemp = (HashMap<String, Integer>) mesOcorrencia.clone();
        for (Map.Entry pairGuardaMes : mesOcorrenciaTemp.entrySet()) {
            String mes = pairGuardaMes.getKey().toString().split(":")[0];

            for (Iterator<String> iterator = nomesGuardas.iterator(); iterator.hasNext();) {
                String guarda = iterator.next();
                if (!mesOcorrencia.containsKey(mes + ":" + guarda)) {
                    mesOcorrencia.put(mes + ":" + guarda, 0);
                }
            }
        }

        relOcorrenciaPorGuardaMensal = new BarChartModel();

        for (Map.Entry pairGuarda : mesOcorrencia.entrySet()) {

            String nomeGuardaInterno = pairGuarda.getKey().toString().split(":")[1];

            // Se já incluiu a série pra esse nome então não precisa mais
            boolean incluirSerie = true;
            for (ChartSeries serie : relOcorrenciaPorGuardaMensal.getSeries()) {
                if (serie.getLabel().equals(nomeGuardaInterno)) {
                    incluirSerie = false;
                    break;
                }
            }

            if (incluirSerie) {

                HashMap<String, Integer> mesOcorrenciaFiltrado = new HashMap<String, Integer>();

                // Montar as ocorrências desse guarda selecionado acima
                for (Map.Entry pairGuardaIteratorInterno : mesOcorrencia.entrySet()) {
                    if (pairGuardaIteratorInterno.getKey().toString().split(":")[1].equals(nomeGuardaInterno)) {
                        mesOcorrenciaFiltrado.put(pairGuardaIteratorInterno.getKey().toString().split(":")[0], (Integer) pairGuardaIteratorInterno.getValue());
                    }
                }

                if (mesOcorrenciaFiltrado.size() > 0) {
                    ChartSeries ocorrenciasSeries = new ChartSeries();
                    ocorrenciasSeries.setLabel(nomeGuardaInterno);
                    for (Map.Entry pair : mesOcorrenciaFiltrado.entrySet()) {
                        ocorrenciasSeries.set(pair.getKey(), (Number) pair.getValue());
                    }

                    relOcorrenciaPorGuardaMensal.addSeries(ocorrenciasSeries);
                }
            }
        }
        relOcorrenciaPorGuardaMensal.setTitle(titleGraficoPorGuardaSemanal);
        relOcorrenciaPorGuardaMensal.setLegendPosition("ne");

        Axis xAxis = relOcorrenciaPorGuardaMensal.getAxis(AxisType.X);
        xAxis.setLabel("Mês");

        Axis yAxis = relOcorrenciaPorGuardaMensal.getAxis(AxisType.Y);
        yAxis.setLabel("Ocorrência");
        yAxis.setMin(0);
        yAxis.setMax(20);
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

    /**
     * Monta o relatório por guarda da semana passada, semana atual e a semana
     * seguinte
     */
    public void emitirProjecaoProximaSemana() {

        HashMap<String, Integer> semanaPassada = new HashMap<String, Integer>();

        LocalDate newDate = new LocalDate();
        int diaSemanaHoje = newDate.getDayOfWeek();
        
        LocalDate semanaPassadaDataInicio = newDate.minusDays((diaSemanaHoje - (diaSemanaHoje - 1)+ 7));
        LocalDate semanaPassadaDataFim = newDate.minusDays((diaSemanaHoje - (diaSemanaHoje - 1)+ 1));
        
        LocalDate semanaAtualDataInicio = newDate.minusDays(diaSemanaHoje - (diaSemanaHoje - 1));
        LocalDate semanaAtualDataFim = newDate.plusDays(7 - diaSemanaHoje);
        
        SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy");
        
        
        List<OcorrenciaDAO> ocorrenciasSemanaPassada = ejbFacade.findByDataInicoFim(semanaPassadaDataInicio.toDate(), semanaPassadaDataFim.toDate());
        List<OcorrenciaDAO> ocorrenciasSemanaAtual = ejbFacade.findByDataInicoFim  (semanaAtualDataInicio.toDate(), semanaAtualDataFim.toDate());
        String semanaPassadaText = "Semana Passada ";
        String semanaAtualText = "Semana Atual";
        String semanaSeguinteText = "Semana Seguinte";
        
        ChartSeries semanaPassadaSeries = new ChartSeries();
        semanaPassadaSeries.setLabel(semanaPassadaText);
        semanaPassadaSeries.set(semanaPassadaText, ocorrenciasSemanaPassada.size());
        semanaPassadaSeries.set(semanaAtualText, 0);
        semanaPassadaSeries.set(semanaSeguinteText, 0);
        relProjecaoProximaSemana.addSeries(semanaPassadaSeries);

        ChartSeries semanaAtualSeries = new ChartSeries();
        semanaAtualSeries.setLabel(semanaAtualText);
        semanaAtualSeries.set(semanaPassadaText, 0);
        semanaAtualSeries.set(semanaAtualText, ocorrenciasSemanaAtual.size());
        semanaAtualSeries.set(semanaSeguinteText, 0);
        relProjecaoProximaSemana.addSeries(semanaAtualSeries);

        int qtdeSemanaPassada = ocorrenciasSemanaPassada.isEmpty() ? 1 : ocorrenciasSemanaPassada.size();
        int qtdeSemanaAtual = ocorrenciasSemanaAtual.isEmpty() ? 1 : ocorrenciasSemanaAtual.size();
        int mediaSemanaSeguinte = (qtdeSemanaPassada + qtdeSemanaAtual) / 2;

        ChartSeries semanaSequinteSeries = new ChartSeries();
        semanaSequinteSeries.setLabel(semanaSeguinteText);
        semanaSequinteSeries.set(semanaPassadaText, 0);
        semanaSequinteSeries.set(semanaAtualText, 0);
        semanaSequinteSeries.set(semanaSeguinteText, mediaSemanaSeguinte);
        relProjecaoProximaSemana.addSeries(semanaSequinteSeries);

        relProjecaoProximaSemana.setTitle("Ocorrências esperadas pra semana seguinte");
        relProjecaoProximaSemana.setLegendPosition("ne");

        Axis xAxis = relProjecaoProximaSemana.getAxis(AxisType.X);
        xAxis.setLabel("");

        Axis yAxis = relProjecaoProximaSemana.getAxis(AxisType.Y);
        yAxis.setLabel("Ocorrências");
        yAxis.setMin(0);
        yAxis.setMax(20);
    }
}
