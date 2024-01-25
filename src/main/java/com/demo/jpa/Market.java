package com.demo.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-25        koiw1       최초 생성
 */
@Entity
@Getter
@Setter
public class Market {

    @Id
    @GeneratedValue
    private Long id ;

    private String name ;

    private long price ;


}
