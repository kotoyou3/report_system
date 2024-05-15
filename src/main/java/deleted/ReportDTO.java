package deleted;

public class ReportDTO {
    private Integer id;
    private String content;
    private boolean deleted;  // 論理削除フラグ

    // getterとsetterも適宜追加
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
