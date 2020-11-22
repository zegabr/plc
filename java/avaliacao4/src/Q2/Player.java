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

    /**
     * check if it has free chairs
     * @return
     */
    public synchronized boolean haveChairsToSit() {
        return !this.game.getChairs().isEmpty();
    }

    /**
     * count number of freee chairs
     * @return
     */
    public synchronized int countChairs() {
        return game.getChairs().size();
    }

    /**
     * start player cycle
     */
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

    /**
     * check if player is already sitting
     * @return
     */
    private boolean isSitting() {
        return chair != null;
    }

    /**
     * return player's current chair
     * @return
     */
    public AtomicBoolean getChair() {
        return chair;
    }

    /**
     * make player leave the chair
     */
    public void leaveChair() {
        chair = null;
    }
}
