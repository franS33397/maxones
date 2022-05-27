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


class Cromosoma {
    String genes;
    int fitness;
    double Probabilidad;

    public Cromosoma() {
        genes = "";
        fitness = 0;
        Probabilidad = 0.0;
    }

    public Cromosoma(String g) {
        genes = g;
        fitness = 0;
        Probabilidad = 0.0;
    }

    public String getGenes() {
        return genes;
    }

    public void setGenes(String genes) {
        this.genes = genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double getProbabilidad() {
        return Probabilidad;
    }

    public void setProbabilidad(double Probabilidad) {
        this.Probabilidad = Probabilidad;
    }

    public String toString() {
        return "Genes: " + genes + " Fitness: " + fitness + " Probabilidad: " +
            Probabilidad;
    }
}