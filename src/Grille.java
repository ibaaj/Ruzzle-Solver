import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;



public class Grille implements Serializable
{
    char[] data_alphabet = new char[]{'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    double[] data_frequence_EN = new double[]{8.08, 7.38, 1.67, 7.47, 3.18, 1.91, 3.99, 0.09,
            12.56, 6.42, 2.17, 6.59, 1.80, 9.15, 5.27, 2.79, 7.24, 1.00,
            0.14, 1.89, 0.63, 0.21, 4.04, 1.65, 2.60, 0.07};   /*based on www.apprendre-en-ligne.net/crypto/stat/anglais.html */
    int[] data_frequence_EN_build = new int[]{808,1546,1713,2460,2778,2969,
            3368,3377,4633,5275,5492,6151,6331,
            7246,7773,8052,8776,8876,8890,9079,
            9142,9163,9567,9732,9992,9999};
    double[] data_frequence_FR = new double[]{7.636,0.901,3.26,3.669,14.715,1.066,0.866,0.737,7.529,
            0.545, 0.049,5.456,2.968,7.095,5.378,3.021,1.362,6.553,7.948,7.244,6.311,1.628,0.114,
            0.387,0.308, 0.136 };
    int[] data_frequence_FR_build = new int[]{763,853,1179,1545,3016,3122,3208,3281,4033,4087,
            4091,4636,4932,5641,6178,6480,6616,7271,8065,8789,9420,9582,
            9593,9631,9661,9674};
            /* http://fr.wikipedia.org/wiki/Fr%C3%A9quence_d'apparition_des_lettres_en_fran%C3%A7ais */

    public char[][] gridc = new char[4][7];

    public final char charJoker = '*';
    public final char charCaseNoir = '+';
    public final int  nbCases = 24;

    Arbre arbre;

    public void setArbre(Arbre a)
    {
        arbre = a;
    }

    public Grille() {
        //To change body of created methods use File | Settings | File Templates.
    }


    public char[][] getGrid()
    {
        return gridc;
    }
    public  String getStringFromPoint(ArrayList<Point> l)
    {
        StringBuilder s = new StringBuilder();

        if(l==null)
            return "";

        for(int i = 0; i < l.size(); i++)
            s.append(getCharFromPoint(l.get(i)));

        return s.toString();
    }
    public char getCharFromPoint (Point z)
    {

        if(!(isCorrectPoint(z.x,z.y)) || (z.x > 4 || z.x < 0 || z.y > 6 || z.y < 0))
            try {
                throw new Exception("illegal grille position : " + z.toString());
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        return gridc[z.x][z.y];
    }

    /**
     *
     * @param langue  1 = anglais, 2 = français
     */

    public Grille(int langue, int casesNoir, int joker)
    {
        int i,j,l,k,o;

        if(langue != 1 && langue != 2)
            throw new IllegalArgumentException(" Erreur Argument Grille; 1 = anglais, 2 = français ");

        if((casesNoir < 0)||(casesNoir > nbCases))
            throw new IllegalArgumentException("Erreur arg grille : 0 < cases noires < 26");

        if((joker < 0)||(joker > nbCases-casesNoir))
            throw new IllegalArgumentException("Erreur arg grille : 0 < joker < 26 - cases noires");


       for(i=0; i < (casesNoir+joker); i++) {
           j= (int) (Math.random() * 4);
           l= (int) (Math.random() * 7);
           while(!isCorrectPoint(j,l)||((gridc[j][l]==charCaseNoir)||gridc[j][l]==charJoker))   {
               j= (int) (Math.random() * 4);
               l= (int) (Math.random() * 7);
           }
           if(i < casesNoir)
               gridc[j][l] = charCaseNoir;
           if(i >= casesNoir)
               gridc[j][l] = charJoker;
       }




        o = ((langue == 1) ? data_frequence_EN_build[data_frequence_EN_build.length-1] : data_frequence_FR_build[data_frequence_FR_build.length-1]);
        for(i = 0; i < 4; i++) {
            for (j = 0; j < 7; j++) {

               if(((gridc[i][j]==charCaseNoir)||gridc[i][j]==charJoker))
                   continue;


                k = (int) (((i == 0 && j == 0)||(j == 6 && i == 0)||(j == 0 && i == 3)||(j == 6 && i == 3)) ? -1 : (Math.random() * o));

                if(k==-1){
                    gridc[i][j] = '.';
                    continue;
                }



                l = 0;
                if(langue == 1)
                while(data_frequence_EN_build[l] < k)
                    l++;
                if(langue == 2)
                    while(data_frequence_FR_build[l] < k)
                        l++;

                    gridc[i][j] = data_alphabet[l];



            }

        }

        toString();
    }
    public String toString()
    {
        int i,j;
        StringBuilder a = new StringBuilder("");

        for(i = 0; i < 4; i++){
            for(j=0; j < 7; j++)
            {
                if(j==0 &&(i==1||i==2))
                    a.append((i==1)?'/':'\\');

                if((((i == 0 && j == 0)||(j == 6 && i == 0)||(j == 0 && i == 3)||(j == 6 && i == 3))))
                    a.append(' ');
                else
                    a.append(Character.toUpperCase(gridc[i][j]));

                if(!((i==0 && j==6)||(i==3&&j==6)))
                    a.append(((i + j) % 2 == 0) ? '/' : '\\');

            }
            a.append("\n");
        }

        return a.toString();
    }
    public void Afficher()
    {
        System.out.println(toString());
    }
    public boolean isCorrectPoint(int x, int y)
    {
        return (!(((x == 0 && y == 0)||(y == 6 && x == 0)||(y == 0 && x == 3)||(y == 6 && x == 3))));
    }

    /**
     * getPathFromWord
     * @param w le mot
     * @return la liste de tous les chemins possibles dans la grille avec le mot en arg.
     */
    public ArrayList<ArrayList<Point>> getPathFromWord(String w)
    {
        ArrayList<ArrayList<Point>> liste = new ArrayList<ArrayList<Point>>();
        ArrayList<Point> bufferPath,bufferPath2, adjacents;
        int i,j,current_point=1;
        Point p;

        if(w.length()==0)
            return liste;

        for(i = 0; i < 4; i++){
            for(j=0; j < 7; j++)
            {
                if(isCorrectPoint(i,j))
                {
                    p = new Point(i,j);
                     if(getCharFromPoint(p)==w.charAt(0)||(getCharFromPoint(p)==(charJoker))){
                          bufferPath = new ArrayList<Point>();
                          bufferPath.add(p);
                          liste.add(bufferPath);
                     }
                }

            }
        }

        if(liste.size()==0)
            return null;

        while(current_point < w.length()){

            for(i=liste.size()-1;i>-1;i--){
                bufferPath = (ArrayList<Point>) (liste.get(i)).clone();


                    adjacents = getAdjacent(bufferPath.get(current_point-1));
                    while(adjacents.size()!=0){
                        if(!bufferPath.contains(adjacents.get(0)) &&
                                (getCharFromPoint(adjacents.get(0))==w.charAt(current_point) || getCharFromPoint(adjacents.get(0))==charJoker)){
                            bufferPath2 = (ArrayList<Point>) bufferPath.clone();
                            bufferPath2.add(adjacents.get(0));
                            liste.add(bufferPath2);
                        }
                        adjacents.remove(0);
                    }

                liste.remove(i);


            }



            current_point++;
        }



        if(liste.size()==0)
            return null;

        return liste;
    }
    public ArrayList<Point> getAdjacent(Point p)
    {
        ArrayList<Point> list = new ArrayList<Point>();
        int x= p.x, y = p.y;


        if(!isCorrectPoint(x,y))
            return null;

        if(y+2 < 7 && isCorrectPoint(x,y+2))
            list.add(new Point(x,y+2));
        if(y+1 < 7 && isCorrectPoint(x,y+1))
            list.add(new Point(x,y+1));
        if(y-2 > -1 && isCorrectPoint(x,y-2))
            list.add(new Point(x,y-2));
        if(y-1 > -1 && isCorrectPoint(x,y-1))
            list.add(new Point(x,y-1));

        if(((x+y) % 2 == 0))
        {
            if(x-1 > -1)
            {
                if(isCorrectPoint(x-1,y))
                    list.add(new Point(x-1,y));
                if(y+2 < 7 && isCorrectPoint(x-1,y+2))
                    list.add(new Point(x-1,y+2));
                if(y+1 < 7 && isCorrectPoint(x-1,y+1))
                    list.add(new Point(x-1,y+1));
                if(y-2 > -1 && isCorrectPoint(x-1,y-2))
                    list.add(new Point(x-1,y-2));
                if(y-1 > -1 && isCorrectPoint(x-1,y-1))
                    list.add(new Point(x-1,y-1));
            }
            if(x+1 < 4)
            {
                if(isCorrectPoint(x+1,y))
                    list.add(new Point(x+1,y));
                if(y+1 < 7 && isCorrectPoint(x+1,y+1))
                    list.add(new Point(x+1,y+1));
                if(y-1 > -1 && isCorrectPoint(x+1,y-1))
                    list.add(new Point(x+1,y-1));
            }
        }
        else
        {
            if(x+1 < 4)
            {
                if(isCorrectPoint(x+1,y))
                    list.add(new Point(x+1,y));
                if(y+2 < 7 && isCorrectPoint(x+1,y+2))
                    list.add(new Point(x+1,y+2));
                if(y+1 < 7 && isCorrectPoint(x+1,y+1))
                    list.add(new Point(x+1,y+1));
                if(y-2 > -1 && isCorrectPoint(x+1,y-2))
                    list.add(new Point(x+1,y-2));
                if(y-1 > -1 && isCorrectPoint(x+1,y-1))
                    list.add(new Point(x+1,y-1));
            }
            if(x-1 > -1)
            {
                if(isCorrectPoint(x-1,y))
                    list.add(new Point(x-1,y));
                if(y+1 < 7 && isCorrectPoint(x-1,y+1))
                    list.add(new Point(x-1,y+1));
                if(y-1 > -1 && isCorrectPoint(x-1,y-1))
                    list.add(new Point(x-1,y-1));
            }
        }
        return list;
    }





}