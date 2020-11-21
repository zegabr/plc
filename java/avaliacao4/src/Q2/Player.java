package Q2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Player extends Thread {
    Integer index;
    ChairGame game;
    AtomicBoolean chair;

    public Player(int i, ChairGame chairGame) {
        index = i;
        game = chairGame;
        chair = null;
    }

    public synchronized boolean haveChairsToSit() {
        return !this.game.getChairs().isEmpty();
    }

    public synchronized int countChairs() {
        return game.getChairs().size();
    }

    public void run() {
        System.out.println("Player " + index + " starded.");
        Random random = new Random();
        while (game.isRunning()) {
            while (!isSitting() && haveChairsToSit()) {
                synchronized (game) {
                    int n = countChairs();
                    if(n == 0) break;
                    int randomIndex = random.nextInt(n);
                    if (countChairs() == 0) break;
                    AtomicBoolean randomChair = game.getChairs().get(randomIndex);
                    if (randomChair != null && !randomChair.getAndSet(true)) {
                        chair = randomChair;//now is sitting
                        game.removeChair(randomChair);//remove from game
                    }
                }
            }
        }
    }

    private boolean isSitting() {
        return chair != null;
    }

    public AtomicBoolean getChair() {
        return chair;
    }

    public void leaveChair() {
        chair = null;
    }
}
