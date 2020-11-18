package Q1;

public class Competitor extends Thread {


    public boolean isSolving() {
        //return this. is running
        return false;
    }

    public void trySolveWith(/*lockable PC*/){//run this in a thread
        //sleep 1500 ms

        //wait to PC be available
        //lock PC
        //sleep 1500 ms
        //unlock PC
    }
}
