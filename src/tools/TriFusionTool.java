package tools;

public class TriFusionTool {
    public static void triFusion(int tableau[], int [] repere)
    {
        int longueur=tableau.length;
        if (longueur>0)
        {
            triFusion(tableau,0,longueur-1, repere);
        }
    }

    private static void triFusion(int tableau[],int deb,int fin, int repere[])
    {
        if (deb!=fin)
        {
            int milieu=(fin+deb)/2;
            triFusion(tableau,deb,milieu,repere);
            triFusion(tableau,milieu+1,fin, repere);
            fusion(tableau,deb,milieu,fin,repere);
        }
    }

    private static void fusion(int tableau[],int deb1,int fin1,int fin2, int repere[])
    {
        int deb2=fin1+1;

        //on recopie les éléments du début du tableau
        int table1[]=new int[fin1-deb1+1];
        int table2[]=new int[fin1-deb1+1];
        for(int i=deb1;i<=fin1;i++)
        {
            table1[i-deb1]=tableau[i];
            table2[i-deb1]=repere[i];
        }

        int compt1=deb1;
        int compt2=deb2;

        for(int i=deb1;i<=fin2;i++)
        {        
            if (compt1==deb2) //c'est que tous les éléments du premier tableau ont été utilisés
            {
                break; //tous les éléments ont donc été classés
            }
            else if (compt2==(fin2+1)) //c'est que tous les éléments du second tableau ont été utilisés
            {
                tableau[i]=table1[compt1-deb1]; //on ajoute les éléments restants du premier tableau
                repere[i]=table2[compt1-deb1];
                compt1++;
            }
            else if (table1[compt1-deb1]<tableau[compt2])
            {
                tableau[i]=table1[compt1-deb1]; //on ajoute un élément du premier tableau
                repere[i]=table2[compt1-deb1];
                compt1++;
            }
            else
            {
                tableau[i]=tableau[compt2]; //on ajoute un élément du second tableau
                repere[i]=repere[compt2];
                compt2++;
            }
        }
    }

}
