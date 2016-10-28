package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import security.IUser;
import security.PasswordStorage;
@Entity
public class User implements IUser, Serializable{
  
  private String password;  //Pleeeeease dont store me in plain text
  @Id
  private String userName;
  @ElementCollection
  @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
  @Column(name = "ROLES")
  
  List<String> roles = new ArrayList();

  public User(){
      
  }
  
  public User(String userName, String password) throws PasswordStorage.CannotPerformOperationException {
    this.userName = userName;
    this.password = password;
  }
  
  public User(String userName, String password, List<String> roles) throws PasswordStorage.CannotPerformOperationException {
    this.userName = userName;
    this.password = password;
    this.roles = roles;
  }
  
  public void addRole(String role){
    roles.add(role);
  }
    
  @Override
  public List<String> getRolesAsStrings() {
   return roles;
  }
 
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

 
          
}
