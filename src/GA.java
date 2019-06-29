import java.util.Random;
public class GA {
    public static void upheap( int[] heap, double [] d ,int c)
    {
        int p, tg;
        while (c > 1 )
        {
            p = c /2 ;
            if (d[heap[p]] >= d[heap[c]]) break;
            tg = heap[c];
            heap[c] = heap[p];
            heap[p] = tg;
            c = p ;
        }
    }
    public static void downheap(int p , int nheap ,double[] d , int[] heap)
    {
        int c;
        int tg;
        while ( p*2 <= nheap)
        {
            c = p*2 ;
            if ( c +1 <= nheap && d[heap[c]] < d[heap[c+1]] ) c++;
            if (d[heap[p]] > d[heap[c]]) break;
            tg = heap[c];
            heap[c] = heap[p];
            heap[p] = tg;
            p = c;
        }
    }
    public static void CROSSOVER(double[][] agents, int A , int B, int numberRequest,double[] childA ,double[] childB)
    {
        Random rd = new Random();
        int temp = rd.nextInt(numberRequest);
       // double [] childA = new double[numberRequest];
       // double [] childB = new double[numberRequest];
        for (int i = 0 ; i < temp ; i++)
        {
            childA[i] = agents[A][i];
            childB[i] = agents[B][i];
        }
        for (int i = temp ; i < numberRequest ; i++) {
            childB[i] = agents[A][i];
            childA[i] = agents[B][i];
        }
    }
    public static void MUTATION(double[] agents, int numRequest)
    {
        Random rd = new Random();
        int temp ;
        double temp1;
        for (int i = 0 ; i < numRequest/2 ; i++)
        {
            temp = rd.nextInt(2);
            if (temp == 1 )
            {
                temp1 = agents[i];
                agents[i] = agents[numRequest - i];
                agents[numRequest- i] = temp1;
            }
        }
    }
}
