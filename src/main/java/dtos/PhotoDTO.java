package dtos;

import entities.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhotoDTO {
    private String location;
    private String name;
    private int viewno;
    private String title;
    private String description;
    private List<TagDTO> tags = new ArrayList();

    public PhotoDTO(Photo photo) {
        this.location = photo.getLocation();
        this.name = photo.getFileName();
        this.viewno = photo.getViewNo();
        this.title = photo.getTitle();
        this.description = photo.getPhotoTxt();
        photo.getTags().forEach(tag->this.tags.add(new TagDTO(tag)));
    }

    public static List<PhotoDTO> toList(List<Photo> photos) {
        return photos.stream().map(PhotoDTO::new).collect(Collectors.toList());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViewNo() {
        return viewno;
    }

    public void setViewNo(int viewNo) {
        this.viewno = viewNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoDTO photoDTO = (PhotoDTO) o;
        return Objects.equals(location, photoDTO.location) && Objects.equals(name, photoDTO.name) && Objects.equals(description, photoDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, name, description);
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", viewNo=" + viewno +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                '}';
    }
}
