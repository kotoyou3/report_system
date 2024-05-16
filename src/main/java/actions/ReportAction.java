package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 */
public class ReportAction extends ActionBase {
    private ReportService service;

    @Override
    public void process() throws ServletException, IOException {
        service = new ReportService();
        invoke();
        service.close();
    }

    // 一覧画面を表示する
    public void index() throws ServletException, IOException {
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);
        long reportsCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports);
        putRequestScope(AttributeConst.REP_COUNT, reportsCount);
        putRequestScope(AttributeConst.PAGE, page);
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);

        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }
        forward(ForwardConst.FW_REP_INDEX);
    }

    // 新規登録画面を表示する
    public void entryNew() throws ServletException, IOException {
        putRequestScope(AttributeConst.TOKEN, getTokenId());
        putRequestScope(AttributeConst.REPORT, new ReportView());
        forward(ForwardConst.FW_REP_NEW);
    }

    // 新規登録を行う
    public void create() throws ServletException, IOException {
        if (checkToken()) {
            LocalDate day = getRequestParam(AttributeConst.REP_DATE) == null || getRequestParam(AttributeConst.REP_DATE).equals("")
                ? LocalDate.now()
                : LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));

            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            ReportView rv = new ReportView(
                null,
                ev,
                day,
                getRequestParam(AttributeConst.REP_TITLE),
                getRequestParam(AttributeConst.REP_CONTENT),
                false);

            List<String> errors = service.create(rv);

            if (errors.size() > 0) {
                putRequestScope(AttributeConst.TOKEN, getTokenId());
                putRequestScope(AttributeConst.REPORT, rv);
                putRequestScope(AttributeConst.ERR, errors);
                forward(ForwardConst.FW_REP_NEW);
            } else {
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

    // 詳細画面を表示する
    public void show() throws ServletException, IOException {
        int reportId = toNumber(getRequestParam(AttributeConst.REP_ID));
        ReportView rv = service.findOne(reportId);
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.REPORT, rv);

            // 管理者の場合に削除ボタンを表示するフラグを設定
            if (ev.getAdminFlag() == JpaConst.ROLE_ADMIN || ev.getId().equals(rv.getEmployee().getId())) {
                request.setAttribute("showDeleteButton", true);
            } else {
                request.setAttribute("showDeleteButton", false);
            }

            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    // 編集画面を表示する
    public void edit() throws ServletException, IOException {
        int reportId = toNumber(getRequestParam(AttributeConst.REP_ID));
        ReportView rv = service.findOne(reportId);
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null || (!rv.getEmployee().getId().equals(ev.getId()) && ev.getAdminFlag() != JpaConst.ROLE_ADMIN)) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.TOKEN, getTokenId());
            putRequestScope(AttributeConst.REPORT, rv);
            request.setAttribute("showDeleteButton", ev.getAdminFlag() == JpaConst.ROLE_ADMIN);
            forward(ForwardConst.FW_REP_EDIT);
        }
    }

    // 更新を行う
    public void update() throws ServletException, IOException {
        if (checkToken()) {
            int reportId = toNumber(getRequestParam(AttributeConst.REP_ID));
            ReportView rv = service.findOne(reportId);

            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            List<String> errors = service.update(rv);
            if (!errors.isEmpty()) {
                putRequestScope(AttributeConst.TOKEN, getTokenId());
                putRequestScope(AttributeConst.REPORT, rv);
                putRequestScope(AttributeConst.ERR, errors);
                forward(ForwardConst.FW_REP_EDIT);
            } else {
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

    // 日報を削除する
    public void destroy() throws ServletException, IOException {
        if (!checkAdmin()) {
            putSessionScope(AttributeConst.FLUSH, "管理者権限が必要です。");
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            return;
        }

        if (!checkToken()) {
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGINED.getMessage());
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            return;
        }

        try {
            int id = toNumber(getRequestParam(AttributeConst.REP_ID));
            service.delete(id);
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
        } catch (Exception e) {
            putSessionScope(AttributeConst.FLUSH, "削除中にエラーが発生しました。");
            forward(ForwardConst.FW_ERR_UNKNOWN);
        }
    }




    // 管理者かどうかを確認する
    private boolean checkAdmin() throws ServletException, IOException {
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        // 管理者でなければエラー画面を表示
        if (ev.getAdminFlag() != JpaConst.ROLE_ADMIN) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return false;
        } else {
            return true;
        }
    }

}