package tools;
import graphe.Edge;
import graphe.Graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ColorationTools {

    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/

    /**
     * Applique un algorithme glouton pour colorer le graphe g
     * @param g
     * @return
     */
    public static Coloration greedyColoration(final Graphe g){
        long start = System.nanoTime();

        //map contenant les associations couleur/sommet
        HashMap<Integer,Color> coloration = new HashMap<Integer, Color>();
        //liste contenant toutes les couleurs crée ainsi que la couleur par default(id = nb sommet)
        HashMap<Integer,Color> colorList = new HashMap<Integer,Color>();
        //sert à l'attribution des ID au couleur
        AtomicInteger next_color_ID = new AtomicInteger();
        //liste des arêtes du graphe
        ArrayList<Edge> edgeList = g.getEdgeList();

        //iterateur sur les sommets du graphe
        Iterator<Integer> it = g.getVertexKeySet().iterator();
        //creation de la couleur par default à ns ( nombre de sommet) position : le nombre max de couleurs est ns-1
        colorList.put(g.getVertexQuantity(),new Color(g.getVertexQuantity(),"default_color"));
        //initialisation des couleurs des sommets à la couleur -1
        Color default_color = colorList.get(g.getVertexQuantity());
        for (Iterator<Integer> iterator = it; iterator.hasNext();) {
            Integer vertexID = (Integer) iterator.next();
            coloration.put(vertexID, default_color);
        }


        //Attribution des couleurs aux sommets
        int[] colorTab;
        boolean done;
        it = g.getVertexKeySet().iterator();
        for (Iterator<Integer> iterator = it; iterator.hasNext();) {
            Integer vertexID = iterator.next();


            //on trouve les couleurs des voisins que l'on ne pourra donc pas utiliser.
            colorTab = setColorTab(coloration, colorList,edgeList,vertexID, g.getVertexQuantity());

            //on va attribué la couleur qui n'as pas été trouvé la plus petite possible
            done = false;
            for (int i = 0; i < colorTab.length-1; i++) {
                if(colorTab[i] == 0){
                    coloration.put(vertexID, colorList.get(i));
                    done = true;
                    break;
                }
            }
            //si toutes les couleurs exeptée default on été trouvé alors on créer une nouvelle couleur
            if(!done){
                int new_color_id = next_color_ID.getAndIncrement();
                colorList.put(new_color_id,new Color(new_color_id, "Couleur " +new_color_id));
                coloration.put(vertexID, colorList.get(new_color_id));
            }

        }
        long end = System.nanoTime();
        
        double executionTime = (end-start)/Math.pow(10, 9);
        System.out.println("Greedy coloration done.\nExecution time : " + executionTime );
        
        
        colorList.remove(g.getVertexQuantity());
        return new Coloration(g, coloration, colorList, executionTime);

    }

    /**
     * Retourne un tableau avec pour index les numero des couleurs et en contenu le nombre de voisin de vertexID qui possèdent ces couleurs 
     * @param coloration : liste associant les sommets à leur couleurs respectives
     * @param colorList : liste des couleurs
     * @param edgeList : liste des arêtes du graphe
     * @param vertexID : ID du sommet que l'on veut colorer
     * @param default_pos : position dans la liste des couleurs de la couleur par defaut
     * @return 
     */
    protected static int[] setColorTab(final HashMap<Integer,Color> coloration, final HashMap<Integer,Color> colorList, final ArrayList<Edge> edgeList,final int vertexID, final int default_pos){
        int tmp,
        neighborColorID;
        int[] colorTab = InitColorTab(colorList, default_pos);
        for (Iterator<Integer> iterator2 = getVertexNeighbors(edgeList, vertexID); iterator2.hasNext();) {
            Integer neighborID = iterator2.next();
            Color neighborColor = coloration.get(neighborID);
            tmp = neighborColor.getId();
            neighborColorID = (tmp == default_pos)? colorTab.length-1 : tmp;
            colorTab[neighborColorID]++;
        }
        return colorTab;
    }

    /**
     * Initialise un tableau de la taille du nombre de couleur
     * @param colorList
     * @param default_pos
     * @return
     */
    protected static int[] InitColorTab(final HashMap<Integer,Color> colorList, final int default_pos){
        int[] tab = new int[colorList.size()];
        int i = 0;
        for (Iterator<Integer> iterator = colorList.keySet().iterator(); iterator.hasNext();) {
            int colorID = iterator.next();
            if(colorID == default_pos){
                tab[tab.length-1] = 0;
            }else{
                tab[i] = 0;
            }
        }
        return tab;
    }

    /**
     * Trouve les voisin de @current_ID dans @edgeList
     * @param edgeList : liste des arête du graphe
     * @param current_id : sommet que le veut colorer
     * @return
     */
    protected static Iterator<Integer> getVertexNeighbors(ArrayList<Edge> edgeList, int current_id){
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (Iterator<Edge> iterator = edgeList.iterator(); iterator.hasNext();) {
            Edge edge = iterator.next();
            if(edge.getVertex_1().getId() == current_id)
                result.add(edge.getVertex_2().getId());
            if(edge.getVertex_2().getId() == current_id)
                result.add(edge.getVertex_1().getId());
        }
        return result.iterator();
    }




    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/



    /**
     * Applique l'algorithme de coloration de Welsh-Powell sur g
     * @param g
     * @return
     */
    public static Coloration WelshPowellColoration(final Graphe g) {
        long start = System.nanoTime();
      //map contenant les associations couleur/sommet
        HashMap<Integer,Color> coloration = new HashMap<Integer, Color>();
        //liste contenant toutes les couleurs crée
        HashMap<Integer,Color> colorList = new HashMap<Integer,Color>();
        //sert à l'attribution des ID au couleur
        AtomicInteger next_color_ID = new AtomicInteger();
        int new_color_id;
        Color c;
        HashMap<Integer,Integer> vertexDegre;
        ArrayList<Integer> sortedVertex,
                           vertexGroup;
        ArrayList<Edge> edgeList = g.getEdgeList();
        
        //Récupération des sommets associés à leur degres
        vertexDegre = getVertexDegre(g);

        do {

            //Récupération des sommets trié par degre decroissant
            sortedVertex = getDescendingFusionSorted(vertexDegre);

            //On prend le sommet de plus haut degre
            vertexGroup = new ArrayList<Integer>();
            vertexGroup.add(sortedVertex.get(0));

            //recherche d"une stable de sommet priorisant les sommets de degre les plus eleves
            vertexGroup = attributColorTo(edgeList,vertexGroup, sortedVertex,0);
            
            // attribution de la couleur à toute la stabe trouvee
            // && supprimer les sommet coloré de vertexdegre
            new_color_id = next_color_ID.getAndIncrement();
            c = new Color(new_color_id, "Couleur " +new_color_id);
            colorList.put(new_color_id,c);            
            for (Iterator<Integer> iterator = vertexGroup.iterator(); iterator.hasNext();) {
                Integer integer = iterator.next();
                coloration.put(integer, c);
                vertexDegre.remove(integer);
            }
            
            

        } while(!vertexDegre.isEmpty());

        long end = System.nanoTime();
        double executionTime = (end-start)/Math.pow(10, 9);
        System.out.println("Welsh-Powell coloration done.\nExecution time : " + executionTime);
        
        return new Coloration(g,coloration,colorList, executionTime);
    }

    /**
     * Retourne les degrees de tous les sommets d'un graphe
     * @param edgeList
     * @return
     */
    protected static HashMap<Integer,Integer> getVertexDegre(final Graphe g){
        HashMap<Integer,Integer> vertexDegre = new HashMap<Integer,Integer>();
        ArrayList<Edge> edgeList = g.getEdgeList();
        for (Integer vertexID : g.getVertexKeySet()) {
            vertexDegre.put(vertexID, CountVertexNeighbors(edgeList, vertexID));
        }
        return vertexDegre;
    }

    /**
     * Retourne les sommets tries dans l'ordre decroissant de leur degre
     * @param edgeList
     * @return
     */
    protected static ArrayList<Integer> getDescendingSorted(final HashMap<Integer,Integer> degreeList){
        ArrayList<Integer> sortedList = new ArrayList<Integer>();
        HashMap<Integer,Integer> clone_degreeList = (HashMap<Integer, Integer>) degreeList.clone();
        int bestDegree = Integer.MIN_VALUE,
            bestVertexID= -1, tmpID, tmpDegre;
        int i = 0;
        
        //tant qu'on a pas tous trie
        while (!clone_degreeList.isEmpty()) {
            //on parcour les sommets restant pour trouver le prochain plus grand degre
            for(Iterator<Integer> iterator = clone_degreeList.keySet().iterator(); iterator.hasNext();){
                tmpID = iterator.next();
                tmpDegre = clone_degreeList.get(tmpID);
                if(bestDegree < tmpDegre){
                    bestDegree = tmpDegre;
                    bestVertexID = tmpID;
                }
            }
            
            sortedList.add(bestVertexID);
            clone_degreeList.remove(bestVertexID);
            bestDegree = Integer.MIN_VALUE;
        }
        return sortedList;
    }
    
    /**
     * Retourne les sommets tries dans l'ordre decroissant de leur degre en appliquant le tri fusion
     * @param edgeList
     * @return
     */
    protected static ArrayList<Integer> getDescendingFusionSorted(final HashMap<Integer,Integer> degreeList){
        int[] tableIndice = new int[degreeList.size()],
              tableValeur = new int[degreeList.size()],
              matchKeyIndice = new int[degreeList.size()];
        
        Set<Integer> set = degreeList.keySet();
        int count = 0;
        for (Integer i : set) {
            tableIndice[count] = count;
            tableValeur[count] = degreeList.get(i);
            matchKeyIndice[count++] = i;
        }

        TriFusionTool.triFusion(tableValeur,tableIndice);
        
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = tableIndice.length-1; i >= 0; i--) {
            result.add(matchKeyIndice[tableIndice[i]]);
        }
        return result;
    }


    /**
     * Cherche la stable la plus grande non colorée
     * @param vertexGroup
     * @param sortedVertex
     * @return la liste de vertex avec un nouveau membre pour la stabe s'il il y a.
     */
    protected static ArrayList<Integer> attributColorTo(final ArrayList<Edge> edgeList, final ArrayList<Integer> vertexGroup, final ArrayList<Integer> sortedVertex, int from){
        int tmp = -1;
        for (int i = from; i < sortedVertex.size();i++) {
            tmp = sortedVertex.get(i);
            if(!isVertexGroupNeighbors(edgeList, vertexGroup, tmp)){
                vertexGroup.add(tmp);
                return attributColorTo(edgeList, vertexGroup, sortedVertex, tmp);
            }
        }
        return vertexGroup;
    }

    /**
     * Compte le nombre de voisin de @current_ID dans @edgeList
     * @param edgeList : liste des arêtes du graphe
     * @param current_id : sommet que l'on veut colorer
     * @return le nombre de voisins
     */
    protected static int CountVertexNeighbors(final ArrayList<Edge> edgeList, final int current_id){
        int result = 0;

        for (Iterator<Edge> iterator = edgeList.iterator(); iterator.hasNext();) {
            Edge edge = iterator.next();
            if(edge.getVertex_1().getId() == current_id)
                result++;
            if(edge.getVertex_2().getId() == current_id)
                result++;
            }
        return result;
    }
    
    protected static boolean isVertexGroupNeighbors(final ArrayList<Edge> edgeList, final ArrayList<Integer> groupVertex,final int vertexID){

        if(groupVertex.contains(vertexID))
            return true;
        
        for (Iterator<Edge> iterator = edgeList.iterator(); iterator.hasNext();) {
            Edge edge = iterator.next();
            if(edge.getVertex_1().getId() == vertexID && groupVertex.contains(edge.getVertex_2().getId()))
                return true;
            if(edge.getVertex_2().getId() == vertexID && groupVertex.contains(edge.getVertex_1().getId()))
                return true;
        }
        return false;
    }
    
    
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/
    /*************************************************************************************************************************************/

    
    /**
     * Applique l'algorithme de coloration de DSAT sur g
     * @param g
     * @return
     */
    public static Coloration DSATColoration(final Graphe g) {
        
          /** Declarations de base ***********************************************************************/
          final long start = System.nanoTime();
          //map contenant les associations couleur/sommet
          final HashMap<Integer,Color> coloration = new HashMap<Integer, Color>();
          //liste contenant toutes les couleurs crée
          final HashMap<Integer,Color> colorList = new HashMap<Integer,Color>();
          //sert à l'attribution des ID au couleur
          final AtomicInteger next_color_ID = new AtomicInteger();
          //variable temporaire pour l'ID de la nouvelle couleur
          int new_color_id;
          //Objet qui instanciera les couleurs
          Color c;
          //liste des arête du graphe
          final ArrayList<Edge> edgeList = g.getEdgeList();
          
          /** Déclarations spécifiques********************************************************************/
          //map des degres de chaque sommet
          HashMap<Integer,Integer> vertexDegre;
          
          ArrayList<Integer> sortedVertex;//liste des sommets tries par degre
          HashMap<Integer,Integer> nbVoisinsColorList = new HashMap<Integer,Integer>();//liste contenant le nombre de voisins colores de chaque vertex
          
          //liste des voisins de chaque sommet
          final HashMap<Integer, ArrayList<Integer>> voisins = new HashMap<Integer,ArrayList<Integer>>();
          //liste des couleurs des voisins existante
          final HashMap<Integer, ArrayList<Integer>> couleursVoisines = new HashMap<Integer,ArrayList<Integer>>();
          

          /** Initialisation ******************************************************************************/
          //Récupération des sommets associés à leur degres
          vertexDegre = getVertexDegre(g);
          
          /** 1 *******/
          /** 2 *******/
          for (Integer vertexID : vertexDegre.keySet()) {
              //Association des sommets à leur voisins
              voisins.put(vertexID, getVertexNeighborsList(edgeList, vertexID)); 
              //Association des sommets aux couleurs de leur voisins
              couleursVoisines.put(vertexID, new ArrayList<Integer>());
              nbVoisinsColorList.put(vertexID,0);
          }
          
          /** 3 *******/
          //Récupération des sommets trié par degre decroissant
          sortedVertex = getDescendingFusionSorted(vertexDegre);

          /** 4 *******/
          //On associe la premiere couleur au sommet de plus haut degre
          int vertexID = sortedVertex.get(0);
          new_color_id = next_color_ID.getAndIncrement();
          c = new Color(new_color_id,"Couleur " + new_color_id);
          colorList.put(new_color_id, c);
          coloration.put(vertexID, c);
          for (Integer v : voisins.get(vertexID)) {
              couleursVoisines.get(v).add(new_color_id);
              nbVoisinsColorList.put(v, nbVoisinsColorList.get(v) + 1);
          }
          
          int untilNbVertex = g.getVertexQuantity();
          
          /** Boucle principale ***************************************************************************/
          do {
              /*************************************************/
              //find the max SAT vertex
              /** Tri sur les SAT *******/
              int[] repere = new int[nbVoisinsColorList.size()],
                    values = new int[nbVoisinsColorList.size()];
              for (int i = 0 ; i<repere.length; i++) {
                  repere[i] = i+1;
                  values[i] = nbVoisinsColorList.get(i+1);
              }
              TriFusionTool.triFusion(values, repere);
              
              
              /** Recuperation des vertex qui ont maxSAT *******/
              ArrayList<Integer> tmpListMAXSAT = new ArrayList<Integer>();
              boolean isMax = true,
                      first = true;
              int max = Integer.MIN_VALUE;
              
              for(int i = repere.length-1;i >=0 && isMax ;i--){
                  if(coloration.containsKey(repere[i]))
                      continue;   
                  if(first){//on trouve le premier vertex qui n'as pas été coloré comme base pour MAX
                      max = values[i];
                      tmpListMAXSAT.add(repere[i]);
                      first = false;
                      continue;
                  }
                  if((isMax = (values[i] == max))) 
                      tmpListMAXSAT.add(repere[i]);
              }
              
              
              /** Selection de celui ayant le plus haut degre *******/
              max = Integer.MIN_VALUE;
              int choosenVertex = 0;

              for (Integer v : tmpListMAXSAT){
                  if(coloration.containsKey(v)) continue;
                  int current_degree = vertexDegre.get(v);
                  if( current_degree > max){
                      max = current_degree;
                      choosenVertex = v;
                  }
              }
              
              
              /** Choix de la coloration du vertex  ******/
              int intChoosenColor = -1;
              int previous = 0;
              ArrayList<Integer> list = (ArrayList<Integer>) couleursVoisines.get(choosenVertex).clone();
              Collections.sort(list);
              
              if(list.get(0) != 0){
                  intChoosenColor = 0;
                  
              }
              else{
                  int tmp1;
                  for (int i = 1; i < list.size(); i++) {
                      tmp1 = list.get(i) - previous;
                      if(tmp1 != 1){// si différent de 1 alors il y a au moins une couleur de libre entre tmp1 et la couleur précédente
                          intChoosenColor = previous + 1;
                          break;
                      }
                      else {
                          previous = list.get(i);
                      }
                  }
              }
              
              //pas de couleur disponible(toutes les couleurs existantes sont occupees par les voisins)
              //creation d'une nouvelle couleur
              if(intChoosenColor == -1) {
                  new_color_id = next_color_ID.getAndIncrement();
                  c = new Color(new_color_id,"Couleur " + new_color_id);
                  colorList.put(new_color_id, c);
              }
              else{
                  c = colorList.get(intChoosenColor);
              }
              /** Ajout du sommet colore *******/
              coloration.put(choosenVertex, c);
              
              /** Mise a jour des couleurs voisines des voisin du sommet qui vient d'etre colore ******/
              for (Integer v : voisins.get(choosenVertex)){
                  nbVoisinsColorList.put(v, nbVoisinsColorList.get(v) + 1);
                  if(! couleursVoisines.get(v).contains(new_color_id))
                      couleursVoisines.get(v).add(new_color_id);
              }
              
          } while(coloration.size() < untilNbVertex);

          long end = System.nanoTime();
          double exexutionTime = (end-start)/Math.pow(10, 9);
          System.out.println("DSAT coloration done.\nExecution time : " + exexutionTime);
          
          
          return new Coloration(g,coloration,colorList, exexutionTime);

        
    }
    
    /**
     * Trouve les voisin de @current_ID dans @edgeList
     * @param edgeList : liste des arête du graphe
     * @param current_id : sommet que le veut colorer
     * @return
     */
    protected static ArrayList<Integer> getVertexNeighborsList(ArrayList<Edge> edgeList, int current_id){
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (Iterator<Edge> iterator = edgeList.iterator(); iterator.hasNext();) {
            Edge edge = iterator.next();
            if(edge.getVertex_1().getId() == current_id)
                result.add(edge.getVertex_2().getId());
            if(edge.getVertex_2().getId() == current_id)
                result.add(edge.getVertex_1().getId());
        }
        return result;
    }
    
}
