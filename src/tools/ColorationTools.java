package tools;
import graphe.Edge;
import graphe.Graphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        System.out.println("done.\nExecution time : " + (end-start)/Math.pow(10, 9));
        return new Coloration(g, coloration, colorList);
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
    public static int[] setColorTab(final HashMap<Integer,Color> coloration, final HashMap<Integer,Color> colorList, final ArrayList<Edge> edgeList,final int vertexID, final int default_pos){
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
    public static int[] InitColorTab(final HashMap<Integer,Color> colorList, final int default_pos){
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
    public static Iterator<Integer> getVertexNeighbors(ArrayList<Edge> edgeList, int current_id){
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
    public static Coloration WelshPowellColoration(Graphe g) {
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
        
        //Récupération des sommets associés à leur degres
        vertexDegre = getVertexDegre(g);

        do {

            //Récupération des sommets trié par degre decroissant
            sortedVertex = getDescendingSorted((HashMap<Integer,Integer>)vertexDegre.clone());

            //On prend le sommet de plus haut degre
            vertexGroup = new ArrayList<Integer>();
            vertexGroup.add(sortedVertex.get(0));

            //recherche d"une stable de sommet priorisant les sommets de degre les plus eleves
            vertexGroup = attributColorTo(vertexGroup, sortedVertex);
            
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
        System.out.println("done.\nExecution time : " + (end-start)/Math.pow(10, 9));
        
        return new Coloration(g,coloration,colorList);
    }

    /**
     * Retourne les degrees de tous les sommets d'un graphe
     * @param edgeList
     * @return
     */
    public static HashMap<Integer,Integer> getVertexDegre(Graphe g){
        //TODO 
        return null;
    }

    /**
     * Retourne les sommets tries dans l'ordre decroissant de leur degre
     * @param edgeList
     * @return
     */
    public static ArrayList<Integer> getDescendingSorted(HashMap<Integer,Integer> degreeList){
        //TODO 
        return null;
    }


    /**
     * Cherche la stable la plus grande non coloré
     * @param vertexGroup
     * @param sortedVertex
     * @return
     */
    public static ArrayList<Integer> attributColorTo(ArrayList<Integer> vertexGroup, ArrayList<Integer> sortedVertex){

        return null;
    }

    /**
     * Compte le nombre de voisin de @current_ID dans @edgeList
     * @param edgeList : liste des arête du graphe
     * @param current_id : sommet que le veut colorer
     * @return le nombre de voisin
     */
    public static int CountVertexNeighbors(ArrayList<Edge> edgeList, int current_id){
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
}
