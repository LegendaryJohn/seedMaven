package facades;

import security.IUserFacade;
import entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade {
  /*When implementing your own database for this seed, you should NOT touch any of the classes in the security folder
    Make sure your new facade implements IUserFacade and keeps the name UserFacade, and that your Entity User class implements 
    IUser interface, then security should work "out of the box" with users and roles stored in your database */
  

  EntityManagerFactory emf = Persistence.createEntityManagerFactory("hashPU");
  
  public UserFacade() {
      EntityManager em = emf.createEntityManager();
    //Test Users
    try{
        
    User user = new User("user",PasswordStorage.createHash("test"));
    user.addRole("User");
    
    User admin = new User("admin",PasswordStorage.createHash("test"));
    admin.addRole("Admin");
    
    
    User both = new User("user_admin",PasswordStorage.createHash("test"));
    both.addRole("User");
    both.addRole("Admin");
  
    
    em.getTransaction().begin();
    em.persist(user);
    em.persist(admin);
    em.persist(both);
    em.getTransaction().commit();
    
    } catch (PasswordStorage.CannotPerformOperationException e){
        Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, e);
        
    } finally{
        em.close();
    }
  }
  
 
  
  @Override
  public IUser getUserByUserId(String id){
    EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class);
        query.setParameter("username", id);
        List<User> result = query.getResultList();
        User user = result.get(0);

        return user;
  }
  
  
  /*
  Return the Roles if users could be authenticated, otherwise null
  */
    @Override
  public List<String> authenticateUser(String userName, String password){
    EntityManager em = emf.createEntityManager();
    
    Query query = em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class);
    query.setParameter("username", userName);
    List<User> result = query.getResultList();
    User user = result.get(0);
    try{
        if(PasswordStorage.verifyPassword(password, user.getPassword()))
        {
            return user.getRolesAsStrings();
        }
        else{
            return null;
        }
    }
    catch(PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException e){
        Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, e);
        return null;
    } finally{
        em.close();
    }
    
  }
  
  
    
  
  
}
