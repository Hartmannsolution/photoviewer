package businessfacades;

import datafacades.IDataFacade;
import datafacades.TagFacade;
import dtos.TagDTO;
import dtos.TagDTO;
import entities.Photo;
import entities.Tag;
import entities.Tag;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TagDTOFacade implements IDataFacade<TagDTO>{
    private static IDataFacade<TagDTO> instance;
    private static IDataFacade<Tag> tagFacade;

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    //Private Constructor to ensure Singleton

    private TagDTOFacade() {}

    public static IDataFacade<TagDTO> getFacade() {
        if (instance == null) {
            tagFacade = TagFacade.getFacade(EMF);
            instance = new TagDTOFacade();
        }
        return instance;
    }

//    private Tag getEntity(TagDTO dto){
//        EntityManager em = EMF.createEntityManager();
//        Tag tag = em.find(Tag.class, dto.getName());
//
//        //Copy all properties from DTO to Entity (that we got from DB)
//        if(dto.getDescription()!=null) tag.setDescription(dto.getDescription());
//        return tag;
//    }

    private Tag getEntity(TagDTO dto){
        EntityManager em = EMF.createEntityManager();
        Tag tag = em.find(Tag.class, dto.getName());
        if (tag == null)
            tag = new Tag(dto.getName(), dto.getDescription());
        else{
            tag.setDescription(dto.getDescription());
            final Tag t = tag;
            if(dto.getPhotos()!=null)
                dto.getPhotos().forEach(photo->t.addPhoto(em.find(Photo.class, photo)));
            tag = t;
        }

        return tag;
    }

    @Override
    public TagDTO create(TagDTO tagDTO) throws API_Exception {
        Tag p = getEntity(tagDTO);
        p = tagFacade.create(p);
        return new TagDTO(p);
    }

    @Override
    public TagDTO getById(String id) throws EntityNotFoundException {
        return new TagDTO(tagFacade.getById(id));
    }

    @Override
    public List<TagDTO> getAll() {
        return TagDTO.toList(tagFacade.getAll());
    }
    
    @Override
    public TagDTO update(TagDTO tagDTO) throws EntityNotFoundException {
        Tag p = tagFacade.update(getEntity(tagDTO));
        return new TagDTO(p);
    }

    @Override
    public TagDTO delete(String id) throws EntityNotFoundException {
        return new TagDTO(tagFacade.delete(id));
    }

    @Override
    public List<TagDTO> findByProperty(String property, String propValue) {
        return TagDTO.toList(tagFacade.findByProperty(property, propValue));
    }
}
