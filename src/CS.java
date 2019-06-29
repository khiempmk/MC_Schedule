import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Gamma;

import java.util.Random;

import static org.apache.commons.math3.special.Gamma.gamma;

public class CS {
    public static void Levy(double lamda , int dim , double anpha , double[] cucEgg) {

        double tu = gamma(1+ lamda) * Math.sin(lamda* Math.PI/ 2) ;
        double mau = gamma((1+lamda)/2) * lamda * Math.pow(2, (lamda -1)/2) ;

        double sigma1 = Math.pow(tu/ mau , 1/ lamda);
        double  sigma2 = 1 ;
        double x;
        double y;
        double step ;
        NormalDistribution randX = new NormalDistribution(0, sigma1);
        NormalDistribution randY = new NormalDistribution(0, sigma2);
        for (int i = 0 ; i < dim ;  i++) {
            x = randX.sample() ;
            y= 0 ;
            while (y == 0 )
                y = randY.sample() ;
            step = x / Math.pow(Math.abs(y),1/ lamda);
            cucEgg[i] = cucEgg[i] + anpha * step ;
        }
    }
    public static void rebuild(double[] agent,  int numRequest) {
        double temp;
        int temp1;
        for (int j = 0; j < numRequest; j++) {
            Random rd = new Random();
            temp = rd.nextDouble();
            temp1 = rd.nextInt(2);
            if (temp1 == 0 )
                agent[j] = temp;
            else agent[j] = -temp ;
        }
    }
}
