package de.dkutzer.buggy.developer.entity;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias("Developer")
@Document(collection = "developer")
public class DeveloperEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeveloperDto toDto(){
        DeveloperDto developerDto = new DeveloperDto();
        developerDto.setName(this.name);
        return developerDto;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeveloperEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
