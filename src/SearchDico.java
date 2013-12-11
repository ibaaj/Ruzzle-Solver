import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 17/11/2013
 * Time: 21:12
 */
public class SearchDico {
    Arbre arbre;
    Grille grille;
    ArrayList<ArrayList<Point>> liste = new ArrayList<ArrayList<Point>>();
    List<String> words = new ArrayList<String>();
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
        long  startTime= System.nanoTime();
        getAllPath(arbre.getRacine(),"");
        sortListe();

        System.out.println(liste.size() + " chemins avec l'algorithme de recherche à partir du dictionnaire");


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

        System.out.println("Nombre de mots trouvés : " + words.size() + " en " + elapsedTime + "ms avec l'algorithme de recherche à partir du dictionnaire");



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
    public void getAllPath(HashMap<Character,HashMap> noeud, String prefixe)
    {
        StringBuilder s = new StringBuilder();
        Iterator it = noeud.entrySet().iterator();

        if(!prefixe.equals(""))
        {
            if(grille.getPathFromWord(prefixe)==null)
            {
                return;
            }
        }

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();

            if((pairs.getKey().equals('\0'))) {
                liste.addAll(grille.getPathFromWord(prefixe));
            }

            else getAllPath(noeud.get(pairs.getKey()), prefixe + pairs.getKey());


        }


    }
    public void sortListe(){
        int i;
        Collections.sort(liste, new Comparator<ArrayList>() {
            public int compare(ArrayList a1, ArrayList a2) {
                return a2.size() - a1.size();
            }
        });

        for (i=liste.size()-1; i > -1; i--) {
            if((liste.get(i)).size()==1)    // remove les lettres seules
                liste.remove(i);
            else
                break;
        }

    }


}
