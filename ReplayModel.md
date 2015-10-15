  * A Replay is a sequence of Actions taken by a set of Avatars across a number of Rounds with some ChanceCards.
  * Avatars are always specified in clockwise-deal-last order, you can access them with the Replay.players() method.
  * You can access the chance cards with Replay.cards().
  * You can access actions with `Replay.action(forPlayer: Avatar): Iterable<Action>`.
  * You can build up a Replay using `addHole(Avatar, Hole)` and `addAction(Avatar, Action)`.<br>Note that as a minimum, when creating a Replay, you must provide: a list of Avatars, ChanceCards (possibly without Holes), and the Round.<br>
<ul><li>StackedReplay represents a Replay together with a map from Avatars to ChipStacks.  These ChipStacks represent how much money each Avatar won/lost from the recorded hand.