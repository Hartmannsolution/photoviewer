package businessfacades;

import datafacades.IDataFacade;
import datafacades.PhotoFacade;
import dtos.PhotoDTO;
import dtos.TagDTO;
import entities.Photo;
import entities.Tag;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class PhotoDTOFacade implements IDataFacade<PhotoDTO>{
    private static IDataFacade<PhotoDTO> instance;
    private static IDataFacade<Photo> photoFacade;

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    //Private Constructor to ensure Singleton

    private PhotoDTOFacade() {}

    public static IDataFacade<PhotoDTO> getFacade() {
        if (instance == null) {
            photoFacade = PhotoFacade.getFacade(EMF);
            instance = new PhotoDTOFacade();
        }
        return instance;
    }

    private Photo getEntity(PhotoDTO dto){
        EntityManager em = EMF.createEntityManager();
        Photo photo = em.find(Photo.class, dto.getName());
        System.out.println("GET ENTITY: "+photo); //Tags are empty
        if (photo == null){
            photo = new Photo(dto.getLocation(),dto.getName(), dto.getDescription());
            final Photo p = photo; //p must be final to work inside lambda
            dto.getTags().forEach(tag->p.addTag(getEntity(tag))); //Changed photo.tags to Set to avoid duplicates
        }
        else{
            //Copy all properties from DTO to Entity (that we got from DB)
            if(dto.getLocation()!=null) photo.setLocation(dto.getLocation());
            if(dto.getViewNo()!=0) photo.setViewNo(dto.getViewNo());
            if(dto.getDescription()!=null) photo.setPhotoTxt(dto.getDescription());
            final Photo p = photo; //p must be final to work inside lambda
            //add tags from dto
            photo.getTags().clear();
            dto.getTags().forEach(tag->p.addTag(getEntity(tag))); //Changed photo.tags to Set to avoid duplicates
        }
        return photo;
    }

    private Tag getEntity(TagDTO dto){
        EntityManager em = EMF.createEntityManager();
        Tag tag = em.find(Tag.class, dto.getName());
        if (tag == null)
            tag = new Tag(dto.getName());
        return tag;
    }

    @Override
    public PhotoDTO create(PhotoDTO photoDTO) {
        Photo p = getEntity(photoDTO);
        p = photoFacade.create(p);
        return new PhotoDTO(p);
    }

    @Override
    public PhotoDTO getById(String id) throws EntityNotFoundException {
        return new PhotoDTO(photoFacade.getById(id));
    }

    @Override
    public List<PhotoDTO> getAll() {
        return PhotoDTO.toList(photoFacade.getAll());
    }

    @Override
    public PhotoDTO update(PhotoDTO photoDTO) throws EntityNotFoundException {
        Photo p = photoFacade.update(getEntity(photoDTO));
        return new PhotoDTO(p);
    }

    @Override
    public PhotoDTO delete(String id) throws EntityNotFoundException {
        return new PhotoDTO(photoFacade.delete(id));
    }

    @Override
    public List<PhotoDTO> findByProperty(String property, String propValue) {
        return PhotoDTO.toList(photoFacade.findByProperty(property, propValue));
    }
}
