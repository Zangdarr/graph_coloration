package graphe;

public interface RandomGraphGeneratorInt {
    /**
     * Génère un graphe aléatoire à partir d'un nombre de sommet et une probabilité
     * @param n : nombre de sommets du graphe
     * @param p : probabilité de la production d'un arête entre deux sommets
     * @return un graphe de n sommets avec arêtes
     * @throws IllegalArgumentException
     */
    public Graphe generateErdosRenyiGraph(int n, float p) throws IllegalArgumentException;
}
