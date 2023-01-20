import java.util.ArrayList;

public class Course{
    ArrayList<Course> adjacencyList;
    int courseNo;
    int slot;
    int enrolled;
    Course(int n, int m){
        courseNo = n;
        adjacencyList = new ArrayList<>();
        slot = -1;
        enrolled = m;
    }

    @Override
    public String toString() {
        return "Course{" +
                "adjacencyListSize=" + adjacencyList.size() +
                ", courseNo=" + courseNo +
                ", slot=" + slot +
                ", enrolled=" + enrolled +
                '}';
    }

    void setSlot(int n) {
        int[] slots = new int[n];
        for(Course c : adjacencyList){
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
