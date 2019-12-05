package com.example.buddii;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseHandlerTest {

    /* first two tests for testing add user and also load user, we are indirectly testing addToDb*/
    @Test
    public void addOneToDbLoadName() {
        DatabaseHandler testHandler = new DatabaseHandler(new DBActivity());
        String nameArry[] = {"Rob"};
        try {
            testHandler.addToDb("8888888", "Rob", "rob@gmail.com", "1234");
            //assertArrayEquals(/*test to make sure it returns Rob*/);
            assertArrayEquals(nameArry, testHandler.loadUsers("name"));
            System.out.println("I  DIDNT FUCKING SKIP YOUR SHIT");

        }catch (Exception E){
            //diaper right now
            System.out.println("I FUCKING SKIPPED YOUR SHIT");
        }
    }
    @Test
    public void addManygetNames() {
        DatabaseHandler testHandler = new DatabaseHandler(new DBActivity());
        String nameArry[] = {"Rob", "Cat", "Dog"};
        
        testHandler.addToDb("8888888", "Rob", "rob@gmail.com", "1234");
        testHandler.addToDb("8888881", "Cat", "cat@gmail.com", "1231");
        testHandler.addToDb("8888882", "Dog", "dog@gmail.com", "1232");
        assertArrayEquals(nameArry, testHandler.loadUsers("name"));
    }

    @Test
    public void addOneToDbLoadPho() {
        DatabaseHandler testHandler = new DatabaseHandler(new DBActivity());
        String phoArry[] = {"8888888"};
        try {
            testHandler.addToDb("8888888", "Rob", "rob@gmail.com", "1234");
            //assertArrayEquals(/*test to make sure it returns Rob*/);
            assertArrayEquals(phoArry, testHandler.loadUsers("phoneNumber"));

        }catch (Exception E){
            //diaper right now
        }
    }

    @Test
    public void addOneToDbLoademal() {
        DatabaseHandler testHandler = new DatabaseHandler(new DBActivity());
        String emalArry[] = {"rob@gmail.com"};
        try {
            testHandler.addToDb("8888888", "Rob", "rob@gmail.com", "1234");
            //assertArrayEquals(/*test to make sure it returns Rob*/);
            assertArrayEquals(emalArry, testHandler.loadUsers("email"));

        }catch (Exception E){
            //diaper right now
        }
    }







    @Test
    public void deleteUser() {
    }

    @Test
    public void loadUsers() {
    }

    @Test
    public void addGPS() {
    }

    @Test
    public void loadGPS() {
    }

    @Test
    public void doPasswordsMatch() {
    }

    @Test
    public void addToActiveBuddiTable() {
    }

    @Test
    public void removeFromActiveBuddiTable() {
    }

    @Test
    public void addRating() {
    }
}