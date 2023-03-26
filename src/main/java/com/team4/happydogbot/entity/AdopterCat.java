package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Класс, описывающий пользователя, как потенциального усыновителя кота
 * @param 'status' поле для отображения статуса пользовательского состояния
 * (отображает этап или состояние, в котором находится пользователь)
 * @see Cat
 * @see AdopterCat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adopter_cat")
public class AdopterCat {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "first_name", length = 25)
    private String firstName;
    @Column(name = "last_name", length = 25)
    private String lastName;
    @Column(name = "user_name", length = 25)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address", length = 50)
    private String address;
    @Column(name = "phone_number", length = 15)
    private String telephoneNumber;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status state = Status.USER;
    @CreationTimestamp
    @Column(name = "status_date")
    private LocalDate statusDate;
    @OneToMany(mappedBy = "adopterCat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ReportCat> reports;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cat_id")
    private Cat cat;
    @Column(name = "is_dog")
    private boolean isDog;

    public AdopterCat(Long chatId, String firstName, String lastName, String userName, int age, String address,
                      String telephoneNumber, Status state) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.age = age;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.state = state;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public boolean isDog() {
        return isDog;
    }

    public void setDog(boolean dog) {
        isDog = dog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdopterCat that)) return false;
        return age == that.age && Objects.equals(chatId, that.chatId) && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName) && Objects.equals(userName, that.userName)
                && Objects.equals(address, that.address) && Objects.equals(telephoneNumber, that.telephoneNumber)
                && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, firstName, lastName, userName, age, address, telephoneNumber, state);
    }
}
