/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maxones;


/**
 *
 * @author Fran33
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class GA {

    private int tamPoblacion;
    private int tamIndividuo;
    private int numeroGeneraciones;



    public GA(int pSize, int iSize, int nGens) {
        tamPoblacion = pSize;
        tamIndividuo = iSize;
        numeroGeneraciones = nGens;
    }

    public ArrayList<Cromosoma> generarPoblacion() {
        ArrayList<Cromosoma> poblacion = new ArrayList<>();
        final String alphabet = "01";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < tamPoblacion; i++) {
            // crea random ind
            for (int j = 0; j < tamIndividuo; j++) {
                int index = random.nextInt(alphabet.length());
                sb.append(alphabet.charAt(index));
            }

            poblacion.add(new Cromosoma(sb.toString()));
            sb.setLength(0);
        }

        return poblacion;
    }

    public int fitnessInd(String ind) {
        int count = 0;

        for (int i = 0; i < ind.length(); i++) {
            if (ind.charAt(i) == '1') {
                count++;
            }
        }

        return count;
    }

    public int fitnessPoblacion(ArrayList<Cromosoma> poblacion) {
        int totalFitness = 0;
        int indFitness = 0;

        for (var ind : poblacion) {
            indFitness = fitnessInd(ind.getGenes());
            ind.setFitness(indFitness);

            totalFitness += indFitness;
        }

        return totalFitness;
    }

    public void setpoblacionProbabilidad(ArrayList<Cromosoma> poblacion) {
        int totalFitness = fitnessPoblacion(poblacion);

        for (var ind : poblacion) {
            double probabilidadInd = (double) ind.getFitness() /
                (double) totalFitness;

            ind.setProbabilidad(probabilidadInd);
        }
    }

    public int[] seleccionPorRuleta(ArrayList<Cromosoma> poblacion) {
        setpoblacionProbabilidad(poblacion);

        double total = 0.0;
        HashMap<Integer, ArrayList<Double>> slices = new HashMap<>();

        for (int i = 0; i < tamPoblacion; i++) {
            ArrayList<Double> values = new ArrayList<>();
            values.add(total);
            var probabilidadInd = poblacion.get(i).getProbabilidad();
            values.add(total + probabilidadInd);
            slices.put(i, values);

            total += probabilidadInd;
        }

        int[] result = new int[tamPoblacion];

        for (int i = 0; i < tamPoblacion; i++) {
            double spin = Math.random();

            for (var key: slices.keySet()) {
                var slice = slices.get(key);

                if (slice.get(0) < spin && spin <= slice.get(1)) {
                    result[i] = key;
                    break;
                }
            }
        }

        return result;
    }

    public ArrayList<Cromosoma>
    onePointCrossover(Cromosoma parentA, Cromosoma parentB) {
        var genesPadreA = parentA.getGenes();
        var genesPadreB = parentB.getGenes();
        ArrayList<Cromosoma> hijo = new ArrayList<>();
        Random random = new Random();
        int xoverPoint = random.nextInt(genesPadreA.length());

        var primerHijo = genesPadreA.substring(0, xoverPoint) + 
            genesPadreB.substring(xoverPoint, genesPadreA.length());

        var segundoHijo = genesPadreB.substring(0, xoverPoint) + 
            genesPadreA.substring(xoverPoint, genesPadreA.length());

        hijo.add(new Cromosoma(primerHijo));
        hijo.add(new Cromosoma(segundoHijo));

        return hijo;
    }

    public void mutarIndividuo(ArrayList<Cromosoma> hijo, int probMutacion) {
        Random random = new Random();

        for (var child : hijo) {
            int idx = random.nextInt(probMutacion);
            StringBuilder sb = new StringBuilder(child.getGenes());

            if (sb.charAt(idx) == '1') {
                sb.setCharAt(idx, '0');
            } else {
                sb.setCharAt(idx, '1');
            }

            child.setGenes(sb.toString());
        }
    }

    public ArrayList<Cromosoma> reproducehijo(int[] elegido, ArrayList<Cromosoma> poblacion) {
        ArrayList<Cromosoma> hijo = new ArrayList<>();

        for (int i = 0; i < (elegido.length / 2 - 1); i++) {
            hijo.addAll(onePointCrossover(poblacion.get(elegido[i]),
                poblacion.get(elegido[i + 1])));
        }

        return hijo;
    }

    public Cromosoma getmejor(ArrayList<Cromosoma> poblacion) {
        int mejorFitness = 0;
        Cromosoma mejorind = new Cromosoma();

        for (var ind : poblacion) {
            if (ind.getFitness() > mejorFitness) {
                mejorFitness = ind.getFitness();
                mejorind = ind;
            }
        }
        System.out.println(mejorind);
        return mejorind;
    } 

    public void runGA() {
        int mejorFitnessGeneral = 0;
        var poblacionGlobal = generarPoblacion();

        for (int i = 0; i < numeroGeneraciones; i++) {
            int mejorFitnessActual = fitnessPoblacion(poblacionGlobal);

            if (mejorFitnessActual > mejorFitnessGeneral) {
                mejorFitnessGeneral = mejorFitnessActual;
            }

            var elegido = seleccionPorRuleta(poblacionGlobal);
            var hijo = reproducehijo(elegido, poblacionGlobal);
            mutarIndividuo(hijo, 8);
            poblacionGlobal.addAll(hijo);
            //
            //var mejor = getmejor(poblacionGlobal);
            System.out.println(mejorFitnessActual+"\t"+hijo+"\n");
        }

        var mejor = getmejor(poblacionGlobal);

        System.out.println("Mejor fitness: " + mejorFitnessGeneral);
        System.out.println("Mejor actual: " + mejor.getGenes());
        System.out.println("Mejor fitness actual: " + mejor.getFitness());
    }

}