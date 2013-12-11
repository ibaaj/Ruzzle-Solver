/**
 * Created with IntelliJ IDEA.
 * Date: 18/10/2013
 * Time: 4:16
 */

import java.io.*;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.regex.Pattern;


class Dictionnaire implements Serializable {
    private  Arbre arbre = new Arbre();
    private String dicoPath;
    private int type;


    /**
     *
     *
     * @param file  : path vers le dictionnaire
     * @param 'type' : 1 est un dico de type /usr/share/dict/words, 2 de type DELA
     * @throws IOException
     */


    public Dictionnaire(String file, int dtype) throws IOException, UnsupportedEncodingException {
        long elapsedTime, startTime;
        int lignes=0,i,j,k;
        String s;
        String[] parts,parts2,parts3;

        dicoPath = new String(file.getBytes(), "UTF-8");
        type = dtype;
        if(type != 1 && type != 2)
            throw new IllegalArgumentException(" Erreur 2nd Argument Dictionnaire; 1 = anglais, 2 = français ");

        startTime = System.nanoTime();
        if(type==1){
            BufferedReader reader=new BufferedReader(new FileReader(file));

        while ((s = reader.readLine()) != null)
        {
            lignes++;
            arbre.add(s.toLowerCase());
        }
            reader.close();
        }
        if(type==2){
          BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-16")));

            while ((s = reader.readLine()) != null)
        {
            s = new String(s.toLowerCase().getBytes(), "UTF-8");
            String q = s;

            String nfdNormalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);    /* 3 lignes pour transformer les  accents en lettres ... :) */
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            s = pattern.matcher(nfdNormalizedString).replaceAll("");

            lignes++;

           if(!s.matches("(.*)npropre(.*)")) {   // ptit regex pour pas prendre les noms propres...

               if (s.contains(".")) {
                  parts = ((s.split("\\."))[0]).split(",");   // split at dot, then get all words .. but space, & \- et autres joies ...

                  for(i=0; i<parts.length;i++)
                  {
                     parts2 = parts[i].split(" ");
                     for (j=0;j<parts2.length;j++){
                            parts3 = parts2[j].split("[^a-zA-Z0-9\\\\s]");  // split punctuation
                         for(k=0; k < parts3.length;k++) {
                             if(parts3[k].length() > 0){
                                 if(parts3[k].charAt(parts3[k].length()-1)=='\\')     // sometime, we can found this at the end of the word ... :)
                                     parts3[k] = parts3[k].substring(0, parts3[k].length()-1);

                                 arbre.add(parts3[k]);
                             }

                         }

                     }
                  }


               } else {
                   throw new IllegalArgumentException("Mauvais pattern de  " + s + " ( pas de \".\" )");
               }





           }

        }
            reader.close();
        }
        elapsedTime = (System.nanoTime() - startTime) / 1000000;


        System.out.println("Détection de " + lignes + " lignes - fichier : " + file);

        System.out.println("Création du dictionnaire en : " + elapsedTime + "ms");


    }

    public  Arbre getArbre()
    {
        return arbre;
    }

    public String getDicoPath()
    {

        return dicoPath;
    }
    public int getType()
    {
        return type;
    }
}
