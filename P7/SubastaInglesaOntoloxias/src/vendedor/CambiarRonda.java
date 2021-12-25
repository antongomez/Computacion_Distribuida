package vendedor;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import ontoloxiaSubasta.Subasta;

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
                comportamento.getSubasta().setEstado(Subasta.ESTADO_2);
                comportamento.actualizarSubasta();
            } else {
                comportamento.setStep(2);
            }
            comportamento.engadirAxentesRonda();
            comportamento.getAxentesRonda().clear();

        } else {
            if (comportamento.getRonda() > 0) {
                comportamento.getSubasta().setEstado(Subasta.ESTADO_2);
                comportamento.getSubasta().setPrezo(comportamento.getSubasta().getPrezo()- comportamento.getSubasta().getIncremento());
                comportamento.actualizarSubasta();

                comportamento.setStep(3);
            } else {
                System.out.println(myAgent.getLocalName() + ": pasaron os 10 segundos e non hai compradores interesados");
                comportamento.setStep(0);
            }

        }

    }

}
