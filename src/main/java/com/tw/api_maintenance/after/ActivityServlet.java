package com.tw.api_maintenance.after;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(value = "/api-maintenance/after")
public class ActivityServlet extends HttpServlet {
    TeamBuildingService teamBuildingService;

    public ActivityServlet() {
        teamBuildingService = new TeamBuildingService(new TeamBuildingPackageItemRepository(), new TeamBuildingPackageRepository(), new ActivityRepository());
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
            Integer count = Integer.parseInt(request.getParameter("count"));
            Error error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, activityItemId, count);
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
            teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, activityItemId);
        } else if ("check-mutex".equals(action)) {
            Long teamBuildingPackageItemId = Long.parseLong(request.getParameter("teamBuildingPackageItemId"));
            Long activityItemId = Long.parseLong(request.getParameter("activityItemId"));
            String mutexActivityIds = request.getParameter("mutexActivityIds");

            Map<Long, Map<Long, Long>> mutexActivityIdMap = objectMapper.readValue(mutexActivityIds, new TypeReference<Map<Long, Map<Long, Long>>>() {
            });
            HashMap<String, String> result = teamBuildingService.checkMutexActivity(teamBuildingPackageItemId, activityItemId, mutexActivityIdMap);

            if (result != null) {
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.write(objectMapper.writeValueAsString(result));
                out.flush();
            }
        }
    }


}
