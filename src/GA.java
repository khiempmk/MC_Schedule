import java.util.Random;
public class GA {
    public static void upheap( int[] heap, float [] d ,int c)
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
    public static void downheap(int p , int nheap ,float[] d , int[] heap)
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
    public static void CROSSOVER(float[][] agents, int A , int B, int numberRequest,float[] childA ,float[] childB)
    {
        Random rd = new Random();
        int temp = rd.nextInt(numberRequest);
       // float [] childA = new float[numberRequest];
       // float [] childB = new float[numberRequest];
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
    public static void MUTATION(float[] agents, int numRequest)
    {
        Random rd = new Random();
        int temp ;
        float temp1;
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
