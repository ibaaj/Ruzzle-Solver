import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 27/11/2013
 * Time: 14:32
 */

public class Debug implements Serializable {
    Dictionnaire dico;
    public Arbre arbre;
    public Grille grille;
    public ArrayList<ArrayList<Point>> listeGrille = new ArrayList<ArrayList<Point>>();
    public ArrayList<ArrayList<Point>> listeDico = new ArrayList<ArrayList<Point>>();

    SearchDico pathsDico;
    SearchGrille pathsGrille;
    final String FiledataSerialized ="/tmp/ruzzle.ser";
    StringBuilder stats = new StringBuilder("NombreMot,nbSearchDicoPath,SearchDicoTime,nbSearchGrillePath,SearchGrilleTime\n");


    public Debug()
    {

    }
    public Debug(Dictionnaire d, Grille g)
    {
        dico = d;
        grille= g;
    }
    public void setDico(Dictionnaire d)
    {
        dico =d;
    }
    public Dictionnaire getDico()
    {
        return dico;
    }
    public void setGrille(Grille g)
    {
        grille = g;
    }
    public void setListes(ArrayList<ArrayList<Point>> d, ArrayList<ArrayList<Point>> g)
    {
        listeDico = d;
        listeGrille = g;
    }
    public boolean previousSavedFileExist()
    {
        File file = new File(FiledataSerialized);

        if (file.exists()) return true;
        else return false;

    }
    public void saveAll() {
        FileOutputStream fout = null;
        File file = new File(FiledataSerialized);

        if(file.delete())
            System.out.println(file.getName() + " a été supprimé");


        try {
            fout = new FileOutputStream(FiledataSerialized, true);
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(fout);
            oos.writeObject(dico);
            oos.writeObject(grille);
            oos.writeObject(listeDico);
            oos.writeObject(listeGrille);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            System.out.println("fichier ruzzle.ser sauvegardé");
        }


    }
    public void loadAll()
    {
        ObjectInputStream objectinputstream = null;
        FileInputStream streamIn = null;

        System.out.println("Chargement en cours de la précédente grille et du dictionnaire...");

        try {
            streamIn = new FileInputStream(FiledataSerialized);
            objectinputstream = new ObjectInputStream(streamIn);
            dico = (Dictionnaire) objectinputstream.readObject();
            arbre = dico.getArbre();
            grille = (Grille) objectinputstream.readObject();
            listeDico = (ArrayList<ArrayList<Point>>) objectinputstream.readObject();
            listeGrille = (ArrayList<ArrayList<Point>>) objectinputstream.readObject();


                objectinputstream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("chargement terminé.");
    }
    public void tryAlgoWithOneListe(ArrayList<ArrayList<Point>> liste)
    {

        ArrayList<Point> bufferPath = new ArrayList<Point>();
        StringBuilder bufferMot;
        int i,k,j;


        for(k=liste.size()-1;k>-1;k--){
            bufferPath = (ArrayList<Point>) (liste.get(k)).clone();
            bufferMot = new StringBuilder();



            for(j=0; j < bufferPath.size(); j++)
                for(i=0; i < bufferPath.size();i++)
                    if(bufferPath.get(i) == bufferPath.get(j) && i != j)
                        System.out.println(grille.getStringFromPoint(bufferPath) + " has duplicate points ");


            for(j=0; j < bufferPath.size(); j++)
            {
                bufferMot.append(grille.getCharFromPoint(bufferPath.get(j)));

                if(!arbre.containsWithJoker(bufferMot.toString(),true))
                    System.out.println(bufferMot.toString() + " is not a prefixe");

            }

            if(!arbre.containsWithJoker(grille.getStringFromPoint(bufferPath),true))
                System.out.println(grille.getStringFromPoint(bufferPath) + " not in arb");


        }



    }
    public void grepAllResult(List<String> list) {

        String w = "";
        char t;
        for(String q : list){
            w="";
            if(dico.getType()==2)
            {
                for(int i=0; i < q.length(); i++)
                {
                   t = q.charAt(i);

                   if(t=='a'||t=='e'||t=='i'||t=='o'||t=='u'||t=='y')
                   w+= "[[="+q.charAt(i)+"=]]";
                   else
                       w+= t;
                }
            }
            else
                w = q;


            try {
                Runtime r = Runtime.getRuntime();
                Process p = null;

                String d;

                if(dico.getType()==1)
                    d = "grep ^" + w + "$ -nr -i " + dico.getDicoPath();
                else
                    d = "iconv -f UTF-16 -t UTF-8 "+ dico.getDicoPath()+" | grep " + w + " -n -i ";

                p = r.exec(d);
                BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";

                while ((line = b.readLine()) != null) {
                    System.out.println("trying " + d + " result " + line);
                }
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }


    /**
     * 3rd party
     * @param nbWord
     * @param nbSearchDicoPath
     * @param SearchDicoTime
     * @param nbSearchGrillePath
     * @param SearchGrilleTime
     */
    public void addStats(int nbWord, int nbSearchDicoPath, long SearchDicoTime, int nbSearchGrillePath, long SearchGrilleTime)
    {
        stats.append(nbWord);
        stats.append(',');
        stats.append(nbSearchDicoPath);
        stats.append(',');
        stats.append(SearchDicoTime);
        stats.append(',');
        stats.append(nbSearchGrillePath);
        stats.append(',');
        stats.append(SearchGrilleTime);
        stats.append(System.getProperty("line.separator"));
    }
    public void saveStats(){
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("stats.csv"));
            out.println(stats.toString());
            System.out.println("Fichier stats.csv sauvegardé.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (out != null) out.close();
        }
    }

}
