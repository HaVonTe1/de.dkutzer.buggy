package de.dkutzer.buggy.developer.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeveloperDto {

    @NotNull
    @NotEmpty
    private String name;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeveloperInDto{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
