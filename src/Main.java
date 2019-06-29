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
   // static double consumeRate = (double) 0.001 ;
    static double thres = (double)1600;
    static double eMax = 8000 ;
    static double eMin = 540 ;
    static double chargRate = (double) 60;
    static int tMax =20000  ;
    static double Go = 100;
    static double beta =20 ;
    static double V = 10 ;
    static double [][] travelTime  ;
    static double [][] agents ;
    static double [] fitness = new double [numAgent] ;
    static double [][] v ;
    static double[] posX,  posY ;
    static double[] p_i ;
    static int [] sensor = new int [11];
    static boolean [] free = new boolean [11];
    static int [] schedule = new int[11];
    static double currentTotal = 1000000000;
    public static void readinput()
    {
        File fi = new File("C:\\Users\\T470S\\IdeaProjects\\ScheduleMC\\src\\data.inp");
        try{
        Scanner input = new Scanner(fi);
        numRequest = input.nextInt();
        travelTime = new double[numRequest+1][numRequest+1];
        agents     = new double[numAgent][numRequest+1];
        v          = new double[numAgent][numRequest+1] ;
        posX = new double[numRequest+1];
        posY = new double[numRequest+1];
        p_i = new double[numRequest+1];
        //dis        = new double[numRequest][numRequest];
        //numAgent = input.nextInt() ;
        //consumeRate = input.nextdouble() ;
        // thres = input.nextdouble();
        // eMax = input.nextdouble() ;
        //chargRate = input.nextdouble() ;
        //tMax = input.nextInt();
        double temp ;
        for (int i =1 ; i <= numRequest ; i++)
        {
            posX[i] = input.nextDouble();
            posY[i] = input.nextDouble();
            p_i[i] = input.nextDouble() ;
            temp = input.nextDouble();
        }
        posX[0]= posY[0] = 0 ;
        for (int i = 0; i <= numRequest; i++)
            for (int j = 0; j <= numRequest; j++)
                travelTime[i][j] = (double) Math.sqrt(Math.pow(posX[i]-posX[j],2)+ Math.pow(posY[i]- posY[j],2));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    //------------- init - Agent ----------------\\
    public static void GSA_process()
    {

        GSA gsa = new GSA();
        gsa.init(agents,numAgent, numRequest);
        double gFit = Double.MAX_VALUE ;
        double [] gBest = new double[numRequest];
        int best , worst;
        double G ;
        double[] M = new double[numAgent];
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
            G = Go* (double)Math.exp(-beta/tMax *iTime );
            // compute M
            double sumM = 0 ;
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
        double temp = 0 ;
        int[] heap = new int [numAgent+1];
        GSA gsa = new GSA();
        gsa.init(agents,numAgent, numRequest);
        Random rd = new Random();
        GA ga = new GA();
        double gFit = Double.MAX_VALUE;
        double [] gBest = new double[numRequest];
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
        double[] childA= new double[numRequest];
        double[] childB= new double[numRequest];
        double CROS_THRES = (double) 0.15 ;
        int i, j ,worst ;
        double fitA, fitB ;
        int count = 0 ;
        for (int ii = 1; ii < tMax ;ii ++) {
            //System.out.printf("%d\n",ii);
            count++ ;
            temp = rd.nextDouble();
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
            if (count >= 15 ) CROS_THRES = (double)0.8 ;
            else CROS_THRES = (double) 0.15;
        }
        System.out.printf("\nFitness = %f\n", gFit);
        gsa.printkq(gBest,numRequest, posX, posY);
    }
    public static void loop(int i) {
        if (i > numRequest) {

           Working work = new Working();
            double total = work.loop_Working(sensor, travelTime, numRequest, p_i, thres, eMax,eMin ,chargRate);
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

    public static void CS_process()
    {
         double Lamda  = 1.5;
         double Anpha  = 0.1 ;   // step _ size
         double p_a = 0.2 ;   // xac xuat phat hien trung la
        GSA gsa = new GSA() ;
        gsa.init(agents,numAgent,numRequest) ;
        double[] best = new double[ numRequest];
        double gBest = Double.MAX_VALUE ;
        for (int i = 0 ; i < numAgent ; i++) {
            fitness[i] = gsa.fitness(agents[i],travelTime,numRequest,p_i,thres,eMax, eMin, chargRate);
            if (fitness[i] < gBest) {
                gBest = fitness[i] ;
                best =  agents[i];
            }
        }
        double [] cuckooEgg = new double[ numRequest];
        double cuckooEggFitness ;
        CS Cs = new CS();
        Random rd = new Random();
        int k;
        double rds ;
        for (int ii = 0 ; ii < tMax ; ii ++)
        {
            // Get cuckoo egg by levy distribution
            Cs.Levy(Lamda,numRequest,Anpha, cuckooEgg);
            cuckooEggFitness = gsa.fitness(cuckooEgg,travelTime,numRequest,p_i,thres,eMax,eMin,chargRate);
            // get a random nest k
            k = rd.nextInt(numAgent) ;

            if (fitness[k] > cuckooEggFitness){
                agents[k] = cuckooEgg ;
                fitness[k] = cuckooEggFitness ;
                if (fitness[k] < gBest ) {
                    gBest = fitness[k] ;
                    best = agents[k] ;
                }
            }
            // phat hien trung la

            for (int i = 0 ; i < numAgent ; i++){
                rds = rd.nextDouble() ;
                if (rds < p_a)
                {
                    // new nest will be build
                    Cs.rebuild(agents[i],numRequest);
                    fitness[i] = gsa.fitness(agents[i],travelTime,numRequest,p_i,thres,eMax,eMin,chargRate);
                    if (fitness[i] < gBest ) {
                        gBest = fitness[i] ;
                        best = agents[i] ;
                    }
                }
            }

        }
        System.out.printf("\nFitness = %f\n", gBest);
        gsa.printkq(best,numRequest, posX, posY);
    }
    public static void main(String[] args) {
        readinput();
     //GSA_process();
      // GA_process();
        //loop_Process();
       CS_process();
    }
}
