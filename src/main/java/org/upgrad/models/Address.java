package org.upgrad.models;

import javax.persistence.*;

@Entity
@Table(name= "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "flat_buil_number")
    private String flatNumber;

    @Column(name="locality" )
    private String locality;

    @Column(name="city")
    private String city;

    @Column(name="zipcode")
    private String zipcode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stateId")
    private States state;

    public Address() {
    }

    public Address(Integer id , String flat_buil_number, String locality, String city, String zipcode, States state) {
        this.id = id ;
        this.flatNumber = flat_buil_number ;
        this.city = city ;
        this.locality = locality ;
        this.zipcode = zipcode ;
        this.state = state ;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }
}
