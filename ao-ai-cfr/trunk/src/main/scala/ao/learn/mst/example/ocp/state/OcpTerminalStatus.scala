package ao.learn.mst.example.ocp.state

import ao.learn.mst.example.ocp.state.OcpPosition._
import ao.learn.mst.example.kuhn.state.terminal.{NamedWinnerIndicator, EnumWithWinner}


//----------------------------------------------------------------------------------------------------------------------
object OcpTerminalStatus
  extends EnumWithWinner[OcpPosition]
{
  //--------------------------------------------------------------------------------------------------------------------
  case class EnumVal private[OcpTerminalStatus](
      name: String, preShowdownWinner: Option[OcpPosition])
    extends Value with NamedWinnerIndicator[OcpPosition]

  type OcpTerminalStatus = EnumVal

  val SmallShowdown = EnumVal("SmallShowdown", None)
  val BigShowdown = EnumVal("BigShowdown", None)

  val FirstToActWins = EnumVal("FirstToAct Wins", Some(OcpPosition.FirstToAct))
  val LastToActWins = EnumVal("LastToAct Wins", Some(OcpPosition.LastToAct))


  //  type KuhnTerminalStatus = Value
  //
  //  val SmallShowdown = Value("SmallShowdown")
  //  val BigShowdown   = Value("BigShowdown")
  //
  //  val FirstToActWins = Value("FirstToAct Wins")
  //  val LastToActWins  = Value("LastToAct Wins")
}