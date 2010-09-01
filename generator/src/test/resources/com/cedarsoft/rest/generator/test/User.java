package com.cedarsoft.rest.generator.test;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class User {
  @NotNull
  private final String email;
  @NotNull
  @NonNls
  private final String name;

  @NotNull
  private final Group group;

  @NotNull
  private final List<User> friends = new ArrayList<User>();

  public User( @NotNull String email, @NotNull String name ) {
    this( email, name, new Group( "NOBODY", "description" ) );
  }

  public User( @NotNull String email, @NotNull String name, @NotNull Group group ) {
    this.email = email;
    this.name = name;
    this.group = group;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public String getEmail() {
    return email;
  }

  @NotNull
  public Group getGroup() {
    return group;
  }

  @NotNull
  public List<? extends User> getFriends() {
    return Collections.unmodifiableList( friends );
  }

  public void addFriend( @NotNull User user ) {
    this.friends.add( user );
  }
}
