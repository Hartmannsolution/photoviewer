package facades;

import datafacades.IDataFacade;
import datafacades.TagFacade;
import datafacades.TagFacade;
import entities.Photo;
import entities.Tag;
import entities.Tag;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Disabled
class TagFacadeTest {

    private static EntityManagerFactory emf;
    private static IDataFacade<Tag> facade;
    Tag t1, t2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = TagFacade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean ut database after test is done or use a persistence unit with drop-and-create to start ut clean on every test
    }

    // Setut the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Tag.deleteAllRows").executeUpdate();
            t1 = new Tag("Dorthea", "Boede i Nørregade i 40erne");
            t2 = new Tag("Frederik", "Kendte onkel jørgen som barn");
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
    void create() throws API_Exception {
        System.out.println("Testing create(Tag t)");
        Tag t = new Tag("Somewhere","TestTag");
        Tag expected = t;
        Tag actual   = facade.create(t);
        assertEquals(expected, actual);
    }
    

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Tag expected = t1;
        Tag actual = facade.getById(t1.getName());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }
    @Test
    void getByProp() throws EntityNotFoundException {
        System.out.println("Testing getByProperty(String property, String value)");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Tag tag3 = new Tag("Helmut", "Boede i Nørregade i 40erne");
        Tag tag4 = new Tag("Helga", "Kendte onkel jørgen som barn");
        Photo photo = new Photo("Test", "Loc", "Desc", "Title",3);
        Photo photo2 = new Photo("Test2", "Loc2", "Desc2", "Title2",5);
        em.persist(photo2);
        em.persist(photo);
        em.persist(tag3);
        em.persist(tag4);
        photo.addTag(t1);
        photo.addTag(t2);
        photo.addTag(tag3);
        photo2.addTag(tag4);
        em.getTransaction().commit();
        int expected = 3;
        int actual = facade.findByProperty("photoLocation", "Loc").size();
        assertEquals(expected,actual);
    }
@Test
    void getAllTags() {
        System.out.println("Testing getAllTags()");
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        System.out.println("Testing Update(Tag p)");
        t2.setDescription("HOHOHO");
        Tag expected = t2;
        Tag actual = facade.update(t2);
        System.out.println("ACTUAL"+actual);
        assertEquals(expected,actual);
    }

    

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id)");
        Tag t = facade.delete(t1.getName());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(t,t1);
    }
}