package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;

/**
 * 日報データのDTOモデル
 */
@Entity
@Table(name = JpaConst.TABLE_REP)
@NamedQueries({
    @NamedQuery(
        name = JpaConst.Q_REP_GET_ALL,
        query = JpaConst.Q_REP_GET_ALL_DEF
    ),
    @NamedQuery(
        name = JpaConst.Q_REP_FIND_BY_EMPLOYEE,
        query = JpaConst.Q_REP_FIND_BY_EMPLOYEE_DEF
    ),
    @NamedQuery(
        name = JpaConst.Q_REP_GET_ALL_MINE,
        query = JpaConst.Q_REP_GET_ALL_MINE_DEF
    ),
    @NamedQuery(
        name = JpaConst.Q_REP_COUNT,
        query = JpaConst.Q_REP_COUNT_DEF
    ),
    @NamedQuery(
        name = JpaConst.Q_REP_COUNT_ALL_MINE,
        query = JpaConst.Q_REP_COUNT_ALL_MINE_DEF
    )
})
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = JpaConst.REP_COL_ID)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = JpaConst.REP_COL_EMP, nullable = false)
    private Employee employee;

    @Column(name = JpaConst.REP_COL_REP_DATE, nullable = false)
    private LocalDate reportDate;

    @Column(name = JpaConst.REP_COL_TITLE, nullable = false)
    private String title;

    @Lob
    @Column(name = JpaConst.REP_COL_CONTENT, nullable = false)
    private String content;

    @Column(name = JpaConst.REP_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = JpaConst.REP_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = JpaConst.REP_COL_DELETED, nullable = false)
    private boolean deleted;

    // デフォルトコンストラクタ（JPA要件）
    public Report() {}

    // 全フィールドを含むコンストラクタ
    public Report(Integer id, Employee employee, LocalDate reportDate, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        this.id = id;
        this.employee = employee;
        this.reportDate = reportDate;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    // ゲッターとセッター
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
