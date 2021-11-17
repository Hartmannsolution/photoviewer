package datafacades;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import errorhandling.EntityNotFoundException;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import utils.EMF_Creator;

public class UserFacade implements IDataFacade<UserDTO>{
    private static UserFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private UserFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static UserFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public UserDTO create(UserDTO user) {
        User u = new User(user.getUsername(), user.getPassword());
        EntityManager em = getEntityManager();
        user.getRoles().forEach(role->{
            Role r = em.find(Role.class,role);
            if(r!=null)
                u.addRole(r);
            else
                u.addRole(new Role(role));
        });
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }
    
    public UserDTO getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

    @Override
    public UserDTO getById(String fileName) throws EntityNotFoundException {
        //return null;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<UserDTO> getAll() {
        //return null;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public UserDTO delete(String id) throws EntityNotFoundException {
        //return null;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<UserDTO> findByProperty(String property, String propValue) {
        //return null;
        throw new UnsupportedOperationException("Not implemented yet");
    }
    public static void main(String[] args) {
        UserFacade uf = UserFacade.getFacade(EMF_Creator.createEntityManagerFactory());
        User user = new User("tester","testUs");
        user.addRole(new Role("user"));
        uf.create(new UserDTO(user));
    }

    @Override
    public UserDTO update(UserDTO t) throws EntityNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
