/**
 * Created by oscar on 5/10/16.
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PracticaADA {
    public static void main(String[] args) throws IOException {
        int TAMMAX = 15; //el tamaño maximo es 5000*TAMMAX+10
        int REPMED = 25; //numero de repeticiones para calcular la media
        int tam,num,vector[],i,j;
        long tiempo,tiempoMedioSec=0,tiempoMedioBin=0,opMediasSec=0,opMediasBin=0,op;

        //En cada vecor guardamos (tamaño de vector, nº operaciones sec, nº operaciones
        // binario, tiempo secuencial, tiempo binario)
        long ordenado[][]=new long[TAMMAX*REPMED][5];
        long ordenadoMedia[][]=new long[TAMMAX][5];

        long desordenado[][]=new long[TAMMAX*REPMED][5];
        long desordenadoMedia[][]=new long[TAMMAX][5];

        //Comenzamos midiendo las operaciones y tiempos de vectores ordenados.
        tam=10;
        for(i=0;i<TAMMAX;i++){
            for (j=0;j<REPMED;j++){
                ordenado[(i*REPMED)+j][0]=tam;
                System.out.print(j);
                //Creacion del vector y numero a buscar
                vector=generaVec(tam);
                ordenaVec(vector);
                num=(int)(Math.random()*tam);

                //medidas secuencial
                tiempo=System.currentTimeMillis();
                op=secuencial(vector,num);
                tiempo=System.currentTimeMillis()-tiempo;

                tiempoMedioSec+=tiempo;
                opMediasSec+=op;

                ordenado[(i*REPMED)+j][1]=op;
                ordenado[(i*REPMED)+j][3]=tiempo;

                //medidas binaria
                tiempo=System.currentTimeMillis();
                op=binaria(vector,num);
                tiempo=System.currentTimeMillis()-tiempo;

                tiempoMedioBin+=tiempo;
                opMediasBin +=op;

                ordenado[(i*REPMED)+j][2]=op;
                ordenado[(i*REPMED)+j][4]=tiempo;
            }

            ordenadoMedia[i][0]=tam;
            ordenadoMedia[i][1]=(opMediasSec/REPMED);
            ordenadoMedia[i][2]=(opMediasBin/REPMED);
            ordenadoMedia[i][3]=(tiempoMedioSec/REPMED);
            ordenadoMedia[i][4]=(tiempoMedioBin/REPMED);

            opMediasBin=0;
            opMediasSec=0;
            tiempoMedioBin=0;
            tiempoMedioSec=0;

            tam+=5000;
            System.out.println(i);


        }

        System.out.print("50%");
        //Ahora seguimos midiendo las operaciones y tiempos de vectores no ordenados.
        tam=10;
        for(i=0;i<TAMMAX;i++){
            for (j=0;j<REPMED;j++){
                System.out.print(j);
                desordenado[(i*REPMED)+j][0]=tam;

                //Creacion del vector y numero a buscar
                vector=generaVec(tam);
                num=(int)(Math.random()*tam);

                //medidas secuencial
                tiempo=System.currentTimeMillis();
                op=secuencial(vector,num);
                tiempo=System.currentTimeMillis()-tiempo;

                tiempoMedioSec+=tiempo;
                opMediasSec+=op;

                desordenado[(i*REPMED)+j][1]=op;
                desordenado[(i*REPMED)+j][3]=tiempo;

                //medidas binaria
                tiempo=System.currentTimeMillis();
                op=ordenaVec(vector);
                op+=binaria(vector,num);
                tiempo=System.currentTimeMillis()-tiempo;

                tiempoMedioBin+=tiempo;
                opMediasBin +=op;

                desordenado[(i*REPMED)+j][2]=op;
                desordenado[(i*REPMED)+j][4]=tiempo;
            }

            desordenadoMedia[i][0]=tam;
            desordenadoMedia[i][1]=(opMediasSec/REPMED);
            desordenadoMedia[i][2]=(opMediasBin/REPMED);
            desordenadoMedia[i][3]=(tiempoMedioSec/REPMED);
            desordenadoMedia[i][4]=(tiempoMedioBin/REPMED);

            opMediasBin=0;
            opMediasSec=0;
            tiempoMedioBin=0;
            tiempoMedioSec=0;

            tam+=5000;
            System.out.println(i);
        }

        imprimeVec(ordenado);
        exportaTabla(ordenado,"vectoresOrdenados");
        imprimeVec(ordenadoMedia);
        exportaTabla(ordenadoMedia,"vectoresOrdenadosMeias");

        imprimeVec(desordenado);
        exportaTabla(desordenado,"vectoresDesordenados");
        imprimeVec(desordenadoMedia);
        exportaTabla(desordenadoMedia,"vectoresDesrdenadosMedia");
    }

    private static int[] generaVec(int tam){
        int vec[] = new int[tam];
        for(int i=0;i<tam;i++){
            vec[i]=(int)(Math.random()*tam);
        }
        return vec;
    }

    private static long ordenaVec(int[] vec){
        int temp;
        long op=0;

        for(int i=1; i<vec.length ;i++){
            for(int j=i-1,k=i; j>=0; j--,k--){
                op++;
                if(vec[k]<vec[j]){
                    op+=3;
                    temp = vec[k];
                    vec[k]=vec[j];
                    vec[j]=temp;
                } else {
                    break;
                }
            }
        }
        return op;
    }

    private static void imprimeVec(long[][] vec){
        for(int i=0; i<vec.length; i++) {
            for (int j = 0; j < vec[i].length; j++) {

                System.out.print(vec[i][j] + " ");

            }
            System.out.println();
        }
        System.out.println("\n");
    }

    public static int secuencial(int[] vec, int num){
        int numOp = 0;
        for(int i=0; i<vec.length;i++){
            numOp++;
            if(vec[i]==num){
                return numOp;
            }
        }
        return numOp;
    }
    public static int binaria(int[] vec, int num){
        int numOp = 0, min=0, max=vec.length-1, med;

        while(min<=max){
            numOp++;
            med=(int)((min+max)/2);
            if(vec[med]==num){
                return numOp;
            } else {
                numOp++;
                if(num<vec[med]){
                    max=med-1;
                } else {
                    min=med+1;
                }
            }
        }
        return numOp;

    }

    public static void exportaTabla(long[][] tabla, String nombre) throws IOException{
       
        File archivoXLS = new File("./"+nombre+".xls");



        Workbook libro = new HSSFWorkbook();

        FileOutputStream archivo = new FileOutputStream(archivoXLS);

        Sheet hoja = libro.createSheet("Hoja1");


        Row fila = hoja.createRow(1);
        Cell celda = fila.createCell(1);
        celda.setCellValue("Tamaño vector");
        celda = fila.createCell(2);
        celda.setCellValue("Nº op Secuencial");
        celda = fila.createCell(3);
        celda.setCellValue("Nº op Binaria");
        celda = fila.createCell(4);
        celda.setCellValue("Tiempo secuencial");
        celda = fila.createCell(5);
        celda.setCellValue("Tiempo binaria");

        for(int i=0;i<tabla.length;i++){
            fila = hoja.createRow(i+2);
            for(int j=0;j<tabla[i].length;j++){
                celda = fila.createCell(j+1);
                celda.setCellValue(tabla[i][j]);
            }
        }

        libro.write(archivo);
        archivo.close();


        Desktop.getDesktop().open(archivoXLS);
    }
}
