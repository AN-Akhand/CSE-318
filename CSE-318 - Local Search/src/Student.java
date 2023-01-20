import java.util.ArrayList;

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

}
