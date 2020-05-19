package sample;

import javafx.scene.image.Image;


public class Piece {

    private PiecesHiearchy rank;
    private Image imagePiece;
    private Image imageBlind;
    private boolean isWhite;

    public Piece(PiecesHiearchy rank, boolean isWhite){

        this.rank = rank;
        this.isWhite = isWhite;
        SetImagePiece(rank,isWhite);

    }

    public PiecesHiearchy getRank(){
        return this.rank;
    }

    public int getRankValue(){
        return rank.getValue();
    }

    public Image getImage(){
        return imagePiece;
    }

    public boolean isWhite(){
        return this.isWhite;
    }

    private void SetImagePiece(PiecesHiearchy rank , boolean isWhite){

        if (isWhite) {
            this.imagePiece = new Image("assets/WHITE PIECES/WHITE_" + rank + ".png");
        } else {
            this.imagePiece = new Image("assets/BLACK PIECES/BLACK_" + rank + ".png");
        }

    }

}
