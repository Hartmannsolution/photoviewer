package datafacades;

import entities.Tag;
import entities.Tag;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;

/**
 *
 * created by THA
 * Purpose of this facade example is to show a facade used as a DB facade (only operating on entity classes - no DTOs
 * And to show case some different scenarios
 */
public class TagFacade implements IDataFacade<Tag>{

    private static TagFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private TagFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static IDataFacade<Tag> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TagFacade();
        }
        return instance;
    }
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public Tag create(Tag p){
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Tag getById(String id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Tag t = em.find(Tag.class, id);
        if (t == null)
            throw new EntityNotFoundException("The Tag entity with ID: "+id+" Was not found");
        return t;
    }

    @Override
    public List<Tag> getAll(){
        EntityManager em = getEntityManager();
        TypedQuery<Tag> query = em.createQuery("SELECT t FROM Tag t", Tag.class);
        List<Tag> Tags = query.getResultList();
        return Tags;
    }


    @Override
    public Tag update(Tag tag) throws EntityNotFoundException {
        System.out.println(tag);
        EntityManager em = getEntityManager();
        if (tag.getName() == null)
            throw new IllegalArgumentException("No Tag by that name. Not updated");
        em.getTransaction().begin();
        Tag t = em.merge(tag);
        System.out.println(t);
        em.getTransaction().commit();
        return t;
    }

    @Override
    public Tag delete(String id) throws EntityNotFoundException{
        EntityManager em = getEntityManager();
        Tag t = em.find(Tag.class, id);
        if (t == null)
            throw new EntityNotFoundException("Could not remove Tag with id: "+id);
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();
        return t;
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        IDataFacade fe = getFacade(emf);
        fe.findByProperty("location", "Joergensen/").forEach(Tag->System.out.println(Tag));
    }

    @Override
    public List<Tag> findByProperty(String property, String propValue) {
        String baseUrl = null;
        try {
            baseUrl = utils.Utility.readFileProperty("BASEURL");
            System.out.println("LOKALITET:"+baseUrl);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        EntityManager em = getEntityManager();
        TypedQuery<Tag> q = em.createQuery("SELECT t FROM Tag t WHERE t."+property+" =:val", Tag.class);
        q.setParameter("val", baseUrl+propValue+"/");
        return q.getResultList();
    }
    
}
