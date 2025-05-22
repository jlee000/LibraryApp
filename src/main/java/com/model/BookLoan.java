package com.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.config.LoanStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class BookLoan {
    @Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime loanDate = LocalDateTime.now();
    @Transient
    @Column(nullable=false)
    @JsonIgnore
    private int daysToLoan;
    private LocalDateTime dueDate = this.loanDate.plusDays(daysToLoan);
    private LocalDateTime returnDate;
	
	@Enumerated(EnumType.STRING)
	private LoanStatus loanStatus;

    private BigDecimal totalLoanValue = setTotalLoanValue();
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonIgnore
	private Users user;

	@OneToMany(mappedBy="bookloan", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference  
	private List<BookLoanItem> bookLoanItems;

    
    public BookLoan() {
        super();
    }

    public BookLoan(Long id, LocalDateTime loanDate, int daysToLoan, LocalDateTime dueDate, LocalDateTime returnDate, LoanStatus loanStatus, Users user, List<BookLoanItem> bookLoanItems) {
        this.id = id;
        this.loanDate = loanDate;
        this.daysToLoan = daysToLoan;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.loanStatus = loanStatus;
        this.user = user;
        this.bookLoanItems = bookLoanItems;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getTotalLoanValue() {
        return totalLoanValue;
    }
    public BigDecimal setTotalLoanValue() {
        BigDecimal value = new BigDecimal(0);
        if(this.bookLoanItems!=null){
            for(BookLoanItem item: this.bookLoanItems){
                value = value.add(item.getLoanItemValue());
            }
        }
        this.totalLoanValue = value;
        return this.totalLoanValue;
    }
    public Long getId() {
        return id;
    }
    public LocalDateTime getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void addDaysToDueDate(int daysToLoan) {
        this.dueDate = this.loanDate.plusDays(daysToLoan);
    }
    public LocalDateTime getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
    public LoanStatus getLoanStatus() {
        return loanStatus;
    }
    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public int getDaysToLoan() {
        return daysToLoan;
    }
    public void setDaysToLoan(int daysToLoan) {
        this.daysToLoan = daysToLoan;
    }
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    public List<BookLoanItem> getBookLoanItems() {
        return bookLoanItems;
    }
    public void setBookLoanItems(List<BookLoanItem> bookLoanItems) {
        this.bookLoanItems = bookLoanItems;
    }
    @Override
    public String toString() {
        return "BookLoan [id=" + id + ", loanDate=" + loanDate + ", daysToLoan=" + daysToLoan + ", dueDate=" + dueDate
                + ", returnDate=" + returnDate + ", loanStatus=" + loanStatus + ", user="
                + user + ", bookLoanItems=" + bookLoanItems + "]";
    }    
}
