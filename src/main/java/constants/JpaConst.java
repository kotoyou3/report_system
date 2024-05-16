package constants;

public class JpaConst {
    // 全ての定数
    public static final String REP_COL_DELETED = "deleted";
    public static final int ROLE_ADMIN = 1; // 管理者権限ON（管理者）
    public static final int ROLE_GENERAL = 0; // 管理者権限OFF（一般）
    public static final int EMP_DEL_TRUE = 1; // 削除フラグON（削除済み）
    public static final int EMP_DEL_FALSE = 0; // 削除フラグOFF（現役）

    // persistence-unit名
    public static final String PERSISTENCE_UNIT_NAME = "daily_report_system";

    // データ取得件数の最大値
    public static final int ROW_PER_PAGE = 15; // 1ページに表示するレコードの数

    // 従業員テーブル
    public static final String TABLE_EMP = "employees"; // テーブル名
    // 従業員テーブルカラム
    public static final String EMP_COL_ID = "id"; // id
    public static final String EMP_COL_CODE = "code"; // 社員番号
    public static final String EMP_COL_NAME = "name"; // 氏名
    public static final String EMP_COL_PASS = "password"; // パスワード
    public static final String EMP_COL_ADMIN_FLAG = "admin_flag"; // 管理者権限
    public static final String EMP_COL_CREATED_AT = "created_at"; // 登録日時
    public static final String EMP_COL_UPDATED_AT = "updated_at"; // 更新日時
    public static final String EMP_COL_DELETE_FLAG = "delete_flag"; // 削除フラグ

    // 日報テーブル
    public static final String TABLE_REP = "reports"; // テーブル名
    // 日報テーブルカラム
    public static final String REP_COL_ID = "id"; // id
    public static final String REP_COL_EMP = "employee_id"; // 日報を作成した従業員のid
    public static final String REP_COL_REP_DATE = "report_date"; // いつの日報かを示す日付
    public static final String REP_COL_TITLE = "title"; // 日報のタイトル
    public static final String REP_COL_CONTENT = "content"; // 日報の内容
    public static final String REP_COL_CREATED_AT = "created_at"; // 登録日時
    public static final String REP_COL_UPDATED_AT = "updated_at"; // 更新日時


    // Entity名
    public static final String ENTITY_EMP = "employee"; // 従業員
    public static final String ENTITY_REP = "report"; // 日報

    // JPQL内パラメータ
    public static final String JPQL_PARM_CODE = "code"; // 社員番号
    public static final String JPQL_PARM_PASSWORD = "password"; // パスワード
    public static final String JPQL_PARM_EMPLOYEE = "employee"; // 従業員

    // NamedQueryの nameとquery
    // 全ての従業員をidの降順に取得する
    public static final String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; // name
    public static final String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; // query
    // 全ての従業員の件数を取得する
    public static final String Q_EMP_COUNT = ENTITY_EMP + ".count";
    public static final String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    // 社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    public static final String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    public static final String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    // 指定した社員番号を保持する従業員の件数を取得する
    public static final String Q_EMP_COUNT_REGISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    public static final String Q_EMP_COUNT_REGISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
    // 全ての日報をidの降順に取得する
    public static final String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
    public static final String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r WHERE r.deleted = false ORDER BY r.id DESC";
    // 全ての日報の件数を取得する
    public static final String Q_REP_COUNT = ENTITY_REP + ".count";
    public static final String Q_REP_COUNT_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.deleted = false";
    // 指定した従業員が作成した日報を全件idの降順で取得する
    public static final String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
    public static final String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " AND r.deleted = false ORDER BY r.id DESC";
    // 指定した従業員が作成した日報の件数を取得する
    public static final String Q_REP_COUNT_ALL_MINE = ENTITY_REP + ".countAllMine";
    public static final String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " AND r.deleted = false";
    public static final String Q_REP_FIND_BY_EMPLOYEE = ENTITY_REP + ".findByEmployee";
    public static final String Q_REP_FIND_BY_EMPLOYEE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " AND r.deleted = false";
}
