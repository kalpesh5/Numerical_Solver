import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class timeStep {
    static void solve(Scanner sc) {
        double[][] temperature = new double[102][102];
        double delX = 0.01;
        double delY = 0.01;
        double delT = 0.01;
        double alpha = 1.0;
        double tol = 1e-6;
        // the y axis and x axis has T=0 given boundary condition
        for(int i=0; i<102; i++){
            temperature[0][i] = 0;
            temperature[i][0] = 0;
        }
        int itr = 100000;
        while(itr-->0){
            double[][] temp2 = new double[102][102];
            for(int i=1; i<101; i++){
                for(int j =1; j<101; j++){
                    temp2[i][j] = temperature[i][j];
                }
            }
            for(int i=1; i<101; i++){
                for(int j=1; j<101; j++){
                    double x = j *delX;
                    double y = i*delY;
                    if(j==100 && i<51){ // neumann
                        temperature[j+1][i] = 8*(y)*delX+temperature[j-1][i];
                    }
                    if(i==100 && j<51){
                        temperature[j][i+1] = 4*(x)*(x)*delY+temperature[j][i-1];
                    }
                    if(j +i>=150){
                        temperature[j][i] = 2*(x)*(x)*(y);
                        continue;
                    }
                    temperature[j][i] = (temperature[j-1][i]+temperature[j+1][i]+temperature[j][i-1]+temperature[j][i+1])/4;
                }
            }
        double res = 0.0;
        double denom = 0.0;
        for(int i=1; i<101;i++){
            for(int j=1; j<101; j++){
                res+=Math.pow(Math.abs(temperature[i][j]-temp2[i][j]),2);
                denom+=(temperature[i][j]*temperature[i][j]);
            }
        }
        if((Math.sqrt(res)/Math.sqrt(denom))<=tol){
            System.out.println("Converged at "+ (100000-itr));
            break;
        }
    }
    
    double[][][] temp_time = new double[102][102][102];
    for(int i=1; i<101; i++){
        for(int j =1; j<101; j++){
            for(int k=0; k<100;k++){
                temp_time[k+1][i][j] = alpha*delT*((temperature[i-1][j]-2*(temperature[i][j])+temperature[i+1][j])/(delX*delX) + (temperature[i][j-1]-2*(temperature[i][j])+temperature[i][j+1])/(delY*delY) ); 
            }
        }
    }
    try (PrintWriter out = new PrintWriter(new FileWriter("temperature_try2.txt"))) {
        for (int i = 0; i <= 100; i++) {
        int maxJ = Math.min(100, 150 - i);
        for (int j = 0; j <= maxJ; j++) {
            double x = j * delX;
            double y = i * delY;
            out.printf(Locale.US, "%.2f %.2f %.10f%n",
                    x, y, temperature[j][i]);
        }
    }

}catch (IOException e) {
    e.printStackTrace();
}
}

public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
        int t = 1;
        while (t-- > 0) {
            solve(sc);
        }
        sc.close();
    }
}
