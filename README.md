# PepseGame
A Java based game - Travel in infinite world, collect as many coins as you can, avoid the dragons and their fire blasting, stay alive!

yarden
amithalbreich


1)
Differences between UML before and after:
In the beginning, we only had the packages that were given
in the supplied materials. For example, the Bonus Package
was not present in the original UML diagram, but it has
been included in the final submission. The Tree package
remained unchanged in the UML, as it became clear to us
that a single class could not handle the creation of all
the tree elements, which will be further explained in a
later section.

The dependencies between the different packages have
undergone significant changes. The PepseGameManager is
now responsible for managing and creating all game
components, and to do so, it must have references to the
relevant objects. Additionally, we added counters and bonus
objects such as Coins and Graphic Life, and grouped them
into the Bonus package.


2)
The infiniteWorld function is responsible for creating
new terrain, trees and coins in the game, as the player's
avatar moves, in order to create an "infinite world"
effect. It does so by using the avatar's x-coordinate as
the trigger to create new terrain, tree and coin objects
and updating the bounding values: leftBound, rightBound
and removing the old objects that are out of range.


3)
**Dilemas we had or design decisions we had to make during the building of the Game Engine:**

In the beggining we decided to start the Game build from scratch and implement the most easy parts such as Sky, Sun, Sun Halo and terrain
without the Perlin Noise so we will first have the basis for the game. Later on we decided to deal with implementing the Avatar and add it's 
functionality and movement. The next stage was to implement the trees using the Tree Factory - we left the behaviour of the leaves to the end
of the tree implementation because it included scheduled task and use of many transitions which we had to explore and reailze.
We wanted to make our Game unique by adding more behaviour to the Avatar such as fire Blasts to defeat a dragon that is summoned 
according to the user's progress in the game which is made by collecting coins. The more coins the user collects - 
the more times the Dragon will be summoned.
We had Dilemas about how to implement the classes we added to the Bonus Package - what object will hold different information about the
game such as coinCounter, livesCounter etc...

We had a Dilema about how to make the game run faster given the InfiniteWorld implementation which was at first very heavy and made the
gameplay very stuck - we used a big "window" that saves 3 times of the original's game window size filled with Game Objects
and it made the gameplay very stuck. We fixed it by narrowing the window to 2 times the original window size filled with Object and in this
case we reduced the amount of Game Objects in a specific delta time - and it made the GamePlay more smooth.


**BONUS:**

We added extra functionallity to the Avatar - he can shout blasts.
The blasts shooting skill
is given to the Avatar so he could defeat a Dragon boss.

Blast - the user can use the UP and DOWN keys to rotate the
anle of the Blast he fires
Counter clockwise and  clockwise accordingly and aim to the
Dragon in order to defeat it.

Dragon - will be summoned after the user collects a certain amount
 of coins which represnts
the progress in the game.
BossHealthBar - Life span of the summoned boss - in this case a Dragon
- can be expended to
further bosses.
DragonFire - a simple extend of Game Object - the Dragon will fire in
random directions
according to the amount of Blasts the user uses. For each user's Blast
the Dragon will
fire one DragonFire Object in a random direction.


FireBlastMaker - Responsible for the management and logics of the Blasts
and DragonFire
Game Objects - create them in the correct location.

Coin and CoinMaker - The coin is a Game Object that the User needs to collect  -
the coins
will be regenerated every once in a while if the Avatar moves to the left
or to the right
and reveals more of the Infinite World.
CoinMaker class is responsible for all the logics of the coins regenaration.

Additional Counters added:
CoinNumericCounter which counts the coins the user collected so far.
NumericEnergyCunter - represents the user stamina/Energy Level according to
the flight
amount he used - will change colors from green to yellow to red according to the current
energy level to alert the user.
GraphicLivesCounter - An extension that can later on be expanded to end game
logic as part of
the Game Engine (we didn't want the game to be closed if Lives Count reaches 0 because
we wanted to follow the exercises instructions and not close the Game Window).

