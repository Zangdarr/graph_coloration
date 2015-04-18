package tools;

import graphe.Graphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Coloration {

    private Graphe g;
    private HashMap<Integer, Color> color; // map vertexID/color
    private HashMap<Integer,Color> colorList; //color list
    private double executionTime;
    
    
    /**
     * 
     * @param g
     * @param color
     * @param colorList
     */
    public Coloration(Graphe g, HashMap<Integer, Color> color, HashMap<Integer,Color> colorList, double executionTime) {
        super();
        this.g = g;
        this.color = color;
        this.colorList = colorList;
        this.executionTime = executionTime;
    }
    
    public HashMap<Integer,Integer> getTabColorCounter(){
        HashMap<Integer,Integer> colorCounterList = new HashMap<Integer,Integer>();
        for(int colorID : colorList.keySet()){
            colorCounterList.put(colorID,0);
        }
        
        for(int i : color.keySet()){
            int colorID = color.get(i).getId();
            colorCounterList.put(colorID, colorCounterList.get(colorID) + 1);
        }
        
        return colorCounterList;
        
    }


    
    public String toStringMinimal(){
        StringBuffer result = new StringBuffer("");
        result.append("Nombre de couleur : ");
        result.append(colorList.size());
        result.append('\n');
        
        HashMap<Integer,Integer> list = getTabColorCounter();
        
        for (int i : list.keySet()) {
            result.append(" - ");
            result.append(colorList.get(i).getName());
            result.append(" : ");
            result.append(list.get(i));
            result.append("\n");
        }
        
        return result.toString();
    }
    
    
    
    
    public double getExecutionTime() {
        return executionTime;
    }

    public String toString(){
        StringBuffer result = new StringBuffer("");
        result.append("Nombre de couleur : ");
        result.append(colorList.size());
        result.append('\n');
        
        
        
        for (Iterator<Integer> iterator = color.keySet().iterator();iterator.hasNext();){
            int id = iterator.next();
            result.append("- Vertex " + id + " ");
            result.append(color.get(id).getName());
            result.append('\n');

        }
        
        
        return result.toString();
    }
    
    public int getNbColors(){
        return colorList.size();
    }
}
