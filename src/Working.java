public class Working {

    public static float loop_Working(int[] b,float[][] travelTime, int numRequest, float[] p_i, float thres, float eMax, float eMin, float chargRate) {

        float[] tTime = new float[numRequest+2];
        float[] cTime = new float[numRequest+2];
        float[] wTime = new float[numRequest+2];
        //float eRemain,totalTime;

        /* totalTime = 0;
        tTime[0] = cTime[0] = wTime[0] = 0;

        for (int i = 1; i <= numRequest; i++) {
            tTime[i] = travelTime[sensor[i-1]][sensor[i]];
            eRemain = thres -( wTime[i - 1]+ tTime[i]) * p_i[sensor[i]];
            if (eRemain < eMin) eRemain = 0;
            cTime[i] = (eMax - eRemain) / chargRate;
            wTime[i] = wTime[i - 1] + tTime[i - 1] + cTime[i - 1];
            totalTime += (wTime[i]+cTime[i]+tTime[i])/numRequest;
        }

        return totalTime;*/
        float eRemain;
        int numDie = 0 ;
        int numNextDie = 0 ;
        b[0] = 0;
       // qSort.sort(a, b, 1, numRequest);
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
        return trvTime;

    }

}
