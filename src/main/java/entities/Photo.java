package entities;

import dtos.PhotoDTO;

import javax.persistence.*;
import java.time.*;
import java.util.*;

@Table(name = "photo")
@Entity
@NamedQuery(name = "Photo.deleteAllRows", query = "DELETE from Photo")
public class Photo {
    @Id
    @Column(name = "FilNavn", length = 35)
    private String fileName;

    @Column(name = "Location", nullable = false, length = 35)
    private String location;


    @Column(name = "VisNr", updatable = false)
    private int viewNo;

    @Lob
    @Column(name = "FotoTxt")
    private String photoTxt;

    @Column(name = "FotoTxtAdd")
    private String photoTxtAdd;


//    @CreationTimestamp //For Hibernate only
//    @Temporal(TemporalType.TIMESTAMP) //NOt necessary to annotate with @Temporal when using java 8 time.LocalDateTime (translates to TIMESTAMP on mysql
    @Column(name = "Oprettet", updatable = false)
//    @CreationTimestamp //this adds the default timestamp on save BUT only for hibernate
//    @UpdateTimestamp //this update the timestamp everytime the entity is changed
    private java.time.LocalDateTime created;

//    @UpdateTimestamp //For Hibernate
//    @Temporal(TemporalType.TIMESTAMP) //NOt necessary when using java.time.LoalDateTime
    @Column(name = "Rettet")//, updatable = false) //We have to make it updatable since it is not handles on database level. PROBLEM: DTO does not contain dates so when photo entities return from the user, their dates are null.
    private LocalDateTime editted;


    @ManyToMany(fetch = FetchType.EAGER) //with FetchType.Lazy we only get the tags when we specifically ask for them (and ONLY if connected to an EM)
    @JoinTable( // This is now the owner side of the relationsship
            name = "photo_tag",
            joinColumns = @JoinColumn(name = "photo_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Photo() {
    }

    public Photo(String location, String fileName, String photoTxt) {
        this.location = location;
        this.fileName = fileName;
        this.photoTxt = photoTxt;
    }

    // Database is not set to handle dates, so I do it with JPA lifecycle methods. For more see: https://www.baeldung.com/jpa-entity-lifecycle-events
    @PreUpdate
    public void onUpdate() {
        editted = LocalDateTime.now(ZoneId.of("GMT+02:00"));
    }

    @PrePersist
    public void onPersist(){
        editted = LocalDateTime.now(ZoneId.of("GMT+02:00"));
        created = LocalDateTime.now(ZoneId.of("GMT+02:00"));
    }

    public LocalDateTime getEditted() {return editted;}

    public LocalDateTime getCreated() {
        return created;
    }

    public String getPhotoTxtAdd() {
        return photoTxtAdd;
    }

    public void setPhotoTxtAdd(String photoTxtAdd) {
        this.photoTxtAdd = photoTxtAdd;
    }

    public String getPhotoTxt() {
        return photoTxt;
    }

    public void setPhotoTxt(String photoTxt) {
        this.photoTxt = photoTxt;
    }

    public int getViewNo() {
        return viewNo;
    }

    public void setViewNo(int viewNo) {
        this.viewNo = viewNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        if(!tag.getPhotos().contains(this))
            tag.getPhotos().add(this);
    }
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        if(!tag.getPhotos().contains(this))
            tag.getPhotos().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(location, photo.location) && Objects.equals(fileName, photo.fileName) && Objects.equals(photoTxt, photo.photoTxt);
    }


    @Override
    public int hashCode() {
        return Objects.hash(location, fileName, photoTxt);
    }

    public static void main(String[] args) {
        Photo p = new Photo();
                p.onUpdate();
        System.out.println(p.editted);

    }

    @Override
    public String toString() {
        return "Photo{" +
                "fileName='" + fileName + '\'' +
                ", location='" + location + '\'' +
                ", viewNo=" + viewNo +
                ", photoTxt='" + photoTxt + '\'' +
                ", photoTxtAdd='" + photoTxtAdd + '\'' +
                ", created=" + created +
                ", editted=" + editted +
                ", tags=" + tags +
                '}';
    }
}
