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
 * Класс, описывающий пользователя, как потенциального усыновителя собаки
 * @param 'status' поле для отображения статуса пользовательского состояния
 * (отображает этап или состояние, в котором находится пользователь)
 * @see Dog
 * @see AdopterDog
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adopter_dog")
public class AdopterDog {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "first_name", length = 25)
    private String firstName;
    @Column(name = "last_name", length = 25)
    private String lastName;
    @Column(name = "user_name",  length = 25)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address", length = 50)
    private String address;
    @Column(name = "phone_number",  length = 15)
    private String telephoneNumber;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status state = Status.USER;
    @CreationTimestamp
    @Column(name = "status_date")
    private LocalDate statusDate;
    @OneToMany(mappedBy = "adopterDog", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ReportDog> reports;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dog_id")
    private Dog dog;
    @Column(name = "is_dog")
    private boolean isDog;

    public void setDog(Dog dog) {
        this.dog = dog;
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
        if (o == null || getClass() != o.getClass()) return false;
        AdopterDog adopterDog = (AdopterDog) o;
        return age == adopterDog.age && Objects.equals(chatId, adopterDog.chatId)
                && Objects.equals(firstName, adopterDog.firstName) && Objects.equals(lastName, adopterDog.lastName)
                && Objects.equals(userName, adopterDog.userName) && Objects.equals(address, adopterDog.address)
                && Objects.equals(telephoneNumber, adopterDog.telephoneNumber) && state == adopterDog.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, firstName, lastName, userName, age, address, telephoneNumber, state);
    }
}
