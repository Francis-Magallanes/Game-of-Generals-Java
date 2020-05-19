package sample;

import javafx.scene.image.Image;

public enum PiecesHiearchy {

    FLAG (0 , "Flag"),
    PRIVATE(1, "Private"),
    SERGEANT(2, "Sergeant"),
    SECOND_LIEUTENANT(3, "Second Lieutenant"),
    FIRST_LIEUTENANT(4, "First Lieutenant"),
    CAPTAIN (5, "Captain"),
    MAJOR (6, "Major"),
    LIEUTENANT_COLONEL (7, "Lieutenant Colonel"),
    COLONEL (8, "Colonel"),
    ONE_STAR_GENERAL(9, "One Star General"),
    TWO_STAR_GENERAL(10, "Two Star General"),
    THREE_STAR_GENERAL(11, "Three Star General"),
    FOUR_STAR_GENERAL(12, "Four Star General"),
    FIVE_STAR_GENERAL(13, "Five Star General"),
    SPY(14, "Spy")
    ;

    private int i;
    private String rank;

    PiecesHiearchy(int i, String rank) {

        this.i = i;
        this.rank = rank;

    }

    public int getValue(){
        return this.i;
    }

    public String getStringValue(){ return this.rank; }

}
