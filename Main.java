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
                                        funcaoObjetivo[i] = Double.parseDouble(linhaFormatada.nextToken()) * (-1); // multiplicando a função objetivo por -1
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

                        Simplex simplex = new Simplex(num_restricoes);
                        Simplex.calculosSimplex(matrizPrincipal);
                          
                            
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

}
