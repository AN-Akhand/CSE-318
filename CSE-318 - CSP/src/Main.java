import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        int n;
        int[][] input;
        BufferedReader reader;
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


        while (true){
            String s = consoleReader.readLine();
            reader = new BufferedReader(new FileReader("data/" + s + ".txt"));
            int[] vah = Arrays.stream(consoleReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            s = consoleReader.readLine();
            n = Integer.parseInt(reader.readLine());
            input = new int[n][n];
            for (int i = 0; i < n; i++) {
                input[i] = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            }

            for (int v : vah) {

                ConstraintGraph csp = new ConstraintGraph(n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (input[i][j] == 0) continue;
                        csp.root.assignVal(input[i][j], i, j);
                    }
                }
                if(s.equals("BT")) csp.fwdCheck = false;
                else csp.fwdCheck = true;
                csp.varChoice = v;

                long t1 = System.currentTimeMillis();
                csp.solve();
                long t2 = System.currentTimeMillis();

                System.out.println("#Node: " + csp.nodeCount + ", #BT: " + csp.backtrackCount + ", Time: " + (t2 - t1));

                for(int i = 0; i < n; i++){
                    for(int j = 0; j < n; j++){
                        System.out.print(csp.root.variables[i][j].value + " ");
                    }
                    System.out.println();
                }

            }

        }
    }
}
