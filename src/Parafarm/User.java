package Parafarm;

import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class User implements Serializable{
private static final AtomicLong idGen = new AtomicLong();
private long employeeid = idGen.incrementAndGet();
private String name;
private String surname;
private int phone;
public String role;
public String username;
public String password;

User(String username,String password){
	this.username = username;
	this.password = password;
}

public String getName()           { return name; }
public String getUsername()       { return username; }
public String getRole()           { return role; }
public void setName(String n)     { this.name = n; }
public void setUsername(String u) { this.username = u; }
public void setRole(String r)     { this.role = r; }
public void setPassword(String p) { this.password = p; }


}
