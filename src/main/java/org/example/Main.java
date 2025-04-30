package org.example;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;
import animals.petstore.pet.types.Snake;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dog dog = new Dog(AnimalType.DOMESTIC, Skin.HAIR,Gender.MALE, Breed.POODLE);
        System.out.println(dog.toString());
        System.out.println("\n");
        dog = new Dog(AnimalType.WILD, Skin.FUR, Gender.MALE, Breed.GERMAN_SHEPARD);
        System.out.println(dog.toString());

        Cat cat = new Cat(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.BURMESE);
        System.out.println(cat.toString());
        System.out.println("\n");

        Snake snake = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON);
        System.out.println(snake.toString());
    }
}