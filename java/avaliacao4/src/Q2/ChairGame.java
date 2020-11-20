package Q2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChairGame {
    List<Player> players;
    List<AtomicBoolean> chairs;


    public ChairGame(Integer N){
        players = new ArrayList<>();
        chairs = new ArrayList<>();

        for(int i = 0; i < N; i++){
            players.add(new Player(i));
            if(i > 0) chairs.add(new AtomicBoolean(false));
        }
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }
    public static void main(String[] args) {
        int N = 10;
        ChairGame chairGame = new ChairGame(N);
        while(chairGame.getPlayers().size() > 1){

            chairGame.playRodada();
            Player looser = chairGame.findLooser();

            System.out.println("Player "+looser.getIndex() + " perdeu");

            chairGame.emptyChairs();
            chairGame.removePlayer(looser);
        }
    }

    private void emptyChairs() {
        for(Player player : this.getPlayers()){
            if(player.getChair() != null){
                player.leaveChair();
            }
        }
    }

    private Player findLooser() {
        Player looser = null;
        for(Player player : this.getPlayers()){
            if(player.getChair() == null){
                looser = player;
                break;
            }
        }
        return looser;
    }

    private void playRodada() {
        int ocupied = 0;
        while(ocupied < this.getChairs().size()){
            for(Player player : this.getPlayers()){
                ocupied += player.tryGetAChair(this.getChairs());
            }
        }
    }

    private List<AtomicBoolean> getChairs() {
        return this.chairs;
    }

    private List<Player> getPlayers() {
        return this.players;
    }
}
