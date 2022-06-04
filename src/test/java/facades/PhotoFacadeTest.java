package facades;

import datafacades.IDataExtra;
import datafacades.IDataFacade;
import datafacades.PhotoFacade;
import datafacades.TagFacade;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;
import entities.Photo;
import entities.Tag;
import groovyjarjarantlr4.v4.runtime.atn.StarLoopEntryState;
import junit.framework.Assert;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled
class PhotoFacadeTest {

    private static EntityManagerFactory emf;
    private static IDataFacade<Photo> facade;
    private static IDataExtra<Photo> facadeExtra;
    private static IDataFacade<Tag> tagsFacade;
    Photo p1,p2,p3;
    Tag t1, t2, t3;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PhotoFacade.getFacade(emf);
        facadeExtra = PhotoFacade.getFacadeExtra(emf);
        tagsFacade = TagFacade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Tag.deleteAllRows").executeUpdate();
            em.createNamedQuery("Photo.deleteAllRows").executeUpdate();
            p1 = new Photo("photo1","Somewhere", "Dette er et meget gammelt billede","ny title",2);
            p2 = new Photo("photo2","Some file name", "Dette er et billede af noget","new title",4);
            p3 = new Photo("photo3","Some file new name", "Dette er et billede af noget","new title",5);
            t1 = new Tag("Dorthea");
            t2 = new Tag("Frederik");
            t3 = new Tag("Tag3");
            p1.addTag(t3);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(t3);
            em.persist(t1);
            em.persist(t2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }


    @Test
    void create() {
        System.out.println("Testing create(Photo p)");
        Photo p = new Photo("Somewhere","TestPhoto", "Something","any title",6);
        Photo expected = p;
        Photo actual   = null;
        try {
            actual = facade.create(p);
        } catch (API_Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, actual);
    }

    @Test
    void createWithTags() {
        System.out.println("Testing create(Photo p) with tags added");
        Photo p = new Photo("Somewhere","TestPhoto", "description","some title",5);
        p.addTag(new Tag("Alfred"));
        p.addTag(new Tag("Roberta"));
        Photo expected = p;
        Photo actual   = null;
        try {
            actual = facade.create(p);
        } catch (API_Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, actual);
    }

    @Test
    void createWithKnownTags() {
        System.out.println("Testing create(Photo p) with Tags added");
        Photo p = new Photo("Somewhere","TestPhoto","Something","some title",4);
        p.addTag(t1);
        p.addTag(t2);
        Photo expected = p;
        Photo actual   = null;
        try {
            actual = facade.create(p);
        } catch (API_Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Photo expected = p1;
        Photo actual = facade.getById(p1.getFileName());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 3;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }

@Test
    void getAllTags() {
        System.out.println("Testing getAllTags()");
        int expected = 3;
        int actual = tagsFacade.getAll().size();
        assertEquals(expected,actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p)");
        p2.setPhotoTxt("HOHO");
        Photo expected = p2;
        Photo actual = facade.update(p2);
        assertEquals(expected,actual);
    }

    @Test
    void updateWithTags() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p) with known Tags");
        p2.addTag(t1);
        p2.addTag(t2);
        Photo p = facade.update(p2);
        int expected = 2;
        int actual = p.getTags().size();
        assertEquals(expected,actual);
    }
    @Test
    void updateWithNonExisting() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p) Non existing");

        assertThrows(EntityNotFoundException.class, ()->facade.update(new Photo("test","test2","test3", "test4",4)));
    }
    @Test //Test if adding a single new Tag will remove the existing ones as it should
    void updateWithNewTag() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p) with new Tag and removing others p1 tags before: "+p1.getTags());
        p1.getTags().clear();
        p1.addTag(new Tag("this is a new tag"));

        Photo p = facade.update(p1);
        int expected = 1;
        int actual = p.getTags().size();
        assertEquals(expected,actual);
    }
    @Test //Test if ignoring tags will remove the existing ones as it should not
    void updateWithNoTag() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p) with new Tag and removing others p1 tags before: "+p1.getTags());
        Photo photo = new Photo("photo1", "loc", "desc", "title",2);

        Photo p = facade.update(photo);
        int expected = 1;
        int actual = p.getTags().size();
        assertEquals(expected,actual);
    }

    @Test //Test if ignoring tags will remove the existing ones as it should not
    void updateWithNoTagAndChangedViewno() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p) with no Tag and not removing other p1 tags : "+p1.getTags());
        Photo photo = new Photo("photo1", "loc", "desc", "title",10);

        Photo p = facade.update(photo);
        int expected = 1;
        int actual = p.getTags().size();
        assertEquals(expected,actual);
    }

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id)");
        Photo p = facade.delete(p1.getFileName());
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(p,p1);
    }
   @Test
   public void pagination() {
       System.out.println("Test the use of pagination when getting images");
       int expected = 2;
       int actual = facadeExtra.getAllPaginated(0, 2).size();

       Assert.assertEquals(expected, actual);
   }
}