package br.com.sgor.bean.relatorio;

import br.com.sgor.dao.OcorrenciaDAO;
import br.com.sgor.facade.OcorrenciaDAOFacade;
import java.io.IOException;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author prohgy
 */
@ManagedBean
public class RelatorioOcorrenciaPorGuarda implements Serializable {

    @EJB
    private br.com.sgor.facade.OcorrenciaDAOFacade ejbFacade;

    private CartesianChartModel relatorioModel;

    public OcorrenciaDAOFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(OcorrenciaDAOFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public CartesianChartModel getRelatorioModel() {
        return relatorioModel;
    }

    public void setRelatorioModel(CartesianChartModel relatorioModel) {
        this.relatorioModel = relatorioModel;
    }

    public void emitir() throws IOException {

        ChartSeries ocorrenciaPorGuarda = new ChartSeries();
        relatorioModel = new CartesianChartModel();

        List<OcorrenciaDAO> ocorrencias = ejbFacade.findAll();

        //finalizados.set(mesAnoAuxiliar, quantidadeDeTratamentosFinalizadosPorMes);
        //        ChartSeries boys = new ChartSeries();
        //        boys.setLabel("Boys");
        //        boys.set("2004", 120);
        //        boys.set("2005", 100);
        //        boys.set("2006", 44);
        //        boys.set("2007", 150);
        //        boys.set("2008", 25);
        //        model.addSeries(boys);
    }
}
