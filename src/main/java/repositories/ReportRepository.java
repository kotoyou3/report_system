package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Report;

/**
 * Reportデータにアクセスするためのリポジトリインターフェースです。
 * 論理削除されていないレポートを取得するメソッドを定義します。
 */
public interface ReportRepository {
    List<Report> findAllByDeletedFalse();
}

/**
 * ReportRepositoryインターフェースのJDBCによる実装クラスです。
 * このクラスはデータベースから論理削除されていないレポートを取得します。
 */
class SimpleReportRepository implements ReportRepository {
    private Connection connection;

    /**
     * コンストラクタでデータベース接続を受け取ります。
     *
     * @param connection データベース接続
     */
    public SimpleReportRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * 論理削除されていないすべてのレポートをデータベースから取得します。
     *
     * @return 論理削除されていないレポートのリスト
     */
    @Override
    public List<Report> findAllByDeletedFalse() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT id, content, deleted FROM reports WHERE deleted = false";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Report report = new Report();
                report.setId(rs.getInt("id"));
                report.setContent(rs.getString("content"));
                report.setDeleted(rs.getBoolean("deleted"));
                reports.add(report);
            }
        } catch (SQLException e) {
            System.err.println("Error accessing the database: " + e.getMessage());
            throw new RuntimeException("Database access error occurred", e);
        }
        return reports;
    }
}
