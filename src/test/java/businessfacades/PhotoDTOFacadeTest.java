package businessfacades;

import datafacades.IDataFacade;
import datafacades.PhotoFacade;
import datafacades.TagFacade;
import dtos.PhotoDTO;
import entities.Photo;
import entities.Tag;
import errorhandling.API_Exception;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class PhotoDTOFacadeTest {
    private static EntityManagerFactory emf;
    private static IDataFacade<PhotoDTO> facade;

    Photo p1,p2;
    Tag t1, t2, t3;
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PhotoDTOFacade.getFacade(emf);

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
            p1 = new Photo("photo1","Somewhere", "Dette er et meget gammelt billede");
            p2 = new Photo("photo2","Some file name", "Dette er et billede af noget");
            t1 = new Tag("Dorthea");
            t2 = new Tag("Frederik");
            t3 = new Tag("Tag3");
            p1.addTag(t3);
            em.persist(p1);
            em.persist(p2);
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
    void create() throws API_Exception {
        PhotoDTO photoDTO = facade.create(new PhotoDTO(new Photo("Name1", "location1", "description")));
        assertEquals("Name1", photoDTO.getName());
    }
}