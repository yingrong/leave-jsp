package com.tw.api_maintenance.after;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(value = "/api-maintenance/after")
public class ActivityServlet extends HttpServlet {
    TeamBuildingService teamBuildingService;

    public ActivityServlet() {
        teamBuildingService = new TeamBuildingService(new TeamBuildingPackageItemRepository(), new TeamBuildingPackageRepository(), new ActivityRepository(), new ActivityMutexRepository(), new ActivityDependentRepository());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("get request");
        TeamBuildingPackageItemDto teamBuildingPackageItemDto = teamBuildingService.queryTeamBuildingPackage(80010L);
        request.setAttribute("teamBuildingPackageItem", teamBuildingPackageItemDto);
        request.getRequestDispatcher("/api_maintenance/after/selectActivity.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("sAction");
        System.out.println("post request. action:" + action);
        ObjectMapper objectMapper = new ObjectMapper();

        if ("select".equals(action)) {
            Long teamBuildingPackageItemId = Long.parseLong(request.getParameter("teamBuildingPackageItemId"));
            Long activityItemId = Long.parseLong(request.getParameter("activityItemId"));
            Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, activityItemId, request.getParameter("count"));
            if (error != null) {
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(objectMapper.writeValueAsString(error));
                out.flush();
            }
        } else if ("unselect".equals(action)) {
            Long teamBuildingPackageItemId = Long.parseLong(request.getParameter("teamBuildingPackageItemId"));
            Long activityItemId = Long.parseLong(request.getParameter("activityItemId"));
            Long unselectedDependentActivityId = teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, activityItemId);
            HashMap<String, Long> result = new HashMap<>();
            result.put("unselectedDependentActivityId", unselectedDependentActivityId);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(objectMapper.writeValueAsString(result));
            out.flush();
        }
    }


}
