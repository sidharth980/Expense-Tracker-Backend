package com.example.expt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity
@Setter
@Table(name = "expense_splits")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "split_id")
    private Long splitId;

    @ManyToOne
    @JsonIgnore
     @JoinColumn(name = "expense_id") // JPA annotation
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "owes_amount")
    private BigDecimal owesAmount;

    @Column(name = "is_settled")
    private Boolean isSettled;

    @Column(name = "created_at")
    private LocalDate created_at;
}
