package businessfacades;

import datafacades.IDataFacade;
import datafacades.PhotoFacade;
import dtos.PhotoDTO;
import dtos.TagDTO;
import entities.Photo;
import entities.Tag;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashSet;
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
    //For testing purposes
    public static IDataFacade<PhotoDTO> getFacade(EntityManagerFactory emf) {
        photoFacade = PhotoFacade.getFacade(emf);
        instance = new PhotoDTOFacade();
        return instance;
    }

    private Photo getEntity(PhotoDTO dto) {
        EntityManager em = EMF.createEntityManager();
        Photo photo = em.find(Photo.class, dto.getName());
//        if(photo==null)
//            throw new EntityNotFoundException("Could not find the photo with name: "+dto.getName());
        if (photo == null){
            photo = new Photo(dto.getName(), dto.getLocation(), dto.getDescription(), dto.getTitle());
//            final Photo p = photo; //p must be final to work inside lambda
//            dto.getTags().forEach(tag->p.addTag(getEntity(tag))); //Changed photo.tags to Set to avoid duplicates
        }
        else{
            //Copy all properties from DTO to Entity (that we got from DB)
            if(dto.getLocation()!=null) photo.setLocation(dto.getLocation());
            if(dto.getViewNo()!=0) photo.setViewNo(dto.getViewNo());
            if(dto.getDescription()!=null) photo.setPhotoTxt(dto.getDescription());
            if(dto.getTitle()!=null) photo.setTitle(dto.getDescription());

        }
        final Photo p = photo; //p must be final to work inside lambda
//        if(photo.getTags()==null)
//            photo.setTags(new HashSet<>());
        photo.getTags().clear();
        if(dto.getTags()==null)
            dto.setTags(new ArrayList<>());
        dto.getTags().forEach(tag->p.addTag(getEntity(tag))); //Changed photo.tags to Set to avoid duplicates
//        System.out.println("PHOTO DTO FACADEN getEntity(photo)");
//        dto.getTags().forEach(System.out::println);
//        System.out.println("END");
        return p;
    }

    private Tag getEntity(TagDTO dto)  {
        EntityManager em = EMF.createEntityManager();
        Tag tag = em.find(Tag.class, dto.getName());
//        if(tag==null)
//            throw new EntityNotFoundException("Cannot find Tag with name: "+dto.getName());
        if (tag == null)
            tag = new Tag(dto.getName());
        return tag;
    }

    @Override
    public PhotoDTO create(PhotoDTO photoDTO) throws API_Exception {
        Photo p = getEntity(photoDTO);
        p = photoFacade.create(p);
        return new PhotoDTO(p);
    }

    @Override
    public PhotoDTO update(PhotoDTO photoDTO) throws EntityNotFoundException {
        Photo p = photoFacade.update(getEntity(photoDTO));
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
    public PhotoDTO delete(String id) throws EntityNotFoundException {
        return new PhotoDTO(photoFacade.delete(id));
    }

    @Override
    public List<PhotoDTO> findByProperty(String property, String propValue) {
        return PhotoDTO.toList(photoFacade.findByProperty(property, propValue));
    }
}
