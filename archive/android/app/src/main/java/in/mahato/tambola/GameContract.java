package in.mahato.tambola;


public class GameContract {
    public static final String TABLE_CALLED = "called_numbers";


    public static final class Columns {
        public static final String ID = "_id";
        public static final String GAME_ID = "game_id";
        public static final String NUMBER = "number";
        public static final String ORDER_INDEX = "order_index";
        public static final String TS = "timestamp";
    }
}