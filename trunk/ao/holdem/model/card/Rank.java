package ao.holdem.model.card;

/**
 *
 */
public enum Rank
{
    //--------------------------------------------------------------------
    TWO("2"), THREE("3"), FOUR("4"),  FIVE("5"),
    SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
    TEN("T"), JACK("J"),  QUEEN("Q"), KING("K"),
    ACE("A");

    public static final Rank VALUES[] = values();


    //--------------------------------------------------------------------
    private final String NAME;


    //--------------------------------------------------------------------
    private Rank(String name)
    {
        NAME  = name;
    }


    //--------------------------------------------------------------------
    public boolean comesAfter(Rank rank)
    {
        return ordinal() > rank.ordinal();
    }


    //--------------------------------------------------------------------
    public String toString()
    {
        return NAME;
    }
}
