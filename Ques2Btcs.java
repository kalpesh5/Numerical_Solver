import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class Ques2Btcs {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double delR = 0.025;
        double delT = 0.005;
        double time = 5;
        int timeStep = (int)Math.ceil(time / delT);
        int div = (int)Math.round((2.0 - 1.0)/delR);
        int nodes = div+1;
        double tolerance = 1e-10;
        double[][] temp = new double[timeStep+1][nodes+1];  
        // at time t =0 all nodes temprature is 0;
        for(int i=0; i<temp[0].length; i++){
            temp[0][i] =0;
        }
        temp[0][nodes] = 1.0;
        for(int i=1; i<=timeStep; i++){
            double currentTime = (i-1)*delT;
            double dtNow = Math.min(delT, time-currentTime);
            int itr = 10000000;
            while(itr-->0){
                // neumann 
                temp[i][0] = temp[i][2];
                // dirichlet
                temp[i][nodes] = 1;
                double[] temprature = new double[nodes+1];
                for(int k =0; k<nodes+1; k++){
                    temprature[k] = temp[i][k];
                }
                for(int j=1; j<nodes; j++){
                    double r = 1.0 + (j-1)*delR;
                    temp[i][j] = ((delR*delR*dtNow)/((delR*delR) + 2*dtNow)) *(((temp[i][j-1]+temp[i][j+1])/(delR*delR)) + (1/r)*((temp[i][j+1] - temp[i][j-1])/(2*delR)) +temp[i-1][j]/dtNow);
                }
                //neuman
                temp[i][0] = temp[i][2];
                double sum =0.0;
                double denom = 0.0;
                for(int j =1; j<nodes; j++){
                    sum+=Math.pow(Math.abs(temp[i][j] - temprature[j]),2);
                    denom += temp[i][j]*temp[i][j];
                }
                if(Math.sqrt(sum)/Math.sqrt(denom)<=tolerance){
                    System.out.println("Converger at itr" + (10000000-itr));
                    break;
                }
            }
            // double sum =0.0;
            // double denom = 0.0;
            // for(int j =1; j<nodes; j++){
            //     sum+=Math.pow(Math.abs(temp[i][j] - temp[i-1][j]),2);
            //     denom += temp[i][j]*temp[i][j];
            // }
            // if(Math.sqrt(sum)/Math.sqrt(denom)<=tolerance){
            //     System.out.println("Converger at timeStep" + i);
            //     break;
            // }
        }
        try {
        PrintWriter pw = new PrintWriter(new FileWriter("Ques2T=0.005.txt"));
        int t = timeStep; 
        for (int j = 1; j <= nodes; j++) {
            double r = 1.0 + (j-1)*delR;
            pw.printf(Locale.US, "%.17g %.17g%n", r, temp[t][j]);
        }
        pw.close();
        System.out.println("Results saved successfully!");

    } catch (Exception e) {
        e.printStackTrace();
    }

    }
}
