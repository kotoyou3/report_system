package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

public class TopAction extends ActionBase {

    private ReportService service;

    @Override
    public void process() throws ServletException, IOException {
        service = new ReportService();
        invoke();
        service.close();
    }

    public void index() throws ServletException, IOException {
        // ログイン中の従業員を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        // 従業員が作成したレポートを取得
        int page = getPage();
        List<ReportView> reports = service.getMinePerPage(ev, page);
        long reportsCount = service.countAllMine(ev);

        putRequestScope(AttributeConst.REPORTS, reports);
        putRequestScope(AttributeConst.REP_COUNT, reportsCount);
        putRequestScope(AttributeConst.PAGE, page);
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);

        // トップページを表示
        forward(ForwardConst.FW_TOP_INDEX);
    }
}
