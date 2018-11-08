package br.com.sgor.bean.relatorio;

import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.facade.OcorrenciaDAOFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author prohgy
 */
@Named("relatorioOcorrenciaController")
@RequestScoped
public class RelatorioOcorrenciaPorGuarda implements Serializable {

    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacade;

    private BarChartModel relatorioModel;

    public OcorrenciaDAOFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(OcorrenciaDAOFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public BarChartModel getRelatorioModel() {
        return relatorioModel;
    }

    public void setRelatorioModel(BarChartModel relatorioModel) {
        this.relatorioModel = relatorioModel;
    }

    @PostConstruct
    public void init() {
        emitir();
    }

    public void emitir() {
        List<OcorrenciaDAO> ocorrencias = ejbFacade.findAll();

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
        
        relatorioModel = new BarChartModel();
        
        for (Map.Entry pair : guardaOcorrencia.entrySet()) {
            ChartSeries ocorrenciasSeries = new ChartSeries();
            ocorrenciasSeries.set(pair.getKey(), (Number) pair.getValue());
            ocorrenciasSeries.setLabel(pair.getKey().toString());
            relatorioModel.addSeries(ocorrenciasSeries);
        }
        
        relatorioModel.setTitle("Ocorrência por guarda");
        relatorioModel.setLegendPosition("ne");

        Axis xAxis = relatorioModel.getAxis(AxisType.X);
        xAxis.setLabel("Guardas");

        Axis yAxis = relatorioModel.getAxis(AxisType.Y);
        yAxis.setLabel("Ocorrências");
        yAxis.setMin(0);
        yAxis.setMax(guardaOcorrencia.size() + 2);
       
    }
}
