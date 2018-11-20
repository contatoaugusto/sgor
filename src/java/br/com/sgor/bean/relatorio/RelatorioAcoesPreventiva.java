package br.com.sgor.bean.relatorio;

import br.com.sgor.dao.GuardaDAO;
import br.com.sgor.dao.MoradorDAO;
import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.dao.UsuarioDAO;
import br.com.sgor.facade.OcorrenciaDAOFacade;
import java.io.Serializable;
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
@Named("relatorioAcaoPreventiva")
@RequestScoped
public class RelatorioAcoesPreventiva implements Serializable {

    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacade;

    /**
     * Verifica o modulo com mais ocorrencia e emite alerta, envia mensagens,
     * toca sirenes, etc
     */
    public void moduloOcorrencia() {

       HashMap<String, Integer> modulos = listOcorrenciasByModulo();

       
       O sistema exibe a mensagem “Atenção os módulos X e Y obtiveram acima de 50% das ocorrências do mês recomenda-se 3 rondas a cada 1 hora durante Z dias.
       
       O sistema exibe se houve uma diminuição das ocorrências naqueles módulos após as rondas preventivas.
               
        O sistema deve sugerir rotas passando por módulos com maior quantidade de ocorrências
        Caso um módulo ultrapasse 20% das ocorrências semanais, exibir uma alerta indicando um sugestão de ronda passando pelo módulo a contar 7 dias da data do relatório
       
    relOcorrenciaPorGuarda  = new BarChartModel();

    ChartSeries ocorrenciasSeries = new ChartSeries();

    ocorrenciasSeries.setLabel (
    "Guarda");
    for (Map.Entry pair

    : guardaOcorrencia.entrySet () 
        ) {
            ocorrenciasSeries.set(pair.getKey().toString(), (Number) pair.getValue());
    }

    relOcorrenciaPorGuarda.addSeries (ocorrenciasSeries);

    relOcorrenciaPorGuarda.setTitle (

    "Ocorrência por guarda");
    relOcorrenciaPorGuarda.setLegendPosition (
    "ne");

        Axis xAxis = relOcorrenciaPorGuarda.getAxis(AxisType.X);

    xAxis.setLabel (
    "");

        Axis yAxis = relOcorrenciaPorGuarda.getAxis(AxisType.Y);

    yAxis.setLabel (

    "Ocorrências");
    yAxis.setMin (

    0);
    yAxis.setMax (

20);
    }

    private HashMap<String, Integer> listOcorrenciasByModulo(){
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
        List<OcorrenciaDAO> ocorrencias = ejbFacade.findAll();

        // Ocorrencias por módulo
        for (OcorrenciaDAO item : ocorrencias) {
            String modulo = item.getIdmorador().getIdresidencia().getModulo();
            if (modulos.containsKey(modulo)) {
                modulos.put(modulo, modulos.get(modulo) + 1);
            } else {
                modulos.put(modulo, 1);
            }
        }
    }
}
