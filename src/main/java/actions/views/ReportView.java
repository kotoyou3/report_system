package actions.views;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日報情報について画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
public class ReportView {

    private Integer id;
    private EmployeeView employee;
    private LocalDate reportDate;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;  // 論理削除フラグ

    // コンストラクタを追加
    public ReportView(Integer id, EmployeeView employee, LocalDate reportDate, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        this.id = id;
        this.employee = employee;
        this.reportDate = reportDate;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

public ReportView(Integer id, EmployeeView employee, LocalDate reportDate, String title, String content, boolean deleted) {
    this.id = id;
    this.employee = employee;
    this.reportDate = reportDate;
    this.title = title;
    this.content = content;
    this.createdAt = LocalDateTime.now(); // 現在の時刻を設定
    this.updatedAt = LocalDateTime.now(); // 現在の時刻を設定
    this.deleted = deleted;
}
}
