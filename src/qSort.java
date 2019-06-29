public class qSort {
    public static void sort(double[]a , int[] b, int l, int r)
    {
        int i = l;
        int j = r;
        if ( l >=r) return;
        double temp ;
        int temp1 ;
        double key = a[(l+r) /2] ;
        do {
            while (a[i] < key ) i++;
            while (a[j] > key ) j--;
            if ( i <= j )
            {
                temp = a[i]; a[i]= a[j]; a[j] = temp;
                temp1= b[i];b[i] = b[j]; b[j] = temp1;
                i++;
                j--;
            }
        }
        while ( i <= j );
        sort(a,b,l,j);
        sort(a,b,i,r);
    }
}
