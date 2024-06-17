package model;

import entity.Animal;

import java.util.List;

public interface AnimalModel{

    boolean saveAnimal(Animal animal);

    Animal searchAnimal(String term);

    List<Animal> getAnimals();

    boolean updateAnimal(Animal animal);
}
