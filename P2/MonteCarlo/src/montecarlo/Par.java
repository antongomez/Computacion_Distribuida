package montecarlo;

import java.io.Serializable;

/**
 *
 * @author Anton Gomez Lopez
 */
public class Par implements Serializable{

    private final Double x;
    private final Double y;

    public Par(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Boolean estaCircunferenciaUnidade() {
        return (Math.pow(x, 2) + Math.pow(y, 2) <= 1);
    }

    public static Par xerarParAleatorio() {
        return new Par(Math.random(), Math.random());
    }
}
