package montecarlo;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Par implements Serializable {

    private final Double x;
    private final Double y;

    public Par(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Boolean estaCircunferenciaUnidade() {
        return (x*x + y*y <= 1);
    }

    public static Par xerarParAleatorio(Random r) {
        return new Par(r.nextDouble(), r.nextDouble());
    }
}
