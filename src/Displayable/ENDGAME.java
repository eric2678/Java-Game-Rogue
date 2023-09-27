package src.Displayable;

import static src.KeyProcess.ObjectDisplayGrid.height;
import static src.KeyProcess.ObjectDisplayGrid.width;
import static src.KeyProcess.ObjectDisplayGrid.topHeight;;


public class ENDGAME {
    public static String ENDMAP[][] = new String[width][height];
    private static char endSymble[][] = {
        {' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', '#', '#','#', '#', '#', '#', '#', '#', '#',' ', ' ', ' ', ' ', ' ','#', '#', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', '#', '#','#', ' ', ' ', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', '#', ' ', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', '#', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', '#', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', '#',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', '#',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ','#',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ','#', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', '#', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', '#', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', '#',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', '#', ' ', ' ',' '},
        {' ', '#', '#', '#','#', '#', '#', '#', '#', '#', '#',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ','#', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', '#', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', '#', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', '#', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', '#', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', '#',' ', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ',' ','#', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ','#', ' ', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', ' ','#',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', '#', ' ', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', ' ', '#',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', '#', ' ',' ', '#', ' ', '#', ' ', ' ',' ', ' ', '#', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', '#',' ', '#', ' ', '#', ' ', ' ',' ', '#', ' ', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', '#', '#', '#','#', '#', '#', '#', '#', '#', '#',' ', ' ', ' ', ' ', ' ','#', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ','#', '#', ' ', '#', '#', '#','#', ' ', ' ', ' ',' ',' ', ' ', ' ', ' ',' '},
        {' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ', ' ', ' ', ' ', ' ', ' ',' ', ' ', ' ', ' ',' ',' ', ' ', ' ', ' ',' '}
    };

    public static void setENDMAP(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                ENDMAP[i][j] = " ";
            }
        }
        for(int i = 0, l = width/2-endSymble[0].length/2; i < endSymble[0].length; i++, l++){
            for(int j = 0, p = height/2-endSymble.length/2; j < endSymble.length; j++, p++){
                ENDMAP[l][p] = String.valueOf(endSymble[j][i]);
            }
        }
    }
}