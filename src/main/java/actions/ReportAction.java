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

public class ReportAction extends ActionBase {
    private ReportService service;

    @Override
    public void process() throws ServletException, IOException {
        service = new ReportService();
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        System.out.println("Logged in employee: " + ev);
        if (ev != null) {
            System.out.println("Admin flag: " + ev.getAdminFlag());
        }
        invoke();
        service.close();
    }

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

    public void entryNew() throws ServletException, IOException {
        generateAndSaveToken();
        putRequestScope(AttributeConst.TOKEN, getTokenId());
        putRequestScope(AttributeConst.REPORT, new ReportView());
        forward(ForwardConst.FW_REP_NEW);
    }

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
                generateAndSaveToken();
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

    public void show() throws ServletException, IOException {
        int reportId = toNumber(getRequestParam(AttributeConst.REP_ID));
        ReportView rv = service.findOne(reportId);
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            putRequestScope(AttributeConst.REPORT, rv);

            if (ev.getAdminFlag() == JpaConst.ROLE_ADMIN || ev.getId().equals(rv.getEmployee().getId())) {
                request.setAttribute("showDeleteButton", true);
            } else {
                request.setAttribute("showDeleteButton", false);
            }

            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    public void edit() throws ServletException, IOException {
        int reportId = toNumber(getRequestParam(AttributeConst.REP_ID));
        ReportView rv = service.findOne(reportId);
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        System.out.println("Report ID: " + reportId);
        System.out.println("Report View: " + rv);
        System.out.println("Logged in Employee: " + ev);
        System.out.println("Admin Flag: " + ev.getAdminFlag());

        if (rv == null || (!rv.getEmployee().getId().equals(ev.getId()) && ev.getAdminFlag() != JpaConst.ROLE_ADMIN)) {
            System.out.println("Access denied or report not found");
            forward(ForwardConst.FW_ERR_UNKNOWN);
        } else {
            generateAndSaveToken();
            putRequestScope(AttributeConst.TOKEN, getTokenId());
            putRequestScope(AttributeConst.REPORT, rv);
            forward(ForwardConst.FW_REP_EDIT);
        }
    }

    public void update() throws ServletException, IOException {
        if (checkToken()) {
            int reportId = toNumber(getRequestParam(AttributeConst.REP_ID));
            ReportView rv = service.findOne(reportId);

            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            List<String> errors = service.update(rv);
            if (!errors.isEmpty()) {
                generateAndSaveToken();
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

    public void destroy() throws ServletException, IOException {
        if (!checkAdmin()) {
            putSessionScope(AttributeConst.FLUSH, "管理者権限が必要です。");
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            return;
        }

        if (!checkToken()) {
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

    private boolean checkAdmin() throws ServletException, IOException {
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        if (ev.getAdminFlag() != JpaConst.ROLE_ADMIN) {
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return false;
        } else {
            return true;
        }
    }
}
