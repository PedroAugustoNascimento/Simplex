public class Simplex {
    private static int num_restricoes;

    public Simplex(int num_restricoes){
        this.num_restricoes = num_restricoes;
    }

    public static int getNumRestricoes(){
        return num_restricoes;
    }


    public static void calculosSimplex(double[][] matriz){
        while(encontraNegativo(matriz)){
            int colunaPivo;
            colunaPivo = escolherColunaPivo(matriz);
            if (colunaPivo == -1) {
                break; // solução ótima
            }
            int linhaPivo;
            linhaPivo = escolherLinhaPivo(matriz, colunaPivo);
            calculoGaussJordan(matriz, colunaPivo, linhaPivo);
        }
        imprimirResultado(matriz);
    }


    public static int escolherColunaPivo(double[][] matriz){
        double menorValor = 99999999;
        int coluna = 0;
        for(int i = 0; i < 1; i++){
            for(int j = 0; j < matriz[0].length; j++){
                if(menorValor > matriz[i][j]){
                    menorValor = matriz[i][j];
                    coluna = j;
                }
            }
        }
        return coluna;
    }

    public static int escolherLinhaPivo(double[][] matriz, int colunaPivo){
        double divisao;
        double menorValor = 99999;
        int linhaPivo = -1;
        for(int i = 1; i < (getNumRestricoes() + 1); i++){
                if(matriz[i][colunaPivo] <= 0){
                    continue;
                }
                divisao = matriz[i][matriz[0].length - 1] / matriz[i][colunaPivo]; //LD / termo da coluna pivô
                
                if(divisao < menorValor){
                    menorValor = divisao;
                    linhaPivo = i;
                }
        }
        return linhaPivo;
    }

    public static void calculoGaussJordan(double[][] matriz, int colunaPivo, int linhaPivo){
        double elementoPivo = matriz[linhaPivo][colunaPivo];
        for(int j = 0; j < matriz[0].length; j++){
                    matriz[linhaPivo][j] = matriz[linhaPivo][j] / elementoPivo; // nova linha pivô = antiga linha pivô / número pivô
        }      

        for(int i = 0; i < (getNumRestricoes() + 1); i++){
            if( i != linhaPivo){
                double novoElemento = matriz[i][colunaPivo];
                for(int j = 0; j < matriz[0].length; j++){
                    matriz[i][j] = matriz[i][j] - novoElemento * matriz[linhaPivo][j]; // nova linha = antiga linha – coeficiente da linha na coluna pivô * coeficiente da coluna na nova linha pivô       
            }
            } 
        }
    }

     public static boolean encontraNegativo(double[][] matriz) {
    for (int j = 0; j < matriz[0].length; j++) {
        if (matriz[0][j] < 0){
            return true;
        } 
    }
    return false;
}
    public static void imprimirResultado(double[][] matriz){
        for(int i = 0; i< (getNumRestricoes() + 1); i++){
            for(int j=0; j < matriz[0].length; j++){
                System.out.print(" "+ matriz[i][j]);
            }
            System.out.println();
        }
    }
}
