package Q2;



import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChairGame{
    Lock preparing = new ReentrantLock();
    List<AtomicBoolean> chairs = new ArrayList<>();
    List<Player> players = new ArrayList<>();
    int N;
    Object nextRound = new Object();

    public ChairGame(int N) {
        this.N = N;
        for (int i = 0; i < N; i++) {
            players.add(new Player(i + 1, this));
        }
    }

    /**
     * start game
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        System.out.println("Starting players");
        for (Player player : players){
            player.start();
        }

        while (isRunning()) {
            playRound();
        }

        for (Player player : players) {
            try {
                player.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * simulates one round
     * @throws InterruptedException
     */
    private void playRound() throws InterruptedException {
        preparing.lock();

        if (chairs.size() > 0){
            preparing.unlock();
            return;
        }

        int numberOfChairs = players.size() - 1;
        chairs = new ArrayList<>();
        for (int i = 1; i <= numberOfChairs; i++) {
            chairs.add(new AtomicBoolean(false));
        }
//        System.out.println("Starting a round with " + players.size() + " players and "+ chairs.size()+" chairs");
        preparing.unlock();

        synchronized (nextRound){
            nextRound.wait(); // wait for other threads call notify when chairs is empty
        }

        preparing.lock();

        players.remove(findLooser());
        makePlayersLeaveChairs();

        preparing.unlock();
    }

    /**
     * find player without chair
     * @return
     */
    private Player findLooser() {
        Player looser = null;
        for (Player p : players){
            if (p.getChair() == null){
                looser = p;
                break;
            }
        }
        System.out.println("player "+looser.index + " lost");
        return looser;
    }

    /**
     * make players leave all chairs
     */
    private void makePlayersLeaveChairs() {
        for(Player player : players)
            player.leaveChair();
    }

    /**
     * check if game is running
     * @return
     */
    public boolean isRunning() {
        return players.size() > 1;
    }

    /**
     * return list of chairs
     * @return
     */
    public List<AtomicBoolean> getChairs() {
        return chairs;
    }

    /**
     * remove one chair of the list by reference
     * @param randomChair
     */
    public void removeChair(AtomicBoolean randomChair) {
        preparing.lock();
        synchronized (nextRound){
            chairs.remove(randomChair);
            if(chairs.isEmpty()) {
                nextRound.notify();
            }
        }
        preparing.unlock();
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("How many players is playing now? (insert an integer)");
        int N = in.nextInt();

        ChairGame chairGame = new ChairGame(N);
        try {
            chairGame.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Player " + chairGame.players.get(0).index + " won.");
    }
}
