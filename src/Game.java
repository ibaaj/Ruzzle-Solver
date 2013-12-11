/**
 * Created with IntelliJ IDEA.
 * Date: 09/11/2013
 * Time: 19:12
 */

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Game {


    Debug debug = new Debug();
    Dictionnaire dico;
    Grille grille;
    SearchGrille paths1;
    SearchDico paths2;


    int langue;
    String pathDico = new String("");
    int isDicoDELA = -1;
    int nbGrillecaseNoir = -1;
    int nbGrilleJoker = -1;
    int reloadPrevious = -1;

    final String defaultDicoDelaPathFR = new String(System.getProperty("user.home") + "/Desktop/dela-fr-public.dic");
    final String defaultDicoPathEN = new String("/usr/share/dict/words");




    public Game()
    {

        loadPreviousSavedFile();

        int i = 0;


        if(reloadPrevious == 0){
             try {
                setParams();
                dico = new Dictionnaire(pathDico,langue);
            } catch (Exception e) {
                e.printStackTrace();
            }




        }


            grille = new Grille(langue,nbGrillecaseNoir,nbGrilleJoker);
            grille.setArbre(dico.getArbre());
             grille.Afficher();


        paths2 = new SearchDico();
        paths2.setGrille(grille);
        paths2.setArbre(dico.getArbre());
        paths2.doIt();


        paths1 = new SearchGrille();
        paths1.setGrille(grille);
        paths1.setArbre(dico.getArbre());
        paths1.doIt();




        nextMenu();




    }
    public void checkDefaultDico() throws Exception {
        File f1 = new File(defaultDicoDelaPathFR), f2 = new File(defaultDicoPathEN);
        if(!f1.exists() || !f2.exists())
           throw new Exception("Les chemins des dictionnaires par défaut ne sont pas corrects.");

    }

    public void setParams() throws IOException {
        BufferedReader bufferRead;
        String s;
        File f;


        while(!(langue == 1 || langue == 2)){
        System.out.println("Entrez la langue souhaitée : 1 pour Anglais, 2 pour français");
         bufferRead = new BufferedReader(new InputStreamReader(System.in));
         s = bufferRead.readLine();
        if(s.equals("1") ||s.equals("2"))
            langue = Integer.parseInt(s);
        }


        while(pathDico.equals("")) {
            System.out.println("Entrez le chemin vers le fichier du dictionnaire que vous souhaitez utiliser.");
            System.out.println("Entrez le chiffre 1 si vous voulez utiliser le Dictionnaire par défaut : " + ((langue==1) ? defaultDicoPathEN : defaultDicoDelaPathFR));
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            s = bufferRead.readLine();
            if(s.equals("1")) {
                pathDico = ((langue==1) ? defaultDicoPathEN : defaultDicoDelaPathFR);
                isDicoDELA = ((langue==2)? 1:0);
            }
            else {
                if(new File(s).exists())
                    pathDico = s;
                else
                    System.out.println("Le chemin ne pointe pas sur un fichier.");
            }
        }

        while(!(isDicoDELA == 1 || isDicoDELA == 0)){
            System.out.println("Est ce un dictionnaire au format DELA ? 1 pour Oui, 0 pour non.");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            s = bufferRead.readLine();
            if(s.equals("1") ||s.equals("0"))
               isDicoDELA = Integer.parseInt(s);
        }


        while(nbGrillecaseNoir==-1 || nbGrillecaseNoir > 4){
            System.out.println("Combien souhaitez vous de cases noires dans votre Grille ? 0 pour aucune, 4 max");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            s = bufferRead.readLine();
            try {
               nbGrillecaseNoir = Integer.parseInt(s);
            } catch(NumberFormatException e) {
            }
        }
        while(nbGrilleJoker==-1 || nbGrilleJoker > 2){
            System.out.println("Combien souhaitez vous de joker dans votre Grille ? 0 pour aucun, 2 max");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            s = bufferRead.readLine();
            try {
                nbGrilleJoker = Integer.parseInt(s);
            } catch(NumberFormatException e) {
            }
        }

    }
    public void resetParams()
    {
         langue = -1;
        pathDico = new String("");
        isDicoDELA = -1;
        nbGrillecaseNoir = -1;
        nbGrilleJoker = -1;
    }
    public void doStats()
    {
        BufferedReader bufferRead;
        String s;
        int i=0,j=-1;

        try {
            setParams();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        while(j<0){
            System.out.println("Entrez le nombre de tests souhaités :");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                s = bufferRead.readLine();
                j = Integer.parseInt(s);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        try {
            dico = new Dictionnaire(pathDico,langue);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        while(i < j)
        {
            grille = new Grille(langue,nbGrillecaseNoir,nbGrilleJoker);
            grille.setArbre(dico.getArbre());


            paths2 = new SearchDico();
            paths2.setGrille(grille);
            paths2.setArbre(dico.getArbre());
            paths2.doIt();


            paths1 = new SearchGrille();
            paths1.setGrille(grille);
            paths1.setArbre(dico.getArbre());
            paths1.doIt();

            debug.addStats(paths1.words.size(),paths2.liste.size(),paths2.elapsedTime, paths1.liste.size(), paths1.elapsedTime);
            i++;
        }

        debug.saveStats();
    }
    public void loadPreviousSavedFile()
    {
        BufferedReader bufferRead;
        String s;

        if(!debug.previousSavedFileExist()) {
              reloadPrevious = 0;
            return;
        }



        while(!(reloadPrevious == 1 || reloadPrevious == 0)){
            System.out.println("Souhaitez vous recharger la dernière sauvegarde? - 1 pour Oui, 0 pour non");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                s = bufferRead.readLine();
                if(s.equals("1") ||s.equals("0"))
                    reloadPrevious = Integer.parseInt(s);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

            if(reloadPrevious==1){
                debug.loadAll();
                dico = debug.getDico();
                grille = debug.grille;
            }
    }
    public void saveInFile(){

        int save=-1;
        BufferedReader bufferRead;
        String s;

        while(!(save == 1 || save == 0)){
            System.out.println("Souhaitez vous sauvegarder? - 1 pour Oui, 0 pour non");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                s = bufferRead.readLine();
                if(s.equals("1") ||s.equals("0"))
                    save = Integer.parseInt(s);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        if(save==1){
            debug.setDico(dico);
            debug.setGrille(grille);
            debug.setListes(paths1.liste,paths2.liste);
            debug.saveAll();
        }
    }
    public void nextMenu()
    {
        int choix = -1;
        BufferedReader bufferRead;
        String s;
        debug.setDico(dico);


        while(choix!=0)
        {
              System.out.println("Que souhaitez-vous faire ? (0 pour quitter)");
              System.out.println("1 : afficher les chemins générés par l'algorithme de recherche par le dictionnaire");
              System.out.println("2 : afficher les mots générés par l'algorithme de recherche par le dictionnaire");
              System.out.println("3 : afficher les chemins générés par l'algorithme de recherche depuis la grille");
              System.out.println("4 : afficher les mots générés par l'algorithme de recherche par la grille");
              System.out.println("5 : \"grepper\" tous les resultats de l'algo de recherche depuis le dico pour vérifier qu'ils sont bien dans le dictionnaire");
              System.out.println("6 : \"grepper\" tous les resultats de l'algo de recherche depuis la grille pour vérifier qu'ils sont bien dans le dictionnaire");
              System.out.println("7 : Sauvegarder pour une prochaine utilisation");
              System.out.println("8 : Générer des statistiques");


                bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                s = bufferRead.readLine();
                if(s.equals("0") ||s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6")||s.equals("7")||s.equals("8"))
                {    choix = Integer.parseInt(s);

                    switch(choix) {
                        case 0 : System.exit(0); break;
                        case 1: paths2.printPaths(); break;
                        case 2: paths2.printWords(); break;
                        case 3: paths1.printPaths(); break;
                        case 4: paths1.printWords(); break;
                        case 5 : debug.grepAllResult(paths2.words);break;
                        case 6 : debug.grepAllResult(paths1.words); break;
                        case 7 : debug.saveAll(); break;
                        case 8 : resetParams();doStats(); break;
                    }
                }



            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

}
