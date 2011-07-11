package com.cedarsoft.rest.generator.test;


import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class User {
  @Nonnull
  private final String email;
  @Nonnull

  private final String name;

  @Nonnull
  private final Group group;

  @Nonnull
  private final List<User> friends = new ArrayList<User>();

  public User( @Nonnull String email, @Nonnull String name ) {
    this( email, name, new Group( "NOBODY", "description" ) );
  }

  public User( @Nonnull String email, @Nonnull String name, @Nonnull Group group ) {
    this.email = email;
    this.name = name;
    this.group = group;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public String getEmail() {
    return email;
  }

  @Nonnull
  public Group getGroup() {
    return group;
  }

  @Nonnull
  public List<? extends User> getFriends() {
    return Collections.unmodifiableList( friends );
  }

  public void addFriend( @Nonnull User user ) {
    this.friends.add( user );
  }
}
