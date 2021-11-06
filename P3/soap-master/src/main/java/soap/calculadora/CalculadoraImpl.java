package soap.calculadora;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebService;

@WebService(
        endpointInterface = "soap.calculadora.Calculadora",
        serviceName = "Calculadora"
)
public class CalculadoraImpl implements Calculadora {

    @Override
    public int suma(int a, int b) {
        return a + b;
    }

    @Override
    public int resta(int a, int b) {
        return a - b;
    }

    @Override
    public int mult(int a, int b) {
        return a * b;
    }

    @Override
    public double div(int a, int b) {
        if (b != 0) {
            return a / (double) b;
        }
        return 0;
    }

    @Override
    public double pot(int a, int b) {
        return Math.pow(a, b);
    }

    @Override
    public double raiz_cadrada(int a) {
        return Math.sqrt(a);
    }

    @Override
    public double ln(int a) {
        return Math.log(a);
    }

    @Override
    public int max(int[] nums) {
        return Arrays.stream(nums).max().getAsInt();
    }

    @Override
    public int min(int[] nums) {
        return Arrays.stream(nums).min().getAsInt();
    }

    @Override
    public double media(int[] nums) {
        return Arrays.stream(nums).average().getAsDouble();
    }

    @Override
    public float mediana(int[] nums) {
        float med = nums.length / (float) 2;
        if ((nums.length % 2) == 0) {
            return (nums[(int) med] + nums[((int) med) - 1]) / (float) 2;
        } else {
            return nums[(int) Math.floor(med)];
        }
    }

    @Override
    public List<Integer> moda(int[] nums) {
        HashMap<Integer, Integer> repes = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int e = nums[i];
            if (repes.get(e) != null) {
                repes.replace(e, repes.get(e) + 1);
            } else {
                repes.put(e, 1);
            }
        }

        int max = this.max(repes.values().stream().mapToInt(Integer::intValue).toArray());

        ArrayList<Integer> moda = new ArrayList<>();
        for (Integer i : repes.keySet()) {
            if (repes.get(i) == max) {
                moda.add(i);
            }
        }

        return moda;
    }

    @Override
    public double desv(int[] nums) {
        double media = this.media(nums);
        double t = 0;
        for (int i = 0; i < nums.length; i++) {
            t += (nums[i] - media) * (nums[i] - media);
        }
        return Math.sqrt(t / (nums.length - 1));
    }

}
