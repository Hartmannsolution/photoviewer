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
        IDataFacade<Tag> fe = getFacade(emf);
//        fe.create(new Tag("Betty Mogensen","Født 13 februar 1945. Navngivet Betty Bügel Jørgensen. Mor: Kylle, Far: Thorkild, Søskende: [Preben Jørgensen, Birgit Christensen]. Boede i Holte fra 6-23. Gift med Henrik Mogensen. Mor til [Thomas Hartmann (f. Mogensen), Marianne Mogensen]. Børnebørn: [Laura, Alva, Victor Leander]. Har boet i Måløv fra 1974 på henholdsvis Knastebakken 117 og Kærlodden 47. Er uddannet Farmaceut og har en licentiatgrad i kemi. Har arbejdet på Ferocan, Miljøstyrelsen og hos en Dyrlæge i Holbæk"));
//        fe.create(new Tag("Henrik Mogensen","Født 20. augutst 1945. Navngivet Henrik Helge Mogensen. Mor: Ditte, Far: Mads, Søskende: [Dina, Birgit]. Boede i Charlottenlund til 6 år og i Hjortekær fra 6-23. Gift med Betty Mogensen (f. Jørgensen). Børnebørn: [Laura, Alva, Victor Leander]. Har boet i Måløv fra 1974 på henholdsvis Knastebakken 117 og Kærlodden 47. Er uddannet Cand Mag i Historie og Dansk ved Københavns Universitet og har arbejde med med undervisning både på voksenuddannelsesområdet samt på Hillerød Handelsskole (til 62 år)." ));
//        fe.create(new Tag("Thomas Hartmann","Født 6. juni 1968. Navngivet Thomas Bügel Mogensen. Mor: Betty, Far: Henrik. Søskende: [Marianne]. Gift med Charlotte Hartmann (Født Christensen) og far til Victor Leander. Er uddannet Cand IT fra IT-Universistet København. Har arbejdet som softwareudvikler og konsulent hos Projectum samt som Adjunkt og Lektor ved erhvervsakademiet CphBusiness. Har boet mange forskellige steder i København gennem mere end 20 år samt i Slette, Humlebæk fra 2014"));
//        fe.create(new Tag("Charlotte Hartmann","Født 25 august 1967. Navngivet Anne Charlotte Christensen. Mor: Else, Far: Ben. Gift med Thomas og mor til Victor Leander. Uddannet Bachelor i Skuespil ved GUILDHALL, London, Har arbejdet som skuespiller frem til 2007. Har drevet selvstændig virksomhed i tøjbranchen fra 2007. Har boet i London fro 1993-2001. I København til 2014 og i Sletten, Humlebæk fra 2014"));
//        fe.create(new Tag("Victor Leander", "Født 24. maj 2009. Navngivet Victor Leander Bügel Hartmann. Mor Charlotte, Far Thomas. Boede på Løgstørgade 14st.tv.,Østerbro fra 2009-2014 og derefter i Sletten, Humlebæk"));
//        fe.create(new Tag("Kylle Jørgensen"));
//        fe.create(new Tag("Thorkild Jørgensen"));
//        fe.create(new Tag("Johannes (Mads) Madsen"));
//        fe.create(new Tag("Edith (Ditte) Madsen"));
//        fe.create(new Tag("Grethe (Granni) Winge"));
//        fe.create(new Tag("Edmund Hartmann"));
//        fe.create(new Tag("Preben Hartmann"));
//        fe.create(new Tag("Børge Hartmann"));
//        fe.create(new Tag("Preben Jørgensen"));
//        fe.create(new Tag("Birgit Christensen"));
//        fe.create(new Tag("Per Winge"));
//        fe.create(new Tag("Lotte Hartmann"));
//        fe.create(new Tag("Else"));
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
