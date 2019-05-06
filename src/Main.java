//VFA_VGA
import org.jfree.ui.RefineryUtilities;

import java.util.Scanner;
import java.io.File ;
import java.io.FileNotFoundException;
import java.util.Random;
public class Main {
    //------------- Read - Input ----------------\\
    static int numRequest ;  // So luong request
    static int numAgent =100 ;    // So luong agent
   // static float consumeRate = (float) 0.001 ;
    static float thres = (float)1600;
    static float eMax = 8000 ;
    static float eMin = 540 ;
    static float chargRate = (float) 60;
    static int tMax =2000  ;
    static float Go = 100;
    static float beta =20 ;
    static float V = 10 ;
    static float [][] travelTime  ;
    static float [][] agents ;
    static float [] fitness = new float [numAgent] ;
    static float [][] v ;
    static float[] posX,  posY ;
    static float[] p_i ;
    static int [] sensor = new int [11];
    static boolean [] free = new boolean [11];
    static int [] schedule = new int[11];
    static float currentTotal = 1000000000;
    public static void readinput()
    {
        File fi = new File("C:\\Users\\T470S\\IdeaProjects\\ScheduleMC\\src\\data.inp");
        try{
        Scanner input = new Scanner(fi);
        numRequest = input.nextInt();
        travelTime = new float[numRequest+1][numRequest+1];
        agents     = new float[numAgent][numRequest+1];
        v          = new float[numAgent][numRequest+1] ;
        posX = new float[numRequest+1];
        posY = new float[numRequest+1];
        p_i = new float[numRequest+1];
        //dis        = new float[numRequest][numRequest];
        //numAgent = input.nextInt() ;
        //consumeRate = input.nextFloat() ;
        // thres = input.nextFloat();
        // eMax = input.nextFloat() ;
        //chargRate = input.nextFloat() ;
        //tMax = input.nextInt();
        float temp ;
        for (int i =1 ; i <= numRequest ; i++)
        {
            posX[i] = input.nextFloat();
            posY[i] = input.nextFloat();
            p_i[i] = input.nextFloat() ;
            temp = input.nextFloat();
        }
        posX[0]= posY[0] = 0 ;
        for (int i = 0; i <= numRequest; i++)
            for (int j = 0; j <= numRequest; j++)
                travelTime[i][j] = (float) Math.sqrt(Math.pow(posX[i]-posX[j],2)+ Math.pow(posY[i]- posY[j],2));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    //------------- init - Agent ----------------\\
    public static void GSA_process()
    {

        GSA gsa = new GSA();
        gsa.init(agents,numAgent, numRequest);
        float gFit = Float.MAX_VALUE ;
        float [] gBest = new float[numRequest];
        int best , worst;
        float G ;
        float[] M = new float[numAgent];
        for (int iTime = 1 ; iTime <= tMax ; iTime++)
        {
            best = worst = 0 ;
            // compute fitness
            // update best , worst
            for (int i = 0 ; i < numAgent ; i++)
            {
                fitness[i] = gsa.fitness(agents[i], travelTime, numRequest, p_i, thres, eMax, eMin,chargRate);
                if (fitness[i] < fitness[best])
                    best = i;
                if (fitness[i] > fitness[worst])
                    worst = i;
            }
            // update gFit
            if (gFit > fitness[best] )
            {
                gFit = fitness[best];
                for (int i = 0 ; i < numRequest ; i++)
                    gBest[i] = agents[best][i];
            }
            // compute G
            G = Go* (float)Math.exp(-beta/tMax *iTime );
            // compute M
            float sumM = 0 ;
            for (int i = 0; i < numAgent ; i++)
            {
                M[i] = (fitness[i] - fitness[worst]) / (fitness[best] - fitness[worst]);
                sumM += M[i];
            }
            for (int i = 0; i < numAgent; i++)
                M[i] = M[i] / sumM ;
            //update Position
            gsa.updatePosition(agents,M,travelTime,v,numRequest,numAgent,G);

        }
        //for (int i = 0; i < numRequest ; i++)
        //  System.out.printf("%f ",gBest[i]);
        //System.out.printf("\n");
     //   gsa.printkq(gBest,numRequest);
        System.out.printf("\nfitness = %f\n", gFit);
        gsa.printkq(gBest,numRequest, posX, posY);
    }
    public static void GA_process()
    {
        int nHeap = 0;
        float temp = 0 ;
        int[] heap = new int [numAgent+1];
        GSA gsa = new GSA();
        gsa.init(agents,numAgent, numRequest);
        Random rd = new Random();
        GA ga = new GA();
        float gFit = Float.MAX_VALUE ;
        float [] gBest = new float[numRequest];
        for (int i = 0 ; i < numAgent ; i++)
        {
            fitness[i] = gsa.fitness(agents[i], travelTime, numRequest, p_i, thres, eMax,eMin ,chargRate);
            nHeap++ ;
            heap[nHeap] = i;
            ga.upheap(heap,fitness,nHeap);
            if (gFit > fitness[i])
            {
                gFit = fitness[i];
                gBest = agents[i];
            }
        }
        float[] childA= new float[numRequest];
        float[] childB= new float[numRequest];
        float CROS_THRES = (float) 0.15 ;
        int i, j ,worst ;
        float fitA, fitB ;
        int count = 0 ;
        for (int ii = 1; ii < tMax ;ii ++) {
            //System.out.printf("%d\n",ii);
            count++ ;
            temp = rd.nextFloat();
            if (temp < 0.8) {
                i = rd.nextInt(numRequest);
                j = rd.nextInt(numRequest);
                if (i != j)
                    ga.CROSSOVER(agents, i, j, numRequest, childA, childB);
                fitA = GSA.fitness(childA, travelTime, numRequest, p_i, thres, eMax, eMin,chargRate);
                fitB = GSA.fitness(childB, travelTime, numRequest, p_i, thres, eMax, eMin,chargRate);
                worst = heap[1];
                if (fitness[worst] > fitA) {
                    for (int k = 0; k < numRequest; k++)
                        agents[worst][k] = childA[k];
                    fitness[worst] = fitA;
                    ga.downheap(1, nHeap, fitness, heap);
                    //count ++ ;
                    if (gFit > fitA) {
                        gFit = fitA;
                        gBest = childA;
                        count = 0 ;
                    }
                   // System.out.printf(".\n");

                }
                worst = heap[1];
                if (fitness[worst] > fitB) {
                    for (int k = 0; k < numRequest; k++)
                        agents[worst][k] = childB[k];
                    fitness[worst] = fitB;
                    ga.downheap(1, nHeap, fitness, heap);
                    //count++ ;
                    if (gFit > fitB) {
                        gFit = fitB;
                        gBest = childB;
                        count = 0 ;
                        //System.out.printf("..\n");
                    }
                }
            }
            if (temp < CROS_THRES) {
                i = rd.nextInt(numRequest)+1;
                ga.MUTATION(agents[heap[i]], numRequest);
                fitness[heap[i]] = GSA.fitness(agents[heap[i]], travelTime, numRequest, p_i, thres, eMax,eMin, chargRate);
                if (fitness[heap[i]] < gFit)
                {
                    gFit = fitness[heap[i]];
                    gBest = agents[heap[i]];
                    count = 0 ;
                }
                ga.upheap(heap, fitness, i);
                //System.out.printf("...\n");
                //System.out.printf(".%d\n",i);
                ga.downheap(i, nHeap, fitness, heap);
                //System.out.printf("....\n");
            }
            if (count >= 15 ) CROS_THRES = (float)0.8 ;
            else CROS_THRES = (float) 0.15;
        }
        System.out.printf("\nFitness = %f\n", gFit);
        gsa.printkq(gBest,numRequest, posX, posY);
    }
    public static void loop(int i) {
        if (i > numRequest) {

           Working work = new Working();
            float total = work.loop_Working(sensor, travelTime, numRequest, p_i, thres, eMax,eMin ,chargRate);
            //System.out.print(total);
            if (total < currentTotal) {
                currentTotal = total;
                for (int j = 1; j <= numRequest; j++) schedule[j] = sensor[j];
            }

        }
        else {
            for (int j = 1; j <= numRequest; j++) {
                if (free[j]) {
                    sensor[i] = j;
                    free[j] = false;
                    //System.out.printf("%d -->", j);
                    loop(i+1);
                    free[j] = true;
                    //System.out.println();
                }
            }
        }
    }

    public static void loop_Process() {
        for (int i = 1; i <= numRequest; i++) free[i] = true;
        loop(1);
        System.out.printf("Fitness= %f",currentTotal);
        System.out.println("Basestation: ");
        for (int i = 1; i <= numRequest; i++) {
            System.out.printf(" sensor %d ---> ", schedule[i]);
        }
        XYLineChart_AWT chart = new XYLineChart_AWT("GA based charging path",
                "", schedule, numRequest,posX, posY);
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
    public static void main(String[] args) {
        readinput();
     //GSA_process();
      //  GA_process();
        loop_Process();
    }
}
