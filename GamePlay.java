import java.util.*;
  
//creates a game on a single computer with four single-user controlled players
//currently only works on the command line, GUI not yet implemented

public class GamePlay {
        public static void main(String args[]) {

                //initialize deck, draw pile, and discard pile
                //deck becomes irrelevant and empty, should we leave this to garbage collector?
                Deck gamePiles = new Deck();

                //initialize the four players
                //rules says we should pass one card out to each player in a circle until all have seven, should we do something about this?
                Player dealer = new Player("Maggie", gamePiles);
                Player second = new Player("Julia", gamePiles);
                Player third = new Player("Riley", gamePiles);
                Player fourth = new Player("Tara", gamePiles);

                //after cards are dealt out, top draw pile card goes onto discard pile
                gamePiles.discardCard(gamePiles.drawPile.remove());
        }
}
