package datafacades;

import entities.Photo;
import entities.Tag;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;
import java.io.IOException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * created by THA
 * Purpose of this facade example is to show a facade used as a DB facade (only operating on entity classes - no DTOs
 * And to show case some different scenarios
 */
public class PhotoFacade implements IDataFacade<Photo>{

    private static PhotoFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PhotoFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static IDataFacade<Photo> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhotoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public Photo create(Photo p) throws API_Exception {
        EntityManager em = getEntityManager();
        if(em.find(Photo.class,p.getFileName())!=null)
            throw new API_Exception("Photo allready exist with this name: "+p.getFileName(),403);
        try {
            em.getTransaction().begin();
            p.getTags().forEach(tag->{
                Tag found = em.find(Tag.class,tag.getName());
                if(found != null)
                    tag = found;
                else {
                    em.persist(tag);
                }
            });
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Photo getById(String id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Photo p = em.find(Photo.class, id);
        if (p == null)
            throw new EntityNotFoundException("The Photo entity with ID: "+id+" Was not found");
        return p;
    }

    @Override
    public Photo update(Photo photo) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Photo found = em.find(Photo.class,photo.getFileName());
        if(photo.getFileName() == null || found == null)
            throw new EntityNotFoundException("No Photo by that name. Not updated");
        em.getTransaction().begin();
        if(photo.getPhotoTxt()!=null) found.setPhotoTxt(photo.getPhotoTxt());
        if(photo.getLocation()!=null) found.setLocation(photo.getLocation());
        if(photo.getViewNo()!=0) found.setViewNo(photo.getViewNo());
        //If photo has any tags, then those are the ones that count.
        if(photo.getTags()!=null && photo.getTags().size()!=0) {
//          Make copy of list before iterating and removing to avoid error
            new ArrayList<Tag>(found.getTags()).forEach(tag ->{
                found.removeTag(tag);
            });
            photo.getTags().forEach(tag->{
                Tag t = em.find(Tag.class, tag.getName());
                if(t == null){
                    em.persist(tag);
                }
                found.addTag(tag);
            });
        }
        Photo p = em.merge(found);
        System.out.println(p);
        em.getTransaction().commit();
        return p;
    }

    @Override
    public Photo delete(String id) throws EntityNotFoundException{
        EntityManager em = getEntityManager();
        Photo p = em.find(Photo.class, id);
        if (p == null)
            throw new EntityNotFoundException("Could not remove Photo with id: "+id);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        return p;
    }

    public static void main(String[] args) throws EntityNotFoundException {
        emf = EMF_Creator.createEntityManagerFactory();
        IDataFacade fe = getFacade(emf);
//        fe.findByProperty("location", "Joergensen/").forEach(photo->System.out.println(photo));
        Photo newP = new Photo("Joergensen_0001_TN.jpg","","");
        Photo newP2 = new Photo("Helge333333","","");
//        try {
//            fe.create(newP);
//        } catch (API_Exception e) {
//            e.printStackTrace();
//        }
//        newP.addTag(new Tag("Hello Test"));
        newP.addTag(new Tag("Mors"));
        newP2.addTag((new Tag("Børge Artmann")));
//        newP.addTag(new Tag("thOrkild22"));
//        newP.addTag(new Tag("hello test"));
        System.out.println(fe.update(newP));

    }

    @Override
    public List<Photo> getAll(){
        EntityManager em = getEntityManager();
        TypedQuery<Photo> query = em.createQuery("SELECT p FROM Photo p", Photo.class);
        List<Photo> Photos = query.getResultList();
        return Photos;
    }


    public List<Photo> getByTagName(String tagName){
        EntityManager em = getEntityManager();
        TypedQuery<Photo> query = em.createQuery("SELECT p FROM Photo p JOIN p.tags t WHERE t.name = :tagName", Photo.class).setParameter("tagName",tagName);
        List<Photo> Photos = query.getResultList();
        return Photos;
    }


    @Override
    public List<Photo> findByProperty(String property, String propValue) {
        String baseUrl = null;
        try {
            baseUrl = utils.Utility.readFileProperty("BASEURL");
            System.out.println("LOKALITET:"+baseUrl);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        EntityManager em = getEntityManager();
        TypedQuery<Photo> q = em.createQuery("SELECT p FROM Photo p WHERE p."+property+" =:val", Photo.class);
        q.setParameter("val", baseUrl+propValue+"/");
        return q.getResultList();
    }
    
}
