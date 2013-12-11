
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * Date: 4/11/2013
 * Time: 20:56
 */
public class Arbre implements Serializable {
    HashMap<Character, HashMap> racine;
    char finMot = '\0';
    char charJoker, charCaseNoir;


    {
        charJoker = (new Grille()).charJoker;
        charCaseNoir = (new Grille()).charCaseNoir;
    }


    public Arbre() {
        racine = new HashMap<Character, HashMap>();
    }
    public HashMap<Character, HashMap> getRacine() {
        return racine;
    }
    public void add(String s) {
        HashMap<Character, HashMap> noeud_courant = racine;
        int i;
        char c;

        for (i=0; i < s.length(); i++) {
             c = s.charAt(i);
            if (noeud_courant.containsKey(c))
                noeud_courant = noeud_courant.get(c);
            else {
               noeud_courant.put(c, new HashMap<Character, HashMap>());
                noeud_courant = noeud_courant.get(c);
            }
        }
        noeud_courant.put(finMot, new HashMap<Character, HashMap>(0));
    }
    public boolean contains(String s, int justPrefixe) {
        HashMap<Character, HashMap> noeud_courant = racine;
        int i;
        char c;

        for (i=0; i < s.length(); i++) {
            c = s.charAt(i);
            if (noeud_courant.containsKey(c))
                noeud_courant = noeud_courant.get(c);
            else
                return false;
        }
        return justPrefixe == 1 || noeud_courant.containsKey(finMot);
    }

    public boolean containsWithJoker (String s, boolean justPrefixe)
    {
        HashMap<Character, HashMap> noeud_courant = racine;
        ArrayList<HashMap<Character, HashMap>> listeNoeuds = new ArrayList<HashMap<Character, HashMap>>();
        ArrayList<HashMap<Character, HashMap>> listeNoeuds2 = new ArrayList<HashMap<Character, HashMap>>();
        char[] data_alphabet = new char[]{ 'a','b','c','d','e','f','g', 'h','i','j','k','l','m','n','o','p','q','r','s',
                't','u','v','w','x','y','z'};
        int i,j,k;
        char c;



        for (i=0; i < s.length(); i++) {
             c = s.charAt(i);



             if(c != charJoker)  {
                if(listeNoeuds.size() == 0) {
                    if(noeud_courant.containsKey(c))
                        noeud_courant = noeud_courant.get(c);
                    else
                        return false;
                }
                else {
                    for(j=listeNoeuds.size()-1;j > -1; j--) {
                        if((listeNoeuds.get(j)).containsKey(c)) {
                            HashMap<Character, HashMap> a =  (listeNoeuds.get(j)).get(c);
                            listeNoeuds.remove(j);
                            listeNoeuds.add(a);
                        }
                        else
                            listeNoeuds.remove(j);
                    }

                    if(listeNoeuds.size()==0)
                        return false;
                }

             }
            else
             {
                 if(c == charCaseNoir)
                     return false;



                 if(listeNoeuds.size() == 0)  {

                     for(char q : noeud_courant.keySet())
                     {

                             listeNoeuds.add((HashMap<Character, HashMap>) (noeud_courant.get(q)));

                     }

                 }
                 else
                 {


                     for(k=listeNoeuds.size()-1; k > -1; k--){


                         for(char q : (listeNoeuds.get(k)).keySet())
                         {

                                 listeNoeuds.add((HashMap<Character, HashMap>) ((listeNoeuds.get(k)).get(q)));

                         }
                          listeNoeuds.remove(k);

                     }




                 }
             }




        }


        if(justPrefixe)
            return true;

        if(listeNoeuds.size() > 0)
        {
            for(k=0; k < listeNoeuds.size(); k++)
                 if(listeNoeuds.get(k).containsKey(finMot))
                         return true;

        }
        else
        {
            if(noeud_courant.containsKey(finMot))
                return true;
        }

        return false;


    }
    public ArrayList<StringBuilder> getWordFromStringJoker(String s)
    {
          ArrayList<StringBuilder> list = new ArrayList<StringBuilder>();
          HashMap<Character, HashMap> noeud_courant = racine;
          ArrayList<HashMap<Character, HashMap>> listeNoeuds = new ArrayList<HashMap<Character, HashMap>>();
          int i,j,k;
          char c;
          StringBuilder t = new StringBuilder("");

        StringBuilder endWithAllJoker = new StringBuilder();
        endWithAllJoker.append(charJoker);
        endWithAllJoker.append(charJoker);


        for (i=0; i < s.length(); i++) {
            c = s.charAt(i);






            if(c != charJoker)
            {
                if(listeNoeuds.size() == 0)
                {
                    if(noeud_courant.containsKey(c)) {
                        noeud_courant = noeud_courant.get(c);
                        t.append(c);
                    }
                    else
                        return null;
                }
                else
                {

                    for(j=listeNoeuds.size()-1;j > -1; j--) {

                        if((listeNoeuds.get(j)).containsKey(c)) {


                            StringBuilder z = new StringBuilder((list.get(j)).toString());
                            z.append(c);
                            list.remove(j);
                            list.add(z);

                            HashMap<Character, HashMap> a =  (listeNoeuds.get(j)).get(c);
                            listeNoeuds.remove(j);
                            listeNoeuds.add(a);
                        }
                        else {
                            listeNoeuds.remove(j);
                            list.remove(j);


                        }


                    }





                    if(listeNoeuds.size()==0)
                        return null;
                }
            }
            else
            {
                if(c == charCaseNoir)
                    return null;



                if(listeNoeuds.size() == 0)  {


                    // avoid ^([a-z]+)‰\*\*$ string, when $1 is a word in the dictionary
                    if(noeud_courant.keySet().size() ==1 && noeud_courant.keySet().contains('\0') && s.substring(Math.max(s.length() - 2, 0)).equals(endWithAllJoker.toString()))
                        return new ArrayList<StringBuilder>();


                    for(char q : noeud_courant.keySet())
                    {


                        StringBuilder z = new StringBuilder(t.toString());
                        z.append(q);
                        list.add(z);


                        listeNoeuds.add((HashMap<Character, HashMap>) (noeud_courant.get(q)));

                    }
                }
                else
                {


                    for(k=listeNoeuds.size()-1; k > -1; k--){


                        for(char q : (listeNoeuds.get(k)).keySet())
                        {


                                StringBuilder z = new StringBuilder((list.get(k)).toString());
                                z.append(q);
                                list.add(z);


                            listeNoeuds.add((HashMap<Character, HashMap>) ((listeNoeuds.get(k)).get(q)));

                        }
                        listeNoeuds.remove(k);
                        list.remove(k);

                    }




                }
            }






        }


        if(listeNoeuds.size() > 0)
        {
            for(k=listeNoeuds.size()-1; k >-1; k--)
                if(!listeNoeuds.get(k).containsKey(finMot))
                    list.remove(k);
        }
        else
        {
            if(noeud_courant.containsKey(finMot)) {
                ArrayList<StringBuilder> x = new ArrayList<StringBuilder>();
                x.add(new StringBuilder(s));
                return x;
            }
        }

        return list;
    }






}
