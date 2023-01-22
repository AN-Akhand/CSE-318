import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] paths = {"car-f-92", "car-s-91", "kfu-s-93", "tre-s-92", "yor-f-83"};
        //String[] paths = {"car-s-91"};
        BufferedWriter[] writers = new BufferedWriter[5];
        for(int i = 1; i <= 5; i++){
            //writers[i - 1] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("temp" + i +".csv")));
            writers[i - 1] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("scheme" + i +".csv")));
            writers[i - 1].write("Benchmark Data, Timeslots, After Largest Degree, After Kempe, After Pairswap\n");
        }
        for(String path : paths) {
            File courseFile = new File(path + ".crs");
            File studentFile = new File(path + ".stu");
            int n = Integer.parseInt(tail(courseFile).split(" ")[0]);
            ArrayList<Course> courseGraph = new ArrayList<>();
            ArrayList<Student> students = new ArrayList<>();
            getInput(courseFile, studentFile, courseGraph, students);

            writers[0].write(path + ",");
            applyScheme1(courseGraph, students, 3000, n, writers[0]);
            writers[1].write(path + ",");
            applyScheme2(courseGraph, students, 3000, n, writers[1]);
            writers[2].write(path + ",");
            applyScheme3(courseGraph, students, 3000, n, writers[2]);
            writers[3].write(path + ",");
            applyScheme4(courseGraph, students, 3000, n, writers[3]);
            writers[4].write(path + ",");
            applyScheme5(courseGraph, students, 3000, n, writers[4]);
        }

        for(int i = 1; i <= 5; i++){
            writers[i - 1].close();
        }

    }

    static void applyScheme1(ArrayList<Course> courseGraph, ArrayList<Student> students, int pertHeu, int n, BufferedWriter writer) throws IOException {
        doScheduling(courseGraph, 1, n);
        courseGraph.sort(Comparator.comparingInt(o -> o.slot));
        System.out.println(courseGraph.get(n - 1).slot + 1);
        writer.write(courseGraph.get(n - 1).slot + 1 + ",");
        applyPerturbativeHeuristics(courseGraph, students, pertHeu, 0, writer);
        reload(courseGraph);
    }
    static void applyScheme2(ArrayList<Course> courseGraph, ArrayList<Student> students, int pertHeu, int n, BufferedWriter writer) throws IOException {
        doScheduling(courseGraph, 2, n);
        courseGraph.sort(Comparator.comparingInt(o -> o.slot));
        System.out.println(courseGraph.get(n - 1).slot + 1);
        writer.write(courseGraph.get(n - 1).slot + 1 + ",");
        applyPerturbativeHeuristics(courseGraph, students, pertHeu, 0, writer);
        reload(courseGraph);
    }
    static void applyScheme3(ArrayList<Course> courseGraph, ArrayList<Student> students, int pertHeu, int n, BufferedWriter writer) throws IOException {
        doScheduling(courseGraph, 3, n);
        courseGraph.sort(Comparator.comparingInt(o -> o.slot));
        System.out.println(courseGraph.get(n - 1).slot + 1);
        writer.write(courseGraph.get(n - 1).slot + 1 + ",");
        applyPerturbativeHeuristics(courseGraph, students, pertHeu, 0, writer);
        reload(courseGraph);
    }
    static void applyScheme4(ArrayList<Course> courseGraph, ArrayList<Student> students, int pertHeu, int n, BufferedWriter writer) throws IOException {
        doScheduling(courseGraph, 4, n);
        courseGraph.sort(Comparator.comparingInt(o -> o.slot));
        System.out.println(courseGraph.get(n - 1).slot + 1);
        writer.write(courseGraph.get(n - 1).slot + 1 + ",");
        applyPerturbativeHeuristics(courseGraph, students, pertHeu, 0, writer);
        reload(courseGraph);
    }

    static void applyScheme5(ArrayList<Course> courseGraph, ArrayList<Student> students, int pertHeu, int n, BufferedWriter writer) throws IOException {
        doScheduling(courseGraph, 2, n);
        courseGraph.sort(Comparator.comparingInt(o -> o.slot));
        System.out.println(courseGraph.get(n - 1).slot + 1);
        writer.write(courseGraph.get(n - 1).slot + 1 + ",");
        applyPerturbativeHeuristics(courseGraph, students, pertHeu, 1, writer);
        reload(courseGraph);
    }

    static void applyPerturbativeHeuristics(ArrayList<Course> courseGraph, ArrayList<Student> students, int m, int penalty, BufferedWriter writer) throws IOException {
        Random random = new Random(123);
        double origPenalty = getPenalty(students, penalty);
        double newPenalty;

        System.out.println(origPenalty);
        writer.write(origPenalty + ",");


        for(int i = 0; i < m; i++){
            Course c = courseGraph.get(random.nextInt(courseGraph.size()));
            int origSlot = c.slot;
            if(c.conflictList.size() == 0) continue;
            int swapSlot = c.conflictList.get(random.nextInt(c.conflictList.size())).slot;
            doKempeChainInterchange(c, swapSlot);
            newPenalty = getPenalty(students, penalty);
            if(newPenalty > origPenalty){
                refresh(courseGraph);
                doKempeChainInterchange(c, origSlot);
                newPenalty = origPenalty;
            }
            origPenalty = newPenalty;
            refresh(courseGraph);
        }
        System.out.println(origPenalty);
        writer.write(origPenalty + ",");

        for(int i = 0; i < m; i++){
            int j = random.nextInt(courseGraph.size());
            int k = random.nextInt(courseGraph.size());
            while(i == k){
                j = random.nextInt(courseGraph.size());
                k = random.nextInt(courseGraph.size());
            }
            Course u = courseGraph.get(j);
            Course v = courseGraph.get(k);
            if(doPairSwap(u, v)){
                newPenalty = getPenalty(students, 0);
                if(newPenalty > origPenalty){
                    doSwap(u, v);
                    newPenalty = origPenalty;
                }
                origPenalty = newPenalty;
            }
        }
        System.out.println(origPenalty);
        writer.write(origPenalty + "\n");

    }

    static void doScheduling(ArrayList<Course> courseGraph, int heuristic, int n){
        if(heuristic == 2){
            for(int i = 0; i < n; i++){
                getMostSaturatedNode(courseGraph, n).setSlot(n);
            }
            return;
        }
        else if(heuristic == 1){
            courseGraph.sort((o1, o2) -> Integer.compare(o2.conflictList.size(), o1.conflictList.size()));
        }
        else if(heuristic == 3){
            courseGraph.sort((o1, o2) -> Integer.compare(o2.enrolled, o1.enrolled));
        }
        else if(heuristic == 4){
            Collections.shuffle(courseGraph, new Random(123));
        }
        for(Course c: courseGraph){
            c.setSlot(n);
        }
    }

    static void doKempeChainInterchange(Course root, int swapSlot){
        int origSlot = root.slot;
        root.slot = swapSlot;
        root.swapped = true;
        for(Course c : root.conflictList){
            if(c.slot == swapSlot && !c.swapped){
                doKempeChainInterchange(c, origSlot);
            }
        }
    }

    static boolean doPairSwap(Course u, Course v){
        if(u == v) return false;
        if(u.slot == v.slot) return false;
        for(Course c : u.conflictList){
            if(c.slot == v.slot) return false;
        }
        for(Course c : v.conflictList){
            if(c.slot == u.slot) return false;
        }
        doSwap(u, v);
        return true;
    }

    static void doSwap(Course u, Course v){
        int temp = u.slot;
        u.slot = v.slot;
        v.slot = temp;
    }

    static double getPenalty(ArrayList<Student> students, int penalty){
        if(penalty == 0) return getAverageExponentialPenalty(students);
        else return getAverageLinearPenalty(students);
    }

    static double getAverageLinearPenalty(ArrayList<Student> students){
        double penalty = 0;
        for(Student s : students){
            penalty += s.getLinearPenalty();
        }
        return penalty / students.size();
    }

    static double getAverageExponentialPenalty(ArrayList<Student> students){
        double penalty = 0;
        for(Student s : students){
            penalty += s.getExponentialPenalty();
        }
        return penalty / students.size();
    }

    static void refresh(ArrayList<Course> courseGraph){
        for(Course c : courseGraph){
            c.swapped = false;
        }
    }

    static void reload(ArrayList<Course> courseGraph){
        for(Course c : courseGraph){
            c.slot = -1;
        }
        courseGraph.sort(Comparator.comparingInt(o -> o.courseNo));
    }

    static Course getMostSaturatedNode(ArrayList<Course> courseGraph, int n){
        Course maxSat = null;
        int max = Integer.MIN_VALUE;
        int maxUnassignedDegree = Integer.MIN_VALUE;
        for(Course c : courseGraph){
            if(c.slot != -1) continue;
            int[] slots = new int[n];
            int satDegree = 0;
            int unassignedDegree = c.conflictList.size();
            for(Course c2 : c.conflictList){
                if(c2.slot == -1) continue;
                slots[c2.slot]++;
            }
            for (int slot : slots) {
                if (slot != 0) {
                    satDegree++;
                    unassignedDegree -= slot;
                }
            }
            if(satDegree > max){
                max = satDegree;
                maxUnassignedDegree = unassignedDegree;
                maxSat = c;
            }
            else if(satDegree == max){
                if(unassignedDegree >= maxUnassignedDegree){
                    maxUnassignedDegree = unassignedDegree;
                    maxSat = c;
                }
            }
        }
        return maxSat;
    }


    static boolean testConstraint(ArrayList<Student> students, int n){
        for(Student s : students){
            if(!s.testConstraint(n)) return false;
        }
        return true;
    }

    static String tail( File file ) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile( file, "r" );
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for(long filePointer = fileLength; filePointer != -1; filePointer--){
                fileHandler.seek( filePointer );
                int readByte = fileHandler.readByte();

                if( readByte == 0xA ) {
                    if( filePointer == fileLength ) {
                        continue;
                    }
                    break;

                } else if( readByte == 0xD ) {
                    if( filePointer == fileLength - 1 ) {
                        continue;
                    }
                    break;
                }

                sb.append( ( char ) readByte );
            }

            return sb.reverse().toString();
        } catch( IOException e ) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileHandler != null )
                try {
                    fileHandler.close();
                } catch (IOException e) {
                    /* ignore */
                }
        }
    }

    static void getInput(File courseFile, File studentFile, ArrayList<Course> courseGraph, ArrayList<Student> students) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(courseFile)));
        String line;
        int j = 0;
        while((line = reader.readLine()) != null){
            if(line.equals("")) continue;
            j++;
            courseGraph.add(new Course(j, Integer.parseInt(line.split(" ")[1])));
        }
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(studentFile)));
        j = 0;
        while((line = reader.readLine()) != null){
            if(line.equals("")) continue;
            int[] courses = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            students.add(new Student(j));
            for(int i : courses){
                students.get(j).courses.add(courseGraph.get(i - 1));
                for(int k : courses){
                    if(k == i) continue;
                    if(!courseGraph.get(i - 1).conflictList.contains(courseGraph.get(k - 1))){
                        courseGraph.get(i - 1).conflictList.add(courseGraph.get(k - 1));
                    }
                }
            }
            j++;
        }
    }

    static void showOutput(ArrayList<Course> courseGraph){
        for(Course c: courseGraph){
            System.out.println(c.courseNo + " " + c.slot);
        }

        System.out.println("\n");
    }

}
