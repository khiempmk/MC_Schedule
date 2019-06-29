import org.jfree.ui.RefineryUtilities;

import java.util.Random ;
public class GSA {
    public static void init(double[][] agents, int numAgent, int numRequest) {
        double temp;
        int temp1;
        for (int i = 0; i < numAgent; i++)
            for (int j = 0; j < numRequest; j++) {
                Random rd = new Random();
                temp = rd.nextDouble();
                temp1 = rd.nextInt(2);
                if (temp1 == 0 )
                    agents[i][j] = temp;
                    else agents[i][j] = -temp ;
            }
    }

    public static double fitness(double[] agent, double[][] travelTime, int numRequest, double[] p_i, double thres, double eMax,double eMin, double chargRate) {
        double fit = 0;
        double[] tTime = new double[numRequest+1];
        double[] cTime = new double[numRequest+1];
        double[] wTime = new double[numRequest+1];

        double[] a = new double[numRequest+1];
        int[] b = new int[numRequest+1];
        for (int i = 0; i < numRequest; i++) {
            a[i + 1] = agent[i];
            b[i + 1] = i + 1;
        }
        double eRemain;
        b[0] = 0;
        qSort.sort(a, b, 1, numRequest);
        tTime[0] = cTime[0] = wTime[0] = 0;
        for (int i = 1; i <= numRequest; i++) {
            tTime[i] = travelTime[b[i - 1]][b[i]];
            eRemain = thres -( wTime[i - 1]+ tTime[i]) * p_i[b[i]];
            if (eRemain < eMin) eRemain = eMin;
            cTime[i] = (eMax - eRemain) / chargRate;
            wTime[i] = wTime[i - 1] + tTime[i - 1] + cTime[i - 1];
            fit += (wTime[i] + cTime[i] + tTime[i]) / numRequest;
        }
        return fit;
    }
    public static double fitness1(double[] agent, double[][] travelTime, int numRequest, double[] p_i, double thres, double eMax, double eMin, double chargRate) {
        double fit = 0;
        double[] tTime = new double[numRequest+2];
        double[] cTime = new double[numRequest+2];
        double[] wTime = new double[numRequest+2];

        double[] a = new double[numRequest+1];
        int[] b = new int[numRequest+1];
        for (int i = 0; i < numRequest; i++) {
            a[i + 1] = agent[i];
            b[i + 1] = i + 1;
        }
        double eRemain;
        int numDie = 0 ;
        int numNextDie = 0 ;
        b[0] = 0;
        qSort.sort(a, b, 1, numRequest);
        tTime[0] = cTime[0] = wTime[0] = 0;
        for (int i = 1; i <= numRequest; i++) {
            tTime[i] = travelTime[b[i - 1]][b[i]];
            eRemain = thres -( wTime[i - 1]+ tTime[i]) * p_i[b[i]];
            if (eRemain < eMin)
            {
                eRemain = eMin;
                numDie++;
            }
            cTime[i] = (eMax - eRemain) / chargRate;
            wTime[i] = wTime[i - 1] + tTime[i - 1] + cTime[i - 1];
          //  fit += (wTime[i]  + tTime[i]) / numRequest;
        }
        wTime[numRequest+1] = wTime[numRequest]+ cTime[numRequest]+ tTime[numRequest] ;
        double trvTime = wTime[numRequest+1] + travelTime[b[numRequest]][b[0]];

        for (int i= 1; i <= numRequest ;i++)
            if (trvTime + travelTime[b[0]][b[i]] - wTime[i+1] >  ((eMax - eMin) / p_i[b[i]]) )
                numNextDie++;
        return numNextDie+ numDie;
    }
    public static void updatePosition(double[][] agent, double[] M, double[][] travelTime,double[][] v, int numRequest, int numAgent, double G) {
        Random rd = new Random();
        double temp;
        double[][] f = new double[numAgent][numAgent];
        double[] F = new double[numAgent];
        double[] a = new double[numAgent];
        double[][] dij = new double[numAgent][numAgent];
        //double[] v = new double[numAgent]; 60*4*60*
        for (int i = 0; i < numAgent; i++)
            for (int j = i; j < numAgent; j++) {
                dij[i][j] = 0;
                for (int k = 0; k < numRequest; k++)
                    dij[i][j] += (agent[i][k] - agent[j][k]) * (agent[i][k] - agent[j][k]);
                dij[j][i] = dij[i][j] = (double) Math.sqrt(dij[i][j]);
            }
        for (int d = 0; d < numRequest; d++) {
            for (int i = 0; i < numAgent; i++)
                for (int j = 0; j < numAgent; j++) {
                    f[i][j] = ((G * M[i] * M[j]) / ((double) dij[i][j] + (double) 0.01)) * (agent[j][d] - agent[i][d]);
                }

            for (int i = 0; i < numAgent; i++) {
                F[i] = 0;
                for (int j = 0; j < numAgent; j++) {
                    if (i != j) {
                        temp = rd.nextDouble();
                        F[i] += temp * f[i][j];
                    }
                }
                a[i] = F[i] / M[i];
                temp = rd.nextDouble();
                v[i][d] = temp * v[i][d] + a[i];
                agent[i][d] += v[i][d];

            }
        }
    }
    public static void printkq(double[] agent, int numRequest, double [] posX, double[] posY)
    {
        double[] a = new double[numRequest+1];
        int[] b = new int[numRequest+1];
        for (int i = 0; i < numRequest; i++)
        {
            a[i+ 1] = agent[i];
            b[i+ 1] = i + 1;
        }
        qSort.sort(a, b, 1, numRequest);
        System.out.println("//------------------ Charging Path ----------------------//");
        System.out.print("BaseStation  ->  ");
        for (int i =  1 ; i <= numRequest ; i++)
            System.out.printf("Sensor %d  ->  ", b[i]);
        System.out.printf("\n//-------------------------------------------------------//");
        XYLineChart_AWT chart = new XYLineChart_AWT("GA based charging path",
                "", b, numRequest,posX, posY);
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }

}