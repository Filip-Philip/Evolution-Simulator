package project;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal animal1, Animal animal2) {
        int energyOfAnimal1 = animal1.getEnergy();
        int energyOfAnimal2 = animal2.getEnergy();
        if (energyOfAnimal1 > energyOfAnimal2) {
            return 1;
        } else if (energyOfAnimal1 < energyOfAnimal2) {
            return -1;
        } else {
            int orientationOfAnimal1 = animal1.getOrientation().ordinal();
            int orientationOfAnimal2 = animal2.getOrientation().ordinal();
            if (orientationOfAnimal1 > orientationOfAnimal2) return 1;
            else if (orientationOfAnimal2 > orientationOfAnimal1) return -1;
            else {
                int genotypeSumOfAnimal1 = animal1.getGenotype().getGenes().stream().mapToInt(a -> (int) a).sum();
                int genotypeSumOfAnimal2 = animal2.getGenotype().getGenes().stream().mapToInt(a -> (int) a).sum();
                if (genotypeSumOfAnimal1 > genotypeSumOfAnimal2) return 1;
                else if (genotypeSumOfAnimal2 > genotypeSumOfAnimal1) return -1;
                else {
                    if (animal1.equals(animal2)) return 0;
                    return 1;
                }
            }
        }
    }
}
