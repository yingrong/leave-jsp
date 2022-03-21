package com.tw.api_maintenance.before;

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

@WebServlet(value = "/api-maintenance/before")
public class ActivityServlet extends HttpServlet {

    TeamBuildingPackageItemRepository teamBuildingPackageItemRepository = new TeamBuildingPackageItemRepository();
    TeamBuildingPackageRepository teamBuildingPackageRepository = new TeamBuildingPackageRepository();
    ActivityRepository activityRepository = new ActivityRepository();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("sAction");
        System.out.println("get request. action:" + action);
        if (action == null) {
            System.out.println("get request");
            TeamBuildingPackageItem teamBuildingPackageItem = teamBuildingPackageItemRepository.findById(80010L);
            TeamBuildingPackageItemDto teamBuildingPackageItemDto = convertToTeamBuildingPackageItemDto(teamBuildingPackageItem);
            request.setAttribute("teamBuildingPackageItem", teamBuildingPackageItemDto);

            request.getRequestDispatcher("/api_maintenance/before/selectActivity.jsp").forward(request, response);
        } else if ("select".equals(action)) {
            Long teamBuildingPackageItemId = Long.parseLong(request.getParameter("teamBuildingPackageItemId"));
            Long activityItemId = Long.parseLong(request.getParameter("activityItemId"));
            Integer count = Integer.parseInt(request.getParameter("count"));

            TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
            ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();

            if (packageItem.getPackageId() == 10010 && activityItem.getActivityId() == 3) {
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ObjectMapper objectMapper = new ObjectMapper();
                out.write(objectMapper.writeValueAsString(new Error("上次已经举办过唱歌活动，本次不可选择！")));
                out.flush();
            } else {
                activityItem.setSelected(true);
                activityItem.setCount(count);
                teamBuildingPackageItemRepository.save(packageItem);
            }
        } else if ("unselect".equals(action)) {
            Long teamBuildingPackageItemId = Long.parseLong(request.getParameter("teamBuildingPackageItemId"));
            Long activityItemId = Long.parseLong(request.getParameter("activityItemId"));

            TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
            ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();
            activityItem.setSelected(false);
            activityItem.setCount(null);

            teamBuildingPackageItemRepository.save(packageItem);
        } else if ("check-mutex".equals(action)) {
            Long teamBuildingPackageItemId = Long.parseLong(request.getParameter("teamBuildingPackageItemId"));
            Long activityItemId = Long.parseLong(request.getParameter("activityItemId"));
            String mutexActivityIds = request.getParameter("mutexActivityIds");

            TeamBuildingPackageItem packageItem = teamBuildingPackageItemRepository.findById(teamBuildingPackageItemId);
            ActivityItem activityItem = packageItem.getActivityItems().stream().filter(i -> i.getId() == activityItemId).findFirst().get();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<Long, Map<Long, Long>> mutexActivityIdMap = objectMapper.readValue(mutexActivityIds, new TypeReference<Map<Long, Map<Long, Long>>>() {
            });

            if (mutexActivityIdMap.get(packageItem.getPackageId()) != null) {
                Long mutexActivityId = mutexActivityIdMap.get(packageItem.getPackageId()).get(activityItem.getActivityId());
                if (mutexActivityId != null) {
                    ActivityItem mutexActivity = packageItem.getActivityItems().stream().filter(i -> i.getActivityId() == mutexActivityId).findFirst().get();
                    if (mutexActivity.getSelected()) {
                        response.setContentType("application/json;charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter outer = response.getWriter();

                        HashMap<String, String> result = new HashMap<>();

                        String packageName = teamBuildingPackageRepository.findById(packageItem.getPackageId()).getName();
                        Map<Long, String> activityIdToName = activityRepository.findByIds(Arrays.asList(activityItem.getActivityId(), mutexActivityId))
                                .stream().collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));
                        String errorMessage = "在" + packageName + "中，" + activityIdToName.get(activityItem.getActivityId()) + "和" + activityIdToName.get(mutexActivityId) + "不能同时选择！";
                        result.put("errorMessage", errorMessage);

                        outer.write(objectMapper.writeValueAsString(result));
                        outer.flush();
                    }
                }
            }
        }

    }

    private TeamBuildingPackageItemDto convertToTeamBuildingPackageItemDto(TeamBuildingPackageItem teamBuildingPackageItem) {
        TeamBuildingPackage teamBuildingPackage = teamBuildingPackageRepository.findById(teamBuildingPackageItem.getPackageId());
        List<Activity> activities = activityRepository.findByIds(teamBuildingPackageItem.getActivityItems().stream().map(item -> item.getActivityId()).collect(Collectors.toList()));
        Map<Long, Activity> idToActivity = activities.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));

        List<ActivityItemDto> activityItemDtos = teamBuildingPackageItem.getActivityItems().stream()
                .map(item -> new ActivityItemDto(item.getId(),
                        item.getActivityId(),
                        idToActivity.get(item.getActivityId()).getName(),
                        item.getCount(),
                        item.getSelected())).collect(Collectors.toList());

        return new TeamBuildingPackageItemDto(teamBuildingPackageItem.getId(),
                teamBuildingPackage.getId(),
                teamBuildingPackage.getName(),
                activityItemDtos);

    }
}
