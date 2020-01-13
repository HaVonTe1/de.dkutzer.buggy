package de.dkutzer.buggy.developer.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeveloperDto {

    @NotNull
    @NotEmpty
    private String name;

//    private int age;

//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeveloperEntity toEntity(){
        final DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setName(this.name);
        return developerEntity;
    }

//    @Override
//    public String toString() {
//        return "DeveloperDto{" + "name='" + name + '\'' + ", age=" + age + '}';
//    }
}
