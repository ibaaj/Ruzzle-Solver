import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 17/11/2013
 * Time: 21:12
 */
public class SearchGrille {
    Grille grille;
    Arbre arbre;
    List<String> words = new ArrayList<String>();
    ArrayList<ArrayList<Point>> liste = new ArrayList<ArrayList<Point>>();
    long elapsedTime;


    public void setGrille(Grille t)
    {
        grille = t;
    }
    public void setArbre(Arbre a)
    {
        arbre = a;
    }
    public void doIt()
    {
        int k,l;
        ArrayList<Point> bufferPoint = new ArrayList<Point>();
        String bufferMot = new String();
        Point z;
        long startTime= System.nanoTime();

        liste = getAllPath();

        System.out.println(liste.size() + " chemins avec l'algorithme de recherche à partir de la grille");


        HashSet<String> listWords = new HashSet<String>();
        for(k=0; k < liste.size();k++)
        {
            ArrayList<StringBuilder> f = new ArrayList<StringBuilder>();
            f = arbre.getWordFromStringJoker(grille.getStringFromPoint((ArrayList<Point>) (liste.get(k)).clone()));

            for(l=0; l < f.size();l++)
                if(!listWords.contains(f.get(l).toString()))
                    listWords.add(f.get(l).toString());
        }

        words = new ArrayList<String>(listWords);


        Collections.sort(words, new Comparator<String>() {
            public int compare(String a1,String a2) {
                return a2.length() - a1.length();
            }
        });
        elapsedTime = (System.nanoTime() - startTime) / 1000000;


        System.out.println("Nombre de mots trouvés : " + words.size() + " en " + elapsedTime + "ms avec l'algorithme de recherche à partir de la grille");

    }
    public void printPaths()
    {
        int k,l;
        Point z;
        ArrayList<Point> bufferPoint;
        String bufferMot;

        for(k = 0; k < liste.size(); k++)
        {
            bufferPoint = (ArrayList<Point>) (liste.get(k)).clone();
            bufferMot = grille.getStringFromPoint(bufferPoint);
            System.out.print(bufferMot);
            System.out.print(" ");

            for(l =0 ; l < (liste.get(k)).size(); l++)
            {
                z = (liste.get(k)).get(l);
                System.out.print(" (");
                System.out.print(z.x);
                System.out.print(",");
                System.out.print(z.y);
                System.out.print(")");
            }
            System.out.println("");
        }
    }
    public void printWords()
    {
        System.out.println(words);
    }
    public ArrayList<ArrayList<Point>> getAllPath()
    {
        int i, j;


        ArrayList<ArrayList<Point>> listeB = new ArrayList<ArrayList<Point>>();



        for(i = 0; i < 4; i++){
            for(j=0; j < 7; j++)
            {

                if(grille.isCorrectPoint(i, j))
                {

                    listeB.addAll(genOnePathFrom(new Point(i,j)));

                    Collections.sort(listeB, new Comparator<ArrayList>() {
                        public int compare(ArrayList a1, ArrayList a2) {
                            return a2.size() - a1.size();
                        }
                    });


                }
            }
        }

        return listeB;
    }

    public ArrayList<ArrayList<Point>> genOnePathFrom(Point p)
    {
        ArrayList<ArrayList<Point>> liste = new ArrayList<ArrayList<Point>>();
        ArrayList<Point> bufferPath,bufferBoucle,bufferPath2,adjacents = grille.getAdjacent(p);
        StringBuilder bufferMot;
        int k,current_point = 2;



        while(adjacents.size() !=0) {


            bufferMot = new StringBuilder();
            bufferMot.append(grille.getCharFromPoint(p));
            bufferMot.append(grille.getCharFromPoint(adjacents.get(0)));



            if(bufferMot.charAt(bufferMot.length()-1) != '+' && arbre.containsWithJoker(bufferMot.toString(),true)){
                bufferPath2 = new ArrayList<Point>();
                bufferPath2.add(p);
                bufferPath2.add(adjacents.get(0));

                liste.add(bufferPath2);

            }
            adjacents.remove(0);

        }



        while(current_point < 24){



            for(k=liste.size()-1;k>-1;k--){
                bufferBoucle = (ArrayList<Point>) (liste.get(k)).clone();

                if(bufferBoucle.size() == current_point){
                    adjacents = grille.getAdjacent(bufferBoucle.get(current_point - 1));


                    while(adjacents.size()!=0){
                        bufferMot = new StringBuilder();
                        bufferMot.append(grille.getStringFromPoint(bufferBoucle));
                        bufferMot.append(grille.getCharFromPoint(adjacents.get(0)));


                        if(!bufferBoucle.contains(adjacents.get(0)) && bufferMot.charAt(bufferMot.length()-1) != '+' && arbre.containsWithJoker(bufferMot.toString(),true)){
                            bufferPath = (ArrayList<Point>) bufferBoucle.clone();
                            bufferPath.add(adjacents.get(0));
                            liste.add(bufferPath);



                        }
                        adjacents.remove(0);
                    }


                }
                else
                {
                    if(!arbre.containsWithJoker(grille.getStringFromPoint(bufferBoucle),false))
                        liste.remove(k);
                }

            }

            current_point++;
        }


        return liste;


    }





}
