package project;

import java.util.*;

public class Genotype {
    private final int GENOTYPE_LENGTH = 32;
    private final int NUMBER_OF_UNIQUE_GENES = 8;
    private final ArrayList<Integer> genes;

    public Genotype(){
        Random random = new Random();
        ArrayList<Integer> randomGenes = new ArrayList<>();
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            int gene = random.nextInt(NUMBER_OF_UNIQUE_GENES);
            randomGenes.add(gene);
        }
        Collections.sort(randomGenes);
        this.genes = randomGenes;
    }

    public Genotype(Animal parent1, Animal parent2){
        int energyOfParent1 = parent1.getEnergy();
        int energyOfParent2 = parent2.getEnergy();
        int numberOfGenesFromParent1 = GENOTYPE_LENGTH * energyOfParent1 / (energyOfParent1 + energyOfParent2);
        int numberOfGenesFromParent2 = GENOTYPE_LENGTH - numberOfGenesFromParent1;
        ArrayList<Integer> genesOfParent1 = parent1.getGenotype().getGenes();
        ArrayList<Integer> genesOfParent2 = parent2.getGenotype().getGenes();
        Random random = new Random();
        int side = random.nextInt(2);
        ArrayList<Integer> genesOfChild = new ArrayList<>();
        int currentGene;
        if ( (side == 0 && energyOfParent1 >= energyOfParent2) || (side == 1 && energyOfParent1 < energyOfParent2) ) {
            for (int i = 0; i < numberOfGenesFromParent1; i++) {
                currentGene = genesOfParent1.get(i);
                genesOfChild.add(currentGene);
            }
            for (int i = numberOfGenesFromParent1; i < GENOTYPE_LENGTH; i++) {
                currentGene = genesOfParent2.get(i);
                genesOfChild.add(currentGene);
            }
        }
        else if ( (side == 1 && energyOfParent1 >= energyOfParent2) || (side == 0 && energyOfParent1 < energyOfParent2) ) {
            for (int i = 0; i < numberOfGenesFromParent2; i++) {
                currentGene = genesOfParent2.get(i);
                genesOfChild.add(currentGene);
            }
            for (int i = numberOfGenesFromParent2; i < GENOTYPE_LENGTH; i++) {
                currentGene = genesOfParent1.get(i);
                genesOfChild.add(currentGene);
            }
        }

        this.genes = genesOfChild;
    }

    public ArrayList getGenes(){ return genes; }

    public int getNumberOfUniqueGenes(){ return NUMBER_OF_UNIQUE_GENES; }

    public int getGENOTYPE_LENGTH() {
        return GENOTYPE_LENGTH;
    }
}
