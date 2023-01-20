import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "inputs/car-s-91.";
        File courseFile = new File(path + "crs");
        File studentFile = new File(path + "stu");
        int n = Integer.parseInt(tail(courseFile).split(" ")[0]);
        ArrayList<Course> courseGraph = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        getInput(courseFile, studentFile, courseGraph, students);

        courseGraph.sort((o1, o2) -> Integer.compare(o2.adjacencyList.size(), o1.adjacencyList.size()));
        System.out.println(courseGraph);
        //courseGraph.sort((o1, o2) -> Integer.compare(o2.enrolled, o1.enrolled));
        //Collections.shuffle(courseGraph);
        for(Course c: courseGraph){
            c.setSlot(n);
        }


        //for(int i = 0; i < n; i++){
        //    getMostSaturatedNode(courseGraph, n).setSlot(n);
        //}

        //showOutput(courseGraph);

        System.out.println(testConstraint(students, n));

    }

    static Course getMostSaturatedNode(ArrayList<Course> courseGraph, int n){
        Course maxSat = null;
        int max = Integer.MIN_VALUE;
        int maxUnassignedDegree = Integer.MIN_VALUE;
        for(Course c : courseGraph){
            if(c.slot != -1) continue;
            int[] slots = new int[n];
            int satDegree = 0;
            int unassignedDegree = c.adjacencyList.size();
            for(Course c2 : c.adjacencyList){
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

    public static String tail( File file ) {
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

    public static void getInput(File courseFile, File studentFile, ArrayList<Course> courseGraph, ArrayList<Student> students) throws IOException {
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
                    if(!courseGraph.get(i - 1).adjacencyList.contains(courseGraph.get(k - 1))){
                        courseGraph.get(i - 1).adjacencyList.add(courseGraph.get(k - 1));
                    }
                }
            }
            j++;
        }
    }

    public static void showOutput(ArrayList<Course> courseGraph){
        courseGraph.sort(Comparator.comparingInt(o -> o.slot));
        for(Course c: courseGraph){
            System.out.println(c.courseNo + " " + c.slot);
        }

        System.out.println("\n");
    }

}
