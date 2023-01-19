import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

class Variable{

    int value;
    boolean[] domain;
    int noOfLegalValues;
    int fwdDegree;
    Pair[] source;


    Variable(int n){
        domain = new boolean[n];
        Arrays.fill(domain, Boolean.TRUE);
        value = 0;
        noOfLegalValues = n;
        fwdDegree = 2 * n - 1;
        source = new Pair[n];
    }


    void removeFromDomain(int val, int x, int y, Pair[][] row, Pair[][] col){
        if(domain[val - 1]){
            source[val - 1] = new Pair(x, y);
            domain[val - 1] = false;
            noOfLegalValues -= 1;
            row[x][val - 1].y--;
            col[x][val - 1].y--;
        }
        fwdDegree -= 1;
    }

    void addToDomain(int val, int x, int y, Pair[][] row, Pair[][] col){
        if(!domain[val - 1] && source[val - 1].equals(new Pair(x, y))){
            domain[val - 1] = true;
            noOfLegalValues += 1;
            row[x][val - 1].y++;
            col[x][val - 1].y++;
        }
        fwdDegree += 1;
    }


    @Override
    public String toString() {
        return value + " " + noOfLegalValues;
    }
}

class Pair implements Comparable<Pair>{
    int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return x == pair.x && y == pair.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


    @Override
    public int compareTo(Pair o) {
        return Integer.compare(y, o.y);
    }

    @Override
    public String toString() {
        return "" + y;
    }
}

class ConstraintGraphNode {

    Variable[][] variables;
    int x, y;
    int noOfAssigned;
    Pair[][] rowValueCount;
    Pair[][] colValueCount;

    ConstraintGraphNode(int n){
        variables = new Variable[n][n];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n;j++)
                variables[i][j] = new Variable(n);
        noOfAssigned = 0;
        rowValueCount = new Pair[n][n];
        colValueCount = new Pair[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                rowValueCount[i][j] = new Pair(j, 10);
                colValueCount[i][j] = new Pair(j, 10);
            }
        }
    }

    ConstraintGraphNode(ConstraintGraphNode parent){
        this.variables = parent.variables;
        this.noOfAssigned = parent.noOfAssigned;
        x = 0;
        y = 0;
        this.rowValueCount = parent.rowValueCount;
        this.colValueCount = parent.colValueCount;
    }

    void VAH1(){
        int min = Integer.MAX_VALUE;
        int n = variables.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0 && variables[i][j].noOfLegalValues <= min){
                    min = variables[i][j].noOfLegalValues;
                    x = i;
                    y = j;
                }
            }
        }
    }

    void VAH2(){
        int max = 0;
        int n = variables.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0 && variables[i][j].fwdDegree > max){
                    max = variables[i][j].fwdDegree;
                    x = i;
                    y = j;
                }
            }
        }
    }

    void VAH3(){
        int min = Integer.MAX_VALUE;
        int n = variables.length;
        int max = -1;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0){
                    if(variables[i][j].noOfLegalValues < min){
                        min = variables[i][j].noOfLegalValues;
                        max = variables[i][j].fwdDegree;
                        x = i;
                        y = j;
                    }
                    else if(variables[i][j].noOfLegalValues == min){
                        if(variables[i][j].fwdDegree > max){
                            min = variables[i][j].noOfLegalValues;
                            max = variables[i][j].fwdDegree;
                            x = i;
                            y = j;
                        }
                    }
                }
            }
        }
    }

    void VAH4(){
        double min = Double.MAX_VALUE;
        int n = variables.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0){
                    if((double)(variables[i][j].noOfLegalValues / variables[i][j].fwdDegree) < min){
                        min = (double)variables[i][j].noOfLegalValues / variables[i][j].fwdDegree;
                        x = i;
                        y = j;
                    }
                }
            }
        }
    }

    void VAH5(){
        int n = variables.length;
        int k = ThreadLocalRandom.current().nextInt(0, n);
        int l = ThreadLocalRandom.current().nextInt(0, n);
        for(int i = l; i < n; i++){
            if (variables[k][i].value == 0) {
                x = k;
                y = i;
                return;
            }
        }
        for(int i = k + 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (variables[i][j].value == 0) {
                    x = i;
                    y = j;
                    return;
                }
            }
        }
        for(int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                if (variables[i][j].value == 0) {
                    x = i;
                    y = j;
                    return;
                }
            }
        }
        for(int i = 0; i < l; i++){
            if (variables[k][i].value == 0) {
                x = k;
                y = i;
                return;
            }
        }
    }

    void VAH6(){
        int min= Integer.MAX_VALUE;
        int n = variables.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0 && variables[i][j].fwdDegree <= min){
                    min = variables[i][j].fwdDegree;
                    x = i;
                    y = j;
                }
            }
        }
    }

    void VAH7(){
        int min = Integer.MAX_VALUE;
        int n = variables.length;
        int minFwd = Integer.MAX_VALUE;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0){
                    if(variables[i][j].noOfLegalValues < min){
                        min = variables[i][j].noOfLegalValues;
                        minFwd = variables[i][j].fwdDegree;
                        x = i;
                        y = j;
                    }
                    else if(variables[i][j].noOfLegalValues == min){
                        if(variables[i][j].fwdDegree < minFwd){
                            min = variables[i][j].noOfLegalValues;
                            minFwd = variables[i][j].fwdDegree;
                            x = i;
                            y = j;
                        }
                    }
                }
            }
        }
    }

    void VAH8(){
        double min = Double.MAX_VALUE;
        int n = variables.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(variables[i][j].value == 0){
                    if(min >= (double)(variables[i][j].noOfLegalValues * variables[i][j].fwdDegree)){
                        min = (double)variables[i][j].noOfLegalValues * variables[i][j].fwdDegree;
                        x = i;
                        y = j;
                    }
                }
            }
        }
    }

    boolean assignVal(int val){
        variables[x][y].value = val;
        noOfAssigned += 1;
        boolean domainEmpty = false;
        for(int i = 0; i < variables.length; i++){
            if(i != x){
                variables[i][y].removeFromDomain(val, x, y, rowValueCount, colValueCount);
                if(variables[i][y].noOfLegalValues == 0) domainEmpty = true;
            }
            if(i != y){
                variables[x][i].removeFromDomain(val, x, y, rowValueCount, colValueCount);
                if(variables[x][i].noOfLegalValues == 0) domainEmpty = true;
            }
        }
        return domainEmpty;
    }

    void unAssignVal(int val){
        variables[x][y].value = 0;
        noOfAssigned -= 1;
        for(int i = 0; i < variables.length; i++){
            if(i != x){
                variables[i][y].addToDomain(val, x, y, rowValueCount, colValueCount);
            }
            if(i != y){
                variables[x][i].addToDomain(val, x, y, rowValueCount, colValueCount);
            }
        }
    }

    void assignVal(int val, int k, int l){
        variables[k][l].value = val;
        noOfAssigned += 1;
        for(int i = 0; i < variables.length; i++){
            if(i != k){
                variables[i][l].removeFromDomain(val, k, l, rowValueCount, colValueCount);
            }
            if(i != l){
                variables[k][i].removeFromDomain(val, k, l, rowValueCount, colValueCount);
            }
        }
    }

    boolean checkCompletion(){
        return noOfAssigned == variables.length * variables.length;
    }


}

public class ConstraintGraph {
    int n;
    ConstraintGraphNode root;
    int varChoice;
    boolean fwdCheck;
    int backtrackCount;
    int nodeCount;

    ConstraintGraph(int n){
        this.n = n;
        root = new ConstraintGraphNode(n);
        backtrackCount = 0;
        nodeCount = 0;
    }

    void solve(){
        recSolve(root);
        int N = root.variables.length;

        // Vector of N sets corresponding
        // to each row.
        HashSet<Integer>[] rows = new HashSet[N];

        // Vector of N sets corresponding
        // to each column.
        HashSet<Integer>[] cols = new HashSet[N];

        for(int i = 0; i < N; i++)
        {
            rows[i] = new HashSet<Integer>();
            cols[i] = new HashSet<Integer>();
        }

        // Number of invalid elements
        int invalid = 0;

        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                rows[i].add(root.variables[i][j].value);
                cols[j].add(root.variables[i][j].value);
                if (root.variables[i][j].value > N || root.variables[i][j].value <= 0)
                {
                    invalid++;
                }
            }
        }

        // Number of rows with
        // repetitive elements.
        int numrows = 0;

        // Number of columns with
        // repetitive elements.
        int numcols = 0;

        // Checking size of every row
        // and column
        for(int i = 0; i < N; i++)
        {
            if (rows[i].size() != N)
            {
                numrows++;
            }
            if (cols[i].size() != N)
            {
                numcols++;
            }
        }

        if (numcols == 0 &&
                numrows == 0 && invalid == 0)
            System.out.print("YES" + "\n");
        else
            System.out.print("NO" + "\n");
    }

    ConstraintGraphNode recSolve(ConstraintGraphNode node){
        nodeCount++;
        if(node.checkCompletion()){
            return node;
        }

        chooseVariable(node);

        int x = node.x;
        int y = node.y;

        Pair[] values = new Pair[n];
        for(int i = 0; i < n; i++){
            values[i] = new Pair(i,node.rowValueCount[x][i].y + node.colValueCount[y][i].y);
        }

        Arrays.sort(values);

        for(int i = 0; i < n; i++){
            if(node.variables[x][y].domain[values[i].x]){
                boolean failure = node.assignVal(values[i].x + 1);
                ConstraintGraphNode newNode = new ConstraintGraphNode(node);
                ConstraintGraphNode result;

                if(fwdCheck){
                    if(!failure){
                        result = recSolve(newNode);
                        if(result != null){
                            return result;
                        }
                    }
                }
                else{
                    result = recSolve(newNode);
                    if(result != null){
                        return result;
                    }
                }
                node.unAssignVal(values[i].x + 1);
            }
        }
        backtrackCount++;
        return null;
    }

    void chooseVariable(ConstraintGraphNode node){
        if(varChoice == 1){
            node.VAH1();
        }
        else if(varChoice == 2){
            node.VAH2();
        }
        else if(varChoice == 3){
            node.VAH3();
        }
        else if(varChoice == 4){
            node.VAH4();
        }
        else if(varChoice == 5){
            node.VAH5();
        }
        else if(varChoice == 6){
            node.VAH6();
        }
        else if(varChoice == 7){
            node.VAH7();
        }
        else if(varChoice == 8){
            node.VAH8();
        }
    }
}


