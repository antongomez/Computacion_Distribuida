package vendedor;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import main.Subasta;

/**
 *
 * @author User
 */
public class CambiarRonda extends WakerBehaviour {

    private final SubastarLibro comportamento;

    public CambiarRonda(Agent a, long timeout, SubastarLibro comportamento) {
        super(a, timeout);
        this.comportamento = comportamento;
    }

    @Override
    protected void handleElapsedTimeout() {
        if (comportamento.getAxentesRonda().size() > 0) {
            // Se hai un ganhador pasamos ao paso 3
            if (comportamento.getAxentesRonda().size() == 1) {
                comportamento.setStep(3);
                System.out.println(myAgent.getLocalName() + ": nesta ronda so puxou un, Ganha");
                comportamento.getSubasta().setEstado(Subasta.ESTADO_2);
                ((Vendedor) myAgent).getGui().actualizarSubasta(comportamento.getSubasta());
            } else {
                comportamento.setStep(2);
            }
            comportamento.engadirAxentesRonda();
            comportamento.getAxentesRonda().clear();

        } else {
            if (comportamento.getRonda() > 0) {
                System.out.println(myAgent.getLocalName() + ": nesta ronda ninguen puxou, ganha o primeiro da anterior");
                comportamento.getSubasta().setEstado(Subasta.ESTADO_2);
                comportamento.getSubasta().setPrezoActual(comportamento.getSubasta().getPrezoActual() - comportamento.getSubasta().getIncremento());                
                ((Vendedor) myAgent).getGui().actualizarSubasta(comportamento.getSubasta());

                comportamento.setStep(3);
            } else {
                System.out.println(myAgent.getLocalName() + ": pasaron os 10 segundos e NON hai compradores interesados");
                System.out.println(myAgent.getLocalName() + ": volvemos a facer o call for proposal\n");
                comportamento.setStep(0);
            }

        }

    }

}
