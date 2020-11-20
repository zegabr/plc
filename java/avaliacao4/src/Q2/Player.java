package Q2;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Thread{

    AtomicBoolean chair = null;
    int index;

    public Player(int index){
        this.index = index;
    }

    public AtomicBoolean getChair(){
        return this.chair;
    }

    public void leaveChair(){
        if(chair == null) return;
        // unlock chair

    }

    public int tryGetAChair(List<AtomicBoolean> chairList){
        if(isSitting()) return 0;

        int N = chairList.size();
        //get random index from 0 to N-1
//        if(chair is locked) return 0;
//        lock chair
        return 1;
    }

    private boolean isSitting() {
        return this.chair != null;
    }

    public Integer getIndex() {
        return this.index;
    }
}
