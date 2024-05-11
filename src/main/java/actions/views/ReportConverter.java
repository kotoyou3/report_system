package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportConverter {

    // ReportViewからReportモデルへの変換
    public static Report toModel(ReportView rv) {
        if (rv == null) {
            return null;
        }
        return new Report(
            rv.getId(),
            EmployeeConverter.toModel(rv.getEmployee()),
            rv.getReportDate(),
            rv.getTitle(),
            rv.getContent(),
            rv.getCreatedAt(),
            rv.getUpdatedAt(),
            rv.isDeleted()  // deleted フラグも変換
        );
    }

    // ReportモデルからReportViewへの変換
    public static ReportView toView(Report r) {
        if (r == null) {
            return null;
        }
        ReportView rv = new ReportView();
        rv.setId(r.getId());
        rv.setEmployee(EmployeeConverter.toView(r.getEmployee()));
        rv.setReportDate(r.getReportDate());
        rv.setTitle(r.getTitle());
        rv.setContent(r.getContent());
        rv.setCreatedAt(r.getCreatedAt());
        rv.setUpdatedAt(r.getUpdatedAt());
        rv.setDeleted(r.isDeleted());  // deleted フラグも変換
        return rv;
    }

    // リスト変換メソッド
    public static List<ReportView> toViewList(List<Report> list) {
        List<ReportView> evs = new ArrayList<>();
        for (Report r : list) {
            evs.add(toView(r));
        }
        return evs;
    }

    // Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピー
    public static void copyViewToModel(Report r, ReportView rv) {
        r.setId(rv.getId());
        r.setEmployee(EmployeeConverter.toModel(rv.getEmployee()));
        r.setReportDate(rv.getReportDate());
        r.setTitle(rv.getTitle());
        r.setContent(rv.getContent());
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());
        r.setDeleted(rv.isDeleted());  // deleted フラグもコピー
    }
}
