import java.util.ArrayList;
import java.util.Comparator;

public class Student {
    int stNo;
    ArrayList<Course> courses;
    Student(int n){
        stNo = n;
        courses = new ArrayList<>();
    }

    boolean testConstraint(int n){
        int[] slots = new int[n];
        for(Course c : courses){
            slots[c.slot]++;
            if(slots[c.slot] > 1) {
                return false;
            }
        }
        return true;
    }

    double getLinearPenalty(){
        double penalty = 0;
        courses.sort(Comparator.comparingInt(o -> o.slot));
        int n = courses.size();
        for(int i = 0; i < n - 1; i++){
            for(int j = i + 1; j < n; j++) {
                if (i == j) continue;
                int gap = Math.abs(courses.get(j).slot - courses.get(i).slot);
                if (gap == 0) {
                    System.out.println("Failed");
                    System.exit(1);
                }
                if(gap <= 5) penalty += (2 * (5 - gap));
            }
        }
        return penalty;
    }

    double getExponentialPenalty(){
        double penalty = 0;
        courses.sort(Comparator.comparingInt(o -> o.slot));
        int n = courses.size();
        for(int i = 0; i < n - 1; i++){
            for(int j = i + 1; j < n; j++) {
                if(i == j) continue;
                int gap = Math.abs(courses.get(j).slot - courses.get(i).slot);
                if (gap == 0) {
                    System.out.println("Failed");
                    System.exit(1);
                }
                if (gap <= 5) penalty += Math.pow(2, 5 - gap);
            }
        }
        return penalty;
    }

}
