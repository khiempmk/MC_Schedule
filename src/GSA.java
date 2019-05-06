import org.jfree.ui.RefineryUtilities;

import java.util.Random ;
public class GSA {
    public static void init(float[][] agents, int numAgent, int numRequest) {
        float temp;
        int temp1;
        for (int i = 0; i < numAgent; i++)
            for (int j = 0; j < numRequest; j++) {
                Random rd = new Random();
                temp = rd.nextFloat();
                temp1 = rd.nextInt(2);
                if (temp1 == 0 )
                    agents[i][j] = temp;
                    else agents[i][j] = -temp ;
            }
    }

    public static float fitness1(float[] agent, float[][] travelTime, int numRequest, float[] p_i, float thres, float eMax,float eMin, float chargRate) {
        float fit = 0;
        float[] tTime = new float[numRequest+1];
        float[] cTime = new float[numRequest+1];
        float[] wTime = new float[numRequest+1];

        float[] a = new float[numRequest+1];
        int[] b = new int[numRequest+1];
        for (int i = 0; i < numRequest; i++) {
            a[i + 1] = agent[i];
            b[i + 1] = i + 1;
        }
        float eRemain;
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
    public static float fitness(float[] agent, float[][] travelTime, int numRequest, float[] p_i, float thres, float eMax, float eMin, float chargRate) {
        float fit = 0;
        float[] tTime = new float[numRequest+2];
        float[] cTime = new float[numRequest+2];
        float[] wTime = new float[numRequest+2];

        float[] a = new float[numRequest+1];
        int[] b = new int[numRequest+1];
        for (int i = 0; i < numRequest; i++) {
            a[i + 1] = agent[i];
            b[i + 1] = i + 1;
        }
        float eRemain;
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
        float trvTime = wTime[numRequest+1] + travelTime[b[numRequest]][b[0]];

        for (int i= 1; i <= numRequest ;i++)
            if (trvTime + travelTime[b[0]][b[i]] - wTime[i+1] >  ((eMax - eMin) / p_i[b[i]]) )
                numNextDie++;
        return numNextDie+ numDie;
    }
    public static void updatePosition(float[][] agent, float[] M, float[][] travelTime,float[][] v, int numRequest, int numAgent, float G) {
        Random rd = new Random();
        float temp;
        float[][] f = new float[numAgent][numAgent];
        float[] F = new float[numAgent];
        float[] a = new float[numAgent];
        float[][] dij = new float[numAgent][numAgent];
        //float[] v = new float[numAgent]; 60*4*60*
        for (int i = 0; i < numAgent; i++)
            for (int j = i; j < numAgent; j++) {
                dij[i][j] = 0;
                for (int k = 0; k < numRequest; k++)
                    dij[i][j] += (agent[i][k] - agent[j][k]) * (agent[i][k] - agent[j][k]);
                dij[j][i] = dij[i][j] = (float) Math.sqrt(dij[i][j]);
            }
        for (int d = 0; d < numRequest; d++) {
            for (int i = 0; i < numAgent; i++)
                for (int j = 0; j < numAgent; j++) {
                    f[i][j] = ((G * M[i] * M[j]) / ((float) dij[i][j] + (float) 0.01)) * (agent[j][d] - agent[i][d]);
                }

            for (int i = 0; i < numAgent; i++) {
                F[i] = 0;
                for (int j = 0; j < numAgent; j++) {
                    if (i != j) {
                        temp = rd.nextFloat();
                        F[i] += temp * f[i][j];
                    }
                }
                a[i] = F[i] / M[i];
                temp = rd.nextFloat();
                v[i][d] = temp * v[i][d] + a[i];
                agent[i][d] += v[i][d];

            }
        }
    }
    public static void printkq(float[] agent, int numRequest, float [] posX, float[] posY)
    {
        float[] a = new float[numRequest+1];
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