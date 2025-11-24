import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {
    static int num_restricoes;
    public static void main(String[] args) {
        String caminhoArquivo = "C:\\Users\\pakkz\\OneDrive\\Documentos\\SIMPLEX";

        File pasta = new File(caminhoArquivo);

        if (pasta.isDirectory()) {
            File arquivos[] = pasta.listFiles();

            if (arquivos != null) {
                for (File file : arquivos) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {

                        try (BufferedReader leitor = new BufferedReader(new FileReader(file))) {
                            String linha;

                            int tipoProblema = 0;
                            int variaveis = 0;
                            double[] funcaoObjetivo;
                            double[][] restricoes;
                            double[][] matrizPrincipal;

                            // primeira linha (tipo de problema)
                            if ((linha = leitor.readLine()) != null) {
                                tipoProblema = Integer.parseInt(linha.trim());
                            }

                            // segunda linha (qtd de variaveis)
                            if ((linha = leitor.readLine()) != null) {
                                variaveis = Integer.parseInt(linha.trim());
                            }

                            // terceira linha (qtd de restricoes)
                            if ((linha = leitor.readLine()) != null) {
                                num_restricoes = Integer.parseInt(linha.trim());
                            }

                            funcaoObjetivo = new double[variaveis];
                            restricoes = new double[num_restricoes][variaveis+1]; // M e N (já vai vir validado no txt)

                            // lendo a funcao objetiva
                            if ((linha = leitor.readLine()) != null) {
                                StringTokenizer linhaFormatada = new StringTokenizer(linha, " ");
                                for (int i = 0; i < variaveis; i++) {
                                    if(tipoProblema == 1){
                                        funcaoObjetivo[i] = Double.parseDouble(linhaFormatada.nextToken()) * (-1);
                                    } else{
                                        funcaoObjetivo[i] = Double.parseDouble(linhaFormatada.nextToken());
                                    }
                                    
                                }
                            }

                            // lendo as restricoes
                            for (int i = 0; i < num_restricoes; i++) {
                                if ((linha = leitor.readLine()) != null) {
                                    StringTokenizer linhaFormatada = new StringTokenizer(linha, " ");
                                    for (int j = 0; j < variaveis+1; j++) {
                                            restricoes[i][j] = Double.parseDouble(linhaFormatada.nextToken());
                                    }
                                }
                            }
                                  
                            // matriz onde os cálculos vão acontecer
                            matrizPrincipal = new double[num_restricoes + 1] [variaveis + num_restricoes + 1]; // 4 linhas e 6 colunas (já contando com as variaveis de folga e lado direito)
                            
                            //preenchendo a primeira linha da matriz principal com a função objetivo
                            for (int j = 0; j< variaveis ; j++){
                                matrizPrincipal[0][j] = funcaoObjetivo[j];
                            }

                            //preenchendo a restante com as restrições
                            for (int i = 1; i <= num_restricoes; i++) {

                            //coeficientes das variáveis
                            for (int j = 0; j < variaveis; j++) {
                                matrizPrincipal[i][j] = restricoes[i - 1][j];
                            }

                            //variável de folga
                            matrizPrincipal[i][variaveis + (i - 1)] = 1;

                            // lado direito
                            matrizPrincipal[i][variaveis + num_restricoes] = restricoes[i - 1][variaveis];
                        }

                        calculosSimplex(matrizPrincipal);        
                            
                        } catch (IOException e) {
                            System.out.println("Erro ao ler o arquivo: " + file.getName());
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("A pasta está vazia ou não pode ser lida.");
            }
        } else {
            System.out.println("O caminho especificado não é um diretório.");
        }
    }

    public static void calculosSimplex(double[][] matriz){
        while(encontraNegativo(matriz)){
            int colunaPivo;
            colunaPivo = escolherColunaPivo(matriz);
            if (colunaPivo == -1) break; // solução ótima
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
        for(int i = 1; i < num_restricoes + 1; i++){
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

        for(int i = 0; i < num_restricoes + 1; i++){
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
        for(int i = 0; i< num_restricoes + 1; i++){
            for(int j=0; j < matriz[0].length; j++){
                System.out.printf("%.2f ", matriz[i][j]);
            }
            System.out.println();
        }
    }

   
}
