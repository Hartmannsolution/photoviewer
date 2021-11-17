package entities;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.joining;

@Table(name = "tag")
@Entity
@NamedQuery(name = "Tag.deleteAllRows", query = "DELETE from Tag")
public class Tag {
    @Id
    @Column(name = "tagname", length = 35)
    private String name;
    private String description;

//    If the "mappedBy" option is not found in any of the related entities JPA will define BOTH entities as the relationship owners: https://enos.itcollege.ee/~jpoial/java/naited/JPA_Mini_Book.pdf
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
        this(name, null);
    }
    public Tag(String name, String description) {
        this.setName(name);
        this.description = description;
    }

    public static void main(String[] args) {
        System.out.println(new Tag("ho hi ha hIhi"));
    }

    private String capitalizeFirst(String s){
        s = s.toLowerCase();
        if (s.length() == 0 || s == null)
            return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String newName = Arrays
                .stream(name.split(" "))
                .map(this::capitalizeFirst)
//                .reduce("", (acc,element)->acc.concat(" "+element));
                .collect(joining(" "));
        this.name = newName;
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
                ", description='" + description + '\'' +
                ", photos=" + photos +
                '}';
    }
}