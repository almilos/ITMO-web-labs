package ru.ifmo.web.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class Metallica {
  private Long id;
  private String name;
  private String instrument;
  private Date entrydate;
  private Integer networth;
  private Date birthdate;
}
