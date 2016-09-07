package richbar.com.github.commandplot.util;

/**
 * Created by Rich Y on 07.09.2016.
 */
public enum TeamColor {
    BLACK(0),
    DARKBLUE(1),
    DARKGREEN(2),
    DARKAQUA(3),
    DARKRED(4),
    DARKPURPLE(5),
    GOLD(6),
    GRAY(7),
    DARKGRAY(8),
    BLUE(9),
    GREEN(10, "a"),
    AQUA(11, "b"),
    RED(12, "c"),
    LIGHTPURPLE(13, "d"),
    YELLOW(14, "e"),
    WHITE(15, "f"),
    NONE(-1);

    int i;
    String h = "";
    TeamColor(int index){
        h += i = index;
    }

    TeamColor(int index, String hex){
        i = index;
        h = hex;
    }

    public int getIndex(){
        return i;
    }

    public static TeamColor getColor(int index){
        if(index == -1) return NONE;
        else return values()[index];
    }

    public static TeamColor getColor(String hex){
        switch(hex){
            case "a": return GREEN;
            case "b": return AQUA;
            case "c": return RED;
            case "d": return LIGHTPURPLE;
            case "e": return YELLOW;
            case "f": return WHITE;
            default: return getColor(Integer.parseInt(hex));
        }
    }

}
