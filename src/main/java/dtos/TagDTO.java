package dtos;

import entities.Photo;
import entities.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagDTO {
    String name;
    String description;
    List<String> photos = new ArrayList();

    public TagDTO(Tag tag) {
        this.name = tag.getName();
        this.description = tag.getDescription();
        tag.getPhotos().forEach(photo->this.photos.add(photo.getFileName()));
    }

    public static List<TagDTO> toList(List<Tag> tags) {
        return tags.stream().map(TagDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return name.equals(tagDTO.name) && description.equals(tagDTO.description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                '}';
    }
}
