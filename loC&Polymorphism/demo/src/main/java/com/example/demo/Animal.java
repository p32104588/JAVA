package com.example.demo;

class Animal {
    void move() {
        System.out.println("move...move...");
    }
}
class Dog extends Animal {
    void move() {
        System.out.println("跑...跑...");
    }
}
class Bird extends Animal {
    void move() {
        System.out.println("飛...飛...");
    }
}
class Fish extends Animal {
    void move() {
        System.out.println("游...游...");
    }
}