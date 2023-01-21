import java.util.ArrayList;

public class Course{
    ArrayList<Course> conflictList;
    int courseNo;
    int slot;
    int enrolled;
    boolean swapped;
    Course(int n, int m){
        courseNo = n;
        conflictList = new ArrayList<>();
        slot = -1;
        enrolled = m;
        swapped = false;
    }

    @Override
    public String toString() {
        return "Course{" +
                "adjacencyListSize=" + conflictList.size() +
                ", courseNo=" + courseNo +
                ", slot=" + slot +
                ", enrolled=" + enrolled +
                '}';
    }

    void setSlot(int n) {
        int[] slots = new int[n];
        for(Course c : conflictList){
            if(c.slot == -1) continue;
            slots[c.slot] = 1;
        }

        for(int i = 0; i < slots.length; i++){
            if(slots[i] == 0){
                slot = i;
                return;
            }
        }
    }
}
