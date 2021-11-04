package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "tag")
@Entity
@NamedQuery(name = "Tag.deleteAllRows", query = "DELETE from Tag")
public class Tag {
    @Id
    @Column(name = "tagname", length = 35)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "tags") //, cascade = CascadeType.PERSIST) //, fetch = FetchType.EAGER) //Target side of relationsship (inverse side)
    private List<Photo> photos = new ArrayList();

    public Tag() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tag(String name) {
        this.name = name;
    }
    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) && Objects.equals(description, tag.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }
}