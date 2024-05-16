package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.TypedQuery;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Report;
import models.validators.ReportValidator;

public class ReportService extends ServiceBase {

    public void delete(int id) {
        em.getTransaction().begin();
        Report report = findOneInternal(id);
        if (report != null) {
            report.setDeleted(true); // 論理削除フラグをtrueに設定
            em.merge(report); // 変更をデータベースに保存
        }
        em.getTransaction().commit();
    }

    public List<ReportView> getMinePerPage(EmployeeView employee, int page) {
        TypedQuery<Report> query = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE);
        return ReportConverter.toViewList(query.getResultList());
    }

    public long countAllMine(EmployeeView employee) {
        TypedQuery<Long> query = em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee));
        return query.getSingleResult();
    }

    public List<ReportView> getAllPerPage(int page) {
        TypedQuery<Report> query = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE);
        return ReportConverter.toViewList(query.getResultList());
    }

    public long countAll() {
        TypedQuery<Long> query = em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class);
        return query.getSingleResult();
    }

    public ReportView findOne(int id) {
        return ReportConverter.toView(findOneInternal(id));
    }

    public List<String> create(ReportView rv) {
        List<String> errors = ReportValidator.validate(rv);
        if (errors.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            rv.setCreatedAt(now);
            rv.setUpdatedAt(now);
            createInternal(rv);
        }
        return errors;
    }

    public List<String> update(ReportView rv) {
        List<String> errors = ReportValidator.validate(rv);
        if (errors.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            rv.setUpdatedAt(now);
            updateInternal(rv);
        }
        return errors;
    }

    private Report findOneInternal(int id) {
        return em.find(Report.class, id);
    }

    private void createInternal(ReportView rv) {
        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();
    }

    private void updateInternal(ReportView rv) {
        em.getTransaction().begin();
        Report r = findOneInternal(rv.getId());
        ReportConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();
    }
}
