package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        int populationSize = 20;
        int dimension = 20;
        int iterations = 1000;
        double minFrequency = 0.0; //max i min czestotliwości
        double maxFrequency = 5.0;
        double alpha = 0.9; //głośność
        double gamma = 0.9; //emisja tętna
        double rMin = 0.0;  //max i min czestostliwości
        double rMax = 1.0;

        double p = 0.8;     // z jaką szansą będzie ruch do najlepszego osobnika / losowy w BOA [0;1]
        double c = 0.5;     // modalność sensoryczna zapachu w BOA
        double lambda = 1.5;// jest wykorzsytane w locie Levyego
        double F = 0.5;     //Moc mutacji
        int CR = 2;         //Skrzyżowanie (na ile części dzielimy osobników)
        double P = 0.3;     //prawdopodobieństwo mutacji

        int n = 200;        //ile raz uruchamainy algorytm dla uzyskania średniego wyniku
        int n1 = 20;        //na ile części dzielimy rozkład od 0 do 1 (podczas badania wyników)

        Functions.function[] functions = {
                Functions::Sphere_Function,
                Functions::Ackley_Function,
                Functions::Cigar,
                Functions::Schwefel_Function,
                Functions::Zakharov_Function,
                Functions::Step_Function
        };

        double[] rozklad01 = new double[n1];
        for (int i = 0; i < n1; i++) {
            rozklad01[i] = (double) 1 / n1 * i;
        }

        double[] populationSizes = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
        double[] alphaArr = rozklad01;
        double[] gammaArr = rozklad01;
        double[] rMinArr = {0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.99};
        double[] rMaxArr = {0.01, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        double[] minFrequencyArr = {0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5};
        double[] maxFrequencyArr = {0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5};

        double[] pArr = rozklad01;
        double[] cArr = rozklad01;
        double[] FArr = rozklad01;
        double[] CRArr = {2, 5, 10, 15, 25};
        double[] PArr = rozklad01;

        double[][] vectorsBat = {populationSizes, alphaArr, gammaArr, rMinArr, rMaxArr, minFrequencyArr, maxFrequencyArr};
        String[] labelsVectorBat = {"Populacja", "Szybkość redukcji głośności (alpha)", "Szsybkość emisji tętna (gamma)",
                "Minimalna częstotliwość emisji impulsów (r)", "Maksymalna częstotliwość emisji impulsów (r)",
                "Minimalna częstotliwość impulsów (f)", "Maksymalna częstotliwość impulsów (f)"};

        double[][] vectorsBoa = {populationSizes, pArr, cArr, FArr, CRArr, PArr};
        String[] labelsBVectorBOA = {"Populacja", "Prawdobopodieństwo przełączenia",
                "Modalność sensoryczna zapachu (c)", "Moc mutacji (F)", "Ilość punktów krzyżowania", "Prawdopodobieństwo mutacji"};


        String[] functionNames = {"Sphere", "Ackley", "Cigar", "Schwefel", "Zakharov", "Step"};
        double[] fMinValues = {-100.0, -50.0, -100.0, -10.0, -5.0, -10.0};
        double[] fMaxValues = {100.0, 50.0, 100.0, 10.0, 10.0, 10.0};

        var workbook = new XSSFWorkbook();


        for (int i = 0; i < functions.length; i++) {
            Sheet sheet = WorksheetUtilities.createSheet(workbook, functionNames[i]);
            Row headerRow = sheet.createRow(0);
            Row underHeaderRow = sheet.createRow(1);
            Row underUnderHeaderRow = sheet.createRow(2);

            Cell algorithm1 = headerRow.createCell(0);
            algorithm1.setCellValue("BAT");
            for (int v = 0; v < vectorsBat.length; v++) {
                double[] results = new double[vectorsBat[v].length];

                Cell parametr = underHeaderRow.createCell(v * 2);
                parametr.setCellValue(labelsVectorBat[v]);
                Cell value = underUnderHeaderRow.createCell(v * 2);
                value.setCellValue("Value");
                Cell result = underUnderHeaderRow.createCell(v * 2 + 1);
                result.setCellValue("Result");


                for (int k = 0; k < vectorsBat[v].length; k++) {
                    double[] valuesVector = {30, 0.0, 5.0, 0.9, 0.9, 0.0, 1.0};
                    valuesVector[v] = vectorsBat[v][k];
                    double actualResult = 0;
                    for (int j = 0; j < n; j++) {
                        BatAlgorithm batAlgorithm = new BatAlgorithm((int) valuesVector[0], valuesVector[1],
                                valuesVector[2], valuesVector[3], valuesVector[4], valuesVector[5],
                                valuesVector[6], fMinValues[i], fMaxValues[i], dimension, iterations, functions[i]);
                        actualResult += batAlgorithm.run();
                    }

                    Row row;
                    if (sheet.getRow(k + 3) != null)
                        row = sheet.getRow(k + 3);
                    else {
                        row = sheet.createRow(k + 3);
                    }
                    Cell cellv = row.createCell(v * 2);
                    cellv.setCellValue(vectorsBat[v][k]);
                    Cell cellr = row.createCell(v * 2 + 1);
                    cellr.setCellValue(actualResult / n);

                    results[k] = actualResult / n;
                    //System.out.println("BAT Minimum najlepszy: " + actualResult / n);
                }
                Charts.plotRoute(functionNames[i], labelsVectorBat[v], "BAT", results, vectorsBat[v]);
            }

            int indent = vectorsBat.length * 2;
            Cell algorithm2 = headerRow.createCell(indent);
            algorithm2.setCellValue("BOA");
            for (int v = 0; v < vectorsBoa.length; v++) {
                double[] results = new double[vectorsBoa[v].length];

                Cell parametr = underHeaderRow.createCell(indent + v * 2);
                parametr.setCellValue(labelsBVectorBOA[v]);
                Cell value = underUnderHeaderRow.createCell(indent + v * 2);
                value.setCellValue("Value");
                Cell result = underUnderHeaderRow.createCell(indent + v * 2 + 1);
                result.setCellValue("Result");

                for (int k = 0; k < vectorsBoa[v].length; k++) {
                    double[] valuesVector = {30, 0.8, 0.5, 0.5, 2, 0.3};
                    valuesVector[v] = vectorsBoa[v][k];
                    double actualResult = 0;
                    for (int j = 0; j < n; j++) {
                            BOAwithLevyFlightAlgorithm boaAlgorithm = new BOAwithLevyFlightAlgorithm((int) valuesVector[0],
                                    valuesVector[1], valuesVector[2],
                                    lambda, valuesVector[3], (int) valuesVector[4], valuesVector[5],
                                    fMinValues[i], fMaxValues[i], dimension, iterations, functions[i]);
                        actualResult += boaAlgorithm.run();
                    }

                    Row row;
                    if (sheet.getRow(k + 3) != null)
                        row = sheet.getRow(k + 3);
                    else {
                        row = sheet.createRow(k + 3);
                    }
                    Cell cellv = row.createCell(indent + v * 2);
                    cellv.setCellValue(vectorsBoa[v][k]);
                    Cell cellr = row.createCell(indent + v * 2 + 1);
                    cellr.setCellValue(actualResult / n);

                    results[k] = actualResult / n;
                    //System.out.println("BAT Minimum najlepszy: " + actualResult / n);
                }
                Charts.plotRoute(functionNames[i], labelsBVectorBOA[v], "BOA", results, vectorsBoa[v]);
            }

            indent += vectorsBoa.length * 2;
            Cell algorithm3 = headerRow.createCell(indent);
            algorithm3.setCellValue("BOA with modification");
            for (int v = 0; v < vectorsBoa.length; v++) {
                double[] results = new double[vectorsBoa[v].length];

                Cell parametr = underHeaderRow.createCell(indent + v * 2);
                parametr.setCellValue(labelsBVectorBOA[v]);
                Cell value = underUnderHeaderRow.createCell(indent + v * 2);
                value.setCellValue("Value");
                Cell result = underUnderHeaderRow.createCell(indent + v * 2 + 1);
                result.setCellValue("Result");

                for (int k = 0; k < vectorsBoa[v].length; k++) {
                    double[] valuesVector = {30, 0.8, 0.5, 0.5, 2, 0.3};
                    valuesVector[v] = vectorsBoa[v][k];
                    double actualResult = 0;
                    for (int j = 0; j < n; j++) {
                        BOAwithLevyFlightAlgorithmOwnVersion boaAlgorithm = new BOAwithLevyFlightAlgorithmOwnVersion((int) valuesVector[0],
                                valuesVector[1], valuesVector[2],
                                lambda, valuesVector[3], (int) valuesVector[4], valuesVector[5],
                                fMinValues[i], fMaxValues[i], dimension, iterations, functions[i]);
                        actualResult += boaAlgorithm.run();
                    }

                    Row row;
                    if (sheet.getRow(k + 3) != null)
                        row = sheet.getRow(k + 3);
                    else {
                        row = sheet.createRow(k + 3);
                    }
                    Cell cellv = row.createCell(indent + v * 2);
                    cellv.setCellValue(vectorsBoa[v][k]);
                    Cell cellr = row.createCell(indent + v * 2 + 1);
                    cellr.setCellValue(actualResult / n);

                    results[k] = actualResult / n;
                    //System.out.println("BAT Minimum najlepszy: " + actualResult / n);
                }
                Charts.plotRoute(functionNames[i], labelsBVectorBOA[v], "BOA with modification", results, vectorsBoa[v]);
            }

            try (FileOutputStream outputStream = new FileOutputStream("data.xlsx")) {
                workbook.write(outputStream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            System.out.println("===================================");
        }
        workbook.close();

    }


}
