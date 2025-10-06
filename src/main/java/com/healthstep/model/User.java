package com.healthstep.mobile.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length=60)
  private String username;

  @Column(nullable=false, length=100)
  private String passwordHash;

  @Column(nullable=false)
  private Instant createdAt = Instant.now();

  public Long getId(){ return id; }
  public void setId(Long id){ this.id = id; }

  public String getUsername(){ return username; }
  public void setUsername(String username){ this.username = username; }

  public String getPasswordHash(){ return passwordHash; }
  public void setPasswordHash(String passwordHash){ this.passwordHash = passwordHash; }

  public Instant getCreatedAt(){ return createdAt; }
  public void setCreatedAt(Instant createdAt){ this.createdAt = createdAt; }
}