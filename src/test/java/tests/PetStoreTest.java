package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;
import animals.petstore.pet.types.Snake;
import animals.petstore.store.DuplicatePetStoreRecordException;
import animals.petstore.store.PetNotFoundSaleException;
import animals.petstore.store.PetStore;
import number.Numbers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PetStoreTest
{
    private static PetStore petStore;

    @BeforeEach
    public void loadThePetStoreInventory()
    {
        petStore = new PetStore();
        petStore.init();
    }

    @Test
    @DisplayName("Get Number of Legs Test for Dog")
    public void getNumberOfLegsTest_Dog() {
        Dog dog = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.GERMAN_SHEPARD,
                new BigDecimal("500.00"), 4);
        assertEquals(4, dog.getNumberOfLegs(), "Expected number of legs is 4 for this dog");
    }

    @Test
    @DisplayName("Poodle Record Not Found Exception Test")
    public void petNotFoundTest() throws PetNotFoundSaleException, DuplicatePetStoreRecordException {
        //soldPetItem method in petstore
        //throws PetNotFoundSaleException if the pet is not in any store aka petStoreid == 0
        petStore.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.FEMALE, Breed.POODLE,
                new BigDecimal("220.00"), 0));
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.FEMALE, Breed.POODLE,
                new BigDecimal("220.00"), 0);

        String expectedMessage = "The Pet is not part of the pet store!!";
        Exception exception = assertThrows(PetNotFoundSaleException.class, () ->{
            petStore.soldPetItem(poodle);});
        assertEquals(expectedMessage, exception.getMessage(), "RecordNotFoundExceptionTest was NOT encountered!");
    } // end of petNotFoundTest

    @Test
    @DisplayName("Get Animal Type")
    public void getAnimalTypeTest_Cat() {
        Cat cat = new Cat(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.SIAMESE,
                new BigDecimal("500.00"), 4);
        assertEquals(AnimalType.DOMESTIC, cat.getAnimalType(), "Expected animal type is Domestic for this cat");
    }

    @Test
    @DisplayName("Get Animal Type")
    public void getAnimalTypeTest_Dog() {
        Dog dog = new Dog(AnimalType.WILD, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("500.00"), 4);
        assertEquals(AnimalType.WILD, dog.getAnimalType(), "Expected animal type is WILD for this dog");
    }

    @Test
    @DisplayName("Get Animal Type")
    public void getAnimalTypeTest_Snake() {
        Snake snake = new Snake(AnimalType.WILD, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("500.00"), 4);
        assertEquals(AnimalType.WILD, snake.getAnimalType(), "Expected animal type is WILD for this snake");
    }

    @Test
    @DisplayName("Duplicate Exception Cat")
    public void testExceptionWhenDuplicateCatsExist(){
        Cat cat1 = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);
        Cat cat2 = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SIAMESE,
                new BigDecimal("100.00"),2);

        petStore.addPetInventoryItem(cat1);
        petStore.addPetInventoryItem(cat2);

        Cat soldCat = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SIAMESE,
                new BigDecimal("100.00"),2);

        DuplicatePetStoreRecordException exception = assertThrows(
                DuplicatePetStoreRecordException.class,
                () -> petStore.soldPetItem(soldCat)
        );

        // Verify exception message contains the duplicate ID
        assertTrue(exception.getMessage().contains(String.valueOf(2)));
    }

    @Test
    @DisplayName("Inventory Count Test")
    public void validateInventory()
    {
        assertEquals(8, petStore.getPetsForSale().size(),"Inventory counts are off!");
    }

    @Test
    @DisplayName("Print Inventory Test")
    public void printInventoryTest()
    {
        petStore.printInventory();
    }

    @Test
    @DisplayName("Sale of Poodle Remove Item Test")
    public void poodleSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        // Validation
        petStore.soldPetItem(poodle);
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Poodle Duplicate Record Exception Test")
    public void poodleDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1));
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        // Validation
        String expectedMessage = "Duplicate Dog record store id [1]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () ->{
            petStore.soldPetItem(poodle);});
        assertEquals(expectedMessage, exception.getMessage(), "DuplicateRecordExceptionTest was NOT encountered!");

    }

    @Test
    @DisplayName("Sale of Ball Python Remove Item Test")
    public void ballPythonSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;
        Snake ballPython = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("650.00"), 1);

        // Validation
        petStore.soldPetItem(ballPython);
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Ball Python Duplicate Record Exception Test")
    public void ballPythonDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("650.00"), 1));
        Snake ballPython = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("650.00"), 1);

        // Validation
        String expectedMessage = "Duplicate Snake record store id [1]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () ->{
            petStore.soldPetItem(ballPython);});
        assertEquals(expectedMessage, exception.getMessage(), "DuplicateRecordExceptionTest was NOT encountered!");

    }

    @Test
    @DisplayName("Sale of Sphynx Remove Item Test")
    public void sphynxSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        // Validation
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
        assertEquals(sphynx.getPetStoreId(), removedItem.getPetStoreId(), "The cat items are identical");
    }

    /**
     * Limitations to test factory as it does not instantiate before all
     * @return list of {@link DynamicNode} that contains the test results
     * @throws DuplicatePetStoreRecordException if duplicate pet record is found
     * @throws PetNotFoundSaleException if pet is not found
     */
    @TestFactory
    @DisplayName("Sale of Sphynx Remove Item Test2")
    public Stream<DynamicNode> sphynxSoldTest2() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        // Validation
        List<DynamicNode> nodes = new ArrayList<>();
        List<DynamicTest> dynamicTests = Arrays.asList(
                dynamicTest("Inventory Check Size Test ", () -> assertEquals(inventorySize,
                        petStore.getPetsForSale().size())),
                dynamicTest("The cat objects match ", () -> assertEquals(sphynx.toString(),
                        removedItem.toString()))
                );
        nodes.add(dynamicContainer("Cat Item 2 Test", dynamicTests));//dynamicNode("", dynamicContainers);

        return nodes.stream();
    }

    /**
     * Example of parameterized test
     * @param number to be tested
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, -10, 128, Integer.MIN_VALUE}) // six numbers
    void isNumberEven(int number)
    {
        assertTrue(Numbers.isEven(number));
    }

}
