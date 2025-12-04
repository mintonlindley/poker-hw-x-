Group members: Lindley Minton, Mehna Lakshminarayanan, Morgan Gurtis 

Begin will instantiate a game of Blackjack, as the 'default' game. To exit a Window, simply click the top left close button like you would to close the window; this will repeon the Menu. 
The interface should be self-explanatory: The menu controls what game you play and contains the rules for each game. The controls for each game are on the bottom of the window.
Features: Multiple rule implementations (Blackjack AND Texas Hold'em) and GUI visuals (cards, chips, option to change background color)

Contributions:
Morgan Gurtis: Morgan fully wrote both BJController.java, as well as BJHand.java. BlackjackWindow was created by Lindley and Morgan rewrote it to integrate our contributions and add a few extra features. Morgan also wrote the CasinoApp.java and rearranged Menu.java, since initially the main method was in Menu.java but we agreed it would be easier to separate these methods into two files. Also redesigned CardImages.java and ChipImages.java to use a classpath implementation, but maintained the same GUI setup that Lindley had originally. 

Lindley Minton: Lindley wrote Card.java, CardPanel.java, Deck.java, PlayerPanel.java, RulesPage.java. Mehna helped her with Betting.java and implementing the Texas logic for TexasWindow.java. Morgan helped with image loading in CardImages.Java and ChipImages.java, Lindley wrote the base code of these two files. The user has the option to change the background color of the game, as well as the color of the back of the card. Lindley wrote BlackJackWindow.java with help from Morgan implementing the blackjack game logic. Lindley wrote Menu.java and Morgan helped implement that within the main program (CasinoApp.java).

Mehna Lakshminarayanan:Mehna wrote the full implementations of TexasGameWindow.java, TexasHoldemGame.java, and ChecksHand.java, creating the GUI framework and hand-evaluation logic for Texas Hold’em. She also worked with Lindley on Betting.java, helping integrate across both Blackjack and Texas Hold’em. In addition, Mehna assisted with connecting the card image system to the Texas Hold’em logic. 
